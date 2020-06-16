<template>
  <div class="_seckillItem">
    <card :thumb="msg.imgUrl" :origin-price="msg.originPrice">
      <template slot="title">
        <div class="seckillTitle">{{msg.title}}</div>
      </template>
      <template slot="desc">
        <div class="seckillContent">{{msg.content}}</div>
      </template>
      <template slot="price">
        <span class="seckillPriceTitle">秒杀价￥</span>
        <span class="seckillPrice">{{msg.seckillPrice}}</span>
      </template>
      <template slot="footer">
        <van-button size="small" round type="danger">立即秒杀</van-button>
      </template>
    </card>
  </div>
</template>

<script>
import { Card, Button } from "vant";
export default {
  components: {
    Card,
    [Button.name]: Button
  },
  props: {
    msg: {
      type: Object,
      default: () => {
        return {};
      }
    }
  }
};
</script>

<style lang="scss">
._seckillItem {
  text-align: left;
  margin-bottom: 0.04rem;
  .seckillTitle {
    font-size: 0.18rem;
    word-break: break-all;
  }
  .seckillPriceTitle {
    color: red;
    font-size: 0.14rem;
  }
  .seckillPrice {
    color: red;
    font-size: 0.24rem;
  }
  .van-card__footer .van-button {
    width: 0.7rem;
  }
}
</style>