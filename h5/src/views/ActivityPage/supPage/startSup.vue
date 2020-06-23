<template>
  <div class="_startSup">
    <img class="bgImg" :src="bgImg" alt />
    <div class="startBtn" @click="startClick"></div>
    <div class="count">已有{{supCount.successCount}}人助力成功</div>
  </div>
</template>

<script>
import { mapActions, mapMutations } from "vuex";
import bgImg1 from "../../../assets/发起助力（成人）.jpg";
import bgImg2 from "../../../assets/发起助力（儿童）.jpg";
import bgImg3 from "../../../assets/发起助力（毛毯）.jpg";
export default {
  data() {
    return {
      bgImg: bgImg1,
      gid: 1
    };
  },
  computed: {
    supCount() {
      return this.$store.state.activity.supCount;
    }
  },
  created() {
    if (this.$route.query.gid) {
      let p = this.$route.query.gid;
      switch (p) {
        case 1: {
          this.bgImg = bgImg1;
          break;
        }
        case 2: {
          this.bgImg = bgImg2;
          break;
        }
        case 3: {
          this.bgImg = bgImg3;
          break;
        }
      }
      this.$forceUpdate();
    }
  },
  methods: {
    startClick() {
      this.$router.push({
        name: "fillMsg",
        query: {
          ...this.$route.query
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
._startSup {
  .bgImg {
    width: 100vw;
  }
  .startBtn {
    position: absolute;
    top: 610px;
    width: 340px;
    height: 80px;
    left: 370px;
  }
  .count {
    color: white;
    position: absolute;
    top: 1360px;
    text-align: center;
    width: 100vw;
  }
}
</style>