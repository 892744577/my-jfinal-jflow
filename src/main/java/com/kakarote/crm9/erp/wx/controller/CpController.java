package com.kakarote.crm9.erp.wx.controller;

import BP.Difference.SystemConfig;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.wx.config.WxCpConfiguration;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import me.chanjar.weixin.cp.api.WxCpService;

public class CpController extends Controller {

    @Inject
    private CpService cpService;

    private String agentId = SystemConfig.getCS_AppSettings().get("CP1.APPID").toString();

    /**
     * 获取应用agentid对应的access_token
     */
    public void getCpAccessToken(){
        WxCpService wxCpService =WxCpConfiguration.getCpService(Integer.parseInt(this.agentId));
    }

    /**
     * 获取当前网页的登陆人信息
     * @param code
     * @param accessToken
     * @return
     */
    public String getUserInfo(String code, String accessToken){
        return "";
    }

    /**
     * 发送消息接口
     * @param wxCpMessageReq
     * @return
     */
    public String sendMsg(WxCpMessageReq wxCpMessageReq){
        return cpService.sendTextMsg(wxCpMessageReq);
    }
}
