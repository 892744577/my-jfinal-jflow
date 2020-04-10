package BP.Difference.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.jfinal.upload.UploadFile;

import BP.Difference.RequestContext;
public class CommonFileUtils {
	
	/**
	 * 上传
	 * @param request
	 */
	public static void upload(HttpServletRequest request,String fileName,File targetFile) throws Exception{
		if(ServletFileUpload.isMultipartContent(request)) {
			UploadFile uploadFile = RequestContext.get().getUploadFile(fileName);
			if(uploadFile != null) {
				FileUtils.copyFile(uploadFile.getFile(), targetFile);
			}
		}
	}
	
	/**
	 * 获取原始的文件名
	 * @param request
	 * @return
	 */
	public static String getOriginalFilename(HttpServletRequest request,String fileName){
		if(ServletFileUpload.isMultipartContent(request)) {
			UploadFile uploadFile = RequestContext.get().getUploadFile(fileName);
			if(uploadFile != null) {
				return uploadFile.getOriginalFileName();
			}
		}
		return null;
	}
	
	/**
	 * 获取上传的文件
	 * @param request
	 * @return
	 */
	public static long getFilesSize(HttpServletRequest request,String fileName){
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			UploadFile uploadFile = RequestContext.get().getUploadFile(fileName);
			if(uploadFile != null && uploadFile.getFile() != null) {
				return uploadFile.getFile().length();
			}
			
		}
		return 0;
	}
	
	/**
	 * 获取文件对于的输入流
	 * @param request
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public static InputStream getInputStream(HttpServletRequest request,String fileName) throws IOException{
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			UploadFile uploadFile = RequestContext.get().getUploadFile(fileName);
			if(uploadFile != null && uploadFile.getFile() != null) {
				return new FileInputStream(uploadFile.getFile());
			}
		}
		return null;
	}
}
