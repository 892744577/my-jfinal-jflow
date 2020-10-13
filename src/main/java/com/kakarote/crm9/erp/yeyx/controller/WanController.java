package com.kakarote.crm9.erp.yeyx.controller;

import BP.Port.Emp;
import BP.Tools.StringUtils;
import BP.Web.WebUser;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanWsfLog;
import com.kakarote.crm9.erp.yeyx.entity.vo.HrGongdanRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.ToCancelOrderRequest;
import com.kakarote.crm9.erp.yeyx.entity.vo.WanGoodsConfigRequest;
import com.kakarote.crm9.erp.yeyx.service.HrGongDanService;
import com.kakarote.crm9.erp.yeyx.service.WanService;
import com.kakarote.crm9.erp.yeyx.util.EncodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
        renderJson(WanService.gatewayRequestJson(wanService.getPath() + "/order/close", JSONObject.toJSONString(currentPrama)));
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
            //跟新数据
            String thirdOrderId = temp.getString("thirdOrderId");
            String wanshifuOrderNo = temp.getString("wanshifuOrderNo");
            HrGongdan hrGongdan = new HrGongdan();
            hrGongdan.setOID(Integer.parseInt(thirdOrderId.split("-")[1]));
            hrGongdan.setOrderId(wanshifuOrderNo);
            hrGongdan.update();
        }
        renderJson();
    }
    /**
     * 订单节点回调通知
     */
    public void toDo(){
        String rawData = getRawData();
        log.info("订单节点回调通知rawData===========:"+rawData);
        JSONObject jsonObject = JSONObject.parseObject(new String(EncodeUtil.decryptBASE64(rawData)));
        String orderStatus = jsonObject.getString("orderStatus");
        String thirdOrderId = jsonObject.getString("thirdOrderId");
        String data = jsonObject.getString("data");
        if(StringUtils.isNotEmpty(thirdOrderId)){
            //记录日志
            HrGongdanWsfLog hrGongdanWsfLog = saveHrGongdanWsfLog(orderStatus,thirdOrderId,null,(new Date()).getTime());
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
                    case "wait_reserve_customer ": //总包已报价（月结订单），已托管费 用（非月结订单）
                        hrGongdanWsfLog.setOfferPrice(dataJSONObject.getString("offerPrice"));
                        hrGongdanWsfLog.save();
                        break;
                    case "enterprise_order_sp ": //已派单
                        hrGongdanWsfLog.setMasterName(dataJSONObject.getString("masterName"));
                        hrGongdanWsfLog.setMasterPhone(dataJSONObject.getString("masterPhone"));
                        hrGongdanWsfLog.setMasterLevel(dataJSONObject.getString("masterLevel"));
                        hrGongdanWsfLog.save();
                        this.enterprise_order_sp(data);
                        break;
                    case "reserve_customer": //预约时间
                        hrGongdanWsfLog.setDutyTime(dataJSONObject.getString("appointTime"));
                        hrGongdanWsfLog.save();
                        this.reserve_customer(data);
                        break;
                    case "logiscs_sign":  //已完成物流点提货（送货到楼下，送 货到家，送货到家并安装才有此节 点）
                        break;
                    case "wait_serve_complete": //已上门
                        hrGongdanWsfLog.save();
                        this.wait_serve_complete(data);
                        break;
                    case "serve_complete ": //已完工
                        hrGongdanWsfLog.setCompleteTime(dataJSONObject.getString("completeTime"));
                        hrGongdanWsfLog.save();
                        this.serve_complete(data);
                        break;
                    case "order_mark ": //备注
                        hrGongdanWsfLog.setFactoryRemark(dataJSONObject.getString("content"));
                        hrGongdanWsfLog.save();
                        this.order_mark(data);
                        break;
                    case "order_cancel ": //订单取消
                        hrGongdanWsfLog.save();
                        this.order_cancel(data);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        renderJson();
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

    @NotAction
    private void enterprise_order_sp(String data) {

    }

    @NotAction
    private void reserve_customer(String data) {
        //记录日志
    }

    @NotAction
    private void wait_serve_complete(String data) {
        //记录日志
    }

    @NotAction
    private void serve_complete(String data) {
        //记录日志
    }

    @NotAction
    private void order_mark(String data) {
        //记录日志
    }

    @NotAction
    public void order_cancel(String data){
        Hashtable myhtSend = new Hashtable();
    }
}
