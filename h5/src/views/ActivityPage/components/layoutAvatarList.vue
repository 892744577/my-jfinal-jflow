<template>
  <div class="_layoutAvatarList">
    <div v-for="(item,index) in empArr" :key="index" class="avatarContainer">
      <van-image :src="list[index]?list[index].headImgUrl:''" round></van-image>
    </div>
  </div>
</template>

<script>
import { Image } from "vant";
export default {
  components: {
    [Image.name]: Image
  },
  props: {
    list: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      empArr: [1, 1, 1, 1, 1]
    };
  }
};
</script>

<style lang="scss">
._layoutAvatarList {
  display: flex;
  align-items: center;
  justify-content: space-around;
  .avatarContainer {
    width: .56rem;
    margin-right: .02rem;
    background-color: white;
    height: .56rem;
  }
}
</style>