<template>
  <div class="_serviceIconContainer flexDiv" @click="handleClick($event)">
    <div :class="collapse?'iconTextHide':'iconTextShow'" class="iconText">客服</div>
    <div class="flexDiv iconContainer">
      <van-icon name="service-o" />
    </div>
  </div>
</template>

<script>
import { Icon } from "vant";
export default {
  components: {
    [Icon.name]: Icon
  },
  data() {
    return {
      collapse: true
    };
  },
  methods: {
    handleClick(e) {
      console.log(e);
      this.collapse = !this.collapse;
    }
  }
};
</script>

<style lang="scss">
._serviceIconContainer {
  align-items: center;
  .iconText {
    transition: all 0.5s;
    font-size: 0;
  }
  .iconTextHide {
    animation: iconHide 0.5s forwards;
  }
  .iconTextShow {
    animation: iconShow 0.5s forwards;
  }
  @keyframes iconHide {
    0% {
      width: 0.32rem;
      font-size: 0;
    }
    100% {
      width: 0;
    }
  }
  @keyframes iconShow {
    0% {
      width: 0;
      font-size: 0;
    }
    50% {
      font-size: 0;
    }
    100% {
      font-size: 0.16rem;
      width: 0.32rem;
    }
  }
  .iconContainer {
    align-items: center;
  }
}
</style>