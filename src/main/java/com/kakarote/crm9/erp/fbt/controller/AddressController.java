package com.kakarote.crm9.erp.fbt.controller;

import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.fbt.vo.CheckDataAddress;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddressController extends Controller {

    public void add(){
        CheckDataAddress checkDataAddress = getModel(CheckDataAddress.class,"",true);
        checkDataAddress = checkDataAddress.dao().findById(checkDataAddress.getMobile());
        if(checkDataAddress.getMobile()!=null){
            checkDataAddress.update();
            renderJson(R.ok().put("msg", "更新常驻地成功！").put("code", "000000"));
        }else{
            checkDataAddress.save();
            renderJson(R.ok().put("msg", "保存常驻地成功!").put("code", "000000"));
        }
    }

    public void query(){
        renderJson(R.ok().put("msg",CheckDataAddress.dao.findAll()).put("code", "000000"));
    }
}
