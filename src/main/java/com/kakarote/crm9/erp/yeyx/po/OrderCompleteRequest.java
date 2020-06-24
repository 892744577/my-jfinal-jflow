package com.kakarote.crm9.erp.yeyx.po;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderCompleteRequest {
    private String orderId;
    private String thirdOrderId;
    private Long optTime;
    private String extraData;
    private String productPictureUrls;
    private String remark;
}
