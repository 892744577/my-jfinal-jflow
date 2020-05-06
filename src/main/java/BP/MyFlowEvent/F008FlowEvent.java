package BP.MyFlowEvent;

import BP.WF.FlowEventBase;

public class F008FlowEvent extends FlowEventBase {
    @Override
    public String getFlowMark() {
        return "aptenon_zcsh";
    }

    public String FlowOverAfter() {

        return "F00101FlowEvent请假流程结束";
    }

}
