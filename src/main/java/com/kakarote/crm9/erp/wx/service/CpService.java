package com.kakarote.crm9.erp.wx.service;

import BP.Difference.SystemConfig;
import com.kakarote.crm9.erp.wx.config.WxCpConfiguration;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpMessage;

@Slf4j
public class CpService {
    private String agentId = SystemConfig.getCS_AppSettings().get("CP1.AGENTID").toString();
    /**
     * 获取企业内部
     */
    public String getCpAccessToken(){
        WxCpService wxCpService = WxCpConfiguration.getCpService(Integer.parseInt(this.agentId));
        try {
            return wxCpService.getAccessToken();
        } catch (WxErrorException e) {
            return e.getError().getJson();
        }
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
     * 发送应用消息
     * @return
     */
    public String sendTextMsg(WxCpMessageReq wxCpMessageReq){
        WxCpMessage message = WxCpMessage.TEXT()
                .agentId(Integer.parseInt(this.agentId)) // 企业号应用ID
                .toUser(wxCpMessageReq.getUser())
                .toParty(wxCpMessageReq.getParty())
                .toTag(wxCpMessageReq.getTag())
                .content(wxCpMessageReq.getContent())
                .build();
        WxCpService wxCpService =WxCpConfiguration.getCpService(Integer.parseInt(this.agentId));
        try {
            wxCpService.messageSend(message);
        } catch (WxErrorException e) {
            return e.getError().getJson();
        }
        return "ok";
    }
}
