package com.kakarote.crm9.erp.ione.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class IoneInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
