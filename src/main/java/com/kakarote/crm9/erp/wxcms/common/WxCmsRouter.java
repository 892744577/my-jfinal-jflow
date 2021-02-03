package com.kakarote.crm9.erp.wxcms.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.wxcms.controller.*;

public class WxCmsRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new WxCmsInterceptor());
        add("/wxcms", WxCmsController.class);
        add("/agent", WxAgentController.class);
        add("/media", WxCmsMediaController.class);
        add("/prize", WxCmsPrizeController.class);
        add("/flu", WxcmsFluCodeController.class);
        add("/competitive", WxCmsCompetitiveController.class);
    }
}
