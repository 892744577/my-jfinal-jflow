<template>
  <div class="_bannerPage">
    <div class="swipeContainer" v-if="swipeImg.length > 0">
      <van-swipe :autoplay="3000" :height="clientHeight">
        <van-swipe-item v-for="(item, index) in swipeImg" :key="index" @click="itemClick(item)">
          <img v-lazy="item.url" class="swipeImg" />
        </van-swipe-item>
      </van-swipe>
    </div>
  </div>
</template>

<script>
import { Swipe, SwipeItem } from "vant";
export default {
  components: {
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem
  },
  data() {
    return {
      swipeImg: [
        {
          url: "https://img.yzcdn.cn/vant/ipad.jpeg",
          routerName: "act7index"
        },
        {
          url: "https://img.yzcdn.cn/vant/ipad.jpeg",
          routerName: ""
        }
      ],
      clientHeight: 667
    };
  },
  created(){
    this.clientHeight = this.$store.state.device.clientHeight || 667
  },
  methods: {
    itemClick(item) {
      if (item.routerName) {
        this.$router.push({
          name: item.routerName,
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
._bannerPage {
  .swipeImg {
    width: 100%;
    height: 100%;
  }
  .swipeContainer {
    text-align: center;
    background-color: white;
  }
}
</style>