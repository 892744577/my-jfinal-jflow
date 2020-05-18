package com.kakarote.crm9.erp.sms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.service.SmsService;
import com.kakarote.crm9.utils.R;

public class SmsController extends Controller {

    @Inject
    private SmsService smsService;

    /**
     * @author tangmanrong
     * 发送手机验证码
     */
    public void sendCode(@Para("") LoginRequestDto loginRequestDto){
        String appid=smsService.getAccount();
        String appSecret=smsService.getPassword();
        smsService.send(loginRequestDto);

        renderJson(R.ok().put("openId","xxx").put("code","000000"));
    }

    public void getSmsByMobile(@Para("") LoginRequestDto loginRequestDto){

        renderJson(R.ok().put("验证码",smsService.getSmsByMobile(loginRequestDto)).put("code","000000"));
    }
}
