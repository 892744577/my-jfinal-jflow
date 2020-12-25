package com.landray.kmss.km.review.client;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * WebService客户端
 * 
 */
public class WebServiceClient {

	/**
	 * 主方法
	 * 
	 * @throws Exception
	 */
	/*public static void main(String[] args) throws Exception {
		WebServiceConfig cfg = WebServiceConfig.getInstance();

		IKmReviewWebserviceService service = (IKmReviewWebserviceService) callService(
				cfg.getAddress(), cfg.getServiceClass());
		KmReviewParamterForm form = new KmReviewParamterForm();
		form.setDocSubject("唐满荣出差测试申请单1");
		form.setFdTemplateId("174becfd4a77a8fcff1452f44a7bbb5a");
		JSONObject docCreator = new JSONObject();
		docCreator.put("PersonNo", "ZN1234");
		form.setDocCreator(docCreator.toString());

		//表单数据
		JSONObject formObject = new JSONObject();
		// 子表
		JSONObject row1 = new JSONObject();
		row1.put("fd_38e05de223c508.fd_3927c70a3e015c", "广州市");
		row1.put("fd_38e05de223c508.fd_3927c70a3e015c_text", "广州市");
		row1.put("fd_38e05de223c508.fd_3927c70a3e015c.fdId", "123123123");
		row1.put("fd_38e05e952dae62.fd_38e05ee8219c40", "2020-12-26 12:00:00");
		row1.put("fd_38e05e952dae62.fd_38e05f44acf5b0", "2020-12-26 15:00:00");

		JSONArray array = new JSONArray();
		array.add(row1);

		formObject.put("fd_38e05e952dae62", array);
		formObject.put("fd_391029e7005bbc", "13580573264");
		formObject.put("fd_38e05e795c24ca", "唐满荣");
		form.setFormValues(formObject.toString());

		String addReview = service.addReview(form);
		System.out.println(addReview);
		// 请在此处添加业务代码

	}*/

	/**
	 * 调用服务，生成客户端的服务代理
	 * 
	 * @param address
	 *            WebService的URL
	 * @param serviceClass
	 *            服务接口全名
	 * @return 服务代理对象
	 * @throws Exception
	 */
	public static Object callService(String address, Class serviceClass)
			throws Exception {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

		// 记录入站消息
		factory.getInInterceptors().add(new LoggingInInterceptor());

		// 记录出站消息
		factory.getOutInterceptors().add(new LoggingOutInterceptor());

		// 添加消息头验证信息。如果服务端要求验证用户密码，请加入此段代码
		// factory.getOutInterceptors().add(new AddSoapHeader());

		factory.setServiceClass(serviceClass);
		factory.setAddress(address);

		// 使用MTOM编码处理消息。如果需要在消息中传输文档附件等二进制内容，请加入此段代码
		// Map<String, Object> props = new HashMap<String, Object>();
		// props.put("mtom-enabled", Boolean.TRUE);
		// factory.setProperties(props);

		// 创建服务代理并返回
		return factory.create();
	}

}
