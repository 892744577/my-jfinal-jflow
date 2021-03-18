package com.kakarote.crm9.erp.finance.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.finance.entity.vo.CallT100ChargeApi6Request;
import com.kakarote.crm9.erp.finance.service.ChargeService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChargeController extends Controller {

    @Inject
    private ChargeService chargeService;

    public void callT100ChargeApi6(@Para("") CallT100ChargeApi6Request callT100ChargeApi6Request) throws Exception {
        String detail = callT100ChargeApi6Request.getDetailListJson();
        renderJson(R.ok().put("data",chargeService.callT100ChargeApi6(
                callT100ChargeApi6Request.getCustomerNo(),
                callT100ChargeApi6Request.getNo(),
                callT100ChargeApi6Request.getNum(),
                null)));
    }

    public void callT100ChargeApi7() throws Exception {
        renderJson(R.ok().put("data",chargeService.callT100ChargeApi7()));
    }
}
