/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.common.RouteType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.model.thymeleaf.CategoryForeachParam;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.web.service.SiteResolveService;
import com.huotu.hotcms.web.thymeleaf.expression.DialectAttributeFactory;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.spring4.expression.SPELVariableExpressionEvaluator;
import org.thymeleaf.standard.expression.StandardExpressionExecutionContext;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwb on 2016/1/6.
 */
public class CategoryForeachProcessorFactory {

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    private static CategoryForeachProcessorFactory instance;

    private CategoryForeachProcessorFactory(){
    }

    public static CategoryForeachProcessorFactory getInstance() {
        if(instance == null) {
            instance = new CategoryForeachProcessorFactory();
        }
        return instance;
    }

    public Object process(IProcessableElementTag elementTag, ITemplateContext context) {
        List<Category> categoryList = new ArrayList<>();
        try {
            WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
            CategoryForeachParam categoryForeachParam = DialectAttributeFactory.getInstance().getForeachParam(elementTag, CategoryForeachParam.class);
            CategoryService categoryService = (CategoryService)applicationContext.getBean("categoryServiceImpl");
            Route route = (Route)VariableExpression.getVariable(context, "route");
            if(!StringUtils.isEmpty(categoryForeachParam.getSpecifyids())) {
                categoryList = categoryService.getCategoryList(categoryForeachParam);
            }else if(route==null){
                if (StringUtils.isEmpty(categoryForeachParam.getSiteid())) {
                    Site site = (Site)VariableExpression.getVariable(context, "site");
                    categoryForeachParam.setSiteid(site.getSiteId());
                }
                categoryList = categoryService.getCategoryList(categoryForeachParam);
            }else {
                Category current = categoryService.getCategoryByRoute(route);
                Category parent = current.getParent();
                categoryList = categoryService.getCategoryList(parent);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return categoryList;
    }
}
