package com.kakarote.crm9.erp.admin.service;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Sys.CCFormAPI;
import com.kakarote.crm9.erp.admin.entity.*;
import com.kakarote.crm9.utils.R;

import java.util.UUID;

public class SysRegistService {

    /*
     * @Description //保存代理商注册信息到用户表
     * @Author wangkaida
     * @Date 10:40 2020/5/9
     * @Param [regist]
     * @return com.kakarote.crm9.utils.R
     **/
    public R savePortEmp(Regist regist) {

        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? LIMIT 0,1", regist.getPhone());

        if (portEmpDb != null) {

            //更新账号信息，主要是账号的小程序id，针对导进去的账号信息
            Paras ps = new Paras();
            ps.Add("WxAppOpenId", regist.getAppOpenId());
            ps.Add("Tel", regist.getPhone());
            String sql = "UPDATE port_emp SET WxAppOpenId="+ SystemConfig.getAppCenterDBVarStr()+"WxAppOpenId,accountType = '1' WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                    + "Tel";
            int num = DBAccess.RunSQL(sql, ps);
            return R.ok().put("msg","更新成功!").put("code","000000");

        }else {
            //1、新增代理商账号
            PortEmp portEmp = new PortEmp();

            String pinyin1 = DataType.ParseStringToPinyin(regist.getName());
            String pinyin2 = DataType.ParseStringToPinyinJianXie(regist.getName());
            String pinyin = "," + pinyin1 + "," + pinyin2 + ",";

            portEmp.setNo(CCFormAPI.ParseStringToPinyinField(regist.getName(), true, true, 100).toLowerCase());
//            portEmp.setNo(UUID.randomUUID().toString().replace("-", ""));
            portEmp.setName(regist.getName());
            portEmp.setWxAppOpenId(regist.getAppOpenId());
            portEmp.setTel(regist.getPhone());
            portEmp.setPinYin(pinyin);
            portEmp.setFkDept("100");
            portEmp.setSignType(0);
            portEmp.setIdx(0);

            //1、代理商标志
            portEmp.setAccountType("1");

            Boolean flag = portEmp.save();

            return flag ? R.ok().put("msg","保存成功!").put("code","000000") : R.error("保存失败!").put("code","000001");
        }

    }

    /*
     * @Description //保存普通员工注册信息到用户表
     * @Author wangkaida
     * @Date 18:11 2020/5/15
     * @Param [regist]
     * @return com.kakarote.crm9.utils.R
     **/
    public R savePortEmpStaff(Regist regist) {
        PortEmp portEmpDb = PortEmp.dao.findFirst("SELECT * FROM port_emp WHERE Tel = ? LIMIT 0,1", regist.getPhone());

        if (portEmpDb != null) {
            //更新账号信息，主要是账号的小程序id，针对导进去的账号信息
            Paras ps = new Paras();
            ps.Add("WxAppOpenId", regist.getAppOpenId());
            ps.Add("Tel", regist.getPhone());
            String sql = "UPDATE port_emp SET WxAppOpenId="+ SystemConfig.getAppCenterDBVarStr()+"WxAppOpenId WHERE Tel=" + SystemConfig.getAppCenterDBVarStr()
                    + "Tel";
            int num = DBAccess.RunSQL(sql, ps);
            return R.ok().put("msg","更新成功!").put("code","000000");
        }else {

            //1、新增账号
            PortEmp portEmp = new PortEmp();

            String pinyin1 = DataType.ParseStringToPinyin(regist.getName());
            String pinyin2 = DataType.ParseStringToPinyinJianXie(regist.getName());
            String pinyin = "," + pinyin1 + "," + pinyin2 + ",";

            String no = CCFormAPI.ParseStringToPinyinField(regist.getName(), true, true, 100).toLowerCase();

            portEmp.setNo(no);
//            portEmp.setNo(UUID.randomUUID().toString().replace("-", ""));
            portEmp.setName(regist.getName());
            portEmp.setWxAppOpenId(regist.getAppOpenId());
            portEmp.setTel(regist.getPhone());
            portEmp.setPinYin(pinyin);
            portEmp.setFkDept("100");
            portEmp.setSignType(0);
            portEmp.setIdx(0);
            portEmp.setAccountType("2");

            Boolean flag = portEmp.save();

            //2、保存上下级关系
            PortEmpRelation portEmpRelation = new PortEmpRelation();
            portEmpRelation.setFkNo(no);
            portEmpRelation.setParentNo(regist.getParentNo());
            portEmpRelation.save();

            return flag ? R.ok().put("msg","保存成功!").put("code","000000") : R.error("保存失败!").put("code","000001");
        }
    }
}
