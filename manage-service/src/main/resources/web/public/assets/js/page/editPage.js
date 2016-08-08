
var editFunc = {
    handleWidgetChildrenIds: function (id, data) {
        var t = editFunc.randomNumber();
        switch (id) {
            case 'navbar':
                data.e.find('button.navbar-toggle').attr('data-target', '#navList' + t);
                data.e.find('div.navbar-collapse').attr('id', '#navList' + t);
                break;
        }
    },
    handleWidgetIds: function(id) {
        var data = editFunc.getWidgetId(id);
        editFunc.handleWidgetChildrenIds(id, data);
        data.e.attr("id", data.n);
    },
    getWidgetId: function(id) {
        var data = {};
        var t = editFunc.randomNumber();
        data.e = $('.pageHTML').find('#'+id);
        data.n = id + "-" + t;
        return data;
    },
    randomNumber: function() {
        return +new Date();
    },
    gridSystemGenerator: function() {
        $(".ncrow .preview input").bind("keyup", function () {
            var e = 0;
            var t = "";
            var n = false;
            var r = $(this).val().split(" ", 12);
            $.each(r, function (r, i) {
                if (!n) {
                    if (parseInt(i) <= 0) n = true;
                    e = e + parseInt(i);
                    t += '<div class="col-md-' + i + ' column"></div>'
                }
            });
            if (e == 12 && !n) {
                $(this).parent().next().children().attr('data-layout', r.join(','));
                $(this).parent().next().children().html(t);
                $(this).parent().prev().show()
            } else {
                $(this).parent().prev().hide()
            }
        });
    },
    removeElement: function() {
        $(".pageHTML").on("click", ".remove", function (e) {
            e.preventDefault();
            $(this).parent().remove();
            if (!$(".pageHTML .ncrow").length > 0) {
                editFunc.clearDemo();
            }
        });
    },
    settingElement: function () {
        $('.pageHTML').on("click", ".setting", function () {
            $('.modal-backdrop').fadeIn();
            var ele = $('#configuration');
            ele.show();
            var id = $(this).data('target');
            $('.conf-body').find('.common-conf').each(function () {
                var oId = $(this).data('id') ;
                if (oId == id) {
                    $(this).show();
                }
            });
            ele.stop().animate({
                right: 0
            },500);
            // 创建当前操作组件的数据
            widgetHandle.createStore($(this));
            var ele = $('#'+GlobalID);
            var styleid = ele.data('styleid');
            var widgetidentity = ele.data('widgetidentity');
            $('#'+ widgetidentity).find('img.changeStyle').each(function () {
               if ( $(this).data('styleid') == styleid ) {
                   editFunc.changeImgStyleActive($(this));
               }
            });
        });
    },
    saveConfig: function () {
        $('#confBtn').click(function () {
            widgetHandle.saveFunc(GlobalID);
        });
    },
    closeConfig: function () {
        $('#cancelBtn').click(function () {
            widgetHandle.closeSetting();
        });
    },
    closeFunc: function () {
        var ele = $('#configuration');
        var w = ele.width();
        ele.stop().animate({
            right: -w
        },500, function () {
            $('.common-conf').hide();
            $('#configuration').hide();
            $('.modal-backdrop').fadeOut();
        });
    },
    clearDemo: function() {
        $(".pageHTML").empty();
    },
    removeMenuClasses: function() {
        $(".operate-buttons li").removeClass("active")
    },
    changeImgStyleActive: function (ele) {
        $('img.changeStyle').removeClass('active');
        ele.addClass('active');
    },
    handleJsIds: function (id) {
        editFunc.handleWidgetIds(id);
    },
    closePreloader: function () {
        $('#status').fadeOut();
        $('#preloader').delay(350).fadeOut();
    },
    init: function () {
        editFunc.removeElement();
        editFunc.settingElement();
        editFunc.saveConfig();
        editFunc.closeConfig();
        editFunc.gridSystemGenerator();
    },
    dragFunc: function () {
        $(".pageHTML, .pageHTML .column").sortable({
            connectWith: ".column",
            opacity: .35,
            handle: ".drag"
        });
        $(".draggable-group .ncrow").draggable({
            connectToSortable: ".pageHTML",
            helper: "clone",
            handle: ".drag",
            drag: function (e, t) {
                t.helper.width(400);
            },
            stop: function () {
                $(".pageHTML .column").sortable({
                    opacity: .35,
                    connectWith: ".column"
                });
            }
        });
    }
};
var Page = {
    widgetHTML: [
        '<li>',
        '<div class="box box-element ui-draggable">',
        '<span class="setting label label-primary">',
        '<i class="fa fa-cog"></i> 设置',
        '</span>',
        '<span class="drag label label-default">',
        '<i class="fa fa-arrows"></i> 拖动',
        '</span>',
        '<span class="remove label label-danger">',
        '<i class="fa fa-times"></i> 删除',
        '</span>',
        '<div class="preview">',
        '<p></p>',
        '</div>',
        '<div class="view"></div>',
        '</div>',
        '</li>'
    ],
    styleList: [
	    '<div>',
	    '<h3><i class="fa fa-puzzle-piece"></i><strong>设置组件参数</strong></h3>',
	    '<div class="swiper-container styles">',
	    '<div class="swiper-wrapper">',
	    '</div>',
	    '<div class="swiper-button-next"></div>',
	    '<div class="swiper-button-prev"></div>',
	    '</div>',
	    '</div>'
	],
    createListAndEditor: function (data) {
        var parent = $('#configuration').find('.conf-body');
        $.each(data, function (i, v) {
            var initData = {};
            initData.script = v.scriptHref;
            initData.properties = v.defaultProperties;
            wsCache.set(v.identity, initData);
            // 组件列表渲染
            var element = $('#widgetLists');
            element.append(Page.widgetHTML.join('\n'));
            element.find('.setting').eq(i).attr('data-target', v.identity);
            element.find('.preview p').eq(i).html(v.locallyName);
            element.find('.view').eq(i).append(v.styles[0].previewHTML);
            element.find('.view').eq(i).children().eq(0).attr('data-widgetidentity', v.identity);
            element.find('.view').eq(i).children().eq(0).attr('data-styleid', 0);
            //编辑器视图渲染
            var child = $('<div class="common-conf"></div>');
            var container = $('<div></div>');
            var h3 = $('<h3><i class="fa fa-puzzle-piece"></i><strong>设置组件参数</strong></h3>');
            child.attr('data-id', v['identity']);
            child.append(Page.styleList.join('\n'));
            $.each(v.styles, function (key, val) {
                var div = $('<div class="swiper-slide"></div>');
                var img = $('<img class="center-block changeStyle">');
                var p = $('<p></p>');
                img.attr('src',val.thumbnail);
                img.attr('data-styleid',val.id);
                p.text(val.locallyName);
                div.append(img);
                div.append(p);
                child.find('.swiper-wrapper').append(div);
            });
            container.append(h3);
            container.append(v['editorHTML']);
            child.append(container);
            parent.append(child);

            $('.swiper-container.styles').swiper({
                nextButton: '.swiper-button-next',
                prevButton: '.swiper-button-prev',
                slidesPerView: 4,
                paginationClickable: true,
                observer: true,
                observeParents: true,
                updateOnImagesReady : true
            });

            $(".color-picker").bigColorpicker();
        });
        Page.draggable();
    },
    init: function (url) {
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json',
            success: function (result) {
                if(result) editFunc.closePreloader();
                Page.createListAndEditor(result);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus);
            }
        });
    },
    draggable: function () {
        $(".draggable-group .box").draggable({
            connectToSortable: ".column",
            helper: "clone",
            handle: ".drag",
            create: function(e, ui ) {
                var ele = $(e.target).find('.view').children().eq(0);
                var oId = ele.attr('id');
                if ( !oId ) {
                    console.log('部分组件缺少唯一ID，可能影响操作。');
                    ele.attr('id', Page.randomId(6))
                }
            },
            drag: function (e, t) {
                t.helper.width(400);
            },
            stop: function (e, t) {
                var oId = t.helper.find('.view').children().eq(0).attr('id');
                editFunc.handleJsIds(oId);
            }
        });
    },
    randomId: function (num) {
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        var s = '';
        for(var i = 0; i < num; i++){
            var rand = Math.floor(Math.random() * str.length);
            s += str.charAt(rand);
        }
        return s;
    }
};

