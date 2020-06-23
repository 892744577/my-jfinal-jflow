<template>
  <div>
    <layout>
      <!-- <keep-alive> -->
      <router-view />
      <!-- </keep-alive> -->
    </layout>

    <!-- <van-tabbar
      v-model="activeTab"
      v-show="!$route.meta.hideTabbar"
      active-color="#07c160"
      inactive-color="#000"
      @change="tabChange"
    >
      <van-tabbar-item icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item icon="tv-o">直播</van-tabbar-item>
      <van-tabbar-item icon="shop-o">门店信息</van-tabbar-item>
      <van-tabbar-item icon-prefix="tenon" icon="icon-share">分享</van-tabbar-item>
    </van-tabbar>-->
  </div>
</template>

<script>
import { Tabbar, TabbarItem, Toast } from "vant";
import { mapMutations, mapActions } from "vuex";
import Layout from "./components/layout";
import wxJSFn from "@/js/wxJSFn";
import {
  shareConfig,
  shareConfig2,
  createShareUrl,
  shareRouter
} from "@/config/shareConfig";
import { formatAvatarList } from "@/js/utils";
import qs from "qs";
export default {
  components: {
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem,
    Layout
  },
  data() {
    return {
      activeTab: 0
    };
  },
  computed: {
    shareParams() {
      return this.$store.state.shareParams;
    },
    activityMsg() {
      return this.$store.state.activityMsg;
    },
    supInfo() {
      return this.$store.state.supInfo;
    },
    dataReady() {
      return {
        isReady: this.$store.state.activity.isReady,
        ...this.$store.state.shareParams,
        $route: this.$route
      };
    }
  },
  watch: {
    activityMsg: {
      deep: true,
      immediate: true,
      handler(val, oldVal) {
        console.log("activityMsgChange", val, oldVal);
        if (val) {
          this.isHasCurSup();
        }
      }
    },
    dataReady: {
      deep: true,
      immediate: true,
      handler(val, oldVal) {
        if (val) {
          if (val.isReady && (val.pId || val.shareId)) {
            let obj = {
              aid: 1
            };
            let link = "";
            let sConfig = shareConfig2;
            if (this.shareParams.pId) obj.pid = this.shareParams.pId;
            if (this.shareParams.shareId)
              obj.shareId = this.shareParams.shareId;
            if (this.$store.state.activity.supInfoMy.id) {
              //当前人有发起过助力
              sConfig = shareConfig;
              link = createShareUrl(
                obj,
                this.$route.path,
                shareRouter.supRouter
              );
            } else if (this.activityMsg.srAsId) {
              //当前页有助力
              let par1 = window.location.href.split("#")[0];
              let par2 = par1.split("?")[1];
              let param = qs.parse(par2);
              if (param.pid) obj.pid = param.pid;
              if (param.shareId) obj.shareId = param.shareId;
              sConfig = shareConfig;
              link = createShareUrl(
                obj,
                this.$route.path,
                shareRouter.supRouter
              );
            } else {
              //都没有
              link = createShareUrl(
                obj,
                this.$route.path,
                shareRouter.actRouter
              );
            }
            console.log("shareLink", link);
            wxJSFn.share({
              ...sConfig,
              link,
              imgUrl: ""
            });
          }
        }
      }
    }
  },
  created() {
    document.title = "亚太天能九周年 四川巡礼";
  },
  mounted() {
    this.getSupNum();
  },
  methods: {
    ...mapActions({
      getSupNum: "activity/getSupNum",
      startSup: "activity/startSup",
      supFriend: "activity/supFriend",
      getSupInfo: "activity/getSupInfo",
      updateShareAndSup: "activity/updateShareAndSup",
      getUserSupId: "activity/getUserSupId"
    }),
    ...mapMutations({
      setSupInfoMy: "activity/setSupInfoMy",
      pageReady: "activity/pageReady",
      setAvListMy: "activity/setAvListMy",
      setSupInfo: "activity/setSupInfo"
    }),
    isHasCurSup() {
      this.getUserSupId({
        wxOpenId: this.$store.state.userId
      })
        .then(res => {
          if (res && res.code == "000000") {
            //当前登录人有发起过助力
            this.setSupInfoMy(res.data);
            return this.getSupInfo({
              assistId: res.data.id
            });
          }
          return undefined;
        })
        .then(res => {
          this.pageReady();
          if (res && res.code == "000000") {
            let list = formatAvatarList(res.data);
            this.setAvListMy(list);
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  }
};
</script>

<style>
</style>