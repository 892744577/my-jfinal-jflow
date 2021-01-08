package com.kakarote.crm9.erp.yeyx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.yeyx.service.WdtService;
import com.kakarote.crm9.utils.R;

import java.util.HashMap;
import java.util.Map;

public class WdtController extends Controller {

    @Inject
    private WdtService wdtService;

    public void getOrder() throws Exception{
//        renderJson(R.ok().put("data",wdtService.gatewayRequest("http://qimen.api.taobao.com/router/qmtest",wdtService.getQmJsonData(new HashMap()))));
        Map map = new HashMap();
        map.put("start_time", "2018-12-11 12:00:00");
        map.put("end_time", "2018-12-11 13:00:00");
//        map.put("start_time", DateUtil.changeStrToDate("2019-11-11 19:00:00"));
//        map.put("end_time", DateUtil.changeStrToDate("2019-11-11 20:00:00"));
        String result = wdtService.gatewayRequest("https://sandbox.wangdian.cn/openapi2/trade_query.php",wdtService.getJsonData(map));
        renderJson(R.ok().put("data",result));
//        renderJson(R.ok().put("data",wdtService.gatewayRequestJson("https://sandbox.wangdian.cn/openapi2/trade_query.php",wdtService.getJsonData(map))));
    }
}
