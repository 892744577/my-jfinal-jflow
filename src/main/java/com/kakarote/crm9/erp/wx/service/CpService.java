package com.kakarote.crm9.erp.wx.service;

import com.alibaba.fastjson.JSON;
import com.kakarote.crm9.erp.wx.config.WxCpAgentIdEmun;
import com.kakarote.crm9.erp.wx.config.WxCpConfiguration;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpMessage;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CpService {
    /**
     * 获取企业内部、第三方应用、自建应用accesstoken
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
            log.info("=======发送无忧消息："+ JSON.toJSONString(message));
            wxCpService.messageSend(message);
        } catch (WxErrorException e) {
            return e.getError().getJson();
        }
        return "ok";
    }
    /**
     * 获取部门byid
     */
    public List<WxCpDepart> getDeptById(Long dept_id){
        return this.getAllDept(dept_id);
    }
    /**
     * 获取所有部门
     */
    public List<WxCpDepart> getAllDept(){
        return this.getAllDept(null);
    }
    /**
     * 获取所有部门
     */
    public List<WxCpDepart> getAllDept(Long dept_id){
        WxCpService wxCpService =WxCpConfiguration.getCpService(WxCpAgentIdEmun.agent0.getCode());
        List<WxCpDepart> list=new ArrayList<>();
        try {
            list= wxCpService.getDepartmentService().list(dept_id);
        } catch (WxErrorException e) {
            e.getError().getJson();
        }
        return list;
    }

    /**
     * 根据code获取userId
     * @param code
     * @return
     */
    public WxCpOauth2UserInfo autoLoginByCodeAgent2(String code) {
        return autoLoginByCode(code,WxCpAgentIdEmun.agent2.getCode());
    }
    public WxCpOauth2UserInfo autoLoginByCodeAgent4(String code) {
        return autoLoginByCode(code,WxCpAgentIdEmun.agent4.getCode());
    }
    public WxCpOauth2UserInfo autoLoginByCode(String code,Integer agentid) {
        WxCpService wxCpService =WxCpConfiguration.getCpService(agentid);
        WxCpOauth2UserInfo wxCpOauth2UserInfo = new WxCpOauth2UserInfo();
        try {
            wxCpOauth2UserInfo = wxCpService.getOauth2Service().getUserInfo(code);
        } catch (WxErrorException e) {
            e.getError().getJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxCpOauth2UserInfo;
    }

    /**
     * 根据userId获取用户信息
     * @param wxCpOauth2UserInfo
     * @return
     */
    public WxCpUser getByIdAgent2(WxCpOauth2UserInfo wxCpOauth2UserInfo) {
        return getById(wxCpOauth2UserInfo,WxCpAgentIdEmun.agent2.getCode());
    }
    public WxCpUser getByIdAgent4(WxCpOauth2UserInfo wxCpOauth2UserInfo) {
        return getById(wxCpOauth2UserInfo,WxCpAgentIdEmun.agent4.getCode()) ;
    }
    public WxCpUser getById(WxCpOauth2UserInfo wxCpOauth2UserInfo,Integer agentid) {
        WxCpService wxCpService =WxCpConfiguration.getCpService(agentid);
        WxCpUser wxCpUser = new WxCpUser();
        try {
            wxCpUser = wxCpService.getUserService().getById(wxCpOauth2UserInfo.getUserId());
        } catch (WxErrorException e) {
            e.getError().getJson();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxCpUser;
    }

    /**
     * 获取config
     * @param url
     * @return
     */
    public WxJsapiSignature getJsapiConfig(String url,Integer agnetId){
        WxCpService wxCpService =WxCpConfiguration.getCpService(agnetId);
        try {
             return wxCpService.createJsapiSignature(url);
        } catch (WxErrorException e) {
            e.getError().getJson();
        }
        return null;
    }

    /*
     * @Description //企业微信userid转换成openid接口
     * @Author wangkaida
     * @Date 11:56 2021/1/13
     * @Param [userId]
     * @return java.lang.String
     **/
    public String getOpenidByUserId(String userId) throws Exception {
        WxCpService wxCpService = WxCpConfiguration.getCpService(WxCpAgentIdEmun.agent2.getCode());
        return wxCpService.getUserService().userId2Openid(userId,null).get("openid");
    }
}
