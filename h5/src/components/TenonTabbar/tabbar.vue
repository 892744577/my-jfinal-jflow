<template>
  <div class="_tenonTabbar">
    <div
      v-for="(item,index) in tabs"
      :key="index"
      :class="active == index?'tabbarActive':''"
      class="tabbarItem"
      @click="itemClick(item)"
    >
      <div>{{item.name}}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    tabs: {
      type: Array,
      default: () => []
    },
    active: {
      type: [String, Number],
      default: 0
    }
  },
  methods: {
    itemClick(item) {
      this.$emit("tab-click", item);
    }
  }
};
</script>

<style lang="scss" scoped>
._tenonTabbar {
  display: flex;
  align-items: center;
  justify-content: space-around;
  .tabbarItem {
    background-color: #5b9bd5;
    padding: 0.04rem 0.22rem;
    border-radius: 20px;
    color: white;
  }
}
</style>