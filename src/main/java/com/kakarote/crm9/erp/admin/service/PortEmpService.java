package com.kakarote.crm9.erp.admin.service;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import BP.Sys.CCFormAPI;
import com.jfinal.aop.Inject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.PortEmpRelation;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

public class PortEmpService {

    @Inject
    private TokenService tokenService;

    public R queryAllUserList() {

        String accessToken = tokenService.getAccessToken(null,tokenService.getErpSecret(),tokenService.getEid(),"resGroupSecret");

        return R.ok().put("data", "");
    }

    /**
     * 手机号查找账号信息
     * @param portEmp
     */
    public PortEmp getPortEmp(PortEmp portEmp){

        PortEmp portEmp0 = new PortEmp();
        PortEmp portEmp1 = new PortEmp();
        PortEmp portEmp2 = new PortEmp();

        //1、账号是否存在
        portEmp0 = PortEmp.dao.findFirst("SELECT * FROM port_emp a where a.tel=? ", portEmp.getTel());
        if(portEmp0!=null){

            //2、账号是否代理商
            portEmp1 = PortEmp.dao.findFirst("SELECT * FROM port_emp a where a.tel=? and a.accountType=1", portEmp.getTel());
            //3、账号是否员工
            portEmp2 = PortEmp.dao.findFirst("SELECT * FROM port_emp_relation a LEFT JOIN port_emp b ON a.FK_No = b.No where tel=?", portEmp.getTel());
            if(portEmp1 !=null ){
                return portEmp1;
            }else if(portEmp2 !=null){
                portEmp2.setAccountType("2");
                return portEmp2;
            }else{
                return portEmp0;
            }

        }else{
            return null;
        }
    }

    /**
     * 主键查找账号信息
     * @param portEmp
     */
    public PortEmp getPortEmpByNo(PortEmp portEmp){
        PortEmp portEmp0 = new PortEmp();
        //1、主键是否存在
        portEmp0 = PortEmp.dao.findFirst("SELECT * FROM port_emp a where a.No=? ", portEmp.getNo());
        if(portEmp0!=null){
            portEmp.setTel(portEmp0.getTel());
            return this.getPortEmp(portEmp); //该小程序openId已绑定手机号，判断是代理商还是员工
        }else{
            return null;
        }
    }

    /**
     * 第三方查找账号信息
     * @param portEmp
     */
    public PortEmp getPortEmpByWxAppOpenId(PortEmp portEmp){
        PortEmp portEmp0 = new PortEmp();
        //1、第三方查找账号是否存在
        portEmp0 = PortEmp.dao.findFirst("SELECT * FROM port_emp a where a.WxAppOpenId=? ", portEmp.getWxAppOpenId());
        if(portEmp0!=null){
            portEmp.setTel(portEmp0.getTel());
            return this.getPortEmp(portEmp); //该小程序openId已绑定手机号，判断是代理商还是员工
        }else{
            return null;
        }
    }

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

    /*
     * @Description //根据姓名获取员工信息
     * @Author wangkaida
     * @Date 15:19 2020/8/18
     * @Param [portEmp]
     * @return com.kakarote.crm9.erp.admin.entity.PortEmp
     **/
    public Page<Record> queryPageList(BasePageRequest basePageRequest) {
        return Db.paginate(
                basePageRequest.getPage(),
                basePageRequest.getLimit(),
                Db.getSqlPara("admin.portEmp.queryPageList")
        );
    }
}
