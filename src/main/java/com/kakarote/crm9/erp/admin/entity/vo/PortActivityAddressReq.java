package com.kakarote.crm9.erp.admin.entity.vo;

import lombok.Data;

@Data
public class PortActivityAddressReq {
    private Long id;
    private String addressData;
    private String dealer;
    private String name;
    private String address;
    private String phone;
    private String wxOpenId;
    private float longitude;
    private float latitude;
}
