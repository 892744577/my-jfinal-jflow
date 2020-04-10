package BP.Difference.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import BP.Difference.RequestContext;

/**
 * 工具类的父类。封装框架的特定实现
 * @author asus
 *
 */
public class CommonUtils {
	/**
	 * 获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		RequestContext rc = null;
		rc = RequestContext.get();
		if(rc == null) {
			return null;
		}
		return rc.getRequest();
	}

	public static HttpServletResponse getResponse() {
		RequestContext rc = null;
		rc = RequestContext.get();
		if(rc == null) {
			return null;
		}
		return rc.getResponse();
	}
}
