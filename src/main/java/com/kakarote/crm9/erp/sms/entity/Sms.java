package com.kakarote.crm9.erp.sms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sms {
    private String  account;

    private String password;

    private String mobile;

    private int mobile_code;

    private String content;


    //返回
    private String code;

    private String msg;

    private String smsid;

}
