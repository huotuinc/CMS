/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hzbc on 2016/7/11.
 */

/**
 * @see com.huotu.hotcms.widget.WidgetStyle
 */
@Setter
@Getter
public class WidgetStyleModel {
    private String id;
    private String locallyName;
    private String thumbnail;
    private String previewHTML;
    private String browseHTML;
    private String previewFailed;
    private String description;
}
