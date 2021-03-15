package com.kakarote.crm9.erp.yeyx.controller;

import BP.Tools.StringUtils;
import com.jfinal.aop.Aop;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.wx.config.WxCpAgentIdEmun;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ProjectManageController
 * @Description 工程项目管理控制类
 * @Author wangkaida
 * @Date 2020/11/4 11:49
 **/
@Slf4j
public class ProjectManageController  extends Controller {

    /**
     * @Description //进行工程项目管理企业微信信息推送
     * @Author wangkaida
     * @Date 13:17 2020/11/4
     * @Param [toUser, sendContent]
     * @return void
     **/
    public void sendPMCpMsg(String toUser, String sendContent) {
        WxCpMessageReq wxCpMessageReq = new WxCpMessageReq();
        wxCpMessageReq.setAgentId(WxCpAgentIdEmun.agent3.getCode());
        if (StringUtils.isNotBlank(toUser)) {
            wxCpMessageReq.setUser(toUser);
        }

        if (StringUtils.isNotBlank(sendContent)) {
            wxCpMessageReq.setContent(sendContent);
        }

        String result = Aop.get(CpService.class).sendTextMsg(wxCpMessageReq);

        if ("ok".equals(result)) {
            renderJson(R.ok("进行工程项目管理企业微信信息推送成功").put("code","000000"));
        }else {
            renderJson(R.error("进行工程项目管理企业微信信息推送失败").put("code","000001"));
        }

    }

    /*
     * @Description //进行警报信息企业微信信息推送
     * @Author wangkaida
     * @Date 11:49 2021/3/15
     * @Param [toUser, sendContent]
     * @return void
     **/
    public void sendAlarmCpMsg(String toUser, String sendContent) {
        WxCpMessageReq wxCpMessageReq = new WxCpMessageReq();
        wxCpMessageReq.setAgentId(WxCpAgentIdEmun.agent5.getCode());
        if (StringUtils.isNotBlank(toUser)) {
            wxCpMessageReq.setUser(toUser);
        }

        if (StringUtils.isNotBlank(sendContent)) {
            wxCpMessageReq.setContent(sendContent);
        }

        String result = Aop.get(CpService.class).sendTextMsg(wxCpMessageReq);

        if ("ok".equals(result)) {
            renderJson(R.ok("进行警报信息企业微信信息推送成功").put("code","000000"));
        }else {
            renderJson(R.error("进行警报信息企业微信信息推送失败").put("code","000001"));
        }

    }
}
