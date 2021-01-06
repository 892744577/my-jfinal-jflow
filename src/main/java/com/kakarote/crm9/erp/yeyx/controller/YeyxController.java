package com.kakarote.crm9.erp.yeyx.controller;

import BP.DA.Log;
import BP.Port.Emp;
import BP.Tools.StringUtils;
import BP.WF.GenerWorkFlow;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanZmnLog;
import com.kakarote.crm9.erp.yeyx.entity.vo.*;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
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
        //重新生成serviceNo
        hrGongdan.setServiceNo(this.getNewServiceNo(hrGongdan));
        //发送
        String result = this.toSendOrder(hrGongdanRequest,hrGongdan);
        JSONObject objectResult = JSONObject.parseObject(result);
        if(objectResult.getInteger("status") == 200 && objectResult.getJSONObject("data") !=null){
            String orderId = objectResult.getJSONObject("data").getString("orderId");
            Log.DebugWriteInfo("==============>调用新增订单接口成功,返回orderId:"+orderId);
            HrGongdan hrGongdanUpdate = new HrGongdan();
            hrGongdanUpdate.setOID(hrGongdanRequest.getOid());
            hrGongdanUpdate.setOrderId(orderId);
            hrGongdanUpdate.setServiceNo(hrGongdan.getServiceNo());
            hrGongdanUpdate.update();
            renderJson(R.ok().put("message", "成功"));

            //记录日志
            HrGongdanZmnLog hrGongdanZmnLog = new HrGongdanZmnLog();
            hrGongdanZmnLog.setFuncId("ReSendOrder");
            hrGongdanZmnLog.setThirdOrderId("009-" + hrGongdanRequest.getOid()+"-" + hrGongdan.getServiceNo());
            hrGongdanZmnLog.setOrderId(orderId);
            hrGongdanZmnLog.setOptTime(new Date().getTime()/1000);
            hrGongdanZmnLog.save();
        }else {
            Log.DebugWriteInfo("==============>调用新增订单接口失败");
            renderJson(R.ok().put("code", 500).put("message", "result"));
        }
    }

    @NotAction
    public String toSendOrder(HrGongdanRequest hrGongdanRequest,HrGongdan hrGongdan){
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

            //如果是取消发送，重新生成serviceNo
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
            String result ="";
            try {
                result = yeyxService.gatewayRequest(yeyxService.getPath() + "/createOrder", param);
                log.info("==============>调用新增订单接口返回数据:"+result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else{
            return "";
        }
    }

    /**
     * 生成新的serviceNo
     * @param hrGongdan
     */
    @NotAction
    public String getNewServiceNo(HrGongdan hrGongdan){
        //重新生成serviceNo
        String serviceSystem = hrGongdan.getServiceSystem() == null ? "" : hrGongdan.getServiceSystem(); //服务单第三方系统
        String serviceType = hrGongdan.getServiceType() == null ? "" : "1".equals(hrGongdan.getServiceType())  ? "A":"S"; //服务单类型
        String serviceSegmentation = hrGongdan.getServiceSegmentation() == null ? "" : hrGongdan.getServiceSegmentation(); //安装细分L--晾衣机，S-锁 ，D--门
        //计算hr_gongdan表数据条数
        String dateTime = DateUtil.changeDateTOStr2(new Date());
        int totalNum = Db.queryInt("select IFNULL(MAX(SUBSTRING(serviceNo,-4)) + 1,0) from hr_gongdan t where DATE_FORMAT(t.rdt,'%Y%m%d') ="+ dateTime );
        String STR_FORMAT = "0000";
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        String serialNum = df.format(totalNum); //流水号
        String random = (new DecimalFormat("00")).format((System.currentTimeMillis()%100));//毫秒数的后两位当随机数
        String serviceNo = serviceSystem  + serviceSegmentation + serviceType + dateTime + random + serialNum; //服务单编号
        return serviceNo;
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
                log.info("==============>调用取消订单接口发送参数:" + JSONObject.toJSONString(param));
                String result = yeyxService.gatewayRequest(yeyxService.getPath() + "/cancelOrder", param);
                JSONObject objectResult = JSONObject.parseObject(result);

                if(objectResult.getInteger("status") == 200){
                    log.info("==================取消订单成功orderId：" + toCancelOrderRequest.getOrderId());
                    if("2".equals(toCancelOrderRequest.getCancelType())){
                        /***
                         * type==2 取消订单后且重新发送
                         */
                        //将用户状态流转到订单取消，将orderId置空
                        HrGongdan hrGongdanUpdate = new HrGongdan();
                        hrGongdanUpdate.setOID(toCancelOrderRequest.getOid());
                        hrGongdanUpdate.setOrderId("");  //更新新的orderId
                        hrGongdanUpdate.update();
                        //组装取消并新增的请求参数
                        HrGongdanRequest hrGongdanRequest = new HrGongdanRequest();
                        hrGongdanRequest.setOid(toCancelOrderRequest.getOid());

                        //查询工单
                        HrGongdan hrGongdan = HrGongdan.dao.findById(hrGongdanRequest.getOid());
                        hrGongdan.setServiceNo(this.getNewServiceNo(hrGongdan));

                        //发送
                        String addResult = this.toSendOrder(hrGongdanRequest,hrGongdan);
                        JSONObject addObjectResult = JSONObject.parseObject(addResult);
                        if(addObjectResult.getInteger("status") == 200 && addObjectResult.getJSONObject("data") !=null){
                            String orderId = addObjectResult.getJSONObject("data").getString("orderId");
                            Log.DebugWriteInfo("==============>取消成功且重新调用新增订单接口成功,返回orderId:"+orderId);
                            HrGongdan hrGongdanAdd = new HrGongdan();
                            hrGongdanAdd.setOID(toCancelOrderRequest.getOid());
                            hrGongdanAdd.setOrderId(orderId);  //更新新的orderId
                            hrGongdanAdd.setServiceNo(hrGongdan.getServiceNo()); //更新ServiceNo
                            hrGongdanAdd.update();
                            renderJson(R.ok().put("code",0).put("message", "取消成功且重新调用新增订单接口成功"));
                        }else {
                            Log.DebugWriteInfo("==============>取消成功后撤回发送："+JSONObject.toJSONString(toCancelOrderRequest));
                            BP.WF.Dev2Interface.Flow_ReSend(toCancelOrderRequest.getOid(),907,"tangmanrong","");
                            Log.DebugWriteInfo("==============>取消成功但重新调用新增订单接口失败:"+addResult);

                            renderJson(R.ok().put("code", 502).put("message", "result"));
                        }
                    }else{
                        /***
                         * type==1 仅取消
                         */
                        //查询工单
                        HrGongdan hrGongdan = HrGongdan.dao.findById(toCancelOrderRequest.getOid());
                        //录入日志
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                "cancelOrder",
                                "009-"+toCancelOrderRequest.getOid()+"-"+hrGongdan.getServiceNo(),
                                toCancelOrderRequest.getOrderId(),
                                new Date().getTime());
                        hrGongdanZmnLog.setCancelRemark("客服主动取消");
                        hrGongdanZmnLog.save();
                        //取消成功并取消流程
                        Log.DebugWriteInfo("==============>取消成功后取消流程："+JSONObject.toJSONString(toCancelOrderRequest));
                        BP.WF.Dev2Interface.Flow_ReSend(toCancelOrderRequest.getOid(),907,hrGongdan.getEmps(),"");
                        renderJson(R.ok().put("code",0).put("message", "取消成功"));
                    }
                }else{
                    log.info("==================取消订单失败：" + result);
                    renderJson(R.ok().put("code",501).put("message", result));
                }
            } catch (Exception e) {
                renderJson(R.ok().put("code",40000).put("message","未知错误"));
                e.printStackTrace();
            }
        }else{
            renderJson(R.ok().put("code",40003).put("message","orderId不能为空"));
        }
    }

    public void testCancel(@Para("") ToCancelOrderRequest toCancelOrderRequest){
        try{

            renderJson(R.ok().put("code",0).put("message", BP.WF.Dev2Interface.Flow_ReSend(toCancelOrderRequest.getOid(),907,"tangmanrong","")));
        } catch (Exception e) {
            renderJson(R.ok().put("code",40000).put("message","未知错误"));
            e.printStackTrace();
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
                log.info("==================当前登陆人："+WebUser.getNo());
                if(WebUser.getNo()!=""){

                }else{
                    WebUser.SignInOfGenerAuth(new Emp("ZhouPan"), "ZhouPan");
                }

                if ("duty_time".equals(toDoRequest.getFunId())) {
                    log.info("==================接收改约时间：" + toDoRequest.getJsonData());
                    DutyTimeRequest dutyTimeRequest = JSONObject.parseObject(toDoRequest.getJsonData(),DutyTimeRequest.class);
                    if(StringUtils.isNotEmpty(dutyTimeRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                dutyTimeRequest.getThirdOrderId(),
                                dutyTimeRequest.getOrderId(),
                                dutyTimeRequest.getOptTime());
                        hrGongdanZmnLog.setDutyTime(dutyTimeRequest.getDutyTime());
                        hrGongdanZmnLog.save();

                        //数据
                        this.duty_time(dutyTimeRequest);
                        log.info("==================接收改约时间成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("master_info".equals(toDoRequest.getFunId())) {
                    log.info("==================接收派单时间：" + toDoRequest.getJsonData());
                    MasterInfoRequest masterInfoRequest = JSONObject.parseObject(toDoRequest.getJsonData(),MasterInfoRequest.class);
                    if(StringUtils.isNotEmpty(masterInfoRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                masterInfoRequest.getThirdOrderId(),
                                masterInfoRequest.getOrderId(),
                                masterInfoRequest.getOptTime());
                        hrGongdanZmnLog.setMasterName(masterInfoRequest.getMasterName());
                        hrGongdanZmnLog.setMasterPhone(masterInfoRequest.getMasterPhone());
                        hrGongdanZmnLog.save();

                        this.master_info(masterInfoRequest);
                        log.info("==================接收派单时间成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("master_visit".equals(toDoRequest.getFunId())) {
                    log.info("==================接收上门时间：" + toDoRequest.getJsonData());
                    MasterVisitRequest masterVisitRequest = JSONObject.parseObject(toDoRequest.getJsonData(),MasterVisitRequest.class);
                    if(StringUtils.isNotEmpty(masterVisitRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                masterVisitRequest.getThirdOrderId(),
                                masterVisitRequest.getOrderId(),
                                masterVisitRequest.getOptTime());
                        hrGongdanZmnLog.save();

                        this.master_visit(masterVisitRequest);
                        log.info("==================接收上门时间成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("order_complete".equals(toDoRequest.getFunId())) {
                    log.info("==================接收完成时间：" + toDoRequest.getJsonData());
                    OrderCompleteRequest orderCompleteRequest = JSONObject.parseObject(toDoRequest.getJsonData(),OrderCompleteRequest.class);
                    if(StringUtils.isNotEmpty(orderCompleteRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                orderCompleteRequest.getThirdOrderId(),
                                orderCompleteRequest.getOrderId(),
                                orderCompleteRequest.getOptTime());
                        hrGongdanZmnLog.setCompleteRemark(orderCompleteRequest.getRemark());
                        hrGongdanZmnLog.setCompleteUrl(orderCompleteRequest.getProductPictureUrls());
                        hrGongdanZmnLog.save();
                        this.order_complete(orderCompleteRequest);
                        log.info("==================接收完成时间成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("order_cancel".equals(toDoRequest.getFunId())) {
                    log.info("==================接收取消：" + toDoRequest.getJsonData());
                    OrderCancelRequest orderCancelRequest = JSONObject.parseObject(toDoRequest.getJsonData(),OrderCancelRequest.class);
                    if(StringUtils.isNotEmpty(orderCancelRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                orderCancelRequest.getThirdOrderId(),
                                orderCancelRequest.getOrderId(),
                                orderCancelRequest.getOptTime());
                        hrGongdanZmnLog.setCancelRemark(orderCancelRequest.getRemark());
                        hrGongdanZmnLog.save();

                        this.order_cancel(orderCancelRequest);
                        log.info("==================接收取消成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("factory_remark".equals(toDoRequest.getFunId())) {
                    log.info("==================接收商家备注：" + toDoRequest.getJsonData());
                    FactoryRemarkRequest factoryRemarkRequest = JSONObject.parseObject(toDoRequest.getJsonData(),FactoryRemarkRequest.class);
                    if(StringUtils.isNotEmpty(factoryRemarkRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                factoryRemarkRequest.getThirdOrderId(),
                                factoryRemarkRequest.getOrderId(),
                                factoryRemarkRequest.getOptTime());
                        hrGongdanZmnLog.setFactoryRemark(factoryRemarkRequest.getRemark());
                        hrGongdanZmnLog.save();

                        this.factory_remark(factoryRemarkRequest);
                        log.info("==================接收商家备注成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                } else if ("order_suspend".equals(toDoRequest.getFunId())) {
                    log.info("===================订单挂起：" + toDoRequest.getJsonData());
                    OrderSuspendRequest orderSuspendRequest = JSONObject.parseObject(toDoRequest.getJsonData(),OrderSuspendRequest.class);
                    if(StringUtils.isNotEmpty(orderSuspendRequest.getThirdOrderId())){
                        //录入记录
                        HrGongdanZmnLog hrGongdanZmnLog = saveHrGongdanZmnLog(
                                toDoRequest.getFunId(),
                                orderSuspendRequest.getThirdOrderId(),
                                orderSuspendRequest.getOrderId(),
                                orderSuspendRequest.getOptTime());
                        hrGongdanZmnLog.save();

                        this.order_suspend(orderSuspendRequest);
                        log.info("==================订单挂起成功" );
                    }else{
                        renderJson(R.ok().put("code",404).put("message", "订单号为空"));
                    }
                }
                WebUser.Exit();
                renderJson(R.ok().put("code",200).put("message", "成功"));
            } catch (Exception e) {
                log.error("==================接收信息流转异常原因：",e);
                log.error("==================接收信息流转异常堆栈：",e.getStackTrace());
                renderJson(R.ok().put("code",40000).put("message","未知错误"));
            }
        }
    }

    @NotAction
    public HrGongdanZmnLog saveHrGongdanZmnLog(String funcId,String thirdOrderId,String orderId,Long optTime){
        //录入记录
        HrGongdanZmnLog hrGongdanZmnLog = new HrGongdanZmnLog();
        hrGongdanZmnLog.setFuncId(funcId);
        hrGongdanZmnLog.setThirdOrderId(thirdOrderId);
        hrGongdanZmnLog.setOrderId(orderId);
        hrGongdanZmnLog.setOptTime(optTime);
        return hrGongdanZmnLog;
    }

    /**
     * 改约，不涉及流程流转
     */
    @NotAction
    public void duty_time(DutyTimeRequest dutyTimeRequest){
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(Integer.valueOf(dutyTimeRequest.getThirdOrderId().split("-")[1]));
        hrGongdan.setDutyTime(DateUtil.changeDateTOStr(new Date(dutyTimeRequest.getDutyTime().longValue()*1000)));
        hrGongdan.update();
    }


    /**
     * 确认订单->分派成功
     */
    @NotAction
    public void master_info(MasterInfoRequest masterInfoRequest) throws Exception{
            //若当前节点不是902，则不流转，直接更新数据
            GenerWorkFlow gwf = new GenerWorkFlow();
            gwf.setWorkID(Long.parseLong(masterInfoRequest.getThirdOrderId().split("-")[1]));
            gwf.RetrieveFromDBSources();

            //若是确认订单节点，往下流转，其他得只是更新数据
            if(902 == gwf.getFK_Node()){
                //发送流程
                Hashtable myhtSend = new Hashtable();
                myhtSend.put("masterName", masterInfoRequest.getMasterName());
                myhtSend.put("masterPhone", masterInfoRequest.getMasterPhone());
                SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                        masterInfoRequest.getThirdOrderId().split("-")[0],
                        Long.parseLong(masterInfoRequest.getThirdOrderId().split("-")[1]),
                        myhtSend,null,903,null);
            }else{
                HrGongdan hrGongdan = new HrGongdan();
                hrGongdan.setOID(Integer.valueOf(masterInfoRequest.getThirdOrderId().split("-")[1]));
                hrGongdan.setMasterName(masterInfoRequest.getMasterName());
                hrGongdan.setMasterPhone(masterInfoRequest.getMasterPhone());
                hrGongdan.update();
            }

    }

    /**
     * 分派成功->预约确认
     * @param masterVisitRequest
     * @throws Exception
     */
    @NotAction
    public void master_visit(MasterVisitRequest masterVisitRequest) throws Exception{
        //若当前节点不是903，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(masterVisitRequest.getThirdOrderId().split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(903 == gwf.getFK_Node()){
            //发送流程
            Hashtable myhtSend = new Hashtable();
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    masterVisitRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(masterVisitRequest.getThirdOrderId().split("-")[1]),
                    myhtSend,null,905,null);
        }

    }

    /**
     * 预约确认->完成
     * @param orderCompleteRequest
     * @throws Exception
     */
    @NotAction
    public void order_complete(OrderCompleteRequest orderCompleteRequest) throws Exception{
        //若当前节点不是905，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(orderCompleteRequest.getThirdOrderId().split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(905 == gwf.getFK_Node() || 903 == gwf.getFK_Node()){
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("productPictureUrls", orderCompleteRequest.getProductPictureUrls());
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    orderCompleteRequest.getThirdOrderId().split("-")[0],
                    Long.parseLong(orderCompleteRequest.getThirdOrderId().split("-")[1]),
                    myhtSend, null, 906, null);
        }else{
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.valueOf(orderCompleteRequest.getThirdOrderId().split("-")[1]));
            hrGongdan.setProductPictureUrls(orderCompleteRequest.getProductPictureUrls());
            hrGongdan.update();
        }
    }

    @NotAction
    public void order_cancel(OrderCancelRequest orderCancelRequest) throws Exception{
        Hashtable myhtSend = new Hashtable();
        myhtSend.put("cancelRemark", orderCancelRequest.getRemark());
        SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                orderCancelRequest.getThirdOrderId().split("-")[0],
                Long.parseLong(orderCancelRequest.getThirdOrderId().split("-")[1]),
                myhtSend, null, 907, null);
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

    /**
     * 挂起，不涉及流程流转
     */
    @NotAction
    public void order_suspend(OrderSuspendRequest orderSuspendRequest){
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(Integer.valueOf(orderSuspendRequest.getThirdOrderId().split("-")[1]));
        hrGongdan.setNextContactTime(DateUtil.changeDateTOStr(new Date(orderSuspendRequest.getNextContactTime().longValue()*1000)));
        hrGongdan.update();
    }

}
