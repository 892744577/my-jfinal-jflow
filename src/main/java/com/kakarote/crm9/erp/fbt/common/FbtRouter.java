package com.kakarote.crm9.erp.fbt.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.fbt.controller.FbtController;

public class FbtRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new FbtInterceptor());
        add("/fbt", FbtController.class);
    }
}
