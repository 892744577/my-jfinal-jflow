package BP.MyFlowEvent;

import BP.WF.FlowEventBase;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.erp.yzj.service.YeyxService;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
                String serviceType = this.getSysPara().get("serviceType") == null ? "": this.getSysPara().get("serviceType").toString(); //服务单类型
                String reworkId = this.getSysPara().get("reworkId") == null ? "": this.getSysPara().get("reworkId").toString(); //返修源单号
                int factory = this.getSysPara().get("factory") == null ? 0: (int) this.getSysPara().get("factory"); //厂商单标志：厂商单传固定值 2
                int facInWarranty = this.getSysPara().get("facInWarranty") == null ? 0: (int) this.getSysPara().get("facInWarranty"); //标识产品是否在保
                int smcCityId = this.getSysPara().get("smcCityId") == null ? 0: (int) this.getSysPara().get("smcCityId"); //城市id
                String telephone = this.getSysPara().get("telephone") == null ? "": this.getSysPara().get("telephone").toString(); //用户手机号码
                String contactName = this.getSysPara().get("contactName") == null ? "": this.getSysPara().get("contactName").toString(); //联系人姓名
                String gender = this.getSysPara().get("gender") == null ? "": this.getSysPara().get("gender").toString(); //性别
                String address = this.getSysPara().get("address") == null ? "": this.getSysPara().get("address").toString(); //详细地址
                String dutyTime = this.getSysPara().get("dutyTime") == null ? "": this.getSysPara().get("dutyTime").toString(); //预约时间
                int productId = this.getSysPara().get("productId") == null ? 0: (int) this.getSysPara().get("productId"); //言而有信产品ID
                String facProductId = this.getSysPara().get("facProductId") == null ? "": this.getSysPara().get("facProductId").toString(); //厂商产品ID
                int productCount = this.getSysPara().get("productCount") == null ? 0: (int) this.getSysPara().get("productCount"); //产品数量
                int orderDiscountAmount = this.getSysPara().get("orderDiscountAmount") == null ? 0: (int) this.getSysPara().get("orderDiscountAmount"); //优惠金额，单位分
                String orderDiscountSourceData = this.getSysPara().get("orderDiscountSourceData") == null ? "": this.getSysPara().get("orderDiscountSourceData").toString(); //订单优惠快照
                String orderDiscountRemark = this.getSysPara().get("orderDiscountRemark") == null ? "": this.getSysPara().get("orderDiscountRemark").toString(); //优惠备注
                String remark = this.getSysPara().get("remark") == null ? "": this.getSysPara().get("remark").toString(); //服务单备注

                String serviceSystem = this.getSysPara().get("serviceSystem") == null ? "": this.getSysPara().get("serviceSystem").toString(); //服务单第三方系统
                String serviceSegmentation = this.getSysPara().get("serviceSegmentation") == null ? "": this.getSysPara().get("serviceSegmentation").toString(); //安装细分L--晾衣机，S-锁 ，D--门
                String dateTime = DateUtil.changeDateTOStr2(new Date());

                //计算hr_gongdan表数据条数
                int totalNum = Db.queryInt("select count(*) from hr_gongdan") + 1;
                String STR_FORMAT = "0000";
                DecimalFormat df = new DecimalFormat(STR_FORMAT);
                String serialNum = df.format(totalNum); //流水号

                String serviceNo = serviceSystem+serviceSegmentation+serviceType+dateTime+serialNum; //服务单编号

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

                YeyxService yeyxService = Aop.get(YeyxService.class);

                long timestamp = System.currentTimeMillis()/1000;
                String jsonStr = JSONObject.toJSONString(currentPrama);
                String md5Str = yeyxService.getMd5(jsonStr,timestamp);

                //调用新增订单接口
                Map param = new LinkedHashMap();
                param.put("appId",yeyxService.getAppId());
                param.put("sign",md5Str);
                param.put("version",1);
                param.put("timestamp",timestamp);
                param.put("jsonData",jsonStr);
                String result = yeyxService.gatewayRequest(url, param);
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