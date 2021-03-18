package com.kakarote.crm9.erp.finance.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.finance.controller.ChargeController;

public class T100Router extends Routes {
    @Override
    public void config() {
        addInterceptor(new T100Interceptor());
        add("/t100", ChargeController.class);
    }
}
