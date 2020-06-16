<template>
  <div>
    <layout>
      <keep-alive>
        <activity-index
          v-if="activeTab == 0"
          :swipe-img="swipeImg"
          :seckill-item="seckillItem"
          :goods-item="goodsItem"
        />

        <shop-msg v-if="activeTab == 2" />
      </keep-alive>
    </layout>

    <van-tabbar v-model="activeTab" active-color="#07c160" inactive-color="#000">
      <van-tabbar-item icon="home-o">首页</van-tabbar-item>
      <van-tabbar-item icon="tv-o">直播</van-tabbar-item>
      <van-tabbar-item icon="shop-o">门店信息</van-tabbar-item>
      <van-tabbar-item icon-prefix="tennon" icon="icon-share">分享</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
import { Tabbar, TabbarItem } from "vant";
import ActivityIndex from "@/components/ActivityIndex";
import ShopMsg from "@/components/ShopMsg";
import Layout from "./components/layout";
export default {
  components: {
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem,
    ActivityIndex,
    Layout,
    ShopMsg
  },
  data() {
    return {
      activeTab: 0,
      swipeImg: [
        {
          url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        },
        {
          url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        }
      ],
      seckillItem: [
        {
          id: 1,
          imgUrl: "https://img.yzcdn.cn/vant/ipad.jpeg",
          title: "秒杀商品123124141412412412412412412",
          seckillPrice: "0.01",
          originPrice: "1000",
          content: "12321321"
        },
        {
          id: 2,
          imgUrl: "https://img.yzcdn.cn/vant/ipad.jpeg",
          title: "秒杀商品2",
          seckillPrice: "0.01",
          originPrice: "1000",
          content: "12321321"
        }
      ],
      goodsItem: [
        {
          id: 1,
          imgUrl: "https://img.yzcdn.cn/vant/ipad.jpeg",
          title: "常规商品1",
          price: "0.01",
          originPrice: "1000",
          content: "12321321"
        },
        {
          id: 2,
          imgUrl: "https://img.yzcdn.cn/vant/ipad.jpeg",
          title: "常规商品2",
          price: "0.01",
          originPrice: "1000",
          content: "12321321"
        }
      ]
    };
  }
};
</script>

<style>
</style>