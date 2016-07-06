/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

/**
 * 菜单JS,用于iframe子页面调用
 * 可以将选择的菜单点亮而关闭其他菜单
 * 要求子页面将body的id定义为指定菜单的class
 * Created by CJ on 6/25/16.
 */

$(function () {
    var debug = false;

    function resetTopMenuStatus() {
        var top = window.top;
        if (!top) {
            console.error('需要在iframe中调用');
            return;
        }
        if (!top.$) {
            console.error('所在frame页面并不支持jQuery');
            return;
        }

        var print = function () {
            if (debug)
                console.log.apply(console, arguments);
        };

        var menuUl = top.$('.nav').not('.mb30').not('.nav-justified');
        print('found menu:', menuUl);
        // 先移除
        $('ul', $('.nav-active', menuUl)).css('display', 'none');
        $('.nav-active', menuUl).removeClass('nav-active');
        $('.active', menuUl).removeClass('active');
        print('done to remove');

        var targetClass = document.body.id;
        print('targetClass:', targetClass);

        // 寻找目标
        var target = top.$('.' + targetClass, menuUl);
        print('target:', target);

        if (target.size() == 0) {
            console.error('找不到对应的Class', targetClass);
            return;
        }
        //先确定下它的上级菜单
        var parent = target.closest('.nav-parent');
        print('parent:', parent);

        target.closest('li').addClass('active');
        target.closest('.nav-parent').addClass('nav-active');

        $('ul', target.closest('.nav-parent')).css('display', 'block');
    }

    resetTopMenuStatus();

    // 让delete class 具备确认能力
    $('.delete').click(function () {
        return confirm('确实要删除么?');
    });

    // 让.datatable 变成真的datatable
    // table
    var dataTable = $('table.table')
    if (dataTable.size() > 0) {
        dataTable.dataTable({
            language: {
                url: 'http://resali.huobanplus.com/cdn/bracket/localisation/dataTable_zh_CN.json'
            },
            sPaginationType: "full_numbers"
        });
    }

    // 让autoGrow class 可以自动增长
    var autoGrow = $('.autoGrow');
    if (autoGrow.size() > 0) {
        //先判断长度是为了避免每个页面都需要载入autogrow页面
        autoGrow.autogrow();
    }
    // 让inputTags class 可以变成点击添加
    var inputTags = $('.inputTags');
    if (inputTags.size() > 0)
        inputTags.tagsInput({width: 'auto'});
});