package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;

@Data
public class HrGongdanAreaRelationRequest {
    private String districts;
    private String fk_system;
    private String fk_emps;
    private String area_type;
    private String priority;
    private String valid_flag;
}
