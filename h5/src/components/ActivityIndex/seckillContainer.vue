<template>
  <div class="_seckillContainer">
    <div class="seckillTitle">
      0.01元助力购
      <span></span>
    </div>
    <div class="seckillBox">
      <div class="seckillItem" @click="seckClick(1)">
        <div class="seckillItemBlank"></div>
        <div class="seckillItemText">我要助力</div>
      </div>
      <div class="seckillItem" @click="seckClick(2)">
        <div class="seckillItemBlank"></div>
        <div class="seckillItemText">我要助力</div>
      </div>
    </div>
    <div class="divideContainer">
      <div class="dividePrimary"></div>
    </div>
    <div class="countContainer">
      <div class="countItem">
        <div class="countItemTop">{{peopleCount.involvedCount}}</div>
        <div class="countItemBottom">已参与人数</div>
      </div>
      <div class="countItem">
        <div class="countItemTop">{{peopleCount.assistCount}}</div>
        <div class="countItemBottom">已助力人数</div>
      </div>
      <div class="countItem">
        <div class="countItemTop">{{peopleCount.browseCount}}</div>
        <div class="countItemBottom">已浏览人数</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    peopleCount: {
      type: Object,
      default: () => {
        return {
          involvedCount: 0,
          assistCount: 0,
          browseCount: 0
        };
      }
    }
  },
  methods: {
    seckClick(i) {
      this.$emit("seckill-click", i);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@/common/var.scss";
._seckillContainer {
  position: relative;
  
  .seckillTitle {
    padding: 0.24rem 0.24rem 0 0.24rem;
    color: white;
    font-size: 0.24rem;
    text-align: justify;
    span {
      display: inline-block /* Opera */;
      padding-left: 100%;
    }
  }
  .seckillBox {
    display: flex;
    justify-content: space-around;
    align-items: center;
    padding-bottom: 0.12rem;
    .seckillItem {
      width: 40%;
      height: 80px;
      background-color: white;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      .seckillItemBlank {
        flex: 1;
      }
      .seckillItemText {
        background-color: $color-primary;
        text-align: center;
      }
    }
  }
  .countContainer {
    display: flex;
    align-items: center;
    padding: 0.08rem 0;
    justify-content: space-around;
    .countItem {
      width: 30%;
      font-size: 0.12rem;
      text-align: center;
      background-color: $color-primary;
      border-radius: 5px;
    }
  }
}
</style>