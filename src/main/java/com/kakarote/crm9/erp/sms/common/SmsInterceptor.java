package com.kakarote.crm9.erp.sms.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class SmsInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
