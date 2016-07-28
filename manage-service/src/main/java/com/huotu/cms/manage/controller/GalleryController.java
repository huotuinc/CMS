/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.controller.support.ContentManageController;
import com.huotu.cms.manage.exception.RedirectException;
import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Gallery;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.model.ContentExtra;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by chendeyu on 2016/1/10.
 */
@Controller
@RequestMapping("/manage/gallery")
public class GalleryController extends ContentManageController<Gallery,ContentExtra>{
    private static final Log log = LogFactory.getLog(GalleryController.class);

    @Autowired
    private ResourceService resourceService;


    @Override
    protected ContentType contentType() {
        return ContentType.Gallery;
    }

    @Override
    protected Gallery preparePersistContext(Login login, Site site, Gallery data, ContentExtra extra, RedirectAttributes attributes) throws RedirectException {
        return data;
    }

    @Override
    protected String indexViewName() {
        return "/view/contents/gallery.html";
    }

    @Override
    protected void prepareUpdate(Login login, Gallery entity, Gallery data, ContentExtra extra, RedirectAttributes attributes) throws RedirectException {
        entity.setTitle(data.getTitle());
        entity.setLinkUrl(data.getLinkUrl());
        entity.setDescription(data.getDescription());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setThumbUri(data.getThumbUri());
        String oldThumbUri=extra.getOldResourcesUri();
        if(!StringUtils.isEmpty(oldThumbUri)){
            try {
                resourceService.deleteResource(oldThumbUri);
            } catch (IOException e) {
                log.error("删除资源失败，原因是："+e.getMessage());
            }
        }
    }

    @Override
    protected String openViewName() {
        return "/view/contents/galleryOpen.html";
    }
}
