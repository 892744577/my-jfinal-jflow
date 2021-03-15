package com.kakarote.crm9.erp.ione.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.jxc.common.JxcInterceptor;
import com.kakarote.crm9.erp.jxc.controller.JxcController;

public class IoneRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new JxcInterceptor());
        add("/ione", JxcController.class);
    }
}
