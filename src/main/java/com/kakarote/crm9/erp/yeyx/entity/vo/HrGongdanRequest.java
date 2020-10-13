package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HrGongdanRequest {
    private Integer oid;
    private String orderId;
    private String serviceSp;

}
