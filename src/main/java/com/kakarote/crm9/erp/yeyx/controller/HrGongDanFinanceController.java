package com.kakarote.crm9.erp.yeyx.controller;

import cn.hutool.core.util.StrUtil;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.service.HrGongdanFinanceService;
import com.kakarote.crm9.utils.R;

public class HrGongDanFinanceController extends Controller {

    @Inject
    private HrGongdanFinanceService hrGongdanFinanceService;

    public void sumRemaining(@Para("no") String no ) throws Exception {
        if(StrUtil.isEmpty(no)){
            renderJson(R.error("员工账号不能为空!").put("data",null).put("code","000012"));
            return;
        }
        renderJson(R.ok().put("data",hrGongdanFinanceService.getRemaining(no)));

    }
}
