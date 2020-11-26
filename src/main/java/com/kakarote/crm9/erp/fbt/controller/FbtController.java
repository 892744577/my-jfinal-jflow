package com.kakarote.crm9.erp.fbt.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.kakarote.crm9.erp.fbt.service.FbtService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FbtController extends Controller {

    @Inject
    private FbtService fbtService;

}
