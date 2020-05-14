package com.kakarote.crm9.erp.admin.controller;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Port.Emp;
import BP.WF.SendReturnObjs;
import BP.Web.WebUser;
import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminUser;
import com.kakarote.crm9.erp.admin.entity.HrRegister;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.admin.service.AdminFileService;
import com.kakarote.crm9.erp.admin.service.AdminUserService;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.BaseUtil;
import com.kakarote.crm9.utils.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.UUID;


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
    public void agentLogin(){

        PortEmp portEmp = getModel(PortEmp.class,"");

        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE WxAppOpenId = ? and accountType = '1' LIMIT 0,1", portEmp.getWxAppOpenId());

        if (portEmpDb != null) {
            if (StrUtil.isNotEmpty(portEmpDb.getWxOpenId())) {

                try {
                    BP.WF.Dev2Interface.Port_Login(portEmpDb.getNo());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //代理商已经创建账号直接登录
                renderJson(R.ok().put("msg","登录成功!").put("code","000000"));
            }else {
                renderJson(R.error("请到微信公众平台注册手机号绑定!").put("code","000002"));
                return;
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("code","000001"));
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
    public void wechatBind(){
        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmp.getTel())){
            renderJson(R.error("请输入手机号!").put("code","000003"));
            return;
        }

        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmp.getTel());

        if (portEmpDb != null) {
            if (StrUtil.isEmpty(portEmpDb.getWxOpenId())) {
                Paras ps = new Paras();
                ps.Add("WxOpenId", portEmp.getWxOpenId());
                ps.Add("Tel", portEmp.getTel());
                String sql = "UPDATE port_emp SET WxOpenId="+SystemConfig.getAppCenterDBVarStr()+"WxOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                        + "Tel and accountType = '1'";
                int num = DBAccess.RunSQL(sql, ps);
                renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
            }else {
                renderJson(R.error("该手机号已经绑定，请勿重复绑定!").put("code","000004"));
                return;
            }

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("code","000001"));
            return;
        }

    }

    /*
     * @Description //小程序openId绑定手机号
     * @Author wangkaida
     * @Date 17:11 2020/5/12
     * @Param [portEmp]
     * @return void
     **/
    public void appBind(){
        PortEmp portEmp = getModel(PortEmp.class,"");

        if(StrUtil.isEmpty(portEmp.getTel())){
            renderJson(R.error("请输入手机号!").put("code","000003"));
            return;
        }

        //判断流程是否在进行中
        HrRegister hrRegister = HrRegister.dao.findFirst("SELECT * FROM hr_register WHERE ShouJiHaoMa = ? and appOpenId = ? LIMIT 0,1", portEmp.getTel(),portEmp.getWxAppOpenId());

        if (hrRegister != null) {
            int wfState = hrRegister.getWFState();
            if (wfState == 2) { //流程进行中
                renderJson(R.error("微信小程序绑定手机号流程已经发起,请勿重复提交!").put("code","000005"));
                return;
            }
        }

        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmp.getTel());

        if (portEmpDb != null) {
            //开始调用注册审批流程
            Hashtable myht = new Hashtable();
            Hashtable myhtSend = new Hashtable();
            try {
                myht.put("ShouJiHaoMa", portEmp.getTel());
                myht.put("appOpenId", portEmp.getWxAppOpenId());
                myht.put("XingMing", portEmp.getName());
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
                myhtSend.put("ShouJiHaoMa", portEmp.getTel());
                myhtSend.put("appOpenId", portEmp.getWxAppOpenId());
                myhtSend.put("XingMing", portEmp.getName());
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

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            PortEmp portEmpTel = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? and accountType = '1' LIMIT 0,1", portEmp.getTel());
            if(portEmpTel != null){
                renderJson(R.error("该手机号已经被注册!"));
                return;
            }

            PortEmp portEmpName = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Name = ? and accountType = '1' LIMIT 0,1", portEmp.getName());
            if(portEmpName != null){
                renderJson(R.error("该姓名已经被注册!"));
                return;
            }

            Regist regist = getModel(Regist.class);
            regist.setMyPK(UUID.randomUUID().toString().replace("-", ""));
            regist.setPhone(portEmp.getTel());
            regist.setAppOpenId(portEmp.getWxAppOpenId());
            regist.setName(portEmp.getName());
            regist.save();

            //开始调用注册审批流程
            Hashtable myht = new Hashtable();
            Hashtable myhtSend = new Hashtable();
            try {
                myht.put("ShouJiHaoMa", portEmp.getTel());
                myht.put("appOpenId", portEmp.getWxAppOpenId());
                myht.put("XingMing", portEmp.getName());
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
                myhtSend.put("ShouJiHaoMa", portEmp.getTel());
                myhtSend.put("appOpenId", portEmp.getWxAppOpenId());
                myhtSend.put("XingMing", portEmp.getName());
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

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        renderJson(R.ok().put("msg","更新成功!").put("code","000000"));

    }

}
