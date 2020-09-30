package com.kakarote.crm9.erp.yeyx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.yeyx.service.WanService;

/**
 * 万师傅接口对接类
 */
public class WanController extends Controller {
    @Inject
    private WanService wanService;
}
