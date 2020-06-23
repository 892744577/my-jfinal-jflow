<template>
  <div id="app">
    <router-view />
    <van-overlay :show="loading" class-name="loadingOverlay">
      <div class="loadingOverlayContainer">
        <van-loading size=".56rem" color="#1989fa" />
      </div>
    </van-overlay>
  </div>
</template>

<script>
import wx from "weixin-js-sdk";
import { basePrefix, getCookie, getStorageObj } from "./js/utils";
import { mapMutations, mapState } from "vuex";
import { TenonWebsocket } from "./js/websocket";
import qs from "qs";
import { post } from "./js/utils";
import { Dialog, Overlay, Loading, Toast } from "vant";
import HelpIcon from "@/components/HelpIcon";

export default {
  name: "App",
  components: {
    Dialog,
    HelpIcon,
    [Overlay.name]: Overlay,
    [Loading.name]: Loading
  },
  async created() {
    //外部浏览器跳过
    let viewer = window.navigator.userAgent.toLowerCase();
    if (viewer.match(/MicroMessenger/i) == "micromessenger") {
      //微信浏览器
      let jsApiTicket = await post("/mp/getJsapiConfig", {
        url: encodeURI(window.location.href)
      });

      //1、判断是否没有授权，没授权则重定向授权
      this.redirectOrNot(jsApiTicket);
      //2、若是通过用户，则获取用户信息
      const resQuery = qs.parse(
        window.location.href.split("?")[1].split("#")[0]
      );
      let user = await post("/mp/oauth2getAccessToken", {
        code: resQuery.code
      });
      console.log("user", user);
      if (user) {
        this.loadUser(user);
      } else {
        // this.setUserId(this.$store.state.userInfo.openId);
        this.redirectAuth(jsApiTicket);
      }
      //3、url添加调试代码
      this.debug();
      //4、添加业务
      await this.doBusiness(resQuery, jsApiTicket);
    }
  },
  computed: {
    ...mapState(["userId", "subscribe", "loading", "userInfo", "sharePagam"])
  },
  mounted() {
    //测试用

    // this.setUserId("test33");
    // this.setActivityMsg({
    //   acName: "618活动直播",
    //   acDetail: "618活动详情",
    //   srShareOpenid: "oJOuUjvrEpFgQbwCO98w0GpIn_Pg",
    //   pbId: 17,
    //   id: 1,
    //   srToShareOpenid: "oJOuUjoth7sO3ybUpUiPsEHYtkcs",
    //   srAsId: 1,
    //   acPlaybillImg: "618back.png"
    // });
    // this.setUserInfo({
    //   privileges: [],
    //   country: "中国",
    //   unionId: "oMGq-5w5af8_MkTVNIsFs_PsRU-0",
    //   qrScene: null,
    //   qrSceneStr: null,
    //   subscribeTime: null,
    //   subscribe: null,
    //   city: "广州",
    //   openId: "test33",
    //   groupId: null,
    //   tagIds: null,
    //   sex: 1,
    //   language: "zh_CN",
    //   remark: null,
    //   province: "广东",
    //   headImgUrl:
    //     "http://thirdwx.qlogo.cn/mmopen/vi_32/nmViceVKhzdibXJiaIRvC2nv0qneDhQuJ8xmiaTojeypq4XCeYdxg1QA20T2m2Ad7vC1bzENBnw0NaxJJRuJb73HQA/132",
    //   sexDesc: "男",
    //   nickname: "仲",
    //   subscribeScene: null
    // });
    // this.setShareParams({
    //   shareId: 12
    // });
    console.log(
      "width,heigth,dpr",
      document.documentElement.clientWidth,
      document.documentElement.clientHeight,
      window.devicePixelRatio
    );
  },
  methods: {
    ...mapMutations([
      "setJsapiTicket",
      "setUserId",
      "setSubscribe",
      "setShareParams",
      "setActivityMsg",
      "setUserInfo"
    ]),
    redirectOrNot(jsApiTicket) {
      const baseUrl = window.location.href;
      const resQuery = qs.parse(
        window.location.href.split("?")[1].split("#")[0]
      );
      //重定向服务器
      if (
        typeof resQuery == "undefined" ||
        typeof resQuery.code == "undefined" ||
        resQuery.code == null
      ) {
        console.log("重定向");
        const redirectUrl = encodeURIComponent(baseUrl);
        const dst = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${jsApiTicket.appId}&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`;
        window.location.href = dst;
      }
    },
    redirectAuth(jsApiTicket) {
      const baseUrl = window.location.href;
      const resQuery = qs.parse(
        window.location.href.split("?")[1].split("#")[0]
      );
      let str = qs.stringify({
        code: resQuery.code,
        state: resQuery.state,
        from: resQuery.from
      });
      let url1 = baseUrl.replace("&" + str, "");
      const redirectUrl = encodeURIComponent(url1);
      const dst = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${jsApiTicket.appId}&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`;
      window.location.href = dst;
    },
    loadUser(user) {
      this.setUserInfo(user);
    },
    debug: function() {
      let { debug } = qs.parse(
        window.location.href.split("#")[0].split("?")[1]
      );
      if (debug) {
        localStorage.clear();
        sessionStorage.clear();
      }
    },
    doBusiness: async function(resQuery, jsApiTicket) {
      let shareNew = {};
      let params = {};
      let self = this;
      if (typeof resQuery.pid != "undefined" && resQuery.pid != null) {
        let hb = await post("/port/activity/getActByPbId", {
          pbId: resQuery.pid
        });
        if (hb) {
          this.setActivityMsg(hb);
          if (hb.pbSourceOpenid != this.userId) {
            //非海报所有者打开了活动连接,通过海报生成本次分享
            shareNew = await post("/port/activity/activityShare", {
              shareOpenId: hb.pbSourceOpenid,
              toShareOpenId: this.userId,
              pbId: resQuery.pid,
              assistId: ""
            });
            Object.assign(params, { shareId: shareNew.id });
          } else {
            Object.assign(params, { pId: resQuery.pid });
          }
        }
      } else if (
        typeof resQuery.shareId != "undefined" &&
        resQuery.shareId != null
      ) {
        let share = await post("/port/activity/getActByShareId", {
          shareId: resQuery.shareId
        });
        if (share) {
          this.setActivityMsg(share);
          if (share.srToShareOpenid != this.userId) {
            //非分享人打开了活动连接,通过海报生成本次分享
            shareNew = await post("/port/activity/activityShare", {
              shareOpenId: share.srToShareOpenid,
              toShareOpenId: this.userId,
              pbId: share.pbId,
              assistId: share.srAsId || ""
            });
            Object.assign(params, { shareId: shareNew.id });
          } else {
            Object.assign(params, { shareId: resQuery.shareId });
          }
        }
      } else {
        console.info("缺少参数海报id或分享id");
        let postMsg = await post("/port/activity/getPlayBillByWxOpenId", {
          wxOpenId: this.userId
        });
        console.log("postMsg", postMsg);
        if (postMsg.id) {
          params.pId = postMsg.id;
        } else {
          console.log("获取海报id失败");
          Toast("获取页面信息失败，请联系客服人员");
        }
        this.setActivityMsg({ id: 0 });
      }

      //分享
      wx.config({
        debug: false,
        beta: true,
        appId: jsApiTicket.appId,
        timestamp: jsApiTicket.timestamp,
        nonceStr: jsApiTicket.nonceStr,
        signature: jsApiTicket.signature,
        jsApiList: [
          "checkJsApi",
          "updateAppMessageShareData",
          "onMenuShareAppMessage",
          "openLocation"
        ]
      });
      wx.ready(function() {
        //需在用户可能点击分享按钮前就先调用
        console.info(params);
        self.setShareParams(params);
      });
    }
  }
};
</script>

<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
}
</style>
