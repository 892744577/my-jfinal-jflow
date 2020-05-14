package com.kakarote.crm9.erp.yzj.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.yzj.controller.YzjController;

public class YzjRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new YzjInterceptor());
        add("/yzj", YzjController.class);
    }
}
