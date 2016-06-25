/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.page;

import me.jiangcai.bracket.test.BracketPage;
import org.openqa.selenium.WebDriver;

/**
 * @author CJ
 */
public abstract class AbstractManagePage extends BracketPage {

    public AbstractManagePage(WebDriver webDriver) {
        super(webDriver);
    }
}
