package com.kakarote.crm9.erp.yeyx.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.yeyx.po.*;
import com.kakarote.crm9.erp.yeyx.service.YeyxService;
import com.kakarote.crm9.utils.R;

/**
 * 言而有信第三方订单平台接口
 */
public class YeyxController extends Controller {

    @Inject
    private YeyxService yeyxService;

    /**
     * 改约
     */
    public void duty_time(@Para("") DutyTimeRequest dutyTimeRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }
    /**
     * 派单
     */
    public void master_info(@Para("") MasterInfoRequest masterInfoRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }
    /**
     * 上门
     */
    public void master_visit(@Para("") MasterVisitRequest masterVisitRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

    /**
     * 完成
     */
    public void order_complete(@Para("") OrderCompleteRequest orderCompleteRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

    /**
     * 取消
     */
    public void order_cancel(@Para("") OrderCancelRequest orderCancelRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

    public void factory_remark(@Para("") FactoryRemarkRequest factoryRemarkRequest){
        renderJson(R.ok().put("code",200).put("message","成功"));
    }

}
