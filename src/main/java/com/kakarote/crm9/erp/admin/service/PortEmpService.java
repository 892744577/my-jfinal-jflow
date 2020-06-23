package com.kakarote.crm9.erp.admin.service;

import BP.DA.DataType;
import BP.Sys.CCFormAPI;
import com.jfinal.aop.Inject;
import com.kakarote.crm9.erp.admin.entity.PortEmp;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.erp.yzj.service.TokenService;
import com.kakarote.crm9.utils.R;

import java.util.List;

public class PortEmpService {

    @Inject
    private TokenService tokenService;

    public R queryAllUserList() {

        String accessToken = tokenService.getAccessToken(null,tokenService.getErpSecret(),tokenService.getEid(),"resGroupSecret");

        return R.ok().put("data", "");
    }

    /**
     * 判断账号情况
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
     * 第三方查找账号
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
}
