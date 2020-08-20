package com.kakarote.crm9.erp.admin.controller;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Port.Emp;
import BP.Tools.HttpClientUtil;
import BP.Tools.StringUtils;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.service.SmsService;
import com.kakarote.crm9.utils.R;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


/*
 * @Description //组织用户管理控制类
 * @Author wangkaida
 * @Date 14:23 2020/5/11
 * @Param
 * @return
 **/
public class PortEmpController extends Controller {

    @Inject
    private PortEmpService portEmpService;

    @Inject
    private SmsService smsService;

    public void queryAllUserList(){
        renderJson(portEmpService.queryAllUserList());
    }

    /*
     * @Description //代理商登录接口
     * @Author wangkaida
     * @Date 14:21 2020/5/12
     * @Param [portEmp]
     * @return void
     **/
    public void agentLogin(@Para("") PortEmpReq portEmpReq){

        //第三方登陆判断--小程序
        PortEmp portEmp = new PortEmp();
        portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
        PortEmp portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);

        if (portEmpDb != null) {
            if("1".equals(portEmpDb.getAccountType())){
                if (StrUtil.isNotEmpty(portEmpDb.getWxOpenId())) {
                    try {
                        BP.WF.Dev2Interface.Port_Login(portEmpDb.getNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //代理商已经创建账号直接登录
                    renderJson(R.ok().put("msg","登录成功!").put("code","000000"));
                }else {
                    renderJson(R.error("公众号没有绑定手机号，请到亚太天能公众号进行手机号绑定!").put("data",null).put("code","000002"));
                    return;
                }
            }else {
                renderJson(R.error("小程序没有绑定手机号,请先进行手机号绑定!").put("data",null).put("code","000001"));
                return;
            }
        }else {
            renderJson(R.error("小程序没有绑定手机号,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }
    }

    /*
     * @Description //普通员工登录接口
     * @Author wangkaida
     * @Date 13:13 2020/5/18
     * @Param [portEmp]
     * @return void
     **/
    public void staffLogin(@Para("") PortEmpReq portEmpReq){

        //第三方登陆判断--小程序
        PortEmp portEmp = new PortEmp();
        portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
        PortEmp portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);

        if (portEmpDb != null) {
            if("2".equals(portEmpDb.getAccountType())){
                if (StrUtil.isNotEmpty(portEmpDb.getWxOpenId())) {
                    try {
                        BP.WF.Dev2Interface.Port_Login(portEmpDb.getNo());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //代理商已经创建账号直接登录
                    renderJson(R.ok().put("msg","登录成功!").put("code","000000"));
                }else {
                    renderJson(R.error("公众号没有绑定手机号，请到亚太天能公众号-产品服务-售后安装-微信绑定手机号进行手机号绑定!").put("data",null).put("code","000002"));
                    return;
                }
            }else{
                renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
                return;
            }
        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }
    }

    /*
     * @Description //根据登录账号获取登录人信息
     * @Author wangkaida
     * @Date 16:15 2020/5/18
     * @Param [portEmp]
     * @return void
     **/
    public void portEmpInfo(@Para("") PortEmpReq portEmpReq){

        PortEmp portEmpDb = null;

        if (StrUtil.isNotEmpty(portEmpReq.getName())) {
            portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE No = ? LIMIT 0,1", portEmpReq.getName());
        }else if (StrUtil.isNotEmpty(portEmpReq.getWxAppOpenId())){
            //第三方id查询用户信息
            PortEmp portEmp = new PortEmp();
            portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
            portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);
        }


        if (portEmpDb != null) {
            portEmpDb.setPass(""); //密码置空返回
            renderJson(R.ok().put("data",portEmpDb).put("code","000000"));

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }
    }

    /*
     * @Description //微信openId绑定手机号
     * @Author wangkaida
     * @Date 16:32 2020/5/12
     * @Param [portEmp]
     * @return void
     **/
    public void wechatBind(@Para("") PortEmpReq portEmpReq){
//        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmpReq.getTel())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000003"));
            return;
        }

        //判断手机验证码是否正确
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobile(portEmpReq.getTel());
        String result = smsService.getSmsByMobile(loginRequestDto);

        if (!portEmpReq.getValiCode().equals(result)) {
            renderJson(R.error("请输入正确的验证码!").put("data",null).put("code","000024"));
            return;
        }

        //手机号获取数据信息
        PortEmp portEmp = new PortEmp();
        portEmp.setTel(portEmpReq.getTel());
        PortEmp portEmpDb = portEmpService.getPortEmp(portEmp);

        if (portEmpDb != null) {
            if (StrUtil.isEmpty(portEmpDb.getWxOpenId())) {
                Paras ps = new Paras();
                ps.Add("WxOpenId", portEmpReq.getWxOpenId());
                ps.Add("Tel", portEmpReq.getTel());
                String sql = "UPDATE port_emp SET WxOpenId="+SystemConfig.getAppCenterDBVarStr()+"WxOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                        + "Tel";
                int num = DBAccess.RunSQL(sql, ps);
                renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
            }else {
                renderJson(R.error("该手机号已经绑定，请勿重复绑定!").put("data",null).put("code","000004"));
                return;
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }

    }

    /*
     * @Description //普通员工微信openId绑定手机号
     * @Author wangkaida
     * @Date 13:33 2020/5/18
     * @Param [portEmp]
     * @return void
     **/
    public void staffWechatBind(@Para("") PortEmpReq portEmpReq){

        if(StrUtil.isEmpty(portEmpReq.getTel())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000003"));
            return;
        }

        //判断手机验证码是否正确
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobile(portEmpReq.getTel());
        String result = smsService.getSmsByMobile(loginRequestDto);

        if (!portEmpReq.getValiCode().equals(result)) {
            renderJson(R.error("请输入正确的验证码!").put("data",null).put("code","000024"));
            return;
        }

        //手机号获取用户数据信息
        PortEmp portEmp = new PortEmp();
        portEmp.setTel(portEmpReq.getTel());
        PortEmp portEmpDb = portEmpService.getPortEmp(portEmp);

        if (portEmpDb != null) {
            portEmpDb = portEmpService.getPortEmp(portEmpDb);
            if("2".equals(portEmpDb.getAccountType())) {
                if (StrUtil.isEmpty(portEmpDb.getWxOpenId())) {
                    Paras ps = new Paras();
                    ps.Add("WxOpenId", portEmp.getWxOpenId());
                    ps.Add("Tel", portEmp.getTel());
                    String sql = "UPDATE port_emp SET WxOpenId=" + SystemConfig.getAppCenterDBVarStr() + "WxOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr() + "Tel";
                    int num = DBAccess.RunSQL(sql, ps);
                    renderJson(R.ok().put("msg", "更新成功!").put("code", "000000"));
                } else {
                    renderJson(R.error("该手机号已经绑定，请勿重复绑定!").put("data", null).put("code", "000004"));
                    return;
                }
            }else {
                renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
                return;
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }

    }

    /*
     * @Description //代理商小程序openId绑定手机号
     * @Author wangkaida
     * @Date 17:11 2020/5/12
     * @Param [portEmp]
     * @return void
     **/
    public void appBind(@Para("") PortEmpReq portEmpReq){
//        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmpReq.getTel())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000003"));
            return;
        }

        //判断流程是否在进行中
        HrRegister hrRegister = HrRegister.dao.findFirst("SELECT * FROM hr_register WHERE ShouJiHaoMa = ? and appOpenId = ? and LeiBie = '1' AND WFState!='3' order by rdt desc  LIMIT 0,1", portEmpReq.getTel(),portEmpReq.getWxAppOpenId());

        if (hrRegister != null) {
            int wfState = hrRegister.getWFState();
            if (wfState == 2) { //流程进行中
                renderJson(R.error("微信小程序绑定手机号流程已经发起,请勿重复提交!").put("data",null).put("code","000005"));
                return;
            }
        }

        //手机号获取用户数据信息
        PortEmp portEmp = new PortEmp();
        portEmp.setTel(portEmpReq.getTel());
        PortEmp portEmpDb = portEmpService.getPortEmp(portEmp);

        if (portEmpDb != null) {
            //开始调用注册审批流程
            Hashtable myht = new Hashtable();
            Hashtable myhtSend = new Hashtable();
            try {
                myht.put("ShouJiHaoMa", portEmpReq.getTel());
                myht.put("appOpenId", portEmpReq.getWxAppOpenId());
                myht.put("XingMing", portEmpReq.getName());
                myht.put("LeiBie", "1");
                myht.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+ DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myht.put("TB_FID", 0);
                myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Rec", "admin");
                myht.put("TB_Emps", "admin");
                myht.put("TB_FK_Dept", 100);
                myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myht.put("TB_MyNum", 1);
                //新建流程
//            WebUser.SignInOfGener(new Emp("admin"));
//            BP.WF.Dev2Interface.Port_Login("admin");
                WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");
                long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",null,0,0,null,0,null,0,null,null,null);
                //发送流程
                myhtSend.put("ShouJiHaoMa", portEmpReq.getTel());
                myhtSend.put("appOpenId", portEmpReq.getWxAppOpenId());
                myhtSend.put("XingMing", portEmpReq.getName());
                myhtSend.put("LeiBie", "1");
                myhtSend.put("TB_OID", workID);
                myhtSend.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myhtSend.put("TB_FID", 0);
                myhtSend.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Rec", "admin");
                myhtSend.put("TB_Emps", "admin");
                myhtSend.put("TB_FK_Dept", 100);
                myhtSend.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myhtSend.put("TB_MyNum", 1);
                SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,null);
                String sendSuccess = "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";

                renderJson(R.ok().put("msg","注册成功，请等待审核!").put("code","000000"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            PortEmp portEmpTel = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmpReq.getTel());
            if(portEmpTel != null){
                renderJson(R.error("该手机号已经被注册!").put("data",null).put("code","000036"));
                return;
            }

            PortEmp portEmpName = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Name = ? LIMIT 0,1", portEmpReq.getName());
            if(portEmpName != null){
                renderJson(R.error("该姓名已经被注册!").put("data",null).put("code","000037"));
                return;
            }

//            Regist regist = getModel(Regist.class);
//            regist.setMyPK(UUID.randomUUID().toString().replace("-", ""));
//            regist.setPhone(portEmp.getTel());
//            regist.setAppOpenId(portEmp.getWxAppOpenId());
//            regist.setName(portEmp.getName());
//            regist.save();

            //开始调用注册审批流程
            Hashtable myht = new Hashtable();
            Hashtable myhtSend = new Hashtable();
            try {
                myht.put("ShouJiHaoMa", portEmpReq.getTel());
                myht.put("appOpenId", portEmpReq.getWxAppOpenId());
                myht.put("XingMing", portEmpReq.getName());
                myht.put("LeiBie", "1");
                myht.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+ DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myht.put("TB_FID", 0);
                myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Rec", "admin");
                myht.put("TB_Emps", "admin");
                myht.put("TB_FK_Dept", 100);
                myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myht.put("TB_MyNum", 1);
                //新建流程
//            WebUser.SignInOfGener(new Emp("admin"));
//            BP.WF.Dev2Interface.Port_Login("admin");
                WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");
                long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",null,0,0,null,0,null,0,null,null,null);
                //发送流程
                myhtSend.put("ShouJiHaoMa", portEmpReq.getTel());
                myhtSend.put("appOpenId", portEmpReq.getWxAppOpenId());
                myhtSend.put("XingMing", portEmpReq.getName());
                myhtSend.put("LeiBie", "1");
                myhtSend.put("TB_OID", workID);
                myhtSend.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myhtSend.put("TB_FID", 0);
                myhtSend.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Rec", "admin");
                myhtSend.put("TB_Emps", "admin");
                myhtSend.put("TB_FK_Dept", 100);
                myhtSend.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myhtSend.put("TB_MyNum", 1);
                SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,null);
                String sendSuccess = "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";
                renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        renderJson(R.ok().put("msg","发起成功，请等待审核!").put("code","000000"));
    }

    /*
     * @Description //普通员工小程序openId绑定手机号
     * @Author wangkaida
     * @Date 11:52 2020/5/15
     * @Param []
     * @return void
     **/
    public void staffAppBind(@Para("") PortEmpReq portEmp){
//        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmp.getParentTel())){
            renderJson(R.error("请输入上级手机号!").put("data",null).put("code","000007"));
            return;
        }

        if(StrUtil.isEmpty(portEmp.getTel())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000003"));
            return;
        }

        //判断上级是否为代理商
        PortEmp portEmpParent = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmp.getParentTel());
        if (portEmpParent == null) {
            renderJson(R.error("上级手机号为非代理商,不支持绑定!").put("data",null).put("code","000008"));
            return;
        }

        //注册手机号必须为非代理商手机号
        PortEmp portEmpTel = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmp.getTel());
        if (portEmpTel != null) {
            renderJson(R.error("注册手机号为代理商,不支持绑定!").put("data",null).put("code","000009"));
            return;
        }

        //判断流程是否在进行中
        HrRegister hrRegister = HrRegister.dao.findFirst("SELECT * FROM hr_register WHERE ShouJiHaoMa = ? and appOpenId = ? and LeiBie = '2' AND WFState!='3' order by rdt desc LIMIT 0,1", portEmp.getTel(),portEmp.getWxAppOpenId());

        if (hrRegister != null) {
            int wfState = hrRegister.getWFState();
            if (wfState == 2) { //流程进行中
                renderJson(R.error("微信小程序绑定手机号流程已经发起,请勿重复提交!").put("data",null).put("code","000005"));
                return;
            }
        }

        //判断账号是否已经存在,
        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? LIMIT 0,1", portEmp.getTel());
        if (portEmpDb != null) {
            //1、更新小程序openid
            portEmpDb.setWxAppOpenId(portEmp.getWxAppOpenId());
            portEmpDb.update();

            //2、判断是否已经存在上下级关系,若已经存在返回，若不存在关系直接保存上下级关系
            PortEmpRelation portEmpRelationDb = PortEmpRelation.dao.findFirst("SELECT * FROM port_emp_relation WHERE FK_No = ? and ParentNo = ? LIMIT 0,1", portEmpDb.getNo(),portEmpParent.getNo());
            if (portEmpRelationDb != null) {
                renderJson(R.error("上下级关系已经存在,请勿重复提交!").put("data",null).put("code","000011"));
                return;
            }

            PortEmpRelation portEmpRelation = new PortEmpRelation();
            portEmpRelation.setFkNo(portEmpDb.getNo());
            portEmpRelation.setParentNo(portEmpParent.getNo());
            Boolean flag = portEmpRelation.save();
            renderJson(R.ok("绑定成功,可直接登陆!").put("data","0").put("code","000000"));



        }else {
            //非代理商账号不存在，发流程给代理商
            //开始调用注册审批流程
            Hashtable myht = new Hashtable();
            Hashtable myhtSend = new Hashtable();
            try {
                myht.put("ShouJiHaoMa", portEmp.getTel());
                myht.put("appOpenId", portEmp.getWxAppOpenId());
                myht.put("XingMing", portEmp.getName());
                myht.put("ParentNo", portEmpParent.getNo());
                myht.put("LeiBie", "2");
                myht.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+ DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myht.put("TB_FID", 0);
                myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myht.put("TB_Rec", "admin");
                myht.put("TB_Emps", "admin");
                myht.put("TB_FK_Dept", 100);
                myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myht.put("TB_MyNum", 1);
                //新建流程
                WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");
                long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",null,0,0,null,0,null,0,portEmpParent.getNo(),null,null);
                //发送流程
                myhtSend.put("ShouJiHaoMa", portEmp.getTel());
                myhtSend.put("appOpenId", portEmp.getWxAppOpenId());
                myhtSend.put("XingMing", portEmp.getName());
                myhtSend.put("ParentNo", portEmpParent.getNo());
                myhtSend.put("LeiBie", "2");
                myhtSend.put("TB_OID", workID);
                myhtSend.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
                myhtSend.put("TB_FID", 0);
                myhtSend.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
                myhtSend.put("TB_Rec", "admin");
                myhtSend.put("TB_Emps", "admin");
                myhtSend.put("TB_FK_Dept", 100);
                myhtSend.put("TB_FK_NY", DataType.getCurrentYearMonth());
                myhtSend.put("TB_MyNum", 1);
                SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,portEmpParent.getNo());
                String sendSuccess = "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";

                renderJson(R.ok().put("msg","绑定流程已发起,请等待负责人审核通过!").put("data","1").put("code","000000"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * @Description //已经注册员工扫码二维码接口
     * @Author wangkaida
     * @Date 18:20 2020/5/18
     * @Param [portEmp]
     * @return void
     **/
    public void registScanQrCode(@Para("") PortEmpReq portEmp){

        if(StrUtil.isEmpty(portEmp.getNo())){
            renderJson(R.error("员工账号不能为空!").put("data",null).put("code","000012"));
            return;
        }

        if(StrUtil.isEmpty(portEmp.getParentNo())){
            renderJson(R.error("父级账号不能为空!").put("data",null).put("code","000013"));
            return;
        }

        //判断上级是否为代理商
        PortEmp portEmpParent = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE No = ? and accountType = '1' LIMIT 0,1", portEmp.getParentNo());
        if (portEmpParent == null) {
            renderJson(R.error("上级手机号为非代理商,不支持绑定!").put("data",null).put("code","000008"));
            return;
        }

        //注册手机号必须为非代理商手机号
        PortEmp portEmpTel = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE No = ? and accountType = '1' LIMIT 0,1", portEmp.getNo());
        if (portEmpTel != null) {
            renderJson(R.error("注册手机号为代理商,不支持绑定!").put("data",null).put("code","000009"));
            return;
        }

        //判断是否已经存在上下级关系
        PortEmpRelation portEmpRelationDb = PortEmpRelation.dao.findFirst("SELECT * FROM port_emp_relation WHERE FK_No = ? and ParentNo = ? LIMIT 0,1", portEmp.getNo(),portEmp.getParentNo());
        if (portEmpRelationDb != null) {
            renderJson(R.error("上下级关系已经存在,请勿重复提交!").put("data",null).put("code","000014"));
            return;
        }

        PortEmpRelation portEmpRelation = new PortEmpRelation();
        portEmpRelation.setFkNo(portEmp.getNo());
        portEmpRelation.setParentNo(portEmp.getParentNo());
        Boolean flag = portEmpRelation.save();

        renderJson(R.ok().put("msg","保存成功!").put("code","000000"));

    }

    /*
     * @Description //获取员工父级接口
     * @Author wangkaida
     * @Date 19:59 2020/5/19
     * @Param [no]
     * @return void
     **/
    public void getParentEmp(@Para("no") String no){

        if(StrUtil.isEmpty(no)){
            renderJson(R.error("员工账号不能为空!").put("data",null).put("code","000012"));
            return;
        }

        PortEmpRelation portEmpRelationDb = PortEmpRelation.dao.findFirst("SELECT * FROM port_emp_relation WHERE FK_No = ? LIMIT 0,1", no);
        if (portEmpRelationDb != null) {
            renderJson(R.ok().put("parentNo",portEmpRelationDb.getParentNo()).put("code","000000"));
        }else {
            renderJson(R.error("上下级关系表查无记录!").put("parentNo",no).put("data",null).put("code","000017"));
            return;
        }

    }
    
    /*
     * @Description //获取代理商员工列表接口
     * @Author wangkaida
     * @Date 9:56 2020/5/21
     * @Param [no]
     * @return void
     **/
    public void getStaffEmpList(@Para("parentNo") String parentNo){

        if(StrUtil.isEmpty(parentNo)){
            renderJson(R.error("代理商账号不能为空!").put("data",null).put("code","000012"));
            return;
        }

        List<PortEmp> portEmpList = PortEmp.dao.find("select a.* from port_emp a left join port_emp_relation b on a.No = b.FK_No where b.ParentNo = ?", parentNo);

        if (portEmpList.size() > 0) {
            renderJson(R.ok().put("data",portEmpList).put("code","000000"));
        }else {
            renderJson(R.error("上下级关系表查无记录!").put("parentNo",parentNo).put("data",null).put("code","000017"));
            return;
        }

    }

    /*
     * @Description //解绑上下级关系接口
     * @Author wangkaida
     * @Date 10:29 2020/5/21
     * @Param [portEmp]
     * @return void
     **/
    public void unBindRelation(@Para("") PortEmpReq portEmp){

        if(StrUtil.isEmpty(portEmp.getNo())){
            renderJson(R.error("员工账号不能为空!").put("data",null).put("code","000012"));
            return;
        }

        if(StrUtil.isEmpty(portEmp.getParentNo())){
            renderJson(R.error("父级账号不能为空!").put("data",null).put("code","000013"));
            return;
        }

        //判断是否存在上下级关系
        PortEmpRelation portEmpRelationDb = PortEmpRelation.dao.findFirst("SELECT * FROM port_emp_relation WHERE FK_No = ? and ParentNo = ? LIMIT 0,1", portEmp.getNo(),portEmp.getParentNo());

        if (portEmpRelationDb != null) {

            Db.delete("delete from port_emp_relation WHERE FK_No = ? and ParentNo = ?", portEmp.getNo(),portEmp.getParentNo());
            renderJson(R.ok().put("msg","解绑成功!").put("code","000000"));

        }else {
            renderJson(R.error("上下级关系表查无记录!").put("data",null).put("code","000017"));
            return;
        }

    }

    /*
     * @Description //根据代理商openId查询粉丝信息接口
     * @Author wangkaida
     * @Date 17:13 2020/5/21
     * @Param [agentId]
     * @return void
     **/
    public void queryFansByAgentId(@Para("") PortEmpReq portEmp){

        if(StrUtil.isEmpty(portEmp.getAgentId())){
            renderJson(R.error("代理商微信openId不能为空!").put("data",null).put("code","000023"));
            return;
        }

        String httpUrl = "http://14.23.82.211:7097/agentfans/queryFansByAgentId";
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("agentId",portEmp.getAgentId());
        map.put("pageSize",String.valueOf(portEmp.getPageSize()));
        map.put("page",String.valueOf(portEmp.getPageIndex()));

        String result = HttpClientUtil.doPost(httpUrl, map,null,null);
        JSONObject jsonObjResult = JSONObject.parseObject(result);

        renderJson(R.ok().put("data",jsonObjResult).put("code","000000"));

    }

    /*
     * @Description //待办通过接口
     * @Author wangkaida
     * @Date 12:02 2020/5/23
     * @Param [portEmp]
     * @return void
     **/
    public void todoPass(@Para("") PortEmpReq portEmp){

        if(StrUtil.isEmpty(portEmp.getFkFlow())){
            renderJson(R.error("FkFlow不能为空!").put("data",null).put("code","000032"));
            return;
        }

        if(portEmp.getWorkID() == null){
            renderJson(R.error("WorkID不能为空!").put("data",null).put("code","000033"));
            return;
        }

        Hashtable myhtSend = new Hashtable();

        try {
            //发送流程
            myhtSend.put("ShouJiHaoMa", portEmp.getTel());
            myhtSend.put("appOpenId", portEmp.getWxAppOpenId());
            myhtSend.put("XingMing", portEmp.getName());
            myhtSend.put("ParentNo", portEmp.getParentNo());
            myhtSend.put("LeiBie", "2");
            myhtSend.put("TB_OID", portEmp.getWorkID());
            myhtSend.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myhtSend.put("TB_Title", URLEncoder.encode("亚太天能-admin,admin在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
            myhtSend.put("TB_FID", 0);
            myhtSend.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myhtSend.put("TB_Rec", "admin");
            myhtSend.put("TB_Emps", "admin");
            myhtSend.put("TB_FK_Dept", 100);
            myhtSend.put("TB_FK_NY", DataType.getCurrentYearMonth());
            myhtSend.put("TB_MyNum", 1);
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork(portEmp.getFkFlow(),portEmp.getWorkID(),myhtSend,null,0,null);
            String sendSuccess = "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("msg","执行成功!").put("code","000000"));

    }

    /*
     * @Description //待办拒绝接口
     * @Author wangkaida
     * @Date 12:14 2020/5/23
     * @Param [portEmp]
     * @return void
     **/
    public void todoRefuse(@Para("") PortEmpReq portEmp){

        if(StrUtil.isEmpty(portEmp.getFkFlow())){
            renderJson(R.error("FkFlow不能为空!").put("data",null).put("code","000032"));
            return;
        }

        if(portEmp.getWorkID() == null){
            renderJson(R.error("WorkID不能为空!").put("data",null).put("code","000033"));
            return;
        }

        String result = "";

        try {
            result = BP.WF.Dev2Interface.Flow_DoFlowOver(portEmp.getFkFlow(),portEmp.getWorkID(),null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderJson(R.ok().put("msg","执行成功!").put("result",result).put("code","000000"));

    }

    /**
     * 判断账号类型
     */
    public void accountTypeback(@Para("") PortEmpReq portEmpReq){
        PortEmp portEmp = new PortEmp();
        portEmp.setTel(portEmpReq.getTel());
        renderJson(R.ok().put("msg","执行成功!").put("data",portEmpService.getPortEmp(portEmp)).put("code","000000"));
    }

    /*
     * @Description //微信openId绑定手机号临时接口
     * @Author wangkaida
     * @Date 10:16 2020/6/19
     * @Param [portEmpReq]
     * @return void
     **/
    public void wechatBindTmp(@Para("") PortEmpReq portEmpReq){
//        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmpReq.getTel())){
            renderJson(R.error("请输入手机号!").put("data",null).put("code","000003"));
            return;
        }

        //判断手机验证码是否正确
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setMobile(portEmpReq.getTel());
        String result = smsService.getSmsByMobile(loginRequestDto);

        if (!portEmpReq.getValiCode().equals(result)) {
            renderJson(R.error("请输入正确的验证码!").put("data",null).put("code","000024"));
            return;
        }

        //手机号获取数据信息
        PortActivityEmp portEmpDb = PortActivityEmp.dao.findFirst("SELECT * FROM port_activity_emp WHERE Tel = ? LIMIT 0,1", portEmpReq.getTel());;

        if (portEmpDb != null) {
            if (StrUtil.isEmpty(portEmpDb.getWxOpenId())) {
                Paras ps = new Paras();
                ps.Add("WxOpenId", portEmpReq.getWxOpenId());
                ps.Add("Tel", portEmpReq.getTel());
                String sql = "UPDATE port_activity_emp SET WxOpenId="+SystemConfig.getAppCenterDBVarStr()+"WxOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                        + "Tel";
                int num = DBAccess.RunSQL(sql, ps);
                renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
            }else {
                renderJson(R.error("该手机号已经绑定，请勿重复绑定!").put("data",null).put("code","000004"));
                return;
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }

    }

    /*
     * @Description //根据微信OpenId获取员工信息接口
     * @Author wangkaida
     * @Date 20:57 2020/7/13
     * @Param [portEmpReq]
     * @return void
     **/
    public void getPortEmpByWxOpenId(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        //手机号获取数据信息
        PortActivityEmp portEmpDb = PortActivityEmp.dao.findFirst("SELECT * FROM port_activity_emp WHERE WxOpenId = ? LIMIT 0,1", portEmpReq.getWxOpenId());

        if (portEmpDb != null) {
            renderJson(R.ok().put("data",portEmpDb).put("code","000000"));

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }

    }

    /**
     * 已登陆账户更新密码
     */
    public void updatePassword(@Para("") PortEmpReq portEmpReq) throws Exception{
        PortEmp portEmp = new PortEmp();
        String no = WebUser.getNo();
        if(StringUtils.isNotEmpty(no)){
            portEmp.setNo(no);
            portEmp.setPass(portEmpReq.getPass());
            portEmp.update();
            renderJson(R.ok().put("data",portEmp).put("code","000000"));
        }else{
            renderJson(R.error("还没有登陆，请登陆!").put("data",null).put("code","000001"));
        }
    }

    /**
     * 查询当前登陆人菜单权限
     */
    public void queryMenuByUserNo() throws Exception{
        String no = WebUser.getNo();
        if( StringUtils.isNotEmpty(no) && !"admin".equals(no)){
            List<Record> recordList = Db.find(Db.getSqlPara("gpm.menu.queryMenuByUserId", Kv.by("userNo",no)));
            renderJson(R.ok().put("data",recordList).put("admin",false).put("code","000000"));
        }else if("admin".equals(no)){
            renderJson(R.ok().put("data","该用户是管理员").put("admin",true).put("code","000000"));
        }else{
            renderJson(R.error("还没有登陆，请登陆!").put("data",null).put("code","000001"));
        }
    }
    /**
     * 查询所有人员
     */
    public void queryEmpAll(BasePageRequest basePageRequest) throws Exception{
        renderJson(R.ok().put("data",portEmpService.queryPageList(basePageRequest)));
    }
}