package com.huotu.hotcms.web.thymeleaf.dialect;

import com.huotu.hotcms.web.service.BaseDialectService;
import com.huotu.hotcms.web.thymeleaf.processor.ForeachProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 *     自定义循环thymeleaf 语法标签基类
 * </p>
 * @since 1.0.0
 *
 * @author xhl
 */
public class ForeachDialect extends AbstractProcessorDialect {
    public static final int PROCESSOR_PRECEDENCE = 800;
    public static  String ATTR_NAME = "foreach";//属性
    private static BaseDialectService baseDialectService;


    public ForeachDialect(String name,String prefix,String attrName,BaseDialectService dialectService){
        super(name,prefix,PROCESSOR_PRECEDENCE);
        this.baseDialectService=dialectService;
        this.ATTR_NAME=attrName;
    }


    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createHotProcessorsSet(this,dialectPrefix);
    }

    public static Set<IProcessor> createHotProcessorsSet(final IProcessorDialect dialect, final String dialectPrefix) {
        Set<IProcessor> processors = new LinkedHashSet<>();
        processors.add(new ForeachProcessor(dialect, TemplateMode.HTML, dialectPrefix,ATTR_NAME,baseDialectService));
        return processors;
    }
}
