/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import org.thymeleaf.context.WebEngineContext;

import java.net.URI;

/**
 * 为控件提供服务，任何widget-api被导入的项目都应该维持实现本接口的实例。
 */
public interface WidgetService {

    /**
     * 获取资源在这个context下的URI
     * @see Widget#publicResources()
     * @param widget 控件
     * @param resourceName 资源名称
     * @return 资源uri, 假设项目运行的context为 /foo  资源uri 为 /bar.png  则完整的url为 http://host:port/foo/bar.png
     */
    URI resourceURI(Widget widget,String resourceName);

    /**
     * 生成预览HTML代码
     *
     * @param widget     控件
     * @param styleId    控件样式id,可选
     * @param cmsContext    交互空间
     * @param properties 组件属性,可选
     * @param context    WebEngineContext
     * @return HTML Code
     */
    String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties,WebEngineContext context);

    /**
     * 生成编辑器HTML代码
     *
     * @param widget  控件
     * @param cmsContext 交互空间
     * @param context  WebEngineContext
     * @return HTML Code
     */
    String editorHTML(Widget widget, CMSContext cmsContext,WebEngineContext context);

    /**
     * 生成一个组件的完整HTML代码
     * <p>
     * 页面的生成者应该是通过调用这个方法获取每一个控件的HTML,技术上我们限定生成一个页面绝对不可以超过0.5s,假定一个页面转载了200个控件
     * 也就是这个方法的响应时间不可以超过0.5/200s 也就是2.5ms</p>
     *
     * @param component 组件
     * @param cmsContext   上下文环境
     * @param context WebEngineContext
     * @return HTML Code
     */
    String componentHTML(Component component, CMSContext cmsContext,WebEngineContext context);
}
