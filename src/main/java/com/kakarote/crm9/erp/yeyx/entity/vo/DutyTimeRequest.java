package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DutyTimeRequest {
    private String orderId;
    private String thirdOrderId;
    private Long optTime;
    private Long dutyTime;
}
