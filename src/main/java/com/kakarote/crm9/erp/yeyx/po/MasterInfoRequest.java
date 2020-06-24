package com.kakarote.crm9.erp.yeyx.po;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MasterInfoRequest {
    private String orderId; //啄木鸟系统订单id
    private String thirdOrderId; //我们的订单id
    private Long optTime; //操作时间，格式为 Unix 时间戳
    private String masterName; //工程师姓名
    private String masterPhone; //工程师电话
}
