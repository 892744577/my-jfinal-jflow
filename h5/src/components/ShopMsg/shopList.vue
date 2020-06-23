<template>
  <div class="_shopListContainer">
    <shop-item v-for="item in shops" :key="item.id" :msg="item"></shop-item>
  </div>
</template>

<script>
import ShopItem from "./shopItem";
export default {
  components: {
    ShopItem
  },
  props: {
    shops: {
      type: Array,
      default: () => []
    }
  }
};
</script>

<style lang="scss" scoped>
._shopListContainer {
  padding: 0.12rem;
}
</style>