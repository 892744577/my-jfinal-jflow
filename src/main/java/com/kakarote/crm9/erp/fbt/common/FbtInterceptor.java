package com.kakarote.crm9.erp.fbt.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class FbtInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
