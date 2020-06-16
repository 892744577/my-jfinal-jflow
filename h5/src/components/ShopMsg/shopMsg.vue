<template>
  <div class="_shopMsg">
    <div>
      <van-search v-model="searchShop" placeholder="搜索门店" />
    </div>
  </div>
</template>

<script>
import { Search } from "vant";
export default {
  components: {
    [Search.name]: Search
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