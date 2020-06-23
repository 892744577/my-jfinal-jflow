<template>
  <div class="_blank">
    <van-loading size="56px" color="#1989fa" />
  </div>
</template>

<script>
import { Loading } from "vant";
export default {
  components: {
    [Loading.name]: Loading
  },
  mounted() {
    this.$router.replace({
      name: "act7index"
    });
  }
};
</script>

<style lang="scss" scoped>
._blank {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
</style>