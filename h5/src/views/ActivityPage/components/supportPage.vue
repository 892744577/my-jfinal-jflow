<template>
  <div class="_supportPage">
    <layout-content>
      <div class="headContainer">
        <van-button
          size="small"
          class="headBtn btnPrimary"
          v-if="pageType == 0"
          @click="headBtnClick"
        >{{isEdit?'填写助力信息':'点击发起助力'}}</van-button>
        <van-button
          v-if="pageType == 2"
          size="small"
          class="headBtn btnPrimary"
          @click="supClick"
        >点击助力好友</van-button>
        <van-button
          v-if="pageType == 2"
          size="small"
          class="headBtn btnPrimary"
          @click="headBtnClick"
        >我也要发起助力</van-button>
      </div>
      <!-- 商品图 -->
      <div class="goodsContainer" v-show="!isEdit && !supSuc && (pageType == 0 || pageType == 2)  "></div>
      <!-- 填写信息 -->
      <edit-form
        v-show="isEdit && !supSuc && (pageType == 0 || pageType == 2) "
        @form-submit="formSubmit"
      />
      <!-- 点击帮助好友助力显示 -->
      <div v-show="supSuc && pageType != 1" class="supSuc">
        <div class="sucContent">已为好友助力成功！</div>
        <layout-avatar-list :list="avatarList" />
        <van-button
          size="small"
          class="headBtn btnPrimary"
          style="margin-top:.08rem"
          @click="sucStartSup"
        >我也要发起助力</van-button>
      </div>
      <!-- 我的助力 -->
      <div v-show="pageType == 1" class="mySucContainer">
        <div class="mySucTitle">我的助力</div>
        <div class="goodsContainer"></div>
        <div class="divideContainer mySucDivide">
          <div class="dividePrimary"></div>
        </div>
        <div class="mySucAvatarContainer">
          <layout-avatar-list :list="avatarListMy" />
        </div>
        <div class="muSucTip">你还差X人助力成功哦！加油！</div>
        <div class="muSucBtnContainer">
          <van-button class="headBtn btnPrimary">邀请好友助力</van-button>
        </div>
      </div>
      <div class="divideContainer" v-show="pageType!=1">
        <div class="dividePrimary"></div>
      </div>
      <layout-footer :suc-num="sucNum" />
    </layout-content>
  </div>
</template>

