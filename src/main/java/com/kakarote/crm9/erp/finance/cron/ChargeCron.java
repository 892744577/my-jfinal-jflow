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
            boolean result4 = chargeService.callT100ChargeApi4();
            if (result4) {
                log.info("调用T100费用接口并保存更新数据成功，传参type=4!");
            }else {
                log.info("调用T100费用接口并保存更新数据出错，传参type=4!");
            }
            boolean result5 = chargeService.callT100ChargeApi5();
            if (result5) {
                log.info("调用T100费用type=5接口并保存更新数据成功，传参type=5!");
            }else {
                log.info("调用T100费用接口并保存更新数据出错，传参type=5!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
