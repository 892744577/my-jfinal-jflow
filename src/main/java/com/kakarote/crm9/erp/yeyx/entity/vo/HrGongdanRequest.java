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
    private String serviceSp; //审批状态
    private String shippingOrderNo; //出货单号
    private String fuselageCode; //机身码
    private String serviceSpRemark; //审批备注


}
