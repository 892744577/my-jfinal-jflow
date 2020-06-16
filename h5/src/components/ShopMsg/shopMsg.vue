<template>
  <div class="_shopMsg">
    <div>
      <van-search v-model="searchShop" placeholder="搜索门店" />
      <shop-list :shops="shops" />
    </div>
  </div>
</template>

<script>
import { Search } from "vant";
import ShopList from "./shopList";
export default {
  components: {
    [Search.name]: Search,
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
  }
};
</script>

<style lang="scss">
</style>