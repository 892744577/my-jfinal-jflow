package BP.Difference.Handler;

import BP.WF.HttpHandler.WF_Admin_CCBPMDesigner;

public class CCBPMDesignerController extends HttpHandlerBase {
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	public final void ProcessRequest() 
	{
		WF_Admin_CCBPMDesigner  CCBPMDHandler = new WF_Admin_CCBPMDesigner();
		super.ProcessRequestPost(CCBPMDHandler);
	}
	@Override
	public Class <WF_Admin_CCBPMDesigner>getCtrlType() {
		return WF_Admin_CCBPMDesigner.class;
	}

}
