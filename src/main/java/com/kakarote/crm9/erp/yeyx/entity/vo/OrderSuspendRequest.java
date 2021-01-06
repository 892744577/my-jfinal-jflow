package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderSuspendRequest {
    private String orderId;
    private String thirdOrderId;
    private Long optTime; //操作时间，格式为 Unix 时间戳
    private String suspendReason; //挂起原因
    private Long nextContactTime; //下次联系时间
}
