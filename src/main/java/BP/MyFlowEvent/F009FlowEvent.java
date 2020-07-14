package BP.MyFlowEvent;

import BP.DA.Log;
import BP.En.Row;
import BP.Tools.StringUtils;
import BP.WF.FlowEventBase;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;

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
            //1判断是否开始节点
            boolean isStartNode = this.HisNode.getIsStartNode();
            if (isStartNode) {

                //1.1若serviceNo为空,初始化流水号
                String serviceNo = this.getSysPara().get("serviceNo").toString();
                if(this.getSysPara().get("serviceNo") == null || "".equals(this.getSysPara().get("serviceNo").toString())){
                    String serviceSystem = this.getSysPara().get("serviceSystem") == null ? "" : this.getSysPara().get("serviceSystem").toString(); //服务单第三方系统
                    String serviceType = this.getSysPara().get("serviceType") == null ? "" : "1".equals(this.getSysPara().get("serviceType"))  ? "A":"S"; //服务单类型
                    String serviceSegmentation = this.getSysPara().get("serviceSegmentation") == null ? "" : this.getSysPara().get("serviceSegmentation").toString(); //安装细分L--晾衣机，S-锁 ，D--门
                    //计算hr_gongdan表数据条数
                    String dateTime = DateUtil.changeDateTOStr2(new Date());
                    int totalNum = Db.queryInt("select count(*) from hr_gongdan t where DATE_FORMAT(t.rdt,'%Y%m%d') ="+ dateTime ) + 1;
                    String STR_FORMAT = "0000";
                    DecimalFormat df = new DecimalFormat(STR_FORMAT);
                    String serialNum = df.format(totalNum); //流水号
                    serviceNo = serviceSystem + serviceSegmentation + serviceType + dateTime + serialNum; //服务单编号
                    Row row = this.HisEn.getRow();
                    row.SetValByKey("serviceNo",serviceNo);
                    this.HisEn.setRow(row);
                    //保存服务单号
                    this.HisEn.Update();
                }

                //1.2若系统是YX
                String serviceSystem = this.getSysPara().get("serviceSystem") == null ? "": this.getSysPara().get("serviceSystem").toString(); //服务单第三方系统
                if("YX".equals(serviceSystem)){
                    Map currentPrama = new HashMap();
                    Map currentJson= new HashMap();

                    //调用新增订单接口
                    if(!StringUtils.isEmpty(this.getSysPara().get("serviceType")))
                        currentPrama.put("type", Integer.parseInt(this.getSysPara().get("serviceType").toString())); //服务单类型
                    if(!StringUtils.isEmpty(this.getSysPara().get("reworkId")))
                        currentPrama.put("reworkId", Long.parseLong(this.getSysPara().get("reworkId").toString()));; //返修源单号
                    if(!StringUtils.isEmpty(this.getSysPara().get("factory")))
                        currentPrama.put("factory", Integer.parseInt(this.getSysPara().get("factory").toString())); //厂商单标志：厂商单传固定值 2
                    if(!StringUtils.isEmpty(this.getSysPara().get("facInWarranty")))
                        currentPrama.put("facInWarranty", Integer.parseInt(this.getSysPara().get("facInWarranty").toString())+1); //标识产品是否在保
                    if(!StringUtils.isEmpty(this.getSysPara().get("SMC")))
                        currentPrama.put("cityId", Integer.parseInt(this.getSysPara().get("SMC").toString().substring(0,4)+"00")); //城市id
                    if(!StringUtils.isEmpty(this.getSysPara().get("telephone")))
                        currentPrama.put("telephone", this.getSysPara().get("telephone").toString()); //用户手机号码
                    if(!StringUtils.isEmpty(this.getSysPara().get("contactName")))
                        currentPrama.put("contactName", this.getSysPara().get("contactName").toString()); //联系人姓名
                    if(!StringUtils.isEmpty(this.getSysPara().get("gender")))
                        currentPrama.put("gender", Integer.parseInt(this.getSysPara().get("gender").toString())); //性别
                    if(!StringUtils.isEmpty(this.getSysPara().get("address"))) {
                        currentPrama.put("street", this.getSysPara().get("address").toString()); //详细地址
                        currentPrama.put("address", this.getSysPara().get("address").toString()); //详细地址
                    }
                    if(!StringUtils.isEmpty(this.getSysPara().get("dutyTime")))
                        currentPrama.put("dutyTime", this.getSysPara().get("dutyTime").toString()); //预约时间
                    if(!StringUtils.isEmpty(this.getSysPara().get("productId")))
                        currentPrama.put("productId", (int)this.getSysPara().get("productId")); //言而有信产品ID
                    if(!StringUtils.isEmpty(this.getSysPara().get("facProductId")))
                        currentPrama.put("facProductId", this.getSysPara().get("facProductId").toString()); //厂商产品ID
                    if(!StringUtils.isEmpty(this.getSysPara().get("productCount")))
                        currentPrama.put("productCount", (int)this.getSysPara().get("productCount")); //产品数量
                    if(!StringUtils.isEmpty(this.getSysPara().get("orderDiscountAmount")))
                        currentJson.put("amount", (int) this.getSysPara().get("orderDiscountAmount")); //优惠金额，单位分
                    if(!StringUtils.isEmpty(this.getSysPara().get("orderDiscountSourceData")))
                        currentJson.put("sourceData", this.getSysPara().get("orderDiscountSourceData").toString()); //订单优惠快照
                    if(!StringUtils.isEmpty(this.getSysPara().get("orderDiscountRemark")))
                        currentJson.put("remark", this.getSysPara().get("orderDiscountRemark").toString()); //优惠备注
                    if(!StringUtils.isEmpty(this.getSysPara().get("remark")))
                        currentPrama.put("remark", this.getSysPara().get("remark").toString()); //服务单备注
                    currentPrama.put("orderDiscount", currentJson);
                    currentPrama.put("thirdOrderId", this.getSysPara().get("FK_Flow") + "-" + this.getSysPara().get("OID")+"-" + serviceNo);

                    YeyxService yeyxService = Aop.get(YeyxService.class);

                    long timestamp = System.currentTimeMillis()/1000;
                    String jsonStr = JSONObject.toJSONString(currentPrama);
                    String md5Str = yeyxService.getMd5(jsonStr,timestamp,"1");

                    //调用新增订单接口
                    Map param = new LinkedHashMap();
                    param.put("appId",yeyxService.getAppId());
                    param.put("sign",md5Str);
                    param.put("version","1");
                    param.put("timestamp",String.valueOf(timestamp));
                    param.put("jsonData",jsonStr);
                    Log.DebugWriteInfo("==============>调用新增订单接口发送参数:" + JSONObject.toJSONString(param));
                    String result = yeyxService.gatewayRequest(yeyxService.getPath() + "/createOrder", param);
                    JSONObject objectResult = JSONObject.parseObject(result);

                    if(objectResult.getInteger("status") == 200 && objectResult.getJSONObject("data") !=null){
                        String orderId = objectResult.getJSONObject("data").getString("orderId");
                        Log.DebugWriteInfo("==============>调用新增订单接口成功,返回orderId:"+orderId);
                        Row row = this.HisEn.getRow();
                        row.SetValByKey("orderId",orderId);
                        this.HisEn.setRow(row);
                        //保存服务单号
                        this.HisEn.Update();
                    }else {
                        Log.DebugWriteInfo("==============>调用新增订单接口失败:"+result);
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "F009FlowEvent工单申请流程";
    }

}