<template>
  <shop-msg :shops="shopsShow" @search="searchShop" @clear-search="resetShopShow" />
</template>

<script>
import ShopMsg from "@/components/ShopMsg";
import Fuse from "fuse.js";
import { mapMutations } from "vuex";
export default {
  components: {
    ShopMsg
  },
  computed: {
    shopsShow() {
      return this.$store.state.activity.shopsShow;
    }
  },
  mounted() {
    this.setShopsShow(
      JSON.parse(JSON.stringify(this.$store.state.activity.shopsOrigin))
    );
  },
  methods: {
    ...mapMutations({
      setShopsShow: "activity/setShopsShow"
    }),
    searchShop(value) {
      if (value) {
        let shopOrigin = JSON.parse(
          JSON.stringify(this.$store.state.activity.shopsOrigin)
        );
        let options = {
          keys: ["name", "address", "phone"]
        };
        const fuse = new Fuse(shopOrigin, options);
        let result = fuse.search(value);
        console.log(result);
        let arr = [];
        result.forEach(e => {
          arr.push(e.item);
        });
        this.setShopsShow(arr);
      } else {
        this.resetShopShow();
      }
    },
    resetShopShow() {
      let shopOrigin = JSON.parse(
        JSON.stringify(this.$store.state.activity.shopsOrigin)
      );
      this.setShopsShow(shopOrigin);
    }
  }
};
</script>

<style>
</style>