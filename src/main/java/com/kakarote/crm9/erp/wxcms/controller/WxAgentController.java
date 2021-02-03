package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.service.PortEmpService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

/**
 * 代理商编码管理
 */
@Slf4j
public class WxAgentController extends Controller {
    @Inject
    private PortEmpService portEmpService;

    /**
     * 查询代理商
     */
    public void getAgents(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",portEmpService.getPortEmpByAccountType(basePageRequest)));
    }
}
