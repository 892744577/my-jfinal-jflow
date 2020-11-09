package com.kakarote.crm9.erp.wxcms.common;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class WxCmsInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv){
        inv.invoke();
    }
}
