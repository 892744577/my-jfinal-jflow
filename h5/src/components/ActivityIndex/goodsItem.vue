<template>
  <div class="_goodsItem">
    <div class="itemContainer1">
      <div class="itemContainer2">
        <div></div>
      </div>
    </div>
    <div class="itemContainer1">
      <div class="itemContainer2">
        <div></div>
      </div>
    </div>
    <div class="itemContainer1">
      <div class="itemContainer2">
        <div></div>
      </div>
    </div>
    <div class="itemContainer1">
      <div class="itemContainer2">
        <div></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss">
$radius:.2rem;
._goodsItem {
  margin: 0.12rem;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
  .itemContainer1 {
    width: 1rem;
    height: 1rem;
    border: 1px solid #3a8294;
    padding: 0.06rem;
    border-radius: $radius;
    margin-bottom: .12rem;
    .itemContainer2 {
      border: 1px solid #5f5462;
      height: 100%;
      border-radius: $radius;
    }
  }
}
</style>