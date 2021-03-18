package com.kakarote.crm9.erp.ione.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.ione.service.UserInfoService;
import com.kakarote.crm9.utils.R;

public class UserInfoController extends Controller {

    @Inject
    private UserInfoService userInfoService;

    public void getMaxCreateDate(){
        renderJson(R.ok().put("data",userInfoService.getMaxCreateDate()));
    }

    public void getWxcmsIoneUserInfo(@Para("") String uuid){
        renderJson(R.ok().put("data",userInfoService.getWxcmsIoneUserInfo(uuid)));
    }
    public void deleteByOpenidAndCode(@Para("openId") String openId,@Para("mcuId") String mcuId){
        renderJson(R.ok().put("data",userInfoService.deleteByOpenidAndCode(openId,mcuId)));
    }

    public void syncUserInfo(@Para("") String createDate) throws Exception {
        renderJson(R.ok().put("data",userInfoService.syncUserInfo(createDate)));
    }
}
