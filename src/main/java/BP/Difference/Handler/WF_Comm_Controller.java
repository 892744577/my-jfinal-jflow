package BP.Difference.Handler;

import javax.servlet.http.HttpServletRequest;

import BP.WF.HttpHandler.WF_Comm;

public class WF_Comm_Controller extends HttpHandlerBase {

	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	public void ProcessRequest() { 
		HttpServletRequest request = getRequest();
//		WF_Comm CommHandler = new WF_Comm();
//		if (request instanceof DefaultMultipartHttpServletRequest) {
//			CommHandler.setMultipartRequest((DefaultMultipartHttpServletRequest) request);
//			BP.WF.Glo.request = (DefaultMultipartHttpServletRequest) request;
//		}
//		super.ProcessRequest(CommHandler);
		
		WF_Comm CommHandler = new WF_Comm();
		
		super.ProcessRequestPost(CommHandler);
	}

	@Override
	public Class<WF_Comm> getCtrlType() {
		return WF_Comm.class;
	}

}
