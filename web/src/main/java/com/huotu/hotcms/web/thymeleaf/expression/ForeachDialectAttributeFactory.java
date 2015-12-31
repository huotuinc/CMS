package com.huotu.hotcms.web.thymeleaf.expression;

import com.huotu.hotcms.web.common.DialectHtml5AttrEnum;
import com.huotu.hotcms.web.model.ForeachDialectModel;
import org.springframework.stereotype.Component;
import org.thymeleaf.model.IElementAttributes;
import org.thymeleaf.model.IProcessableElementTag;

/**
 * <P>
 *     Thymeleaf 自定义方言循环html5参数解析
 * </P>
 * @since 1.0.0
 *
 * @author xhl
 */
public class ForeachDialectAttributeFactory implements IDialectAttributeFactory {
    private static ForeachDialectAttributeFactory instance=new ForeachDialectAttributeFactory();

    /*
    * 单例出口
    * */
    public static ForeachDialectAttributeFactory getInstance(){
        if(instance==null)
            instance=new ForeachDialectAttributeFactory();
        return  instance;
    }

    @Override
    public String getHtml5Attr(IProcessableElementTag elementTag, String name) {
        if(elementTag!=null)
        {
            return elementTag.getAttributes().getValue(name);
        }
        return null;
    }

    @Override
    public ForeachDialectModel getHtml5Attr(IProcessableElementTag elementTag) {
        ForeachDialectModel model=null;
        if(elementTag!=null)
        {
            model=new ForeachDialectModel();
            IElementAttributes attributes=elementTag.getAttributes();
            if(attributes!=null) {

                String id=attributes.getValue(DialectHtml5AttrEnum.DATA_HOT_ID.getValue().toString());
                String ignoreID=attributes.getValue(DialectHtml5AttrEnum.DATA_HOT_IgnoreID.getValue().toString());
                String size=attributes.getValue(DialectHtml5AttrEnum.DATA_HOT_SIZE.getValue().toString());
                String siteId=attributes.getValue(DialectHtml5AttrEnum.DATA_HOT_SITEID.getValue().toString());
                String dataSources=attributes.getValue(DialectHtml5AttrEnum.DATA_HOT_DATATYPE.getValue().toString());
                model.setIgnoreId(ignoreID);
                if(size!=null&&size!="") {
                    model.setSize(Integer.parseInt(size));
                }
                if(id!=null&&id!="") {
                    model.setId(Long.parseLong(id));
                }
                if(siteId!=null&&siteId!="") {
                    model.setSiteId(Long.parseLong(siteId));
                }
                model.setDataSources(dataSources);
            }
        }
        return model;
    }
}