package com.kakarote.crm9.erp.yeyx.po;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FactoryRemarkRequest {
    private String orderId;
    private String remark;
}
