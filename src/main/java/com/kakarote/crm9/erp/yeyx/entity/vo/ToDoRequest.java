package com.kakarote.crm9.erp.yeyx.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ToDoRequest {
    private String appId;
    private String sign;
    private String version;
    private String timestamp;
    private String funId;
    private String jsonData;
}
