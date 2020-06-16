<template>
  <div class="_countDown">
    <van-count-down :time="time">
      <template v-slot="timeData">
        <div class="flexDiv countDownContainer">
          <div class="countDownItem">{{timeData.days}}</div>
          <div class="countDownItem">天</div>
          <div class="countDownItem">{{ timeData.hours }}</div>
          <div class="countDownItem">时</div>
          <div class="countDownItem">{{ timeData.minutes }}</div>
          <div class="countDownItem">分</div>
          <div class="countDownItem countDownSecond">{{ timeData.seconds }}</div>
          <div class="countDownItem">秒</div>
        </div>
      </template>
    </van-count-down>
  </div>
</template>

<script>
import { CountDown } from "vant";

export default {
  components: {
    [CountDown.name]: CountDown
  },
  props: {
    endDay: {
      type: [Date, String, Number],
      default: ""
    }
  },
  data() {
    return {
      time: 0
    };
  },
  watch: {
    endDay: {
      handler(newValue, oldValue) {
        if (newValue !== oldValue) {
          try {
            let now = new Date().valueOf();
            let timeStamp = 0;
            if (typeof newValue == "string") {
              // 2020-01-01
              timeStamp = new Date(newValue).valueOf();
            }
            if (typeof newValue == "number") {
              //时间戳
              timeStamp = newValue;
            }
            if (typeof newValue == "object") {
              //Date格式
              timeStamp = newValue.valueOf();
            }
            if (timeStamp > now) {
              this.time = timeStamp - now;
            } else {
              this.time = 0;
            }
          } catch (error) {
            console.log(error);
            this.time = 0;
          }
        }
      },
      deep: true,
      immediate: true
    }
  }
};
</script>

<style lang="scss">
._countDown {
  .countDownContainer {
    font-size: 0.24rem;
    line-height: initial;
    align-items: center;
    .countDownItem {
      flex: 1;
    }
    .countDownSecond{
      transition: all .5s;
    }
  }
}
</style>