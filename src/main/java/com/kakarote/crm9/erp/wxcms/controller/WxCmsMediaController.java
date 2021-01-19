package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;
import com.kakarote.crm9.erp.wx.vo.WxMpMaterialReq;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 粉丝管理
 */
@Slf4j
public class WxCmsMediaController extends Controller {

    /**
     * 店面展示
     */
    public void getShopMedia(@Para("") WxMpMaterialReq wxMpMaterialReq){
        Map map = new HashMap<>();
        List list1 = new ArrayList();
        list1.add("侧门头-http://app.aptenon.com/crm/shop/A1.jpg");
        list1.add("局部1-http://app.aptenon.com/crm/shop/A2.jpg");
        list1.add("局部2-http://app.aptenon.com/crm/shop/A3.jpg");
        list1.add("正门头-http://app.aptenon.com/crm/shop/A4.jpg");
        map.put("AL",list1);
        List list2 = new ArrayList();
        list2.add("局部1-http://app.aptenon.com/crm/shop/A5.jpg");
        list2.add("局部2-http://app.aptenon.com/crm/shop/A6.jpg");
        list2.add("局部3-http://app.aptenon.com/crm/shop/A7.jpg");
        list2.add("门头-http://app.aptenon.com/crm/shop/A8.jpg");
        map.put("AU",list2);

        List list3 = new ArrayList();
        list3.add("侧门头-http://app.aptenon.com/crm/shop/B1.jpg");
        list3.add("局部1-http://app.aptenon.com/crm/shop/B2.jpg");
        list3.add("局部2-http://app.aptenon.com/crm/shop/B3.jpg");
        list3.add("正门头-http://app.aptenon.com/crm/shop/B4.jpg");
        map.put("BL",list3);
        List list4 = new ArrayList();
        list4.add("局部1-http://app.aptenon.com/crm/shop/B5.jpg");
        list4.add("局部2-http://app.aptenon.com/crm/shop/B6.jpg");
        list4.add("局部3-http://app.aptenon.com/crm/shop/B7.jpg");
        list4.add("门头-http://app.aptenon.com/crm/shop/B8.jpg");
        map.put("BU",list4);


        List list5 = new ArrayList();
        list5.add("侧门头-http://app.aptenon.com/crm/shop/C1.jpg");
        list5.add("局部1-http://app.aptenon.com/crm/shop/C2.jpg");
        list5.add("局部2-http://app.aptenon.com/crm/shop/C3.jpg");
        list5.add("正门头-http://app.aptenon.com/crm/shop/C4.jpg");
        map.put("CL",list5);

        List list6 = new ArrayList();
        list6.add("侧门头-http://app.aptenon.com/crm/shop/D1.jpg");
        list6.add("局部1-http://app.aptenon.com/crm/shop/D2.jpg");
        list6.add("局部2-http://app.aptenon.com/crm/shop/D3.jpg");
        list6.add("正门头-http://app.aptenon.com/crm/shop/D4.jpg");
        map.put("DL",list6);
        List list7 = new ArrayList();
        list7.add("侧门头-http://app.aptenon.com/crm/shop/D5.jpg");
        list7.add("局部1-http://app.aptenon.com/crm/shop/D6.jpg");
        list7.add("局部2-http://app.aptenon.com/crm/shop/D7.jpg");
        list7.add("正门头-http://app.aptenon.com/crm/shop/D8.jpg");
        map.put("DU",list7);
        renderJson(R.ok().put("data",map));
    }
}
