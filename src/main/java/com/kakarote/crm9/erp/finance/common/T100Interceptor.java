package com.kakarote.crm9.erp.finance.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class T100Interceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
