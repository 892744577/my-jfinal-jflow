<template>
  <div class="_layoutContent">
    <div class="topLogo"></div>
    <div class="title">
      <div class="title1">亚太天能九周年</div>
      <div class="title2">四川巡礼</div>
    </div>
    <div class="divideContainer">
      <div class="dividePrimary"></div>
    </div>
    <slot></slot>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
@import "@/common/var.scss";
._layoutContent {
  margin: .08rem 0.24rem 0.12rem 0.24rem;
  border: 1px solid white;
  position: relative;
  padding: 30px 0 0.12rem 0;
  .topLogo {
    position: absolute;
    top: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 120px;
    height: 30px;
    background-color: white;
  }
  .title {
    text-align: center;
    padding: 0.12rem 0.24rem;
    font-size: 0.24rem;
    .title1 {
      color: $color-primary;
    }
    .title2 {
      color: red;
    }
  }
  
}
</style>