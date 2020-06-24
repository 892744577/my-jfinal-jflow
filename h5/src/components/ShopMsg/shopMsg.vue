<template>
  <div class="_shopMsg">
    <div>
      <van-search
        v-model="searchShop"
        @search="searchShopConfirm"
        @clear="searchClear"
        class="shopMsgSearch"
      ></van-search>
      <shop-list :shops="shops" />
    </div>
  </div>
</template>

<script>
import { Search, Icon } from "vant";
import ShopList from "./shopList";
export default {
  components: {
    [Search.name]: Search,
    [Icon.name]: Icon,
    ShopList
  },
  props: {
    shops: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      searchShop: ""
    };
  },
  methods: {
    searchShopConfirm(value) {
      this.$emit("search", value);
    },
    searchClear() {
      this.$emit("clear-search");
    }
  }
};
</script>

<style lang="scss">
._shopMsg {
  background: url("../../assets/门店导航页.jpg") no-repeat;
  min-height: 100vh;
  background-size: 100% 100%;
  .van-search {
    background-color: #5096b2;

    .van-search__content {
      background-color: #81d4fa;
    }
    .van-icon {
      color: black;
    }
  }
}
</style>