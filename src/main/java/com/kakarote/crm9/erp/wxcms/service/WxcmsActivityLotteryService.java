package com.kakarote.crm9.erp.wxcms.service;

import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsActivityLottery;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
public class WxcmsActivityLotteryService {

    public boolean add(WxcmsActivityLottery wxcmsActivityLottery) {
        wxcmsActivityLottery.setCreateTime(new Date());
        return wxcmsActivityLottery.save();
    }

    public boolean update(WxcmsActivityLottery wxcmsActivityLottery) {
        return wxcmsActivityLottery.update();
    }

    public List<WxcmsActivityLottery> query(WxcmsActivityLottery wxcmsActivityLottery) {
        return WxcmsActivityLottery.dao.find(
                Db.getSqlPara("admin.wxcmsActivityLottery.getActivityLotteryByOpenid").getSql(),
                wxcmsActivityLottery.getAcId(),
                wxcmsActivityLottery.getOpenId()
        );
    }

    public Object queryWinner(WxcmsActivityLottery wxcmsActivityLottery) {
        return WxcmsActivityLottery.dao.find(
                Db.getSqlPara("admin.wxcmsActivityLottery.getActivityLotteryByWinner").getSql(),
                wxcmsActivityLottery.getAcId(),
                1
        );
    }
}
