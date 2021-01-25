package com.kakarote.crm9.erp.wxcms.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.NotAction;
import com.jfinal.upload.UploadFile;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.common.constant.BaseConstant;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsCompetitive;
import com.kakarote.crm9.erp.wxcms.entity.WxcmsCompetitiveNews;
import com.kakarote.crm9.erp.wxcms.service.WxcmsCompetitiveNewsService;
import com.kakarote.crm9.erp.wxcms.service.WxcmsCompetitiveService;
import com.kakarote.crm9.utils.FileUploadUtil;
import com.kakarote.crm9.utils.R;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 粉丝管理
 */
@Slf4j
public class WxCmsCompetitiveController extends Controller {

    @Inject
    private WxcmsCompetitiveService wxcmsCompetitiveService;
    @Inject
    private WxcmsCompetitiveNewsService wxcmsCompetitiveNewsService;
    /**
     * 查询竞品
     */
    public void getCompetitive(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",wxcmsCompetitiveService.queryPageList(basePageRequest)));
    }
    /**
     * 保存竞品
     */
    public void saveCompetitive() {
        WxcmsCompetitive wxcmsCompetitive = getModel(WxcmsCompetitive.class,"",true);
        String photos = "";
        if(getFiles().size()>0){
            photos = upload(getFiles()).stream().map(item->item.get(FileUploadUtil.ACCESS_PATH)).collect(Collectors.joining(";"));
        }
        wxcmsCompetitive.setPic(photos);
        renderJson(R.ok().put("data",wxcmsCompetitiveService.save(wxcmsCompetitive)));
    }
    /**
     * 查询竞品信息
     */
    public void getCompetitiveNews(BasePageRequest basePageRequest) {
        renderJson(R.ok().put("data",wxcmsCompetitiveNewsService.queryPageList(basePageRequest)));
    }
    /**
     * 竞品单上传文件
     * @param list
     * @return
     */
    @NotAction
    public List<Map<String, String>> upload(List<UploadFile> list ) {
        log.info("开始执行文件上传方法!");
        return FileUploadUtil.upload(list, BaseConstant.UPLOAD_PATH_GDCX,"");

    }
    /**
     * 保存竞品信息
     */
    public void saveCompetitiveNews(BasePageRequest basePageRequest) {
        WxcmsCompetitiveNews wxcmsCompetitiveNews = getModel(WxcmsCompetitiveNews.class,"",true);
        renderJson(R.ok().put("data",wxcmsCompetitiveNewsService.save(wxcmsCompetitiveNews)));
    }
}
