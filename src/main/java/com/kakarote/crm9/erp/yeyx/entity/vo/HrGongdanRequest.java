package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class HrGongdanRequest {
    private int oid;
    private int orderId;
    private String serviceSp;

}
