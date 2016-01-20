package com.huotu.hotcms.web.thymeleaf.processor;

import com.huotu.hotcms.web.service.NextProcessorService;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 * 自定义thymeleaf 方言(下一条输出语法方言)
 * </P>
 *
 * @author xhl
 *
 * @since 1.0.0
 */
public class NextProcessor extends AbstractAttributeTagProcessor {
    public static final int PRECEDENCE = 200;

    public static final String ATTR_NAME = "next";
    private NextProcessorService nextProcessorService;

    public NextProcessor(final IProcessorDialect dialect, final String dialectPrefix) {
        super(dialect, TemplateMode.HTML, dialectPrefix, null, false, ATTR_NAME, true, PRECEDENCE, true);
        this.nextProcessorService = new NextProcessorService();
        this.nextProcessorService.setDialectPrefix(dialectPrefix);
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName, String attributeValue, String attributeTemplateName, int attributeLine, int attributeCol, IElementTagStructureHandler structureHandler){
        final Object iteratedValue;
        iteratedValue = nextProcessorService.resolveDataByAttr(tag, context);
        structureHandler.iterateElement(attributeValue, null, iteratedValue);
    }
}
