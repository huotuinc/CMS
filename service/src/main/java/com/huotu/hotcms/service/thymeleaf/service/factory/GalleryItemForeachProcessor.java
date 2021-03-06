/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.GalleryItem;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.foreach.GalleryForeachParam;
import com.huotu.hotcms.service.service.GalleryItemService;
import com.huotu.hotcms.service.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.model.IProcessableElementTag;

import javax.servlet.http.HttpServletRequest;

@Component
public class GalleryItemForeachProcessor {

    private static Log log = LogFactory.getLog(GalleryItemForeachProcessor.class);
    @Autowired
    private GalleryItemService galleryItemService;

    @Autowired
    private DialectAttributeFactory dialectAttributeFactory;

    public Object process(IProcessableElementTag elementTag,ITemplateContext context) {
        Page<GalleryItem> galleries = null;
        try {
            HttpServletRequest request = ((IWebContext)context).getRequest();
            String selvertUrl=PatternMatchUtil.getServletUrl(request);
            GalleryForeachParam galleryListForeachParam =dialectAttributeFactory.getForeachParam(elementTag, GalleryForeachParam.class);
//            Route route = (Route) VariableExpression.getVariable(context, "route");
            Route route=(Route)context.getVariable("route");
            galleryListForeachParam=dialectAttributeFactory.getForeachParamByRequest(request,galleryListForeachParam);
            //根据当前请求的Uri来获得指定的ID
            if (galleryListForeachParam.getGalleryId() == null) {
                galleryListForeachParam.setGalleryId(PatternMatchUtil.getUrlIdByLongType(selvertUrl
                        , PatternMatchUtil.urlParamRegexp));
            }
            galleries = galleryItemService.getGalleryItem(galleryListForeachParam);
            //图片路径处理
//            Site site = (Site)VariableExpression.getVariable(context,"site");
            Site site=(Site)context.getVariable("site");
            for (GalleryItem galleryItem : galleries) {
                galleryItem.setThumbUri(site.getResourceUrl() + galleryItem.getThumbUri());
            }
            dialectAttributeFactory.setPageList(galleryListForeachParam,galleries,context);
        }catch (Exception e) {
            log.error("galleryListForeach process-->"+e.getMessage());
        }
        return galleries;
    }

    private int calculateStartPageNo(int currentPage, int pageNumber, int totalPages) {
        if(pageNumber == totalPages) {
            return 1;
        }
        return currentPage - pageNumber + 1 < 1 ? 1 : currentPage - pageNumber + 1;
    }

}
