package com.kakarote.crm9.erp.yzj.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class YzjInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
