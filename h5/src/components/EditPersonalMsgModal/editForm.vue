<template>
  <div class="_editPersonalMsgForm">
    <van-form ref="editForm" @submit="formSubmit">
      <div class="title">填写信息以便邮寄哦</div>
      <van-field
        v-model="userName"
        label="姓名："
        :border="false"
        :rules="[{ required:true,message:'请输入姓名' }]"
      />
      <van-field
        v-model="phone"
        label="电话："
        :border="false"
        :rules="[{ validator:phoneVali,message:'请输入正确的电话' }]"
      />
      <van-field
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
      </van-popup>
      <van-field
        v-model="address"
        type="textarea"
        autosize
        label="详细地址："
        :border="false"
        :rules="[{ required:true,message:'请输入详细地址' }]"
      />
    </van-form>
    <van-row type="flex" justify="center" class="btnContainer">
      <van-button size="small" class="btn" @click="saveClick">提交</van-button>
    </van-row>
  </div>
</template>

<script>
import { Popup, Form, Field, Area, Row, Button } from "vant";
import areaList from "@/js/areaList";
import { phoneReg } from "@/js/reg";
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
      areaList,
      showArea: false,
      areaValue: "",
      userName: "",
      phone: "",
      address: ""
    };
  },
  methods: {
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
      this.$emit("form-submit", {
        userName: this.userName,
        phone: this.phone,
        address: this.areaValue + this.address
      });
    },
    saveClick() {
      this.$refs.editForm.submit();
    }
  }
};
</script>

<style lang="scss">
._editPersonalMsgForm {
  margin: 0.18rem;
  background-color: rgba($color: #5096b2, $alpha: 1);
  border-radius: 0.2rem;
  padding-bottom: .08rem;
  .title {
    color: white;
    text-align: center;
    padding: 0.08rem;
    font-size: 0.14rem;
  }
  .btnContainer {
    margin-top: 0.08rem;
    .btn {
      margin-left: 0.08rem;
    }
  }
  .van-cell {
    background-color: transparent;
    border-radius: 0.2rem;
    padding: 0.04rem 0.26rem;
    .van-cell__title {
      font-weight: 600;
    }
    .van-field__value {
      background-color: #81d4fa;
    }
  }
}
</style>