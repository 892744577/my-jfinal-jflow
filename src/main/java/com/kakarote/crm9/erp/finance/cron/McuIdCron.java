package com.kakarote.crm9.erp.finance.cron;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.finance.service.ChargeService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class McuIdCron implements Runnable {

    @Override
    public void run() {
        try {
            ChargeService chargeService = Aop.get(ChargeService.class);
            boolean result7 = chargeService.callT100ChargeApi7();
            if (result7) {
                log.info("调用T100费用接口并保存更新数据成功，传参type=7!");
            }else {
                log.info("调用T100费用接口并保存更新数据出错，传参type=7!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
