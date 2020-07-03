package com.kakarote.crm9.erp.yeyx.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class YeyxInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
