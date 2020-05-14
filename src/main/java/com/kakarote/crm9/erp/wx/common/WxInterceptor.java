package com.kakarote.crm9.erp.wx.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class WxInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
