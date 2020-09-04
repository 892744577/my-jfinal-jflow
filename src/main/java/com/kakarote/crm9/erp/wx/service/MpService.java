package com.kakarote.crm9.erp.wx.service;

import BP.Difference.SystemConfig;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.wx.config.WxMpConfiguration;
import com.kakarote.crm9.erp.wx.vo.MpMsgSendReq;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import java.util.List;

@Slf4j
public class MpService {
    @Inject
    private WxMpConfiguration wxMpConfiguration;

    @Getter
    private String appid = SystemConfig.getCS_AppSettings().get("MP.APPID").toString();
    @Getter
    private String maAppid = SystemConfig.getCS_AppSettings().get("MA.APPID").toString();

    /**
     * 发送公众号模板消息
     * @param mpMsgSendReq
     * @return
     * @throws Exception
     */
    public String send(MpMsgSendReq mpMsgSendReq) {
        WxMpService wxMpService = this.wxMpConfiguration.wxMpService();
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        wxMpTemplateMessage.setToUser(mpMsgSendReq.getTouser());
        wxMpTemplateMessage.setTemplateId(mpMsgSendReq.getTemplate_id());

        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
        miniProgram.setAppid(this.maAppid);
        miniProgram.setPagePath(mpMsgSendReq.getPage());
        wxMpTemplateMessage.setMiniProgram(miniProgram);
        List<WxMpTemplateData> ja = JSONArray.parseArray(mpMsgSendReq.getData(), WxMpTemplateData.class);
        wxMpTemplateMessage.setData(ja);
        try{
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        } catch (WxErrorException e) {
            return e.getError().getJson();
        }
        return "ok";
    }
}
