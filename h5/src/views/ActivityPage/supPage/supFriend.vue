<template>
  <div class="_supFriend">
    <img class="bgImg" :src="bgImg" alt />
    <div class="supBtn" @click="supBtnClick"></div>
    <div class="startBtn" @click="startBtnClick"></div>
    <div class="count">已有{{supCount.successCount}}人助力成功</div>
  </div>
</template>

<script>
import bgImg1 from "../../../assets/好友助力（成人）.jpg";
import bgImg2 from "../../../assets/好友助力（儿童）.jpg";
import bgImg3 from "../../../assets/好友助力（摊子）.jpg";
import { mapMutations, mapActions } from "vuex";
import qs from "qs";
import { Toast } from "vant";
export default {
  data() {
    return {
      bgImg: bgImg1,
      gid: 0,
      canClick: false
    };
  },
  computed: {
    activityMsg() {
      return this.$store.state.activityMsg;
    },
    supCount() {
      return this.$store.state.activity.supCount;
    }
  },
  watch: {
    activityMsg: {
      deep: true,
      immediate: true,
      handler(val, oldVal) {
        if (val && val.srAsId) {
          this.initData(val);
        }
      }
    }
  },
  methods: {
    ...mapActions({
      getUserSupId: "activity/getUserSupId",
      supFriend: "activity/supFriend",
      getSupInfo: "activity/getSupInfo"
    }),
    initData(acMsg) {
      this.getSupInfo({
        assistId: acMsg.srAsId
      })
        .then(res => {
          if (res && res.code == "000000") {
            if (res.portActivityAssist.asProductid) {
              let p = res.portActivityAssist.asProductid
              switch (p) {
                case "1": {
                  this.bgImg = bgImg1;
                  break;
                }
                case "2": {
                  this.bgImg = bgImg2;
                  break;
                }
                case "3": {
                  this.bgImg = bgImg3;
                  break;
                }
              }
              this.canClick = true;
            }
          } else {
            Toast("获取助力信息失败");
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    supBtnClick() {
      if (this.canClick) {
        this.supFriend({
          helperOpenId: this.$store.state.userId,
          assistId: this.activityMsg.srAsId,
          helperInfo: JSON.stringify(this.$store.state.userInfo)
        })
          .then(res => {
            if (res && (res.code == "000000" || res.code == "000030")) {
              this.$router.push({
                name: "supFriendSuc",
                query: {
                  ...this.$route.query,
                  gid: this.gid
                }
              });
            } else {
              Toast(res.msg);
            }
          })
          .catch(err => {
            console.log(err);
          });
      } else {
        Toast("获取信息失败，请重新打开页面");
      }
    },
    startBtnClick() {
      if (this.$store.state.activity.supInfoMy.asProductid) {
        //当前人有发起过助力
        this.$router.push({
          name: "mySup",
          query: {
            ...this.$route.query,
            gid: this.$store.state.activity.supInfoMy.asProductid
          }
        });
      } else {
        this.$router.push({
          name: "startSup",
          query: {
            ...this.$route.query,
            gid: this.gid
          }
        });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
._supFriend {
  position: relative;
  .bgImg {
    width: 100vw;
  }
  .supBtn {
    position: absolute;
    top: 603px;
    left: 185px;
    width: 348px;
    height: 86px;
  }
  .startBtn {
    position: absolute;
    top: 603px;
    left: 605px;
    width: 348px;
    height: 86px;
  }
  .count {
    width: 100vw;
    position: absolute;
    top: 1360px;
    text-align: center;
    color: white;
  }
}
</style>