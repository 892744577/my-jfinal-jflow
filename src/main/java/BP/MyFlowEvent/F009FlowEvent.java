package BP.MyFlowEvent;

import BP.WF.FlowEventBase;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.kakarote.crm9.erp.admin.service.SysRegistService;
import com.kakarote.crm9.erp.yzj.service.TokenService;

import java.util.HashMap;
import java.util.Map;

public class F009FlowEvent extends FlowEventBase {
    @Override
    public String getFlowMark() {
        return "aptenon_wf_gdsq";
    }

    @Override
    public String SendSuccess(){

        try {
            //判断是否开始节点
           boolean isStartNode = this.HisNode.getIsStartNode();
            if (isStartNode) {
                String url = "http://test-api-oms.xiujiadian.com/v1/createOrder";

                //调用新增订单接口
                String serviceType = (String) this.getSysPara().get("serviceType"); //服务单类型
                String reworkId = (String) this.getSysPara().get("reworkId"); //返修源单号
                int factory = (int) this.getSysPara().get("factory"); //厂商单标志：厂商单传固定值 2
                int facInWarranty = (int) this.getSysPara().get("facInWarranty"); //标识产品是否在保
                int smcCityId = (int) this.getSysPara().get("smcCityId"); //城市id
                String telephone = (String) this.getSysPara().get("telephone"); //用户手机号码
                String contactName = (String) this.getSysPara().get("contactName"); //联系人姓名
                String gender = (String) this.getSysPara().get("gender"); //性别
                String address = (String) this.getSysPara().get("address"); //详细地址
                String dutyTime = (String) this.getSysPara().get("dutyTime"); //预约时间
                int productId = (int) this.getSysPara().get("productId"); //言而有信产品ID
                String facProductId = (String) this.getSysPara().get("facProductId"); //厂商产品ID
                int productCount = (int) this.getSysPara().get("productCount"); //产品数量
                int orderDiscountAmount = (int) this.getSysPara().get("orderDiscountAmount"); //优惠金额，单位分
                String orderDiscountSourceData = (String) this.getSysPara().get("orderDiscountSourceData"); //订单优惠快照
                String orderDiscountRemark = (String) this.getSysPara().get("orderDiscountRemark"); //优惠备注
                String remark = (String) this.getSysPara().get("remark"); //服务单备注
                String serviceNo = (String) this.getSysPara().get("serviceNo"); //服务单编号

                Map currentPrama = new HashMap();
                Map currentJson= new HashMap();
                currentJson.put("amount", orderDiscountAmount);
                currentJson.put("sourceData", orderDiscountSourceData);
                currentJson.put("remark", orderDiscountRemark);
                currentPrama.put("orderDiscount", JSONObject.toJSONString(currentJson));
                currentPrama.put("type", serviceType);
                currentPrama.put("reworkId", reworkId);
                currentPrama.put("factory", factory);
                currentPrama.put("facInWarranty", facInWarranty);
                currentPrama.put("cityId", smcCityId);
                currentPrama.put("telephone", telephone);
                currentPrama.put("contactName", contactName);
                currentPrama.put("gender", gender);
                currentPrama.put("latitude", null);
                currentPrama.put("longitude", null);
                currentPrama.put("street", address);
                currentPrama.put("address", address);
                currentPrama.put("dutyTime", dutyTime);
                currentPrama.put("productId", productId);
                currentPrama.put("facProductId", facProductId);
                currentPrama.put("productCount", productCount);
                currentPrama.put("remark", remark);
                currentPrama.put("thirdOrderId", serviceNo);
                //调用新增订单接口
                String result = Aop.get(TokenService.class).gatewayRequest(url, currentPrama);
                JSONObject objectResult = JSONObject.parseObject(result);

                if(objectResult.getInteger("status") == 200 && objectResult.getJSONObject("data") !=null){
                    String orderId = objectResult.getJSONObject("data").getString("orderId");
                    System.out.println("==============>调用新增订单接口成功,返回orderId:"+orderId);
                }else {
                    System.out.println("==============>调用新增订单接口失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "F009FlowEvent工单申请流程结束";
    }

}