<template>
  <div class="_layoutFooter">
    <div class="footerText">已有{{sucNum}}人助力成功</div>
    <div class="footerBtnContainer">
      <van-button size="small" class="footerBtn btnPrimary" @click="visible = true">活动须知</van-button>
    </div>
    <van-popup v-model="visible" class="activityReadmeModal">
      <div class="activityReadme">活动须知</div>
    </van-popup>
  </div>
</template>

<script>
import { Popup, Button } from "vant";
export default {
  components: {
    [Popup.name]: Popup,
    [Button.name]: Button
  },
  props:{
    sucNum:{
      type:[String,Number],
      default:0
    }
  },
  data() {
    return {
      visible: false
    };
  }
};
</script>

<style lang="scss" scoped>
._layoutFooter {
  margin: 0.12rem 0;
  text-align: center;
  color: black;
  .footerBtnContainer{
      margin-top: .24rem;
  }
  .footerText {
    color: white;
  }
  .activityReadmeModal {
    width: 80%;
  }
  .activityReadme {
    word-break: break-all;
  }
}
</style>