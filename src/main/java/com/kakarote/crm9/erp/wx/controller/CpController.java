package com.kakarote.crm9.erp.wx.controller;

import BP.WF.Dev2Interface;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.wx.config.WxCpAgentIdEmun;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;

public class CpController extends Controller {

    @Inject
    private CpService cpService;
    /**
     * 获取应用agentid对应的access_token
     */
    public void getCpAccessToken(@Para("") WxCpMessageReq wxCpMessageReq){
        renderJson(R.ok().put("data",cpService.getCpAccessToken(wxCpMessageReq.getAgentId())));
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

    public void autoLoginByCode(String code) throws Exception {
        WxCpOauth2UserInfo wxCpOauth2UserInfo = cpService.autoLoginByCode(code);
        if(wxCpOauth2UserInfo!=null && wxCpOauth2UserInfo.getUserId()!=null){
            //获取emp信息
            PortEmp emp = PortEmp.dao.findFirst(Db.getSql("admin.portEmp.getEmpByTel"),
                    wxCpOauth2UserInfo.getUserId());
            Dev2Interface.Port_Login(emp.getNo());
            renderJson(R.ok().put("msg","登录成功!").put("code","000000"));
        }else{
            renderJson(R.ok().put("msg","登录失败!").put("code","000001"));
        }
    }

    public void getJsapiConfig(@Para("url") String url){
        renderJson(R.ok()
                .put("data",cpService.getJsapiConfig(url,WxCpAgentIdEmun.agent2.getCode()))
                .put("agentId", WxCpAgentIdEmun.agent2.getCode()));
    }
}
