package com.kakarote.crm9.erp.jxc.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class JxcInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
