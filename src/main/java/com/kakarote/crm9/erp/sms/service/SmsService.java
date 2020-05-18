package com.kakarote.crm9.erp.sms.service;

import BP.Difference.SystemConfig;
import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.entity.Sms;
import com.kakarote.crm9.erp.sms.util.RedisUtils;
import com.kakarote.crm9.erp.sms.util.Sendsms;
import lombok.Data;

@Data
public class SmsService {

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private String account = SystemConfig.getCS_AppSettings().get("SMS.ACCOUNT").toString();

    private String password = SystemConfig.getCS_AppSettings().get("SMS.PASSWORD").toString();

    private Sendsms sendSms = Aop.get(Sendsms.class);

    @Inject
    private RedisUtils redisUtils;

    public Sms send(LoginRequestDto entity){

        Sms sms = Sms.builder()
                .account(this.account)
                .password(this.password)
                .mobile_code((int)((Math.random()*9+1)*100000))
                .mobile(entity.getMobile()).build();
        Sms.builder().account(this.account).password(this.password).mobile_code((int)((Math.random()*9+1)*100000)).mobile(entity.getMobile()).build();

        /*String count = redisUtils.get(entity.getMobile()+"-count");
        if(StringUtil.isNotEmpty(count) && Integer.parseInt(count)>4){
            sms.setMsg("获取太频繁了,请稍后再试");
            sms.setCode("500");
        }else{
            sms = sendSms.send(sms);
        }*/
        //sms本身有5条限制
        sms = sendSms.send(sms);
        if("2".equals(sms.getCode())){
            sms.setCode("200");
        }
        return sms;
    }

    /**
     * 获取redis的手机号为key的缓存
     */
    public String getSmsByMobile(LoginRequestDto entity){


        return redisUtils.get(entity.getMobile());
    }
}
