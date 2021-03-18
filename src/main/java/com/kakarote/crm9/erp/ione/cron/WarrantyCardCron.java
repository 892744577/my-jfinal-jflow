package com.kakarote.crm9.erp.ione.cron;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.ione.service.WarrantyCardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WarrantyCardCron implements Runnable {
    @Override
    public void run() {
        try {
            WarrantyCardService warrantyCardService = Aop.get(WarrantyCardService.class);
            boolean result = warrantyCardService.syncWarrantyCard("2018-01-01");
            if (result) {
                log.info("调用T100费用接口并保存更新保修卡数据成功!");
            }else {
                log.info("调用T100费用接口并保存更新保修卡数据出错!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
