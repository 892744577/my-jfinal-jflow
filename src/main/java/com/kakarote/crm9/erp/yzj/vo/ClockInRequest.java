package com.kakarote.crm9.erp.yzj.vo;

import lombok.Data;

@Data
public class ClockInRequest {
    private String openId;
    private String positionId;
    private int clockInTime;
}
