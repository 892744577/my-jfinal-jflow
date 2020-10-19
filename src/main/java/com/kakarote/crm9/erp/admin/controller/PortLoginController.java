package com.kakarote.crm9.erp.admin.controller;

import BP.Port.Emp;
import BP.Web.WebUser;
import cn.hutool.core.util.StrUtil;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.utils.R;

public class PortLoginController extends Controller {

    /*
     * @Description //账号登录接口
     * @Author wangkaida
     * @Date 11:24 2020/5/23
     * @Param [portEmp]
     * @return void
     **/
    public void Port_LoginByNo(@Para("") PortEmpReq portEmp) throws Exception{

        if(StrUtil.isEmpty(portEmp.getNo())){
            renderJson(R.error("员工账号不能为空!").put("data",null).put("code","000031"));
            return;
        }
        WebUser.SignInOfGenerAuth(new Emp(portEmp.getNo()), "admin");

        renderJson(R.ok().put("msg","登录成功!").put("code","000000"));

    }

    /**
     * @Description //根据小程序openid登录接口
     * @param portEmp
     * @throws Exception
     */
    public void Port_LoginByWxAppOpenId(@Para("") PortEmpReq portEmp)  throws Exception{
        if(StrUtil.isEmpty(portEmp.getWxAppOpenId())){
            renderJson(R.error("小程序openid不能为空!").put("data",null).put("code","000031"));
            return;
        }
        PortEmp emp = PortEmp.dao.findFirst(Db.getSql("admin.portEmp.getEmpByWxAppOpenId"),
                portEmp.getWxAppOpenId());
        if(emp!=null){
            BP.WF.Dev2Interface.Port_Login(emp.getNo());
        }
        renderJson(R.ok().put("msg","登录成功!").put("code","000000"));
    }

    /**
     * @Description //根据账号密码登陆
     * @param portEmp
     * @throws Exception
     */
    public void Port_LoginByPass(@Para("") PortEmpReq portEmp)  throws Exception{
        if(StrUtil.isEmpty(portEmp.getNo())){
            renderJson(R.error("账号不能为空!").put("data",null).put("code","000031"));
            return;
        }
        if(StrUtil.isEmpty(portEmp.getPass())){
            renderJson(R.error("密码不能为空!").put("data",null).put("code","000031"));
            return;
        }
        PortEmp emp = PortEmp.dao.findFirst(Db.getSql("admin.portEmp.getEmpByPass"),
                portEmp.getNo(),portEmp.getPass());
        if(emp!=null){
            BP.WF.Dev2Interface.Port_Login(emp.getNo());
            renderJson(R.ok().put("msg","登录成功!").put("code","000000").put("data",emp));
        }else{
            renderJson(R.ok().put("msg","账号不存在或密码错误!").put("code","000031"));
        }
    }
}
