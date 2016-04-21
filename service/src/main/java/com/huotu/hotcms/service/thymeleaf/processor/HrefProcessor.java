/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.thymeleaf.processor;

import com.huotu.hotcms.service.thymeleaf.service.HrefProcessorService;
import com.huotu.hotcms.service.util.StringUtil;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;
import org.thymeleaf.standard.expression.Assignation;
import org.thymeleaf.standard.expression.AssignationSequence;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.LinkExpression;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.util.List;

/**
 * <p>
 * 自定义thymeleaf 语法标签解析
 * </P>
 *
 * @author xhl
 * @since 1.0.0
 */
public class HrefProcessor  extends AbstractStandardExpressionAttributeTagProcessor {
    public static final int PRECEDENCE = 1000;
    public static final String ATTR_NAME = "href";

    private final HrefProcessorService hrefProcessorService;
    private final String dialectPrefix;

    public HrefProcessor(final IProcessorDialect dialect, final String dialectPrefix, HrefProcessorService hrefProcessorService) {
        super(dialect, TemplateMode.HTML, dialectPrefix, ATTR_NAME, PRECEDENCE, true);
        this.hrefProcessorService = hrefProcessorService;
//        this.hrefProcessorService.setDialectPrefix(dialectPrefix);
        this.dialectPrefix = dialectPrefix;
    }

    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag tag,
                             AttributeName attributeName,
                             String attributeValue,
                             String attributeTemplateName,
                             int attributeLine,
                             int attributeCol,
                             Object expressionResult,
                             IElementTagStructureHandler structureHandler) {
        String newAttributeValue=null;

        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());

        if (attributeValue != null) {
            final IStandardExpression expression = expressionParser.parseExpression(context, attributeValue);
            AssignationSequence assignations=((LinkExpression) expression).getParameters();
            List<Assignation> list= assignations.getAssignations();
            String linkExpression=((LinkExpression) expression).getBase().toString();//获得链接Template
            linkExpression= StringUtil.Trim(linkExpression,"'");
            newAttributeValue = this.hrefProcessorService.resolveLinkData(dialectPrefix, list, linkExpression, context);
        }
        newAttributeValue=HtmlEscape.escapeHtml4Xml(newAttributeValue == null ? "" : newAttributeValue.toString());;


        // Let RequestDataValueProcessor modify the attribute value if needed
        newAttributeValue = RequestDataValueProcessorUtils.processUrl(context, newAttributeValue);

        // Set the real, non prefixed attribute
        tag.getAttributes().replaceAttribute(attributeName, ATTR_NAME, (newAttributeValue == null? "" : newAttributeValue));

    }
}
