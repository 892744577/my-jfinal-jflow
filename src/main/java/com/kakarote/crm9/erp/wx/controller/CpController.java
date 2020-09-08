package com.kakarote.crm9.erp.wx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import com.kakarote.crm9.utils.R;

public class CpController extends Controller {

    @Inject
    private CpService cpService;

    /**
     * 获取应用agentid对应的access_token
     */
    public void getCpAccessToken(){
        renderJson(R.ok().put("data",cpService.getCpAccessToken()));
    }

    /**
     * 发送消息接口
     * @param wxCpMessageReq
     * @return
     */
    public void sendMsg(@Para("") WxCpMessageReq wxCpMessageReq){
        renderJson(R.ok().put("data",cpService.sendTextMsg(wxCpMessageReq)));
    }
    /**
     * 获取部门列表
     * @return
     */
    public void getAllDept(){
        renderJson(R.ok().put("data",cpService.getAllDept()));
    }
}
