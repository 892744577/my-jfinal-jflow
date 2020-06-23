<template>
  <div class="_supFriendSuc">
    <img class="bgImg" :src="bgImg" alt />
    <img v-if="list[0]" class="img1" :src="list[0].headImgUrl" />
    <img v-if="list[1]" class="img2" :src="list[1].headImgUrl" />
    <img v-if="list[2]" class="img3" :src="list[2].headImgUrl" />
    <img v-if="list[3]" class="img4" :src="list[3].headImgUrl" />
    <img v-if="list[4]" class="img5" :src="list[4].headImgUrl" />
    <div class="startBtn" @click="startBtnClick"></div>
  </div>
</template>

<script>
import bgImg1 from "../../../assets/好友助力成功页.jpg";
import { mapActions } from "vuex";
import { formatAvatarList } from "@/js/utils";
import {} from "vant";
export default {
  data() {
    return {
      bgImg: bgImg1,
      list: []
    };
  },
  computed: {
    activityMsg() {
      return this.$store.state.activityMsg;
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
      getSupInfo: "activity/getSupInfo"
    }),
    initData(acMsg) {
      this.getSupInfo({
        assistId: acMsg.srAsId
      })
        .then(res => {
          console.log(res);
          if (res && res.code == "000000") {
            let list = formatAvatarList(res.data);
            this.list = list.reverse();
          } else {
            Toast(res.msg);
          }
        })
        .catch(err => {
          console.log(err);
        });
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
            ...this.$route.query
          }
        });
      }
    }
  }
};
</script>

<style lang="scss" scoped>
@mixin avatar {
  position: absolute;
  width: 116px;
  height: 116px;
  top: 750px;
  border-radius: 50%;
}
._supFriendSuc {
  position: relative;
  .bgImg {
    width: 100vw;
  }
  .img1 {
    @include avatar;
    left: 155px;
  }
  .img2 {
    @include avatar;
    left: 328px;
  }
  .img3 {
    @include avatar;
    left: 501px;
  }
  .img4 {
    @include avatar;
    left: 678px;
  }
  .img5 {
    @include avatar;
    left: 854px;
  }
  .startBtn {
    position: absolute;
    left: 368px;
    top: 938px;
    width: 348px;
    height: 90px;
  }
}
</style>