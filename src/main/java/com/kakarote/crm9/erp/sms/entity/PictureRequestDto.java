package com.kakarote.crm9.erp.sms.entity;

import lombok.Data;

@Data
public class PictureRequestDto {
    /**
     * 二维码二进制数据
     */
    private byte [] bt;

    /**
     * 二维码内容
     */
    private String content;

    /**
     * os系统物理输入路径且后缀为png
     */
    private String backPicPath;
    /**
     * os系统物理输出路径且后缀为jpg
     */
    private String outPicPath;

}
