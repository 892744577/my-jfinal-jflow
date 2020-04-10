/**
 * 
 */
package BP.Difference;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;

import com.jfinal.upload.UploadFile;

/**
 * 请求上下文
 * @author brycehan
 *
 */
public class RequestContext {

	private static final ThreadLocal<RequestContext> requestContexts = new ThreadLocal<>();
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private List<UploadFile> uploadFiles;
	
	/**
	 * 存储请求上下文
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public static void set(HttpServletRequest request, HttpServletResponse response) {
		RequestContext requestContext = new RequestContext();
		requestContext.setRequest(request);
		requestContext.setResponse(response);
		
		if(ServletFileUpload.isMultipartContent(request)) {
			List<UploadFile> uploadFiles = (List<UploadFile>) request.getAttribute("uploadFiles");
			if(uploadFiles != null) {
				requestContext.setUploadFiles(uploadFiles);
			}else {
				requestContext.setUploadFiles(new ArrayList<UploadFile>());
			}
		}
		requestContexts.set(requestContext);
	}
	
	/**
	 * 获取当前线程的请求上下文
	 * @return
	 */
	public static RequestContext get() {
		return requestContexts.get();
	}
	
	/**
	 * 根据form表单文件name属性获取上传文件
	 * @param parameterName form的file标签的name属性
	 * @return
	 */
	public UploadFile getUploadFile(String parameterName) {
		if(StringUtils.isBlank(parameterName)) {
			return null;
		}
		List<UploadFile> uploadFiles = requestContexts.get().getUploadFiles();
		for (UploadFile uploadFile : uploadFiles) {
			if(parameterName.equals(uploadFile.getParameterName())) {
				return uploadFile;
			}
		}
		return null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public List<UploadFile> getUploadFiles() {
		return uploadFiles;
	}

	public void setUploadFiles(List<UploadFile> uploadFiles) {
		this.uploadFiles = uploadFiles;
	}
}
