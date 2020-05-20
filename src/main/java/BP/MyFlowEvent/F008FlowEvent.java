package BP.MyFlowEvent;

import BP.WF.FlowEventBase;
import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.admin.entity.Regist;
import com.kakarote.crm9.erp.admin.service.SysRegistService;

public class F008FlowEvent extends FlowEventBase {
    @Override
    public String getFlowMark() {
        return "aptenon_zcsh";
    }

    public String FlowOverAfter() {

       String type = (String) this.getSysPara().get("LeiBie");  //1.代理商 2.非代理商

        if ("1".equals(type)) {

            Regist regist = new Regist();
            regist.setPhone((String) this.getSysPara().get("ShouJiHaoMa"));
            regist.setAppOpenId((String) this.getSysPara().get("appOpenId"));
            regist.setName((String) this.getSysPara().get("XingMing"));
            Aop.get(SysRegistService.class).savePortEmp(regist);

        }else if ("2".equals(type)) {

            Regist regist = new Regist();
            regist.setPhone((String) this.getSysPara().get("ShouJiHaoMa"));
            regist.setAppOpenId((String) this.getSysPara().get("appOpenId"));
            regist.setName((String) this.getSysPara().get("XingMing"));
            Aop.get(SysRegistService.class).savePortEmpStaff(regist);

        }

        return "F008FlowEvent注册审核流程结束";
    }

}
