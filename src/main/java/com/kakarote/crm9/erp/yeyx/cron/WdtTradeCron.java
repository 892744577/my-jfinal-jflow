package com.kakarote.crm9.erp.yeyx.cron;

import com.jfinal.aop.Aop;
import com.jfinal.plugin.activerecord.Db;
import com.kakarote.crm9.erp.wx.util.DateUtil;
import com.kakarote.crm9.erp.yeyx.entity.HrGongdanWdtTrade;
import com.kakarote.crm9.erp.yeyx.service.WdtService;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @Description //旺店通同步销售订单定时任务
 * @Author wangkaida
 * @Date 16:32 2021/1/18
 **/
@Slf4j
public class WdtTradeCron implements Runnable {
    @Override
    public void run() {
        HrGongdanWdtTrade wdtTradeInfoDb = HrGongdanWdtTrade.dao.findFirst(Db.getSql("admin.hrGongdanWdtTrade.getLatestWdtTradeInfo"));
        if (wdtTradeInfoDb != null) {
            Date startDate = wdtTradeInfoDb.getModified();
            Calendar cStart = Calendar.getInstance();
            cStart.setTime(startDate);
            Calendar cModified = Calendar.getInstance();
            cModified.setTime(startDate);
            cModified.set(Calendar.HOUR_OF_DAY, cModified.get(Calendar.HOUR_OF_DAY) + 1);
            Date modifiedDate = cModified.getTime();
            Calendar cEnd = Calendar.getInstance();
            cEnd.setTime(new Date());

            int hourStart = cStart.get(Calendar.HOUR_OF_DAY);
            int hourEnd = cEnd.get(Calendar.HOUR_OF_DAY);
            int mStart = cStart.get(Calendar.MINUTE);
            int mEnd = cEnd.get(Calendar.MINUTE);
            int hourAdd = (mStart+mEnd) / 60;
            try {
                int dayDiff = DateUtil.dayDiff(DateUtil.changeDateTOStr(cStart.getTime()), DateUtil.changeDateTOStr(cEnd.getTime()))-1;
                if (dayDiff < 0) {
                    dayDiff = 0;
                }
                int totalHour = hourStart + dayDiff * 24 + hourEnd + hourAdd;

                WdtService wdtService = Aop.get(WdtService.class);
                Map map = new HashMap();
                for (int i = 0; i < totalHour; i++) {
                    map.put("start_time", DateUtil.changeDateTOStr(startDate));
                    map.put("end_time", DateUtil.changeDateTOStr(modifiedDate));
                    log.info(DateUtil.changeDateTOStr(startDate));
                    log.info(DateUtil.changeDateTOStr(modifiedDate));

                    if(modifiedDate.getTime() > new Date().getTime()){
                        //按5分钟执行
                        long minutes = DateUtil.pastMinutes(startDate);
                        long mCount = minutes / 5;
                        cStart.setTime(startDate);
                        cStart.set(Calendar.MINUTE, cStart.get(Calendar.MINUTE) + 5);
                        modifiedDate = cStart.getTime();
                        for(int j = 0; j < mCount; j++){
                            map.put("start_time", DateUtil.changeDateTOStr(startDate));
                            map.put("end_time", DateUtil.changeDateTOStr(modifiedDate));
                            log.info(DateUtil.changeDateTOStr(startDate));
                            log.info(DateUtil.changeDateTOStr(modifiedDate));
                            if(modifiedDate.getTime() > new Date().getTime()){
                                //不足5分钟不执行
                                break;
                            }
                            sendWdtRequest(wdtService,map);
                            //计算下一次的执行时间差
                            cStart.setTime(modifiedDate);
                            cStart.set(Calendar.MINUTE, cStart.get(Calendar.MINUTE) + 5);
                            startDate = modifiedDate;
                            modifiedDate = cStart.getTime();
                        }
                        break;
                    }

                    sendWdtRequest(wdtService,map);
                    //计算下一次的执行时间差
                    cStart.setTime(modifiedDate);
                    cStart.set(Calendar.HOUR_OF_DAY, cStart.get(Calendar.HOUR_OF_DAY) + 1);
                    startDate = modifiedDate;
                    modifiedDate = cStart.getTime();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendWdtRequest(WdtService wdtService, Map map) throws Exception {
        String result = wdtService.gatewayRequest("http://hu3cgwt0tc.api.taobao.com/router/qm",wdtService.getQmJsonData(map));
        boolean saveResult = wdtService.saveTradeInfo(result);
        if (saveResult == true) {
            log.info("遍历保存销售订单信息到数据库成功!"+map.get("start_time")+"到"+map.get("end_time"));
        }else {
            log.info("遍历保存销售订单信息到数据库失败!"+map.get("start_time")+"到"+map.get("end_time"));
        }
    }
}