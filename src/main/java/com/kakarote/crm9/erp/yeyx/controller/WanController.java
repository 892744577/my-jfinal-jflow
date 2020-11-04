package com.kakarote.crm9.erp.yeyx.controller;

import BP.Port.Emp;
import BP.Tools.StringUtils;
import BP.WF.GenerWorkFlow;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.util.EncodeUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanWsfLog;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.ToCancelOrderRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.WanGoodsConfigRequest;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yeyx.service.WanService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 万师傅接口对接类
 */
@Slf4j
public class WanController extends Controller {
    @Inject
    private HrGongDanService hrGongDanService;
    @Inject
    private WanService wanService;
    /**
     * 获取商品类目
     */
    public void getServerCategory(@Para("") WanGoodsConfigRequest wanGoodsConfigRequest) throws Exception {
        Map currentPrama = new HashMap();
        currentPrama.put("licenseId",wanService.getLicenseId());
        currentPrama.put("timestamp",System.currentTimeMillis());
        currentPrama.put("serverCategoryId",wanGoodsConfigRequest.getServerCategoryId());
        renderJson(WanService.gatewayRequestJson(wanService.getPath() + "/orderConfig/getGoodsConfig", JSONObject.toJSONString(currentPrama)));
    }

    /**
     * 新增订单
     *
     */
    public void sendOrder(@Para("") HrGongdanRequest hrGongdanRequest){

    }
    /**
     * 取消订单
     */
    public void cancelOrder(@Para("") ToCancelOrderRequest toCancelOrderRequest) throws Exception {
        HrGongdan hrGongdan = hrGongDanService.getHrGongdanByOrderId(toCancelOrderRequest);
        Map currentPrama = new HashMap();
        currentPrama.put("licenseId",wanService.getLicenseId());
        currentPrama.put("timestamp",System.currentTimeMillis());
        currentPrama.put("thirdOrderId","009-" + hrGongdan.getOID()+"-" + hrGongdan.getServiceNo());
        currentPrama.put("reason",toCancelOrderRequest.getRemark());
        log.info("发送取消通知===========:"+ JSONObject.toJSONString(currentPrama));
        WanService.gatewayRequestJson(wanService.getPath() + "/order/close", JSONObject.toJSONString(currentPrama));
        renderJson(R.ok().put("code",0).put("message", "取消通知发送成功"));
    }
    /**
     * 下单结果回调通知
     */
    public void toResult(){
        String rawData = getRawData();
        log.info("下单结果回调通知rawData===========:"+rawData);
        String busData = new String(EncodeUtil.decryptBASE64(JSONObject.parseObject(rawData).getString("busData")));
        log.info("下单结果回调通知busData===========:"+busData);
        JSONObject jsonObject = JSONObject.parseObject(busData);
        JSONArray array = jsonObject.getJSONArray("successInfo");
        for(int i=0;i<array.size();i++){
            JSONObject temp = array.getJSONObject(i);
            //更新orderId
            String thirdOrderId = temp.getString("thirdOrderId");
            String wanshifuOrderNo = temp.getString("wanshifuOrderNo");
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.parseInt(thirdOrderId.split("-")[1]));
            hrGongdan.setOrderId(wanshifuOrderNo);
            hrGongdan.update();
            //记录日志
            HrGongdanWsfLog hrGongdanWsfLog = saveHrGongdanWsfLog("createOrder",thirdOrderId,wanshifuOrderNo,(new Date()).getTime()/1000);
            hrGongdanWsfLog.save();
        }
        renderJson();
    }
    /**
     * 订单节点回调通知
     */
    public void toDo(){
        String rawData = getRawData();
        log.info("订单节点回调通知rawData===========:"+rawData);
        JSONObject rawJSONObject = JSONObject.parseObject(rawData);
        JSONObject jsonObject = JSONObject.parseObject(new String(EncodeUtil.decryptBASE64(rawJSONObject.getString("busData"))));
        log.info("订单节点回调通知busData===========:"+jsonObject.toJSONString());
        String orderStatus = jsonObject.getString("orderStatus");
        String thirdOrderId = jsonObject.getString("thirdOrderId");
        String data = jsonObject.getString("data");
        if(StringUtils.isNotEmpty(thirdOrderId)){
            //记录日志
            HrGongdan hrGongdan = HrGongdan.dao.findById(thirdOrderId.split("-")[1]);
            HrGongdanWsfLog hrGongdanWsfLog = saveHrGongdanWsfLog(orderStatus,thirdOrderId,hrGongdan.getOrderId(),(new Date()).getTime());

            //处理data数据
            JSONObject dataJSONObject= JSONObject.parseObject(data);
            try {
                log.info("==================当前登陆人："+ WebUser.getNo());
                if(WebUser.getNo()!=""){

                }else{
                    WebUser.SignInOfGenerAuth(new Emp("WSFHuangQiang"), "WSFHuangQiang");
                }
                switch(orderStatus){
                    case "wait_pay":
                        break;
                    case "wait_reserve_customer": //总包已报价（月结订单），已托管费 用（非月结订单）
                        log.info("==================订单节点回调通知备注wait_reserve_customer");
                        hrGongdanWsfLog.setOfferPrice(dataJSONObject.getString("offerPrice"));
                        hrGongdanWsfLog.save();
                        break;
                    case "enterprise_order_sp": //已派单
                        log.info("==================订单节点回调通知备注enterprise_order_sp");
                        hrGongdanWsfLog.setMasterName(dataJSONObject.getString("masterName"));
                        hrGongdanWsfLog.setMasterPhone(dataJSONObject.getString("masterPhone"));
                        hrGongdanWsfLog.setMasterLevel(dataJSONObject.getString("masterLevel"));
                        hrGongdanWsfLog.save();
                        this.enterprise_order_sp(thirdOrderId,dataJSONObject);
                        break;
                    case "reserve_customer": //预约时间
                        log.info("==================订单节点回调通知备注reserve_customer");
                        hrGongdanWsfLog.setDutyTime(dataJSONObject.getString("appointTime"));
                        hrGongdanWsfLog.save();
                        this.reserve_customer(thirdOrderId,dataJSONObject);
                        break;
                    case "logiscs_sign":  //已完成物流点提货（送货到楼下，送 货到家，送货到家并安装才有此节 点）
                        break;
                    case "wait_serve_complete": //已上门
                        log.info("==================订单节点回调通知备注wait_serve_complete");
                        hrGongdanWsfLog.save();
                        this.wait_serve_complete(thirdOrderId,dataJSONObject);
                        break;
                    case "serve_complete": //已完工
                        log.info("==================订单节点回调通知备注serve_complete");
                        JSONArray jsonArray = dataJSONObject.getJSONArray("completePictureList");
                        List<String> list = jsonArray.toJavaList(String.class);
                        String str = String.join(",", list);
                        hrGongdanWsfLog.setCompleteUrl(str);
                        hrGongdanWsfLog.setCompleteTime(dataJSONObject.getString("completeTime"));
                        hrGongdanWsfLog.save();
                        this.serve_complete(thirdOrderId,dataJSONObject,str);
                        break;
                    case "order_mark": //备注
                        log.info("==================订单节点回调通知备注order_mark");
                        hrGongdanWsfLog.setFactoryRemark(dataJSONObject.getString("content"));
                        hrGongdanWsfLog.save();
                        this.order_mark(thirdOrderId,dataJSONObject);
                        break;
                    case "order_cancel": //订单取消
                        log.info("==================订单节点回调通知备注order_cancel");
                        hrGongdanWsfLog.save();
                        this.order_cancel(thirdOrderId,dataJSONObject);
                        break;
                }
                log.info("==================订单节点回调通知结束");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        renderJson(R.ok().put("code",0));
    }

    @NotAction
    public HrGongdanWsfLog saveHrGongdanWsfLog(String funcId,String thirdOrderId,String orderId,Long optTime){
        //录入记录
        HrGongdanWsfLog hrGongdanWsfLog = new HrGongdanWsfLog();
        hrGongdanWsfLog.setFuncId(funcId);
        hrGongdanWsfLog.setThirdOrderId(thirdOrderId);
        hrGongdanWsfLog.setOrderId(orderId);
        hrGongdanWsfLog.setOptTime(optTime);
        return hrGongdanWsfLog;
    }

    /**
     * 确认订单->分派成功
     * @param thirdOrderId
     * @param dataJSONObject
     * @throws Exception
     */
    @NotAction
    private void enterprise_order_sp(String thirdOrderId, JSONObject dataJSONObject) throws Exception{

        //若当前节点不是912，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(thirdOrderId.split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(912 == gwf.getFK_Node()){
            //发送流程
            Hashtable myhtSend = new Hashtable();
            myhtSend.put("masterName", dataJSONObject.getString("masterName"));
            myhtSend.put("masterPhone", dataJSONObject.getString("masterPhone"));
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    thirdOrderId.split("-")[0],
                    Long.parseLong(thirdOrderId.split("-")[1]),
                    myhtSend,null,913,null);
        }else{
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.valueOf(thirdOrderId.split("-")[1]));
            hrGongdan.setMasterName(dataJSONObject.getString("masterName"));
            hrGongdan.setMasterPhone(dataJSONObject.getString("masterPhone"));
            hrGongdan.update();
        }
    }

    /**
     * 记录预约时间
     * @param thirdOrderId
     * @param dataJSONObject
     * @throws Exception
     */
    @NotAction
    private void reserve_customer(String thirdOrderId, JSONObject dataJSONObject) throws Exception{
        HrGongdan hrGongdan = new HrGongdan();
        hrGongdan.setOID(Integer.valueOf(thirdOrderId.split("-")[1]));
        hrGongdan.setDutyTime1(dataJSONObject.getString("masterPhone"));
        hrGongdan.update();
    }

    /**
     * 分派成功->预约确认
     * @param thirdOrderId
     * @param dataJSONObject
     * @throws Exception
     */
    @NotAction
    private void wait_serve_complete(String thirdOrderId, JSONObject dataJSONObject) throws Exception{
        //若当前节点不是903，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(thirdOrderId.split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(913 == gwf.getFK_Node()){
            //发送流程
            Hashtable myhtSend = new Hashtable();
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    thirdOrderId.split("-")[0],
                    Long.parseLong(thirdOrderId.split("-")[1]),
                    myhtSend,null,914,null);
        }
    }

    /**
     *
     * 成功预约、预约确认->完成
     * @param thirdOrderId
     * @param dataJSONObject
     * @param str
     * @throws Exception
     */
    @NotAction
    private void serve_complete(String thirdOrderId, JSONObject dataJSONObject, String str) throws Exception{
        //若当前节点不是905，则不流转，直接更新数据
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(thirdOrderId.split("-")[1]));
        gwf.RetrieveFromDBSources();

        //若是确认订单节点，往下流转，其他得只是更新数据
        if(914 == gwf.getFK_Node() || 913 == gwf.getFK_Node()){
            Hashtable myhtSend = new Hashtable();
            //发送流程
            myhtSend.put("productPictureUrls", str);
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    thirdOrderId.split("-")[0],
                    Long.parseLong(thirdOrderId.split("-")[1]),
                    myhtSend, null, 906, null);
        }else{
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.valueOf(thirdOrderId.split("-")[1]));
            //hrGongdan.setProductPictureUrls(orderCompleteRequest.getProductPictureUrls());
            hrGongdan.update();
        }
    }

    @NotAction
    private void order_mark(String thirdOrderId, JSONObject dataJSONObject) throws Exception{
        //记录日志
    }

    @NotAction
    public void order_cancel(String thirdOrderId, JSONObject dataJSONObject) throws Exception{
        GenerWorkFlow gwf = new GenerWorkFlow();
        gwf.setWorkID(Long.parseLong(thirdOrderId.split("-")[1]));
        gwf.RetrieveFromDBSources();
        //完成前都可取消
        if(912 == gwf.getFK_Node() || 913 == gwf.getFK_Node() || 914 == gwf.getFK_Node()){
            Hashtable myhtSend = new Hashtable();
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(
                    thirdOrderId.split("-")[0],
                    Long.parseLong(thirdOrderId.split("-")[1]),
                    myhtSend, null, 907, null);
        }

    }
}
