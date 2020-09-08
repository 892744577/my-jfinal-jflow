package com.kakarote.crm9.erp.wx.service;

import com.kakarote.crm9.erp.wx.config.WxCpConfiguration;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpMessage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CpService {
    /**
     * 获取企业内部
     */
    public String getCpAccessToken(Integer agentId){
        WxCpService wxCpService = WxCpConfiguration.getCpService(agentId);
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
                .agentId(wxCpMessageReq.getAgentId()) // 企业号应用ID
                .toUser(wxCpMessageReq.getUser())
                .toParty(wxCpMessageReq.getParty())
                .toTag(wxCpMessageReq.getTag())
                .content(wxCpMessageReq.getContent())
                .build();
        WxCpService wxCpService =WxCpConfiguration.getCpService(wxCpMessageReq.getAgentId());
        try {
            wxCpService.messageSend(message);
        } catch (WxErrorException e) {
            return e.getError().getJson();
        }
        return "ok";
    }

    /**
     * 获取所有部门
     */
    public List<WxCpDepart> getAllDept(){
        WxCpService wxCpService =WxCpConfiguration.getCpService(0);
        List<WxCpDepart> list=new ArrayList<>();
        try {
            list= wxCpService.getDepartmentService().list(null);
        } catch (WxErrorException e) {
            e.getError().getJson();
        }
        return list;
    }
}