<script>
import { Button, Toast } from "vant";
import LayoutContent from "./layoutContent";
import EditForm from "@/components/EditPersonalMsgModal/editForm";
import LayoutFooter from "./layoutFooter";
import LayoutAvatarList from "./layoutAvatarList";
import { post } from "@/js/utils";
import { mapActions, mapMutations } from "vuex";
export default {
  components: {
    [Button.name]: Button,
    LayoutContent,
    LayoutFooter,
    EditForm,
    LayoutAvatarList
  },
  data() {
    return {
      isEdit: false,
      pageAsId: 0, //页面助力id
      curAsId: 0, //当前登录人助力id
      pageType: 0,
      supSuc: false,
      avatarList: [
        // {
        //   url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        // },
        // {
        //   url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        // },
        // {
        //   url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        // }
      ],
      avatarListMy: [],
      goodsImg: "",
      goodsId: 0
    };
  },
  computed: {
    activityMsg() {
      return this.$store.state.activityMsg;
    },
    shareParams() {
      return this.$store.state.shareParams;
    },
    sucNum() {
      return this.$store.state.activity.supCount.successCount;
    }
  },
  watch: {
    activityMsg: {
      deep: true,
      immediate: true,
      handler(val, oldVal) {
        console.log("supportWatchAct", val, oldVal);
        if (val) {
          this.initData(val);
        }
      }
    }
  },
  created() {
    if (this.$route.query.gid) {
      this.goodsId = this.$route.query.gid;
    }
  },
  mounted() {
    // if (this.curAsId) {
    //   if (this.curAsId == this.pageAsId) {
    //     //当前人打开自己的助力
    //     this.pageType = 1;
    //   } else {
    //     //当前人有助力id但打开别人的助力
    //     this.pageType = 2;
    //   }
    // } else {
    //   //当前人没发起过助力
    //   this.pageType = 0;
    // }
  },
  methods: {
    ...mapActions({
      startSup: "activity/startSup",
      supFriend: "activity/supFriend",
      getSupInfo: "activity/getSupInfo",
      updateShareAndSup: "activity/updateShareAndSup",
      getUserSupId: "activity/getUserSupId"
    }),
    ...mapMutations({
      showLoading: "showLoading",
      hideLoading: "hideLoading",
      setActivityMsg: "setActivityMsg"
    }),
    initData(val) {
      this.getUserSupId({
        wxOpenId: this.$store.state.userId
      })
        .then(res => {
          if (res && res.code == "000000") {
            this.curAsId = res.data.id;
          }
          if (val.srAsId) {
            this.getSupInfo({
              assistId: val.srAsId
            })
              .then(res => {
                console.log(res);
                if (res.code == "000000") {
                  if (val.srAsId == this.curAsId) {
                    //看自己的助力
                    this.avatarListMy = this.formatAvatarList(res.data);
                    this.avatarList = [];
                    this.pageType = 1;
                  } else {
                    //别人助力
                    this.avatarList = this.formatAvatarList(res.data);
                    this.avatarListMy = [];
                    this.pageType = 2;
                  }
                }
              })
              .catch(err => {
                console.log(err);
              });
          } else {
            if (this.curAsId) {
              //看自己的助力
              this.pageType = 1;
              this.getSupInfo({
                assistId: this.curAsId
              })
                .then(res => {
                  console.log(res);
                  if (res.code == "000000") {
                    if (val.srAsId == this.curAsId) {
                      this.avatarListMy = this.formatAvatarList(res.data);
                      this.avatarList = [];
                      this.pageType = 1;
                    } else {
                      this.avatarList = this.formatAvatarList(res.data);
                      this.avatarListMy = [];
                      this.pageType = 2;
                    }
                  }
                })
                .catch(err => {
                  console.log(err);
                });
            } else {
              //都没有
              this.curAsId = 0;
              this.pageAsId = 0;
              this.pageType = 0;
              this.avatarList = [];
              this.avatarListMy = [];
            }
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    headBtnClick() {
      if (this.curAsId) {
        //已经发起过助力
        this.pageType = 1;
      } else {
        this.isEdit = !this.isEdit;
      }
    },
    supClick() {
      this.showLoading();
      this.supFriend({
        helperOpenId: this.$store.state.userId,
        assistId: this.activityMsg.srAsId,
        helperInfo: JSON.stringify(this.$store.state.userInfo)
      })
        .then(res => {
          console.log(res);
          this.hideLoading();
          this.supSuc = true;
          if (res && res.code == "000000") {
          } else {
            Toast(res.msg);
          }

          return this.getSupInfo({
            assistId: this.activityMsg.srAsId
          });
        })
        .then(res => {
          if (res && res.code == "000000") {
            this.avatarList = this.formatAvatarList(res.data);
          } else {
            Toast(res.msg);
          }
        })
        .catch(err => {
          console.log(err);
          this.hideLoading();
        });
    },
    sucStartSup() {
      this.supSuc = false;
      this.isEdit = true;
    },
    formatAvatarList(arr) {
      let avList = [];
      arr.forEach((e, i) => {
        if (e.helperInfo) {
          let obj = JSON.parse(e.helperInfo);
          avList.push(obj);
        }
      });
      return avList;
    },
    formSubmit(form) {
      let sendData = {
        asMobile: form.phone,
        asName: form.userName,
        asAddress: form.address,
        acId: 1,
        asOpenId: this.$store.state.userId,
        asProductId: this.goodsId
      };
      if (this.shareParams.shareId) {
        this.showLoading();
        this.startSup(sendData)
          .then(res => {
            this.hideLoading();
            console.log(res);
            if (res.code == "000000") {
              let srAsId = res.data.id;
              this.curAsId = srAsId;

              return this.updateShareAndSup({
                shareId: this.shareParams.shareId,
                assistId: res.data.id
              });
            } else {
              return { msg: res.msg };
            }
          })
          .then(res => {
            console.log(res);
            if (res && res.code == "000000") {
              Toast.success("发起助力成功");
              this.pageType = 1;
              this.avatarListMy = [];
              this.setActivityMsg({
                ...this.activityMsg,
                srAsId: this.curAsId
              });
            } else {
              Toast.fail(res.msg);
            }
          })
          .catch(err => {
            this.hideLoading();
            console.log(err);
          });
      } else {
        Toast.fail("获取分享信息失败，请重新打开页面");
      }
    }
  }
};
</script>

<style lang="scss">
._supportPage {
  color: white;
  .headContainer {
    text-align: center;
    margin: 0.12rem;
    display: flex;
    justify-content: space-around;
    .headBtn {
      color: black;
    }
  }
  .goodsContainer {
    height: 1rem;
    margin: 0 0.24rem;
    background-color: white;
  }
  .supSuc {
    padding: 0.12rem;
    text-align: center;
    .sucContent {
      color: white;
      font-weight: 600;
      margin: 0.08rem;
    }
  }
  .mySucContainer {
    .mySucTitle {
      font-weight: 600;
      font-size: 0.3rem;
      text-align: center;
    }
    .mySucDivide {
      margin-top: 0.12rem;
      margin-bottom: 0.12rem;
    }
    .mySucAvatarContainer {
      padding: 0 0.12rem;
    }
    .muSucTip {
      padding: 0.08rem 0.12rem;
    }
    .muSucBtnContainer {
      text-align: center;
      button {
        font-weight: 600;
        font-size: 0.18rem;
      }
    }
  }
}
</style>