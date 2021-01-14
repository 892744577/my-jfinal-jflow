package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HrGongdanWdtTradeRequest {
    private String startTime;
    private String endTime;
}