package com.kakarote.crm9.erp.wxcms.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.wxcms.controller.WxCmsController;
import com.kakarote.crm9.erp.wxcms.controller.WxCmsPrizeController;
import com.kakarote.crm9.erp.wxcms.controller.WxcmsFluCodeController;

public class WxCmsRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new WxCmsInterceptor());
        add("/wxcms", WxCmsController.class);
        add("/prize", WxCmsPrizeController.class);
        add("/flu", WxcmsFluCodeController.class);
    }
}
