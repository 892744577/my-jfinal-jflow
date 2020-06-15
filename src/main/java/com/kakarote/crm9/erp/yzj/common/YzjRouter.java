package com.kakarote.crm9.erp.yzj.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.yzj.controller.WxMiddleWareController;
import com.kakarote.crm9.erp.yzj.controller.YeyxController;
import com.kakarote.crm9.erp.yzj.controller.YzjController;

public class YzjRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new YzjInterceptor());
        add("/yzj", YzjController.class);
        add("/wxMiddleWare", WxMiddleWareController.class); //微信中台
        add("/yeyx", YeyxController.class); //言而有信订单平台
    }
}
