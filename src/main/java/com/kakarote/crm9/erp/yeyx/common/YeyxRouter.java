package com.kakarote.crm9.erp.yeyx.common;

import com.jfinal.config.Routes;
import com.kakarote.crm9.erp.yeyx.controller.*;

public class YeyxRouter extends Routes {
    @Override
    public void config() {
        addInterceptor(new YeyxInterceptor());
        add("/yeyx", YeyxController.class); //言而有信
        add("/wan", WanController.class); //万师傅
        add("/sab", SabController.class); //锁安帮
        add("/wdt", WdtController.class); //旺店通
        //工单系统
        add("/hr/gongdan", HrGongDanController.class);
        //工程项目管理系统
        add("/projectmanage", ProjectManageController.class);
    }
}
