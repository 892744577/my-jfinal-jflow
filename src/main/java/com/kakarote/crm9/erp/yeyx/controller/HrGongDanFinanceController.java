package com.kakarote.crm9.erp.yeyx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.yeyx.service.HrGongdanFinanceService;
import com.kakarote.crm9.utils.R;

public class HrGongDanFinanceController extends Controller {

    @Inject
    private HrGongdanFinanceService hrGongdanFinanceService;

    public void sumRemaining() throws Exception {
        renderJson(R.ok().put("data",hrGongdanFinanceService.getRemaining()));

    }
}
