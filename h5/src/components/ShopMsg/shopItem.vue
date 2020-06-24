<template>
  <div class="_shopItem">
    <div class="shopItemNameContainer">
      <div class="titleIconContainer">
        <van-icon class="titleIcon" name="shop-o" />
      </div>
      <div class="shopItemName">
        <span>{{msg.name || ''}}</span>
      </div>
      <div>
        <van-button
          size="small"
          icon="icon-daohang"
          icon-prefix="tenon"
          round
          class="btnPrimary"
          plain
          @click="navClick"
        >导航</van-button>
      </div>
    </div>
    <div class="shopItemPhone">
      <div class="titleIconContainer">
        <van-icon class="titleIcon" name="phone-o" />
      </div>
      <div>
        <a :href="msg.phone?'tel:'+msg.phone:'javascript:void(0);'" class="call">{{msg.phone || ''}}</a>
      </div>
    </div>
    <div class="shopItemLoc">
      <div class="titleIconContainer">
        <van-icon class="titleIcon" name="location-o" />
      </div>
      <div>{{msg.address || '' }}</div>
    </div>
  </div>
</template>

<script>
import { Button, Icon } from "vant";
import wxJSFn from "@/js/wxJSFn";
export default {
  components: {
    [Button.name]: Button,
    [Icon.name]: Icon
  },
  props: {
    msg: {
      type: Object,
      default: () => {
        return {};
      }
    }
  },
  methods: {
    navClick() {
      wxJSFn.openLocation({
        latitude: this.msg.latitude,
        longitude: this.msg.longitude,
        name: this.msg.name,
        address: this.msg.address
      });
    }
  }
};
</script>

<style lang="scss" scoped>
._shopItem {
  // background-color: #5096B2;
  background-image: linear-gradient(45deg, #1d7daf, #524ea3);
  border-radius: 5px;
  font-size: 0.18rem;
  padding: 0.12rem;
  margin-bottom: 0.12rem;
  .titleIconContainer {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .shopItemNameContainer {
    display: flex;
    align-items: center;
    .shopItemName {
      padding: 0 0.04rem;
      word-break: break-all;
      flex: 1;
      font-weight: 600;
    }
  }
  .shopItemPhone {
    display: flex;
    align-items: center;
    margin-top: 0.04rem;
  }
  .shopItemLoc {
    display: flex;
    align-items: center;
    margin-top: 0.04rem;
  }
}
</style>