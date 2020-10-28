package BP.MyFlowEvent;

import BP.DA.Log;
import BP.En.Row;
import BP.Tools.StringUtils;
import BP.WF.FlowEventBase;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.wx.service.MpService;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanBook;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanRepair;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanZmnLog;
import com.kakarote.crm9.erp.yeyx.service.WanService;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;

import java.text.DecimalFormat;
import java.util.*;

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
            //下个节点名称
            String nextNodeID = "";
            String nextNodeName = "";
            for (BP.WF.SendReturnObj sendReturnObj: SendReturnObjs) {
                if ("VarToNodeID".equals(sendReturnObj.MsgFlag)) {
                    nextNodeID = sendReturnObj.MsgOfText;
                }
                if ("VarToNodeName".equals(sendReturnObj.MsgFlag)) {
                    nextNodeName = sendReturnObj.MsgOfText;
                }
            }

            //当工单去到服务完成时，售后单自动审核通过
            if("906".equals(nextNodeID) &&
                    (
                            "KS".equals(this.getSysPara().get("serviceSegmentation").toString()) ||
                                    "GS".equals(this.getSysPara().get("serviceSegmentation").toString()) ||
                                    "FGS".equals(this.getSysPara().get("serviceSegmentation").toString()) ||
                                    "KP".equals(this.getSysPara().get("serviceSegmentation").toString())
                    )
            ){
                Log.DebugWriteInfo("==============>调用新增订单生成serviceSp开始");
                HrGongdan hrGongdan = HrGongdan.dao.findById(this.getSysPara().get("OID").toString());
                hrGongdan.setServiceSp("1");
                hrGongdan.update();
                Log.DebugWriteInfo("==============>调用新增订单生成serviceSp结束");
            }

            String serviceSystem = this.getSysPara().get("serviceSystem") == null ? "": this.getSysPara().get("serviceSystem").toString(); //服务单第三方系统
            Row row = this.HisEn.getRow();
            if (isStartNode) {

                //1.1若serviceNo为空,初始化流水号
                String serviceNo = this.getSysPara().get("serviceNo").toString();
                if(this.getSysPara().get("serviceNo") == null || "".equals(this.getSysPara().get("serviceNo").toString())){
//                    String serviceSystem = this.getSysPara().get("serviceSystem") == null ? "" : this.getSysPara().get("serviceSystem").toString(); //服务单第三方系统
                    String serviceType = this.getSysPara().get("serviceType") == null ? "" : "1".equals(this.getSysPara().get("serviceType"))  ? "A":"S"; //服务单类型
                    String serviceSegmentation = this.getSysPara().get("serviceSegmentation") == null ? "" : this.getSysPara().get("serviceSegmentation").toString(); //安装细分L--晾衣机，S-锁 ，D--门
                    //计算hr_gongdan表数据条数
                    String dateTime = DateUtil.changeDateTOStr2(new Date());
                    int totalNum = Db.queryInt("select IFNULL(MAX(SUBSTRING(serviceNo,-4)) + 1,0) from hr_gongdan t where DATE_FORMAT(t.rdt,'%Y%m%d') ="+ dateTime );
                    String serialNum = (new DecimalFormat("0000")).format(totalNum);//流水号格式化
                    serviceNo = serviceSystem + serviceSegmentation + serviceType + dateTime + serialNum; //服务单编号
                    row.SetValByKey("serviceNo",serviceNo);
                    Log.DebugWriteInfo("==============>调用新增订单生成serviceNo:" + serviceNo);
                }
                //1.2若是预约单或报修单将旧单改为已生成工单
                if(this.getSysPara().get("preServiceNo") != null){
                    String preServiceNo = this.getSysPara().get("preServiceNo") == null ? "" : this.getSysPara().get("preServiceNo").toString(); //预约单或报修单号
                    if(preServiceNo.contains("YY")){
                        HrGongdanBook hrGongdanBook = new HrGongdanBook();
                        hrGongdanBook.setOrderNumber(preServiceNo);
                        hrGongdanBook.setIsGenerate(1);
                        hrGongdanBook.update();
                    }else if(preServiceNo.contains("BX")){
                        HrGongdanRepair hrGongdanRepair = new HrGongdanRepair();
                        hrGongdanRepair.setOrderNumber(preServiceNo);
                        hrGongdanRepair.setIsGenerate(1);
                        hrGongdanRepair.update();
                    }
                }
                //1.3若系统是YX
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
                        currentPrama.put("productId", Integer.parseInt(this.getSysPara().get("productId").toString())); //言而有信产品ID
                    if(!StringUtils.isEmpty(this.getSysPara().get("facProductId")))
                        currentPrama.put("facProductId", this.getSysPara().get("facProductId").toString()); //厂商产品ID
                    if(!StringUtils.isEmpty(this.getSysPara().get("productCount")))
                        currentPrama.put("productCount", Integer.parseInt(this.getSysPara().get("productCount").toString())); //产品数量
                    if(!StringUtils.isEmpty(this.getSysPara().get("orderDiscountAmount")))
                        currentJson.put("amount", Integer.parseInt(this.getSysPara().get("orderDiscountAmount").toString())); //优惠金额，单位分
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
                    Log.DebugWriteInfo("==============>调用新增订单接口返回结果:" + result);
                    JSONObject objectResult = JSONObject.parseObject(result);
                    if(objectResult.getInteger("status") == 200 && objectResult.getJSONObject("data") !=null){
                        String orderId = objectResult.getJSONObject("data").getString("orderId");
                        Log.DebugWriteInfo("==============>调用新增订单接口成功,返回orderId:"+orderId);
                        row.SetValByKey("orderId",orderId);
                        //记录日志
                        HrGongdanZmnLog hrGongdanZmnLog = new HrGongdanZmnLog();
                        hrGongdanZmnLog.setFuncId("createOrder");
                        hrGongdanZmnLog.setThirdOrderId(this.getSysPara().get("FK_Flow") + "-" + this.getSysPara().get("OID")+"-" + serviceNo);
                        hrGongdanZmnLog.setOrderId(orderId);
                        hrGongdanZmnLog.setOptTime(new Date().getTime()/1000);
                        hrGongdanZmnLog.save();
                    }else {
                        Log.DebugWriteInfo("==============>调用新增订单接口失败");
                        return result;
                    }
                   //保存本系统服务单号、及第三方系统单号
                   this.HisEn.setRow(row);
                   this.HisEn.Update();
                   Log.DebugWriteInfo("==============>调用新增订单更新服务单信息");

               }else if ("WSF".equals(serviceSystem)) {
                   //add by wangkaidda 调用万师傅下单接口
                   List wanList = new ArrayList();
                   Map currentPrama = new HashMap();

                   //调用新增订单接口
                   if(!StringUtils.isEmpty(this.getSysPara().get("contactName")))
                       currentPrama.put("buyerName", this.getSysPara().get("contactName").toString()); //联系人姓名
                       currentPrama.put("contactName", this.getSysPara().get("contactName").toString()); //联系人姓名
                   if(!StringUtils.isEmpty(this.getSysPara().get("telephone")))
                       currentPrama.put("buyerPhone", this.getSysPara().get("telephone").toString()); //用户手机号码
                       currentPrama.put("contactPhone", this.getSysPara().get("telephone").toString()); //用户手机号码
                   if(!StringUtils.isEmpty(this.getSysPara().get("smcProvinceIdT")))
                       currentPrama.put("province", this.getSysPara().get("smcProvinceIdT").toString()); //省
                   if(!StringUtils.isEmpty(this.getSysPara().get("smcCityIdT")))
                       currentPrama.put("city", this.getSysPara().get("smcCityIdT").toString()); //城市
                   if(!StringUtils.isEmpty(this.getSysPara().get("smcDistrictIdT")))
                       currentPrama.put("county", this.getSysPara().get("smcDistrictIdT").toString()); //区
                   if(!StringUtils.isEmpty(this.getSysPara().get("address")))
                       currentPrama.put("address", this.getSysPara().get("address").toString()); //详细地址
                   if(!StringUtils.isEmpty(this.getSysPara().get("remark")))
                       currentPrama.put("buyerNote", this.getSysPara().get("remark").toString()); //服务单备注
                   if(!StringUtils.isEmpty(this.getSysPara().get("customArriveStatus")))
                       currentPrama.put("customArriveStatus", this.getSysPara().get("customArriveStatus").toString()); //货物是否到客户家
                   currentPrama.put("orderId", this.getSysPara().get("FK_Flow") + "-" + this.getSysPara().get("OID")+"-" + serviceNo);
                   //单子是安装还是维修
                   if("DS".equals(this.getSysPara().get("serviceSegmentation").toString())
                           ||"SS".equals(this.getSysPara().get("serviceSegmentation").toString())
                           ||"L".equals(this.getSysPara().get("serviceSegmentation").toString())){
                       currentPrama.put("serveType", 4); //安装
                   }else{
                       currentPrama.put("serveType", 5); //维修
                   }
                   //新增商品列表
                   List goodsList = new ArrayList();
                   Map goodsList1 = new HashMap();

                   //商品信息
                   JSONObject productOneT = JSONObject.parseObject(this.getSysPara().get("productOneT").toString());
                   if(!StringUtils.isEmpty(productOneT.get("listPicUrl")))
                       goodsList1.put("goodsImgUrl", productOneT.get("listPicUrl"));
                   else
                       goodsList1.put("goodsImgUrl", "http://pic1.nipic.com/2008-08-14/2008814183939909_2.jpg");


                   if("12122".equals(this.getSysPara().get("facProductId").toString())){
                       currentPrama.put("serveCategory", 17); //服务单，晾衣架
                       currentPrama.put("toMasterId", 4957974691L); //总包，晾衣架
                       goodsList1.put("goodsCategory",323); //晾衣架
                       goodsList1.put("categoryChild",324); //晾衣架
                       if(!StringUtils.isEmpty(productOneT.get("name")))
                           goodsList1.put("goodsName", productOneT.get("name"));
                       else
                           goodsList1.put("goodsName", "晾衣架");
                   }else{
                       currentPrama.put("serveCategory", 15); //服务单，智能锁
                       currentPrama.put("toMasterId", 4957869477L); //总包，智能锁
                       goodsList1.put("goodsCategory",338); //根类型
                       goodsList1.put("categoryChild",0); //半自动智能锁
                       if(!StringUtils.isEmpty(productOneT.get("name")))
                           goodsList1.put("goodsName", productOneT.get("name"));
                       else
                           goodsList1.put("goodsName", "智能锁");
                   }
                   goodsList1.put("goodsNote",this.getSysPara().get("serviceSegmentationT").toString());
                   goodsList1.put("goodsNumber",Integer.parseInt(this.getSysPara().get("productCount").toString()));


                   goodsList.add(goodsList1);
                   currentPrama.put("goodsList", goodsList);


                   wanList.add(currentPrama);
                   String jsonStr = JSONObject.toJSONString(wanList);

                   //加密参数
                   WanService wanService = Aop.get(WanService.class);
                   String reqJsonStr = wanService.getJsonData(jsonStr);

                   //调用新增订单接口
                   Log.DebugWriteInfo("==============>调用新增订单接口发送参数:" + jsonStr);
                   Log.DebugWriteInfo("==============>调用新增订单接口发送参数:" + reqJsonStr);
                   String result = wanService.gatewayRequestJson(wanService.getPath() + "/order/batchCreateAsync", reqJsonStr);
                   Log.DebugWriteInfo("==============>调用新增订单接口返回结果:" + result);
               }
               this.HisEn.Update();
               Log.DebugWriteInfo("==============>服务商更新服务单信息");
            }
            //发送公众号信息
            sendMpMsg(serviceSystem,nextNodeID,nextNodeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "F009FlowEvent工单申请流程";
    }

    /*
     * @Description //进行公众号信息推送
     * @Author wangkaida
     * @Date 15:22 2020/8/28
     * @Param [serviceSystem, nextNodeName]
     * @return void
     **/
    public void sendMpMsg(String serviceSystem,String nextNodeID,String nextNodeName) {
        if("FWS".equals(serviceSystem)){
            //如果是服务商，则获取下一节点处理人信息
            String acceptor = this.getSysPara().get("acceptor").toString();
            PortEmp portEmp = new PortEmp();
            portEmp.setNo(acceptor);
            PortEmp portEmpDb = Aop.get(PortEmpService.class).getPortEmpByNo(portEmp);

            if (portEmpDb != null) {
                String openId = portEmpDb.getWxOpenId();

                if(!StringUtils.isEmpty(openId)) {
                    //进行信息推送
                    MpMsgSendReq mpReq = new MpMsgSendReq();
                    mpReq.setTouser(openId);
                    mpReq.setTemplate_id("XTmM0MzNMV-9ZKjPh4AFRwOgrGFM1nnFDGsLoS-erA0");
                    mpReq.setPage("pages/index/index");

                    JSONArray jsonArray=new JSONArray();
                    String title = "908".equals(nextNodeID) ? "你有新的订单! ":nextNodeName;
                    jsonArray.add(new JSONObject().fluentPut("name","first").fluentPut("value",title+(String) this.getSysPara().get("serviceNo")));
                    jsonArray.add(new JSONObject().fluentPut("name","keyword1").fluentPut("value",(String) this.getSysPara().get("serviceSegmentationT")));
                    jsonArray.add(new JSONObject().fluentPut("name","keyword2").fluentPut("value",(String) this.getSysPara().get("contactName")+"\n"+(String) this.getSysPara().get("telephone")));
                    jsonArray.add(new JSONObject().fluentPut("name","remark").fluentPut("value",(String) this.getSysPara().get("remark")));

                    mpReq.setData(jsonArray.toJSONString());
                    Log.DebugWriteInfo("=====================发送通知请求参数："+jsonArray.toJSONString());
                    Aop.get(MpService.class).send(mpReq);
                }else {
                    Log.DebugWriteInfo("进行小程序信息推送获取到的员工小程序openId为空!"+acceptor);
                }

            }else {
                Log.DebugWriteInfo("进行小程序信息推送获取到的员工信息为空!"+acceptor);
            }
        }
    }

}