package BP.workFlowWeb.config;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class RestFulInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		inv.invoke();
		//处理jfinal的返回结果
		Controller controller = inv.getController();
		if(controller != null) {
			Object result = inv.getReturnValue();
			if(result != null) {
				controller.renderJson(result);
			}else {
				controller.renderJson();
			}
		}
	}
}
