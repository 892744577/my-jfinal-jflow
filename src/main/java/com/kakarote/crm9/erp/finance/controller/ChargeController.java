package com.kakarote.crm9.erp.finance.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.finance.service.ChargeService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChargeController extends Controller {

    @Inject
    private ChargeService chargeService;

    public void callT100ChargeApi7() throws Exception {
        renderJson(R.ok().put("data",chargeService.callT100ChargeApi7()));
    }
}
