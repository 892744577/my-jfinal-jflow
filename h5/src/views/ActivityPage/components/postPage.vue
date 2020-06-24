<template>
  <div class="_postPage">
    <img class="bgImg" ref="bgImg" :src="bgImg" />
    <div ref="code" class="codeContainer"></div>
  </div>
</template>

<script>
import bgImg from "../../../assets/海报.jpg";
import { createShareUrl, shareRouter } from "@/config/shareConfig";
import qs from "qs";
export default {
  data() {
    return {
      bgImg
    };
  },
  computed: {
    shareParams() {
      return this.$store.state.shareParams;
    },
    supInfoMy() {
      return this.$store.state.activity.supInfoMy;
    },
    activityMsg() {
      return this.$store.state.activityMsg;
    }
  },
  mounted() {
    let height = this.$store.state.device.clientWidth / (1080 / 233);
    let width = this.$store.state.device.clientWidth / (1080 / 233);
    console.log(height, width);
    let obj = {
      aid: 1
    };
    let link = "";
    // if (this.shareParams.pId) obj.pid = this.shareParams.pId;
    if (this.shareParams.shareId) obj.shareId = this.shareParams.shareId;
    if (this.supInfoMy.id) {
      //当前人有发起过助力
      link = createShareUrl(obj, this.$route.path, shareRouter.supRouter);
    } else if (this.activityMsg.srAsId) {
      //当前打开的页面有助力
      let par1 = window.location.href.split("#")[0];
      let par2 = par1.split("?")[1];
      let param = qs.parse(par2);
      // if (param.pid) obj.pid = param.pid;
      if (param.shareId) obj.shareId = param.shareId;
      link = createShareUrl(obj, this.$route.path, shareRouter.supRouter);
    } else {
      //都没助力
      link = createShareUrl(obj, this.$route.path, shareRouter.actRouter);
    }

    console.log(link);
    var qrcode = new QRCode(this.$refs.code, {
      text: link,
      width,
      height
    });
  }
};
</script>

<style lang="scss" scoped>
._postPage {
  position: relative;
  .bgImg {
    width: 100vw;
  }
  .codeContainer {
    width: 232px;
    height: 230px;
    position: absolute;
    left: 270px;
    top: 894px;
  }
}
</style>