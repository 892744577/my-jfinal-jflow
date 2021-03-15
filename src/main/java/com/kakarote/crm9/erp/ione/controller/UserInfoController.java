package com.kakarote.crm9.erp.ione.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.ione.service.UserInfoService;
import com.kakarote.crm9.utils.R;

public class UserInfoController extends Controller {

    @Inject
    private UserInfoService userInfoService;

    public void getMaxCreateDate(){
        renderJson(R.ok().put("data",userInfoService.getMaxCreateDate()));
    }
}
