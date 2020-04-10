package BP.Difference;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * 获取request, response并存储，用于工具类
 * @author brycehan
 *
 */
public class RequestInterceptor implements Interceptor{

	@Override
	public void intercept(Invocation inv) {
		
		Controller controller = inv.getController();
		if(controller != null) {
			List<UploadFile> uploadFiles = new ArrayList<UploadFile>();
			if(ServletFileUpload.isMultipartContent(controller.getRequest())) {
				uploadFiles = controller.getFiles();
			}
			HttpServletRequest request = controller.getRequest();
			HttpServletResponse response = controller.getResponse();
			request.setAttribute("uploadFiles", uploadFiles);
			RequestContext.set(request, response);
		}
		inv.invoke();		
	}
}
