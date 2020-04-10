/**
 * 
 */
package BP.workFlowWeb.jflowHandler;

import com.jfinal.handler.Handler;
import com.jfinal.kit.PathKit;
import com.jfinal.render.RenderManager;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 静态资源处理器
 * @author brycehan
 *
 */
public class StaticHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
		
		if(isDownloadResource(target)) {
			isHandled[0] = true;
			response.setStatus(HttpServletResponse.SC_OK);
			try {
				RenderManager.me().getRenderFactory().getFileRender(new File(PathKit.getWebRootPath() + URLDecoder.decode(target, "UTF-8"))).setContext(request, response).render();
			} catch (UnsupportedEncodingException e) {
				String msg = "err@在执行类[" + this.getClass().getName() + "]，方法[handle]错误 \t\n @" + e.getMessage();
				BP.DA.Log.DebugWriteError(msg);
			}
			return;
		}

		next.handle(target, request, response, isHandled);
	}

	/**
	 * 是否下载资源
	 * @param target
	 * @return
	 */
	private boolean isDownloadResource(String target) {
		if(target.lastIndexOf(".") >= 0) {
			String suffix = target.substring(target.lastIndexOf("."));
			if(StringUtils.isNotBlank(suffix)) {
				suffix = suffix.toLowerCase();
				return target.startsWith("/DataUser/CyclostyleFile")
						||suffix.equals(".doc") || suffix.equals(".docs")
						|| suffix.equals(".xls") || suffix.equals(".xlsx")
						|| suffix.equals(".ppt") || suffix.equals(".pptx")
						|| suffix.equals(".rtf");
			}
		}
		
		return false;
	}
}
