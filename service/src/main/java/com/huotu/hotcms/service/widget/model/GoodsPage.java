/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 *  (c) Copyright Hangzhou Hot Technology Co., Ltd.
 *  Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District 2013-2015. All rights reserved.
 */

package com.huotu.hotcms.service.widget.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * Created by cwb on 2016/4/7.
 */
@Getter
@Setter
public class GoodsPage {

    List<GoodsModel> goodses;

    private int pageNo;

    private int totalPages;

    private long totalRecords;

}
