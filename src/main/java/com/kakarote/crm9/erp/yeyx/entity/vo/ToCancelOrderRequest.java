package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ToCancelOrderRequest {
    private String cancelType;
    private int oid; //当cancelType==2时必填
    private String orderId;
    private String remark;
}
