/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.service.factory;

import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.util.PatternMatchUtil;
import com.huotu.hotcms.service.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;

/**
 * Created by Administrator xhl 2016/1/6.
 */
@Component
public class SiteCurrentProcessor {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(CategoryForeachProcessor.class);

    public Object resolveDataByAttr(String attributeValue, ITemplateContext context){
//        Site site = (Site) VariableExpression.getVariable(context, "site");
        Site site=(Site)context.getVariable("site");
        try {
            String attributeName= PatternMatchUtil.getMatchVal(attributeValue, regexp);
            attributeName= StringUtil.toUpperCase(attributeName);
            Object object = site.getClass().getMethod("get"+attributeName).invoke(site);
            return object;
        }
        catch (Exception ex){
            log.error(ex.getMessage());
        }
        return null;
    }
}
