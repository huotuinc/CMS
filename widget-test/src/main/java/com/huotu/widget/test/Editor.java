/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Download;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Notice;
import org.openqa.selenium.WebElement;

/**
 * 编辑器,我们通常会内置很多功能
 *
 * @author CJ
 */
public class Editor {

    private final WebElement web;

    Editor(WebElement editor) {
        this.web = editor;
    }

    /**
     * 选择文章
     *
     * @param name    存储该内容{@link com.huotu.hotcms.service.entity.AbstractContent#serial}的属性名
     * @param article 内容
     */
    void chooseArticle(String name, Article article) {

    }

    /**
     * 选择下载
     *
     * @param name     存储该内容{@link com.huotu.hotcms.service.entity.AbstractContent#serial}的属性名
     * @param download 内容
     */
    void chooseDownload(String name, Download download) {

    }

    /**
     * 选择链接
     *
     * @param name 存储该内容{@link com.huotu.hotcms.service.entity.AbstractContent#serial}的属性名
     * @param link 内容
     */
    void chooseLink(String name, Link link) {

    }

    /**
     * 选择公告
     *
     * @param name   存储该内容{@link com.huotu.hotcms.service.entity.AbstractContent#serial}的属性名
     * @param notice 内容
     */
    void chooseNotice(String name, Notice notice) {

    }

    /**
     * 选择数据源
     *
     * @param name     存储该数据源{@link com.huotu.hotcms.service.entity.Category#serial}的属性名
     * @param category 数据源
     */
    void chooseCategory(String name, Category category) {

    }

}
