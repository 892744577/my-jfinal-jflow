package com.kakarote.crm9.erp.yzj.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.oa.entity.OaEvent;
import com.kakarote.crm9.erp.yzj.service.TokenService;

public class YzjController extends Controller {

    @Inject
    private TokenService tokenService;

    /**
     * @author wyq
     * 查询日程列表
     */
    public void queryList(@Para("") OaEvent oaEvent){
        String appid=tokenService.getAppid();
        String appSecret=tokenService.getAppSecret();
    }
}
