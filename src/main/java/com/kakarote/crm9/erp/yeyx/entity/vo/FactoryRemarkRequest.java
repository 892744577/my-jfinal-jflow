package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FactoryRemarkRequest {
    private Long optTime; //操作时间，格式为 Unix 时间戳
    private String orderId;
    private String remark;
    private String thirdOrderId;
}
