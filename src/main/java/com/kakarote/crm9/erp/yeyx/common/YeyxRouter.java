package com.kakarote.crm9.erp.yeyx.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.yeyx.controller.YeyxController;

public class YeyxRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new YeyxInterceptor());
        add("/yeyx", YeyxController.class); //言而有信订单平台
    }
}
