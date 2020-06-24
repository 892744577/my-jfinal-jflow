<template>
  <div class="activityIndex">
    <div class="swipeContainer" v-if="swipeImg.length > 0">
      <van-swipe :autoplay="3000" height="200">
        <van-swipe-item v-for="(item, index) in swipeImg" :key="index">
          <img v-lazy="item.url" class="swipeImg" />
        </van-swipe-item>
      </van-swipe>
    </div>

    <div class="indexContainer">
      <seckill-container />

      <!-- <div class="shopListContainer" v-if="goodsItem.length > 0">
        <van-tabs :value="0">
          <van-tab title="折扣产品">
            <div class="shopListItemContainer">
              <goods-item v-for="(item,index) in goodsItem" :key="index" :msg="item"></goods-item>
            </div>
          </van-tab>
          <van-tab title=" " disabled></van-tab>
        </van-tabs>
      </div>-->
    </div>
  </div>
</template>

<script>
import { Swipe, SwipeItem, Tab, Tabs } from "vant";
// import SeckillItem from "./seckillItem";
import GoodsItem from "./goodsItem";
import SeckillContainer from "./seckillContainer";
export default {
  components: {
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Tab.name]: Tab,
    [Tabs.name]: Tabs,
    // SeckillItem,
    GoodsItem,
    SeckillContainer
  },
  props: {
    seckillItem: {
      type: Array,
      default: () => []
    },
    goodsItem: {
      type: Array,
      default: () => []
    },
    swipeImg: {
      type: Array,
      default: () => []
    },
    peopleCount: {
      type: Object,
      default: () => {
        return {
          sumNum: 0,
          supportNum: 0,
          surfNum: 0
        };
      }
    }
  },
  data() {
    return {};
  },
  methods: {
    seckBtnClick(item) {
      this.$emit("seck-click", item);
    }
  }
};
</script>

<style lang="scss">
.activityIndex {
  .swipeImg {
    height: 100%;
  }
  .swipeContainer {
    text-align: center;
  }
  .indexContainer {
    margin: 0 0.24rem 0.12rem 0.24rem;
    border: 1px solid white;
  }
  .seckillContainer {
    margin-top: 0.08rem;
    .seckillItemContainer {
      padding-top: 0.04rem;
    }
    .van-tab__text {
      font-size: 0.2rem;
      font-weight: 600;
    }
    
  }
  .shopListContainer {
    margin-top: 0.08rem;
    .van-tab__text {
      font-size: 0.18rem;
      font-weight: 600;
    }
    .shopListItemContainer {
      padding: 0.24rem;
    }
  }
}
</style>