var editPage = {};

editPage.init = function () {
    $(document.body).css("min-height", $(window).height() - 90);
    $(".pageHTML").css("min-height", $(window).height() - 160);

    editFunc.dragFunc();

    $("#editBtn").click(function () {
        $(document.body).removeClass("sourcepreview");
        $(document.body).addClass("edit");
        editFunc.removeMenuClasses();
        $(this).addClass("active");
        $('.edit').find('.sidebar').show();
        return false;
    });
    $("#previewBtn").click(function () {
        if ( $('#pageHTML').html() === '' ) return false;
        $(document.body).removeClass("edit");
        $(document.body).addClass("sourcepreview");
        editFunc.removeMenuClasses();
        $(this).addClass("active");
        $('.sourcepreview').find('.sidebar').hide();
        return false;
    });
    $("#clearBtn").click(function (e) {
        if ( $('#pageHTML').html() === '' ) return false;
        e.preventDefault();
        layer.confirm('页面会被清空？', {
            title: '警告',
            btn: ['重做','取消']
        }, function(index){
            editFunc.clearDemo();
            layer.close(index);
        });
    });

    $('.conf-body').on('click','img.changeStyle', function () {
        editFunc.changeImgStyleActive($(this));
        var id = $(this).data('styleid');
        $('#'+GlobalID).attr('data-styleid',id);
    });
    editFunc.init();
    Page.init(initPath);
};

editPage.init();



