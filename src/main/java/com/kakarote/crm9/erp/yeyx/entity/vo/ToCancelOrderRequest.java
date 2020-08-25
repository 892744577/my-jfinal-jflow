package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ToCancelOrderRequest {
    private String cancelType;//1、取消；2、取消后重新发送
    private int oid;
    private String orderId;
    private String remark;

    //流程信息
    private String fk_flow;
}
