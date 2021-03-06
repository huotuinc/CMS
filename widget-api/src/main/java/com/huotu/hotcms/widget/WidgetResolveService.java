/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget;

import com.huotu.hotcms.widget.page.PageElement;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 控件解析服务
 */
public interface WidgetResolveService {

    /**
     * 获取资源在这个context下的URI
     *
     * @param widget       控件
     * @param resourceName 资源名称
     * @return 资源uri, 假设项目运行的context为 /foo  资源uri 为 /bar.png  则完整的url为 http://host:port/foo/bar.png
     * @see Widget#publicResources()
     */
    URI resourceURI(Widget widget, String resourceName) throws URISyntaxException, IOException;

    /**
     * 生成预览HTML代码
     * <p>当执行第三方生成代码时发生错误，应该给予一段空HTML代码</p>
     *
     * @param widget     控件
     * @param styleId    控件样式id,可选
     * @param cmsContext 交互空间
     * @param properties 组件属性,可选
     * @return HTML Code
     */
    String previewHTML(Widget widget, String styleId, CMSContext cmsContext, ComponentProperties properties);

    /**
     * 生成编辑器HTML代码
     *
     * @param widget     控件
     * @param cmsContext 交互空间
     * @param properties
     * @return HTML Code
     */
    String editorHTML(Widget widget, CMSContext cmsContext, ComponentProperties properties);

//    /**
//     * 生成一个组件的完整HTML代码
//     * <p>
//     * 页面的生成者应该是通过调用这个方法获取每一个控件的HTML,技术上我们限定生成一个页面绝对不可以超过0.5s,假定一个页面转载了200个控件
//     * 也就是这个方法的响应时间不可以超过0.5/200s 也就是2.5ms</p>
//     *
//     * @param component  组件
//     * @param cmsContext 上下文环境
//     * @return HTML Code
//     */
//    String componentHTML(Component component, CMSContext cmsContext);

    /**
     * 生成一个pageElement的完整HTML代码
     * <p>
     * 页面的生成者应该是通过调用这个方法获取每一个pageElement的HTML,技术上我们限定生成一个页面绝对不可以超过0.5s
     * ,假定一个页面转载了200个控件 也就是这个方法的响应时间不可以超过0.5/200s 也就是2.5ms</p>
     * <p>当执行第三方生成代码时发生错误，应该给予一段空HTML代码</p>
     *
     * @param pageElement 页面节点
     * @param cmsContext  上下文环境
     * @param writer      HTML Code 的输出点
     * @throws IOException 找不到资源
     */
    void pageElementHTML(PageElement pageElement, CMSContext cmsContext, Writer writer) throws IOException;

    /**
     * 生成控件(组件)依赖context{@link Widget#widgetDependencyContent(MediaType)}
     * <p>当执行第三方生成代码时发生错误，啥都不干</p>
     *
     * @param context cms上下文
     * @param widget  控件,element或者widget总要选一个
     * @param type    目标类型 css or js
     * @param element 组件 （可选),在这个情况下 应该采纳控件默认的属性
     * @param out     输出流
     * @throws IOException 找不到资源
     */
    void widgetDependencyContent(CMSContext context, Widget widget, MediaType type, PageElement element
            , OutputStream out) throws IOException;

    /**
     * 生成一个pageElement的完整HTML代码
     * <p>
     * 页面的生成者应该是通过调用这个方法获取每一个pageElement的HTML,技术上我们限定生成一个页面绝对不可以超过0.5s
     * ,假定一个页面转载了200个控件 也就是这个方法的响应时间不可以超过0.5/200s 也就是2.5ms</p>
     *
     * @param pageElement 页面节点
     * @param cmsContext  上下文环境
     * @return HTML Code
     */
    String pageElementHTML(PageElement pageElement, CMSContext cmsContext);

}
