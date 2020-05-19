package com.kakarote.crm9.erp.sms.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.sms.controller.SmsController;

public class SmsRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new SmsInterceptor());
        add("/sms", SmsController.class);
    }
}
