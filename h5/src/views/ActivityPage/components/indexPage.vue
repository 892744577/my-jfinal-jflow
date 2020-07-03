<template>
  <div class="activityIndex">
    <div class="swipeContainer" v-if="swipeImg.length > 0">
      <van-swipe :autoplay="3000">
        <van-swipe-item v-for="(item, index) in swipeImg" :key="index">
          <img :src="item.img" class="swipeImg" />
        </van-swipe-item>
      </van-swipe>
    </div>

    <!-- <layout-content> -->
    <!-- <seckill-container @seckill-click="seckClick" :people-count="peopleCount" />

      <div class="bookBuyContainer">
        <van-button size="small" style="width:1rem">抢先预购</van-button>
      </div>

    <goods-item />-->

    <!-- </layout-content> -->
    <div class="indexImgContainer">
      <img class="indexImg" src="../../../assets/首页.jpg" alt />
      <div class="sup1" @click="seckClick(1)"></div>
      <div class="sup2" @click="seckClick(2)"></div>
      <div class="sup3" @click="seckClick(3)"></div>

      <div class="count1">
        <div>{{peopleCount.involvedCount}}</div>
        <div>已参加人数</div>
      </div>
      <div class="count2">
        <div>{{peopleCount.involvedCount}}</div>
        <div>已参加人数</div>
      </div>
      <div class="count3">
        <div>{{peopleCount.involvedCount}}</div>
        <div>已参加人数</div>
      </div>

      <div class="goodsImg1"></div>
      <div class="goodsImg2"></div>
      <div class="goodsImg3"></div>
      <div class="goodsImg4"></div>

      <div class="goods1">1899元</div>
      <div class="goods2">1399元</div>
      <div class="goods3">2199元</div>
      <div class="goods4">4299元</div>
    </div>
  </div>
</template>

<script>
import SeckillContainer from "@/components/ActivityIndex/seckillContainer";
import GoodsItem from "@/components/ActivityIndex/goodsItem";
import LayoutContent from "./layoutContent";
import { Swipe, SwipeItem, Tab, Tabs, Button } from "vant";
import swipeImg1 from "assets/轮播图1.jpg";
import swipeImg2 from "assets/轮播图2.jpg";
import swipeImg3 from 'assets/mmexport1593745482035.jpeg'
// import indexImg from "assets/活动主页.jpg";
export default {
  components: {
    SeckillContainer,
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Tab.name]: Tab,
    [Tabs.name]: Tabs,
    [Button.name]: Button,
    GoodsItem,
    LayoutContent
  },
  computed: {
    peopleCount() {
      return this.$store.state.activity.supCount;
    },
    isReady() {
      return this.$store.state.activity.isReady;
    }
  },
  data() {
    return {
      swipeImg: [
        {
          img: swipeImg3
        }
      ]
    };
  },
  methods: {
    seckClick(item) {
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
            gid: item
          }
        });
      }
    }
  }
};
</script>

<style lang="scss">
@mixin sup {
  width: 244px;
  height: 70px;
  top: 800px;
  position: absolute;
}

@mixin count {
  width: 226px;
  height: 82px;
  position: absolute;
  top: 960px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-around;
  color: white;
  font-size: 0.08rem;
  // transform: scale(0.8);
}

@mixin goods {
  position: absolute;
  color: #66dcff;
}

@mixin goodsImg {
  position: absolute;
  width: 294px;
  height: 196px;
}
.activityIndex {
  .van-swipe-item {
    height: 100vw;
  }
  .swipeImg {
    width: 100vw;
    height: 100vw;
  }
  .swipeContainer {
    text-align: center;
  }

  .bookBuyContainer {
    text-align: center;
  }
  .indexImgContainer {
    position: relative;
    .indexImg {
      width: 100vw;
      position: relative;
      z-index: -1;
    }
    .sup1 {
      @include sup;
      left: 150px;
    }
    .sup2 {
      @include sup;
      left: 432px;
    }
    .sup3 {
      @include sup;
      left: 732px;
    }
    .count1 {
      @include count;
      left: 162px;
    }
    .count2 {
      @include count;
      left: 450px;
    }
    .count3 {
      @include count;
      left: 742px;
    }
    .goodsImg1 {
      @include goodsImg;
      left: 250px;
      top: 1230px;
    }
    .goodsImg2 {
      @include goodsImg;
      left: 645px;
      top: 1230px;
    }
    .goodsImg3 {
      @include goodsImg;
      left: 250px;
      top: 1508px;
    }
    .goodsImg4 {
      @include goodsImg;
      left: 645px;
      top: 1508px;
    }
    .goods1 {
      @include goods;
      left: 350px;
      top: 1435px;
    }
    .goods2 {
      @include goods;
      left: 740px;
      top: 1435px;
    }
    .goods3 {
      @include goods;
      left: 350px;
      top: 1720px;
    }
    .goods4 {
      @include goods;
      left: 740px;
      top: 1715px;
    }
  }
}
</style>