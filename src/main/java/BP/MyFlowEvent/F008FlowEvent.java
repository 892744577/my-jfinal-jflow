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

        Regist regist = new Regist();
        regist.setPhone((String) this.getSysPara().get("ShouJiHaoMa"));
        regist.setAppOpenId((String) this.getSysPara().get("appOpenId"));
        regist.setName((String) this.getSysPara().get("XingMing"));
        Aop.get(SysRegistService.class).savePortEmp(regist);

        return "F008FlowEvent注册审核流程结束";
    }

}
