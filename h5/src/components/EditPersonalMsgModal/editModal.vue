<template>
  <van-popup
    class="_editPersonalMsgModalPop"
    :value="visible"
    @close="closeModal"
    @closed="closeModal"
    :close-on-click-overlay="false"
  >
    <div class="_editPersonalMsgModal">
      <van-form ref="editForm" @submit="formSubmit">
        <van-field
          v-model="userName"
          required
          label="姓名"
          placeholder="请输入姓名"
          :rules="[{ required:true,message:'请输入姓名' }]"
        />
        <van-field
          v-model="phone"
          required
          label="电话"
          placeholder="请输入电话"
          :rules="[{ validator:phoneVali,message:'请输入正确的电话' }]"
        />
        <van-field
          readonly
          required
          clickable
          name="area"
          :value="areaValue"
          label="地区选择"
          placeholder="点击选择省市区"
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
          required
          label="详细地址"
          placeholder="请输入详细地址"
          :rules="[{ required:true,message:'请输入详细地址' }]"
        />
      </van-form>
      <van-row type="flex" justify="end" class="btnContainer">
        <van-button size="small" class="btn" @click="closeModal">取消</van-button>
        <van-button size="small" type="info" class="btn" @click="saveClick">确定</van-button>
      </van-row>
    </div>
  </van-popup>
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
  props: {
    visible: {
      type: Boolean,
      default: false
    }
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
    closeModal() {
      console.log("close");
      this.$emit("close");
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
    },
    saveClick() {
      this.$refs.editForm.submit();
    }
  }
};
</script>

<style lang="scss">
._editPersonalMsgModalPop {
  width: 80%;
  ._editPersonalMsgModal {
    padding: 0.12rem;
  }
  .btnContainer {
    margin-top: 0.08rem;
    .btn {
      margin-left: 0.08rem;
    }
  }
}
</style>