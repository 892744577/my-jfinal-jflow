package com.kakarote.crm9.erp.finance.cron;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.finance.service.ChargeService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ChargeCron implements Runnable {

    @Override
    public void run() {
        try {
            ChargeService chargeService = Aop.get(ChargeService.class);
            boolean result = chargeService.callT100ChargeApi();
            if (result) {
                log.info("调用T100费用接口并保存更新数据成功!");
            }else {
                log.info("调用T100费用接口并保存更新数据出错!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
