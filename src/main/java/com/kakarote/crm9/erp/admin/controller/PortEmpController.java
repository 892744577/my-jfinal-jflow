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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.core.paragetter.Para;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.erp.sms.entity.LoginRequestDto;
import com.kakarote.crm9.erp.sms.service.SmsService;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/*
 * @Description //组织用户管理控制类
 * @Author wangkaida
 * @Date 14:23 2020/5/11
 * @Param
 * @return
 **/
@Slf4j
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
    public void agentLogin(@Para("") PortEmpReq portEmpReq) throws Exception{

        //第三方登陆判断--小程序
        PortEmp portEmp = new PortEmp();
        portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
        PortEmp portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);

        if (portEmpDb != null) {
            if("1".equals(portEmpDb.getAccountType())){
                if (StrUtil.isNotEmpty(portEmpDb.getWxOpenId())) {
                    BP.WF.Dev2Interface.Port_Login(portEmpDb.getNo());
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
    public void staffLogin(@Para("") PortEmpReq portEmpReq) throws Exception{

        //第三方登陆判断--小程序
        PortEmp portEmp = new PortEmp();
        portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
        PortEmp portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);

        if (portEmpDb != null) {
            if("2".equals(portEmpDb.getAccountType())){
                if (StrUtil.isNotEmpty(portEmpDb.getWxOpenId())) {
                    BP.WF.Dev2Interface.Port_Login(portEmpDb.getNo());
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
        PortEmp portEmp = new PortEmp();
        if (StrUtil.isNotEmpty(portEmpReq.getName())) {
            portEmp.setNo(portEmpReq.getName());
            portEmpDb = portEmpService.getPortEmpByNo(portEmp);
            //portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE No = ? LIMIT 0,1", portEmpReq.getName());
        }else if (StrUtil.isNotEmpty(portEmpReq.getWxAppOpenId())){
            //第三方id查询用户信息
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
    @Deprecated
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

    /**
     * 绑定小程序openid与微信公众号openid
     * @param portEmpReq
     */
    public void wechatBindByAppOpenId(@Para("") PortEmpReq portEmpReq){
        //手机号获取数据信息
        PortEmp portEmp = new PortEmp();
        portEmp.setWxAppOpenId(portEmpReq.getWxAppOpenId());
        PortEmp portEmpDb = portEmpService.getPortEmpByWxAppOpenId(portEmp);

        if (portEmpDb != null) {
            portEmpDb.setWxOpenId(portEmpReq.getWxOpenId());
            portEmpDb.update();
            renderJson(R.error("小程序openid绑定公众号openid成功").put("data",null).put("code","000000"));
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

    /**
     * 发起注册流程
     * @param portEmpReq
     * @return
     */
    @NotAction
    public String  createRegisterFlow(PortEmpReq portEmpReq,String lb,String parentNo){
        //开始调用注册审批流程
        Hashtable myht = new Hashtable();
        Hashtable myhtSend = new Hashtable();
        try {
            myht.put("TB_RDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myht.put("TB_Title", URLEncoder.encode(portEmpReq.getName()+"在"+ DataType.getCurrentDateTime()+"发起.", "UTF-8"));
            myht.put("TB_FID", 0);
            myht.put("TB_CDT", URLEncoder.encode(DataType.getCurrentDateTime(), "UTF-8"));
            myht.put("TB_Rec", "admin");
            myht.put("TB_Emps", "admin");
            myht.put("TB_FK_Dept", 100);
            myht.put("TB_FK_NY", DataType.getCurrentYearMonth());
            myht.put("TB_MyNum", 1);
            myht.put("ShouJiHaoMa", portEmpReq.getTel());
            myht.put("appOpenId", portEmpReq.getWxAppOpenId());
            myht.put("XingMing", portEmpReq.getName());
            if("2".equals(lb)){ //员工发起的注册申请需要父级no
                myht.put("ParentNo", parentNo);
            }
            myht.put("LeiBie", lb);
            myhtSend.putAll(myht);
            WebUser.SignInOfGenerAuth(new Emp("admin"), "admin");
            long workID = BP.WF.Dev2Interface.Node_CreateBlankWork("008",myht,null,"admin",
                    null,0,0,null,0,null,0,null,null,null);
            //发送流程
            myhtSend.put("TB_OID", workID);
            SendReturnObjs returnObjs = BP.WF.Dev2Interface.Node_SendWork("008",workID,myhtSend,null,0,null);
            return "父流程自动运行到下一个节点，发送过程如下：\n @接收人" + returnObjs.getVarAcceptersName() + "\n @下一步[" + returnObjs.getVarCurrNodeName() + "]启动";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
            log.info("=======appBind接口,代理商绑定，根据手机查账号已存在："+ JSON.toJSONString(portEmpReq));
            //创建并发起注册审批流程
            createRegisterFlow(portEmpReq,"1",null);
            renderJson(R.ok().put("msg","注册成功，请等待审核!").put("code","000000"));
        }else {
            log.info("=======appBind接口,代理商绑定，根据手机查账号不存在："+ JSON.toJSONString(portEmpReq));
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
            //创建并发起注册审批流程
            createRegisterFlow(portEmpReq,"1",null);
            renderJson(R.ok().put("msg","更新成功!").put("code","000000"));
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
            log.info("=======staffAppBind接口,团员绑定，根据手机查账号已存在："+ JSON.toJSONString(portEmpDb));
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
            log.info("=======staffAppBind接口,团员绑定，根据手机查账号不存在，发起流程给代理商："+ JSON.toJSONString(portEmp));
            //创建并发起注册审批流程
            createRegisterFlow(portEmp,"2",portEmpParent.getNo());
            renderJson(R.ok().put("msg","绑定流程已发起,请等待负责人审核通过!").put("data","1").put("code","000000"));
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
    @Deprecated
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
        }
    }

    /*
     * @Description //获取团队成员列表接口
     * @Author wangkaida
     * @Date 9:56 2020/5/21
     * @Param [no]
     * @return void
     **/
    public void getStaffEmpListByTeamNo(@Para("teamNo") String teamNo){

        List<PortEmp> list = PortEmp.dao.find("select a.* from port_emp a where a.accountType='1' and a.teamNo=?",teamNo);
        list.stream().forEach(item->{
            List<PortEmp> portEmpList = PortEmp.dao.find("select a.* from port_emp a left join port_emp_relation b on a.No = b.FK_No where b.ParentNo = ?", item.getNo());
            item.put("teamList",portEmpList);
        });
        if (list.size() > 0) {
            renderJson(R.ok().put("data",list).put("code","000000"));
        }else {
            renderJson(R.error("上下级关系表查无记录!").put("parentNo",teamNo).put("data",null).put("code","000017"));
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
    @Deprecated
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
     * @Description //根据fkDept查询粉丝信息接口
     * @Author wangkaida
     * @Date 11:31 2021/1/7
     * @Param [portEmp]
     * @return void
     **/
    public void queryFansByFkDept(BasePageRequest basePageRequest) {
        String teamNo = basePageRequest.getJsonObject().getString("teamNo");
        String portEmpNo = basePageRequest.getJsonObject().getString("portEmpNo");

        if(StrUtil.isEmpty(teamNo)){
            renderJson(R.error("teamNo不能为空!").put("data",null).put("code","000023"));
            return;
        }

        if(StrUtil.isEmpty(portEmpNo)){
            renderJson(R.error("No不能为空!").put("data",null).put("code","000024"));
            return;
        }

        renderJson(R.ok().put("data",portEmpService.queryFansByFkDeptPageList(basePageRequest,teamNo,portEmpNo)));
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
            myhtSend.put("TB_Title", URLEncoder.encode(portEmp.getName()+"在"+DataType.getCurrentDateTime()+"发起.", "UTF-8"));
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
    public void getActivityEmpByWxOpenId(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        //根据微信号和活动id获取店员信息
        PortActivityEmp portEmpDb = PortActivityEmp.dao.findFirst(
                Db.getSql("admin.portActivityEmp.getActivityEmpByWxOpenId"),
                portEmpReq.getWxOpenId(),
                portEmpReq.getPb_ac_id());

        if (portEmpDb != null) {
            renderJson(R.ok().put("data",portEmpDb).put("code","000000"));

        }else {
            renderJson(R.error("查无此人,请先进行手机号绑定!").put("data",null).put("code","000001"));
            return;
        }
    }

    /**
     * 上传群二维码
     * @param portEmpReq
     */
    public void saveCrowdWxCodeByOpenId(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        //根据微信号和活动id获取店员信息
        PortActivityEmp portActivityEmp = PortActivityEmp.dao.findFirst(
                Db.getSql("admin.portActivityEmp.getActivityEmpByWxOpenId"),
                portEmpReq.getWxOpenId(),
                portEmpReq.getPb_ac_id());

        if(getFiles().size()>0 && portActivityEmp!=null){
            String fileName = upload(getFiles()).stream().map(item->item.get(FileUploadUtil.ACCESS_PATH)).collect(Collectors.joining(";"));
            portActivityEmp.setCrowdName(portEmpReq.getCrowdName());
            portActivityEmp.setSID(fileName);
            portActivityEmp.update();
            renderJson(R.ok().put("data",portActivityEmp).put("code","000000"));
        }else {
            renderJson(R.error("文件上传失败，请联系管理员").put("data",null).put("code","000001"));
            return;
        }
    }

    /**
     * 上传群二维码到海报表
     * @param portEmpReq
     */
    public void saveCrowdWxCodeByOpenId2(@Para("") PortEmpReq portEmpReq){
        if(StrUtil.isEmpty(portEmpReq.getWxOpenId())){
            renderJson(R.error("请输入微信公众号openId!").put("data",null).put("code","000038"));
            return;
        }

        //根据微信号和活动id获取店员信息
        PortActivityPlaybill portActivityPlaybill = PortActivityPlaybill.dao.findFirst(
                Db.getSql("admin.portActivityEmp.getActivityPlaybillByWxOpenId"),
                portEmpReq.getWxOpenId(),
                portEmpReq.getPb_ac_id());

        if(getFiles().size()>0 && portActivityPlaybill!=null){
            String fileName = upload(getFiles()).stream().map(item->item.get(FileUploadUtil.ACCESS_PATH)).collect(Collectors.joining(";"));
            portActivityPlaybill.setPbCrowdName(portEmpReq.getCrowdName());
            portActivityPlaybill.setPbCrowd(fileName);
            portActivityPlaybill.update();
            renderJson(R.ok().put("data",portActivityPlaybill).put("code","000000"));
        }else {
            renderJson(R.error("文件上传失败，请联系管理员").put("data",null).put("code","000001"));
            return;
        }
    }

    /**
     * 群二维码上传文件
     * @param list
     * @return
     */
    @NotAction
    public List<Map<String, String>> upload(List<UploadFile> list ) {
        log.info("开始执行文件上传方法!");
        return FileUploadUtil.upload(list, BaseConstant.UPLOAD_PATH_HD,"");
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