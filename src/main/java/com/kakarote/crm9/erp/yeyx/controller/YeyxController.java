package com.kakarote.crm9.erp.yeyx.controller;

import BP.DA.Log;
import BP.Port.Emp;
import BP.Tools.StringUtils;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.vo.*;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 言而有信第三方订单平台接口
 */
@Slf4j
public class YeyxController extends Controller {

    @Inject
    private HrGongDanService hrGongDanService;

    @Inject
    private YeyxService yeyxService;
    /**
     * 新增订单
     *
     */
    public void sendOrder(@Para("") HrGongdanRequest hrGongdanRequest){
        HrGongdan hrGongdan = HrGongdan.dao.findById(hrGongdanRequest.getOid());
        try {
            if("YX".equals(hrGongdan.getServiceSystem())){

                Map currentPrama = new HashMap();
                Map currentJson= new HashMap();

                //调用新增订单接口
                if(!StringUtils.isEmpty(hrGongdan.getServiceType()))
                    currentPrama.put("type", hrGongdan.getServiceType()); //服务单类型
                if(!StringUtils.isEmpty(hrGongdan.getReworkId()))
                    currentPrama.put("reworkId", hrGongdan.getReworkId()); //返修源单号
                if(!StringUtils.isEmpty(hrGongdan.getFactory()))
                    currentPrama.put("factory", hrGongdan.getFactory()); //厂商单标志：厂商单传固定值 2
                if(!StringUtils.isEmpty(hrGongdan.getFacInWarranty()))
                    currentPrama.put("facInWarranty", hrGongdan.getFacInWarranty()+1); //标识产品是否在保
                if(!StringUtils.isEmpty(Integer.parseInt(hrGongdan.getSMC().substring(0,4)+"00")))
                    currentPrama.put("cityId", Integer.parseInt(hrGongdan.getSMC().substring(0,4)+"00")); //城市id
                if(!StringUtils.isEmpty(hrGongdan.getTelephone()))
                    currentPrama.put("telephone", hrGongdan.getTelephone()); //用户手机号码
                if(!StringUtils.isEmpty(hrGongdan.getContactName()))
                    currentPrama.put("contactName", hrGongdan.getContactName()); //联系人姓名
                if(!StringUtils.isEmpty(hrGongdan.getGender()))
                    currentPrama.put("gender", hrGongdan.getGender()); //性别
                if(!StringUtils.isEmpty(hrGongdan.getAddress())) {
                    currentPrama.put("street", hrGongdan.getAddress()); //详细地址
                    currentPrama.put("address", hrGongdan.getAddress()); //详细地址
                }
                if(!StringUtils.isEmpty(hrGongdan.getDutyTime()))
                    currentPrama.put("dutyTime",hrGongdan.getDutyTime()); //预约时间
                if(!StringUtils.isEmpty(hrGongdan.getProductId()))
                    currentPrama.put("productId", hrGongdan.getProductId()); //言而有信产品ID
                if(!StringUtils.isEmpty(hrGongdan.getFacProductId()))
                    currentPrama.put("facProductId",hrGongdan.getFacProductId()); //厂商产品ID
                if(!StringUtils.isEmpty(hrGongdan.getProductCount()))
                    currentPrama.put("productCount", hrGongdan.getProductCount()); //产品数量
                if(!StringUtils.isEmpty(hrGongdan.getOrderDiscountAmount()))
                    currentJson.put("amount", hrGongdan.getOrderDiscountAmount()); //优惠金额，单位分
                if(!StringUtils.isEmpty(hrGongdan.getOrderDiscountSourceData()))
                    currentJson.put("sourceData", hrGongdan.getOrderDiscountSourceData()); //订单优惠快照
                if(!StringUtils.isEmpty(hrGongdan.getOrderDiscountRemark()))
                    currentJson.put("remark", hrGongdan.getOrderDiscountRemark()); //优惠备注
                if(!StringUtils.isEmpty(hrGongdan.getRemark()))
                    currentPrama.put("remark", hrGongdan.getRemark()); //服务单备注
                currentPrama.put("orderDiscount", currentJson);
                currentPrama.put("thirdOrderId", "009" + "-" + hrGongdan.get("OID")+"-" + hrGongdan.getServiceNo());

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
                    HrGongdan hrGongdanUpdate = new HrGongdan();
                    hrGongdanUpdate.setOID(hrGongdanRequest.getOid());
                    hrGongdanUpdate.setOrderId(orderId);
                    hrGongdanUpdate.update();
                    renderJson(R.ok().put("message", "成功"));
                }else {
                    Log.DebugWriteInfo("==============>调用新增订单接口失败:"+result);
                    renderJson(R.ok().put("code", 500).put("message", "result"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     */
    public void cancelOrder(@Para("") ToCancelOrderRequest toCancelOrderRequest){
        //调用新增订单接口
        if(!StringUtils.isEmpty(toCancelOrderRequest.getOrderId())){
            try{
                Map currentPrama = new HashMap();
                currentPrama.put("orderId", toCancelOrderRequest.getOrderId()); //服务单类型
                currentPrama.put("remark",toCancelOrderRequest.getRemark() ); //服务单类型
                //调用新增订单接口
                long timestamp = System.currentTimeMillis()/1000;
                String jsonStr = JSONObject.toJSONString(currentPrama);
                String md5Str = yeyxService.getMd5(jsonStr,timestamp,"1");
                Map param = new LinkedHashMap();
                param.put("appId",yeyxService.getAppId());
                param.put("sign",md5Str);
                param.put("version","1");
                param.put("timestamp",String.valueOf(timestamp));
                param.put("jsonData",jsonStr);
                Log.DebugWriteInfo("==============>调用新增订单接口发送参数:" + JSONObject.toJSONString(param));
                String result = yeyxService.gatewayRequest(yeyxService.getPath() + "/cancelOrder", param);
                JSONObject objectResult = JSONObject.parseObject(result);

                if(objectResult.getInteger("status") == 200){
                    log.info("==================取消订单成功orderId：" + toCancelOrderRequest.getOrderId());
                    renderJson(R.ok().put("code",0).put("message", "取消成功"));
                }else{
                    log.info("==================取消订单失败：" + result);
                    renderJson(R.ok().put("code",500).put("message", result));
                }
            } catch (Exception e) {
                renderJson(R.ok().put("code",40000).put("message","未知错误"));
                e.printStackTrace();
            }
        }else{
            renderJson(R.ok().put("code",40003).put("message","orderId不能为空"));
        }

    }
    /**
     * 统一入口
     */
    public void toDo(@Para("") ToDoRequest toDoRequest){
        if(toDoRequest.getJsonData() == null || "".equals(toDoRequest.getJsonData())) {
            renderJson(R.ok().put("code",40003).put("message","参数不能为空"));
        }else{
            try{
                if(WebUser.getNo()!=""){

                }else{
                    WebUser.SignInOfGenerAuth(new Emp("ZhouPan"), "ZhouPan");
                }

                if ("duty_time".equals(toDoRequest.getFunId())) {
                    log.info("==================接收改约时间：" + toDoRequest.getJsonData());
                    DutyTimeRequest dutyTimeRequest = JSONObject.parseObject(toDoRequest.getJsonData(),DutyTimeRequest.class);
                    this.duty_time(dutyTimeRequest);
                } else if ("master_info".equals(toDoRequest.getFunId())) {
                    log.info("==================接收派单时间：" + toDoRequest.getJsonData());
                    MasterInfoRequest masterInfoRequest = JSONObject.parseObject(toDoRequest.getJsonData(),MasterInfoRequest.class);
                    this.master_info(masterInfoRequest);
                } else if ("master_visit".equals(toDoRequest.getFunId())) {
                    log.info("==================接收上门时间：" + toDoRequest.getJsonData());
                    MasterVisitRequest masterVisitRequest = JSONObject.parseObject(toDoRequest.getJsonData(),MasterVisitRequest.class);
                    this.master_visit(masterVisitRequest);
                } else if ("order_complete".equals(toDoRequest.getFunId())) {
                    log.info("==================接收完成时间：" + toDoRequest.getJsonData());
                    OrderCompleteRequest orderCompleteRequest = JSONObject.parseObject(toDoRequest.getJsonData(),OrderCompleteRequest.class);
                    this.order_complete(orderCompleteRequest);
                } else if ("order_cancel".equals(toDoRequest.getFunId())) {
                    log.info("==================接收完成时间：" + toDoRequest.getJsonData());
                    OrderCancelRequest orderCancelRequest = JSONObject.parseObject(toDoRequest.getJsonData(),OrderCancelRequest.class);
                    this.order_cancel(orderCancelRequest);
                } else if ("factory_remark".equals(toDoRequest.getFunId())) {
                    log.info("==================接收完成时间：" + toDoRequest.getJsonData());
                    FactoryRemarkRequest factoryRemarkRequest = JSONObject.parseObject(toDoRequest.getJsonData(),FactoryRemarkRequest.class);
                    this.factory_remark(factoryRemarkRequest);
                }
                WebUser.Exit();
                renderJson(R.ok().put("code",200).put("message", "成功"));
            } catch (Exception e) {
                renderJson(R.ok().put("code",40000).put("message","未知错误"));
                e.printStackTrace();
            }
        }
    }

    /**
     * 改约，不涉及流程流转
     */
    @NotAction
    public void duty_time(DutyTimeRequest dutyTimeRequest){
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(Integer.valueOf(dutyTimeRequest.getThirdOrderId().split("-")[1]));
        hrGongdan.setDutyTime(new Date(dutyTimeRequest.getDutyTime().longValue()*1000));
        hrGongdan.update();
    }


    /**
     * 派单,流程从订单确认流转到下一环节，派单
     */
    @NotAction
    public void master_info(MasterInfoRequest masterInfoRequest) throws Exception{
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("masterName", masterInfoRequest.getMasterName());
            myhtSend.put("masterPhone", masterInfoRequest.getMasterPhone());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    masterInfoRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(masterInfoRequest.getThirdOrderId().split("-")[1]),
                    myhtSend,null,903,"ZhouPan");
    }
    /**
     * 上门
     */
    @NotAction
    public void master_visit(MasterVisitRequest masterVisitRequest) throws Exception{
        Hashtable myhtSend = new Hashtable();
        //发送流程
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                masterVisitRequest.getThirdOrderId().split("-")[0],
                Long.parseLong(masterVisitRequest.getThirdOrderId().split("-")[1]),
                myhtSend,null,905,"ZhouPan");
    }

    /**
     * 完成
     */
    @NotAction
    public void order_complete(OrderCompleteRequest orderCompleteRequest) throws Exception{
        Hashtable myhtSend = new Hashtable();
        //发送流程
        myhtSend.put("productPictureUrls", orderCompleteRequest.getProductPictureUrls());
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                orderCompleteRequest.getThirdOrderId().split("-")[0],
                Long.parseLong(orderCompleteRequest.getThirdOrderId().split("-")[1]),
                myhtSend, null, 906, "ZhouPan");
    }

    @NotAction
    public void order_cancel(OrderCancelRequest OrderCancelRequest){

    }

    /**
     * 商家备注，不涉及流程流转
     * @param factoryRemarkRequest
     */
    @NotAction
    public void factory_remark(FactoryRemarkRequest factoryRemarkRequest){
        HrGongdan hrGongdan = hrGongDanService.getHrGongdanByOrderId(factoryRemarkRequest);
        HrGongdan hrGongdanToUpdate = new HrGongdan();
        hrGongdanToUpdate.setOID(hrGongdan.getOID());
        hrGongdanToUpdate.setOtherRemark(factoryRemarkRequest.getRemark());
        hrGongdanToUpdate.update();
    }

}
