package com.kakarote.crm9.erp.wx.controller;

import com.kakarote.crm9.erp.wx.util.MpUtil;
import com.kakarote.crm9.utils.R;
import com.jfinal.core.Controller;

public class MpController extends Controller {

    public void getAccessTokenByInterface(){
        renderJson(R.ok().put("data",MpUtil.getAccessTokenByInterface()));
    }

}
