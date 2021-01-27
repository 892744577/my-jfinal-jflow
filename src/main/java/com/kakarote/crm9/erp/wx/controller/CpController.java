package com.kakarote.crm9.erp.wx.controller;

import BP.WF.Dev2Interface;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.wx.config.WxCpAgentIdEmun;
import com.kakarote.crm9.erp.wx.service.CpService;
import com.kakarote.crm9.erp.wx.vo.WxCpMessageReq;
import com.kakarote.crm9.utils.R;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;

import java.util.List;

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

    /*
     * @Description //根据code获取用户信息
     * @Author wangkaida
     * @Date 14:39 2020/9/24
     * @Param [code]
     * @return void
     **/
    public void getUserInfoByCode(String code) throws Exception {
        WxCpOauth2UserInfo wxCpOauth2UserInfo = cpService.autoLoginByCode(code);
        if(wxCpOauth2UserInfo!=null && wxCpOauth2UserInfo.getUserId()!=null){
            WxCpUser wxCpUser = cpService.getById(wxCpOauth2UserInfo);

            renderJson(R.ok().put("data",wxCpUser).put("code","000000"));
        }else{
            renderJson(R.ok().put("data",null).put("code","000001"));
        }
    }

    /*
     * @Description //根据部门Id部门信息
     * @Author wangkaida
     * @Date 17:50 2021/1/26
     * @Param [deptId]
     * @return void
     **/
    public void getUserDeptByDeptId(String deptId) {
        if(StrUtil.isEmpty(deptId)){
            renderJson(R.error("deptId不能为空!").put("data",null).put("code","000001"));
            return;
        }

        List<WxCpDepart> wxCpDepartList = cpService.getDeptById(Long.valueOf(deptId));

        if (wxCpDepartList.size() > 0) {
            renderJson(R.ok().put("data",wxCpDepartList.get(0)).put("code","000000"));
        }else {
            renderJson(R.error().put("data",null).put("code","000001"));
        }
    }

    public void getJsapiConfig(@Para("url") String url){
        renderJson(R.ok()
                .put("data",cpService.getJsapiConfig(url,WxCpAgentIdEmun.agent2.getCode()))
                .put("agentId", WxCpAgentIdEmun.agent2.getCode()));
    }

    /*
     * @Description //企业微信userid转换成openid接口
     * @Author wangkaida
     * @Date 11:26 2021/1/13
     * @Param [userId]
     * @return void
     **/
    public void changeUserIdToOpenId(String userId) throws Exception {
        if(StrUtil.isEmpty(userId)){
            renderJson(R.error("userId不能为空!").put("data",null).put("code","000001"));
            return;
        }

        String openId = cpService.getOpenidByUserId(userId);

        if (StrUtil.isNotBlank(openId)) {
            renderJson(R.ok().put("data",openId).put("code","000000"));
        }else {
            renderJson(R.error().put("msg","企业微信userid转换成openid失败!").put("code","000002"));
        }
    }

}
