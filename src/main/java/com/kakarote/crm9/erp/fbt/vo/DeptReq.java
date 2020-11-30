package com.kakarote.crm9.erp.fbt.vo;

import lombok.Data;

@Data
public class DeptReq {
    private String employee_id; //操作人id,调用接口人 id
    private String employee_type; //类型，0为分贝用户，1为第三方用户
    private String data; //请求数据
    private String accessToken; //accessToken
}
