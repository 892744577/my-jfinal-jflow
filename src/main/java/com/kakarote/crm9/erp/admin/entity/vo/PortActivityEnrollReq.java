package com.kakarote.crm9.erp.admin.entity.vo;

import lombok.Data;

@Data
public class PortActivityEnrollReq {
    private Long id;
    private String wxOpenId;
    private String name;
    private String address;
    private String phone;
    private String en_ac_id;
}
