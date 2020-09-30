package com.kakarote.crm9.erp.yeyx.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdan;
import com.kakarote.crm9.erp.yeyx.service.WanService;
import com.kakarote.crm9.erp.yeyx.util.EncodeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 万师傅接口对接类
 */
@Slf4j
public class WanController extends Controller {
    @Inject
    private WanService wanService;
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
        String thirdOrderId = jsonObject.getString("thirdOrderId");
        String orderStatus = jsonObject.getString("orderStatus");
        switch(orderStatus){
            case "wait_pay":
                break;
            case "wait_reserve_customer ": //已派单
                break;
            case "reserve_customer": //预约时间
                break;
            case "logiscs_sign":  //已提货
                break;
            case "wait_serve_complete": //已上门
                break;
            case "serve_complete ": //已完工
                break;
        }
        renderJson();
    }
}
