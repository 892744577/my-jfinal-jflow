<template>
  <div class="_postPage">
    <div style="display:none">
      <img class="bgImg" ref="bgImg" :src="bgImg" />
    </div>
    <div style="display:none">
      <div ref="code" class="codeContainer"></div>
    </div>
    <canvas ref="canvas" class="canvas" width="1080" height="1920"></canvas>
    <img class="bgImg" :src="postImg" v-show="postImg" />
  </div>
</template>

<script>
import bgImg from "../../../assets/海报.jpg";
import { createShareUrl, shareRouter } from "@/config/shareConfig";
import qs from "qs";
export default {
  data() {
    return {
      bgImg,
      codeReady: false,
      bgReady: false,
      postImg: null
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
    },
    device() {
      return this.$store.state.device;
    },
    isReady() {
      return this.codeReady && this.bgReady;
    }
  },
  watch: {
    isReady: {
      immediate: true,
      handler(val) {
        if (val) {
          let deviceWidth = this.$store.state.device.clientWidth;
          let deviceHeight = this.$store.state.device.clientHeight;
          let codeHeight = deviceWidth / (1080 / 233);
          let codeWidth = deviceWidth / (1080 / 233);
          let canvas = this.$refs.canvas;
          let ctx = canvas.getContext("2d");
          let bgImgDom = this.$refs.bgImg;
          let codeDom = this.$refs.code.getElementsByTagName("img")[0];
          ctx.drawImage(bgImgDom, 0, 0, 1080, 1920, 0, 0, 1080, 1920);
          ctx.drawImage(codeDom, 0, 0, 233, 233, 270, 894, 233, 233);
          this.postImg = canvas.toDataURL("image/png");
        }
      }
    }
  },
  mounted() {
    let deviceWidth = this.$store.state.device.clientWidth;
    let deviceHeight = this.$store.state.device.clientHeight;
    let codeHeight = deviceWidth / (1080 / 233);
    let codeWidth = deviceWidth / (1080 / 233);
    console.log(codeHeight, codeWidth);
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
      width: 233,
      height: 233
    });

    let bgImgDom = this.$refs.bgImg;
    this.$nextTick(() => {
      let codeDom = this.$refs.code.getElementsByTagName("img")[0];
      console.log(codeDom);
      codeDom.onload = () => {
        this.codeReady = true;
      };
      bgImgDom.onload = () => {
        bgImgDom.width = 1080;
        bgImgDom.height = 1920;
        this.bgReady = true;
      };
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
  .canvas {
    width: 100vw;
    display: none;
  }
}
</style>