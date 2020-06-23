package com.kakarote.crm9.erp.admin.controller;

import BP.DA.DBAccess;
import BP.DA.Paras;
import BP.Difference.SystemConfig;
import cn.hutool.core.util.StrUtil;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.admin.entity.PortDept;
import com.kakarote.crm9.erp.admin.entity.vo.PortEmpReq;
import com.kakarote.crm9.utils.R;

import java.util.UUID;

public class PortDeptController extends Controller {
    /*
     * @Description //t100同步代理商部门接口
     * @Author wangkaida
     * @Date 10:08 2020/6/1
     * @Param [portEmp]
     * @return void
     **/
    public void optAgentDept(@Para("") PortEmpReq portEmpReq){

        int optType = portEmpReq.getOptType(); //操作类型: 1.新增 2.修改

        if(StrUtil.isEmpty(portEmpReq.getT100Code())){
            renderJson(R.error("客户编号不能为空!").put("data",null).put("code","000038"));
            return;
        }

        if(StrUtil.isEmpty(portEmpReq.getName())){
            renderJson(R.error("客户名称不能为空!").put("data",null).put("code","000037"));
            return;
        }

        if (optType == 1) {

            PortDept portDeptDb = PortDept.dao.findFirst("SELECT * FROM port_dept WHERE No = ? LIMIT 0,1", portEmpReq.getT100Code());

            if (portDeptDb != null) {
                renderJson(R.error("客户编号已经存在!").put("data",null).put("code","000039"));
                return;
            }

            PortDept portDept = new PortDept();
            portDept.setNo(portEmpReq.getT100Code());
            portDept.setName(portEmpReq.getName());
            portDept.setParentNo("121");
            portDept.setIdx(0);
            portDept.save();

            renderJson(R.ok().put("msg","保存成功!").put("data",portDept).put("code","000000"));

        }else if (optType == 2) {

            PortDept portDeptDb = PortDept.dao.findFirst("SELECT * FROM port_dept WHERE No = ? LIMIT 0,1", portEmpReq.getT100Code());

            if (portDeptDb != null) {
//                Paras ps = new Paras();
//                ps.Add("Name", portEmpReq.getName());
//                ps.Add("No", portEmpReq.getT100Code());
//                String sql = "UPDATE port_dept SET Name="+ SystemConfig.getAppCenterDBVarStr()+"Name WHERE No=" + SystemConfig.getAppCenterDBVarStr()
//                        + "No";
//                int num = DBAccess.RunSQL(sql, ps);

                PortDept portDept = new PortDept();
                portDept.setName(portEmpReq.getName());
                portDept.setNo(portEmpReq.getT100Code());
                portDept.update();
                renderJson(R.ok().put("msg","更新成功!").put("data",portDeptDb).put("code","000000"));

            }else {
                renderJson(R.error("客户编号不存在!").put("data",null).put("code","000040"));
                return;
            }
        }
    }
}
