<template>
  <div class="_fillMsg">
    <img class="bgImg" :src="bgImg" alt />
    <div class="formContainer">
      <van-form ref="editForm" @submit="formSubmit">
        <div class="tip">填写信息以便邮寄哦</div>
        <div class="tip">将有专业工作人员与您联系</div>
        <van-field v-model="userName" label="姓 名：" :border="false" />
        <van-field v-model="phone" label="电 话：" :border="false" />
        <!-- <van-field
          readonly
          clickable
          name="area"
          :value="areaValue"
          label="地区："
          :border="false"
          :rules="[{ required:true,message:'请选择省市区' }]"
          @click="showArea = true"
        />
        <van-popup v-model="showArea" position="bottom" get-container="body">
          <van-area :area-list="areaList" @confirm="onAreaConfirm" @cancel="showArea = false" />
        </van-popup>-->
        <van-field v-model="address" label="地 址：" :border="false" />
      </van-form>
    </div>
    <div class="submitBtn" @click="formSubmit"></div>
  </div>
</template>

<script>
import { mapActions, mapMutations } from "vuex";
import { Popup, Form, Field, Area, Row, Button, Toast } from "vant";
import bgImg from "../../../assets/填写个人信息.jpg";
// import areaList from "@/js/areaList";
import { phoneReg } from "@/js/reg";
import wxJSFn from "@/js/wxJSFn";
import { shareConfig, createShareUrl, shareRouter } from "@/config/shareConfig";
export default {
  components: {
    [Popup.name]: Popup,
    [Form.name]: Form,
    [Field.name]: Field,
    [Area.name]: Area,
    [Row.name]: Row,
    [Button.name]: Button
  },
  data() {
    return {
      bgImg,
      //   areaList,
      showArea: false,
      areaValue: "",
      userName: "",
      phone: "",
      address: "",
      goodsId: 0
    };
  },
  created() {
    if (this.$route.query.gid) {
      this.goodsId = this.$route.query.gid;
    }
  },
  computed: {
    shareParams() {
      return this.$store.state.shareParams;
    }
  },
  mounted() {
    let self = this;
    let inputs = document.getElementsByTagName("input");
    inputs.forEach(element => {
      element.addEventListener("focus", this.handleInputFocus);
      element.addEventListener("blur", this.handleInputBlur);
    });
  },
  beforeDestroy() {
    let self = this;
    let inputs = document.getElementsByTagName("input");
    inputs.forEach(element => {
      element.removeEventListener("focus", this.handleInputFocus);
      element.removeEventListener("blur", this.handleInputBlur);
    });
  },
  methods: {
    ...mapActions({
      startSup: "activity/startSup",
      updateShareAndSup: "activity/updateShareAndSup"
    }),
    ...mapMutations({
      setSupInfoMy: "activity/setSupInfoMy",
      setTabbarShow: "activity/setTabbarShow"
    }),
    handleInputFocus() {
      this.setTabbarShow(false);
    },
    handleInputBlur() {
      this.setTabbarShow(true);
    },
    phoneVali(val) {
      return val ? phoneReg.test(val) : false;
    },
    onAreaConfirm(value) {
      console.log(value);
      let str = "";
      value.forEach(e => {
        str += e.name;
      });
      this.areaValue = str;
      this.showArea = false;
    },
    formSubmit() {
      console.log("submit");
      if (!this.userName) {
        return Toast("请输入姓名");
      }
      if (!phoneReg.test(this.phone)) {
        return Toast("请输入电话");
      }
      if (!this.address) {
        return Toast("请输入地址");
      }
      let sendData = {
        asMobile: this.phone,
        asName: this.userName,
        asAddress: this.address,
        acId: 1,
        asOpenId: this.$store.state.userId,
        asProductId: this.goodsId
      };
      if (this.shareParams.shareId) {
        this.startSup(sendData)
          .then(res => {
            console.log(res);
            if (res.code == "000000") {
              let srAsId = res.data.id;
              this.setSupInfoMy(res.data);
              return this.updateShareAndSup({
                shareId: this.shareParams.shareId,
                assistId: res.data.id
              });
            } else {
              return { msg: res.msg };
            }
          })
          .then(res => {
            console.log(res);
            if (res && res.code == "000000") {
              Toast.success("发起助力成功");
              // this.updateShareMsg();
              this.$router.replace({
                name: "mySup",
                query: {
                  ...this.$route.query
                }
              });
            } else {
              Toast.fail(res.msg);
            }
          })
          .catch(err => {
            this.hideLoading();
            console.log(err);
          });
      } else {
        Toast.fail("获取分享信息失败，请重新打开页面");
      }
    },
    updateShareMsg() {
      let obj = {
        aid: 1
      };
      if (this.shareParams.pId) obj.pid = this.shareParams.pId;
      if (this.shareParams.shareId) obj.shareId = this.shareParams.shareId;

      let link = createShareUrl(obj, this.$route.path, shareRouter.supRouter);
      console.log("link", link);
      let imgUrl = "";
      wxJSFn.share({
        ...shareConfig,
        link,
        imgUrl: imgUrl
      });
    },
    saveClick() {
      this.$refs.editForm.submit();
    }
  }
};
</script>

<style lang="scss">
._fillMsg {
  position: relative;
  .bgImg {
    width: 100vw;
  }
  .formContainer {
    position: absolute;
    left: 210px;
    top: 715px;
    width: 694px;
    height: 416px;
    font-size: 47px;
    text-align: center;
    font-weight: 700;
    padding-top: 8px;
    .tip {
      padding-bottom: 20px;
    }
  }
  .van-cell {
    background-color: transparent;
    border-radius: 0.2rem;
    padding: 0 0.46rem 0 0.36rem;
    margin: 0.04rem 0;
    color: white;
    line-height: initial;
    .van-field__label {
      width: 200px;
      font-size: 50px;
    }
    .van-cell__title {
      font-weight: 500;
    }
    .van-field__value {
      background-color: #81d4fa;
      border-radius: 10px;
      .van-field__body {
        height: 100%;
        .van-field__control {
          height: 100%;
        }
      }
    }
  }
  .submitBtn {
    position: absolute;
    top: 1140px;
    left: 465px;
    width: 212px;
    height: 70px;
  }
}
</style>