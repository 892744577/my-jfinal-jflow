package com.kakarote.crm9.erp.wx.config;

import BP.Difference.SystemConfig;
import lombok.Getter;

@Getter
public enum WxMaAppIdEmun {
    ma0(SystemConfig.getCS_AppSettings().get("MA1.APPID").toString(),
            SystemConfig.getCS_AppSettings().get("MA1.APPSECRET").toString(),"无忧小程序");

    private String code;
    private String scret;
    private String description;
    WxMaAppIdEmun(String code,String scret,String description) {
        this.code = code;
        this.scret = scret;
        this.description = description;
    }
}
