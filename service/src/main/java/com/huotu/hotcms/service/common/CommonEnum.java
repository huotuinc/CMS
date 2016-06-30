/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.common;

/**
 * Created by cwb on 2015/12/21.
 */
public interface CommonEnum {
    /**
     * @return 编码
     */
    Object getCode();

    /**
     * @return 用于显示的值
     */
    Object getValue();

    default String getDescription() {
        return "";
    }
}
