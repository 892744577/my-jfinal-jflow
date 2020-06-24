<template>
  <div class="_mySup">
    <img class="bgImg" :src="isSuc?sucImg:bgImg" alt />

    <img v-if="!isSuc && list[0]" class="img1" :src="list[0].headImgUrl" />
    <img v-if="!isSuc && list[1]" class="img2" :src="list[1].headImgUrl" />
    <img v-if="!isSuc && list[2]" class="img3" :src="list[2].headImgUrl" />
    <img v-if="!isSuc && list[3]" class="img4" :src="list[3].headImgUrl" />
    <img v-if="!isSuc && list[4]" class="img5" :src="list[4].headImgUrl" />
    <div v-if="!isSuc" class="supBtn" @click="supBtnClick"></div>

    <van-popup v-model="popVisible">
      <div class="popupContent">点击右上方三个点，然后点击发送给好友来邀请好友助力</div>
    </van-popup>
  </div>
</template>

<script>
import bgImg1 from "../../../assets/我的助力（成人枕）.jpg";
import bgImg2 from "../../../assets/我的助力（儿童枕）.jpg";
import bgImg3 from "../../../assets/我的助力（毛毯）.jpg";
import sucImg from "../../../assets/恭喜助力成功页.jpg";
import { Popup } from "vant";
export default {
  components: {
    [Popup.name]: Popup
  },
  data() {
    return {
      bgImg: bgImg1,
      sucImg,
      popVisible: false
    };
  },
  computed: {
    activityMsg() {
      return this.$store.state.activityMsg;
    },
    supInfoMy() {
      return this.$store.state.activity.supInfoMy;
    },
    list() {
      return this.$store.state.activity.avListMy.reverse();
    },
    isSuc() {
      return this.$store.state.activity.avListMy.length >= 5;
    }
  },
  watch: {
    supInfoMy: {
      deep: true,
      immediate: true,
      handler(val, oldVal) {
        if (val && val.asProductid) {
          this.initData(val);
        }
      }
    }
  },
  methods: {
    initData(info) {
      switch (info.asProductid) {
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
      this.$forceUpdate();
    },
    supBtnClick() {
      if (!this.isSuc) {
        this.popVisible = true;
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
  top: 1060px;
  border-radius: 50%;
}
._mySup {
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
    left: 502px;
  }
  .img4 {
    @include avatar;
    left: 678px;
  }
  .img5 {
    @include avatar;
    left: 854px;
  }
  .supBtn {
    position: absolute;
    top: 1295px;
    left: 485px;
    width: 350px;
    height: 90px;
  }
  .popupContent {
    padding: 0.2rem;
  }
}
</style>