<template>
  <div class="_supportIndex">
    <div class="swipeContainer" v-if="swipeImg.length > 0">
      <van-swipe :autoplay="3000" height="300">
        <van-swipe-item v-for="(item, index) in swipeImg" :key="index">
          <img v-lazy="item.url" class="swipeImg" />
        </van-swipe-item>
      </van-swipe>
    </div>
    <div class="avatarContainer">
      <div v-for="(item,index) in avatarList" :key="index" class="avatarItem">
        <van-image round width=".34rem" height=".34rem" :src="item.url" />
      </div>
      <div>
        <van-icon size=".24rem" class-prefix="tenon" name="icon-shenglvehao" />
      </div>
    </div>
    <div class="tip tip1">你还差X人助力成功哦！加油！</div>
    <div class="tip tip2">已有X人助力成功</div>
    <div class="tip tip3">
      <van-button type="primary" @click="openShareTip">邀请好友助力</van-button>
    </div>
    <van-popup class="shareTipPop" v-model="shareTipVisible">
      <div class="shareTipContainer">
        <div class="shareTipText">
          点击右上角三个点
          <div class="shareTipIcon">
            <van-icon size=".22rem" class-prefix="tenon" name="icon-shenglvehao" />
          </div>
        </div>
        <div class="shareTipText">转发本链接即可邀请好友参与助力</div>
      </div>
    </van-popup>
  </div>
</template>

<script>
import { Swipe, SwipeItem, Image, Icon, Button, Popup } from "vant";
export default {
  components: {
    [Swipe.name]: Swipe,
    [SwipeItem.name]: SwipeItem,
    [Image.name]: Image,
    [Icon.name]: Icon,
    [Button.name]: Button,
    [Popup.name]: Popup
  },
  data() {
    return {
      swipeImg: [
        {
          url: "https://img.yzcdn.cn/vant/ipad.jpeg"
        }
      ],
      avatarList: [
        {
          url: "https://img.yzcdn.cn/vant/cat.jpeg"
        },
        {
          url: "https://img.yzcdn.cn/vant/cat.jpeg"
        }
      ],
      shareTipVisible: false
    };
  },
  methods: {
    openShareTip() {
      this.shareTipVisible = true;
    }
  }
};
</script>

<style lang="scss">
._supportIndex {
  .swipeImg {
    height: 100%;
  }
  .swipeContainer {
    text-align: center;
    background-color: white;
  }
  .avatarContainer {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0.12rem;
    .avatarItem {
      margin-right: 0.04rem;
    }
  }
  .tip {
    text-align: center;
    margin: 0.12rem;
  }
  .tip2 {
    font-size: 0.2rem;
    font-weight: 600;
  }
  .shareTipPop {
    width: 80%;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.849);
    .shareTipContainer {
      padding: 0.20rem;
      .shareTipText {
        margin: 0.08rem;
      }
      .shareTipIcon {
        display: inline-block;
        background-color: white;
        border-radius: 10px;
        padding: 0 0.04rem;
      }
    }
  }
}
</style>