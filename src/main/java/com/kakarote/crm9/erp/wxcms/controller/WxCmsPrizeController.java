package com.kakarote.crm9.erp.wxcms.controller;


import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityEnroll;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityLottery;
import com.kakarote.crm9.erp.wxcms.service.WxcmsActivityEnrollService;
import com.kakarote.crm9.erp.wxcms.service.WxcmsActivityLotteryService;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

/**
 * 粉丝管理
 */
@Slf4j
public class WxCmsPrizeController extends Controller {
    @Inject
    private WxcmsActivityLotteryService wxcmsActivityLotteryService;

    @Inject
    private WxcmsActivityEnrollService wxcmsActivityEnrollService;
    /**
     * 记录寄送信息
     */
    public void addEnroll() {
        WxcmsActivityEnroll wxcmsActivityEnroll = getModel(WxcmsActivityEnroll.class,"",true);
        renderJson(R.ok().put("data",wxcmsActivityEnrollService.add(wxcmsActivityEnroll)));
    }
    /**
     * 记录抽奖
     */
    public void addLottery() {
        WxcmsActivityLottery wxcmsActivityLottery = getModel(WxcmsActivityLottery.class,"",true);
        renderJson(R.ok().put("data",wxcmsActivityLotteryService.add(wxcmsActivityLottery)));
    }

    /**
     * 根据openid查询抽奖
     */
    public void queryLottery() {
        WxcmsActivityLottery wxcmsActivityLottery = getModel(WxcmsActivityLottery.class,"",true);
        renderJson(R.ok().put("data",wxcmsActivityLotteryService.query(wxcmsActivityLottery)));
    }

}
