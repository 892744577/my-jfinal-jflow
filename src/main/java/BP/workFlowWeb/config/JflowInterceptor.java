package BP.workFlowWeb.config;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class JflowInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation invocation) {
        invocation.invoke();
    }
}
