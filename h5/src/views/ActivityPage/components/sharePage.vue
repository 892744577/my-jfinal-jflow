<template>
  <div class="_sharePage">
    <img class="bgImg" :src="bgImg" alt />
    <div class="shareBtn" @click="shareBtnClick"></div>
  </div>
</template>

<script>
import bgImg from "../../../assets/分享页.gif";
export default {
  data() {
    return {
      bgImg
    };
  },
  methods: {
    shareBtnClick() {
      this.$router.push({
        name: "postPage"
      });
    }
  }
};
</script>

<style lang="scss" scoped>
._sharePage {
  position: relative;
  .bgImg {
    width: 100vw;
  }
  .shareBtn {
    position: absolute;
    top: 1615px;
    left: 530px;
    width: 312px;
    height: 80px;
  }
}
</style>