/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * Created by CJ on 6/26/16.
 */

$(function () {

    // Page Preloader
    jQuery('#status').fadeOut();
    jQuery('#preloader').delay(350).fadeOut(function () {
        jQuery('body').delay(350).css({'overflow': 'visible'});
    });
});
