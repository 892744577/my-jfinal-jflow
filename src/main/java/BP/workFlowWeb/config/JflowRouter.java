package BP.workFlowWeb.config;

import BP.Difference.Handler.*;
import BP.workFlowWeb.Controller.RestFulController;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.FakeStaticHandler;

public class JflowRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new JflowInterceptor());
        add("/WF/Comm", WF_Comm_Controller.class);
        add("/WF/Admin/CCBPMDesigner", CCBPMDesignerController.class);
        add("/DataUser/SFTableHandler", SFTableHandler_Controller.class);
        add("/WF/Ath", AttachmentUploadController.class);
        add("/restful", RestFulController.class);
    }
}
