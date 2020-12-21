package com.kakarote.crm9.erp.yeyx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.yeyx.service.WdtService;
import com.kakarote.crm9.utils.R;

import java.util.HashMap;

public class WdtController extends Controller {

    @Inject
    private WdtService wdtService;

    public void getOrder() throws Exception{
        renderJson(R.ok().put("data",wdtService.gatewayRequest("http://qimen.api.taobao.com/router/qmtest",wdtService.getQmJsonData(new HashMap()))));
    }
}
