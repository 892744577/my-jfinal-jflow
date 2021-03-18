package com.kakarote.crm9.erp.jxc.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.jxc.controller.JxcController;
import com.kakarote.crm9.erp.jxc.controller.JxcReportController;

public class JxcRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new JxcInterceptor());
        add("/jxc", JxcController.class);
        add("/jxc/report", JxcReportController.class);
    }
}
