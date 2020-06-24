<template>
  <div class="_helpIconContainer">
    <service-icon />
  </div>
</template>

<script>
import ServiceIcon from "./serviceIcon";
export default {
  components: {
    ServiceIcon
  }
};
</script>

<style lang="scss">
._helpIconContainer {
  position: fixed;
  left: 0;
  top: 30%;
}
</style>