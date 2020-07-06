package com.kakarote.crm9.erp.crm.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.crm.service.CrmBackLogService;

/**
 * @author wyq
 */
public class CrmBackLogController extends Controller {
    @Inject
    CrmBackLogService crmBackLogService;

    /**
     * 待办事项数量统计
     */
    public void num(){
        renderJson(crmBackLogService.num());
    }

    /**
     *今日需联系客户
     */
    public void todayCustomer(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.todayCustomer(basePageRequest));
    }

    /**
     * 标记线索为已跟进
     */
    public void setLeadsFollowup(@Para("ids")String ids){
        renderJson(crmBackLogService.setLeadsFollowup(ids));
    }

    /**
     *分配给我的线索
     */
    public void followLeads(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.followLeads(basePageRequest));
    }

    /**
     * 标记客户为已跟进
     */
    public void setCustomerFollowup(@Para("ids")String ids){
        renderJson(crmBackLogService.setCustomerFollowup(ids));
    }

    /**
     *分配给我的客户
     */
    public void followCustomer(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.followCustomer(basePageRequest));
    }

    /**
     *待审核合同
     */
    public void checkContract(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.checkContract(basePageRequest));
    }

    /**
     *待审核回款
     */
    public void checkReceivables(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.checkReceivables(basePageRequest));
    }

    /**
     *待回款提醒
     */
    public void remindReceivables(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.remindReceivables(basePageRequest));
    }

    /**
     *即将到期的合同
     */
    public void endContract(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.endContract(basePageRequest));
    }

    /**
     * @author wyq
     * 待进入客户池提醒
     */
    public void putInPoolRemind(BasePageRequest basePageRequest) throws Exception {
        renderJson(crmBackLogService.putInPoolRemind(basePageRequest));
    }
}
