package com.kakarote.crm9.erp.wx.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.wx.controller.CpController;
import com.kakarote.crm9.erp.wx.controller.MiniappController;
import com.kakarote.crm9.erp.wx.controller.MpController;

public class WxRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new WxInterceptor());
        add("/miniapp", MiniappController.class);
        add("/mp", MpController.class);
        add("/cp", CpController.class);
    }
}
