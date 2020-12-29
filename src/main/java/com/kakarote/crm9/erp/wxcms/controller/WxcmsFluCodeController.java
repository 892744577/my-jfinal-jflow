package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.wxcms.service.WxcmsFluCodeService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WxcmsFluCodeController extends Controller {
    @Inject
    private WxcmsFluCodeService wxcmsFluCodeService;

    public void findAll(){
        renderJson(R.ok().put("data",wxcmsFluCodeService.findAll()));
    }
}
