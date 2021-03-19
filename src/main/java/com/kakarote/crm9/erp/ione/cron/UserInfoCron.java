package com.kakarote.crm9.erp.ione.cron;

import com.jfinal.aop.Aop;
import com.kakarote.crm9.erp.ione.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserInfoCron implements Runnable {
    @Override
    public void run() {
        try {
            UserInfoService userInfoService = Aop.get(UserInfoService.class);
            boolean result = userInfoService.syncUserInfo("2018-01-01");
            if (result) {
                log.info("调用T100费用接口并保存更新绑定用户数据成功!");
            }else {
                log.info("调用T100费用接口并保存更新绑定用户数据出错!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
