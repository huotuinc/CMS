var version="1.2.299449995500";
seajs.config({
	alias: {
		"jquery": "js/jquery-1.9.1.min.js",
		"bootstrap": "js/bootstrap.min.js",
		"validate": "libs/validate/jquery.validate.min.js",
		"message": "libs/validate/jquery.validate.addMethod.js",
		"common":"js/page/common.js?v="+version,
		"main":"js/page/main.js?v="+version,
		"home":"js/page/home.js?v="+version,
		"JGrid":"libs/JGrid/jquery.JGrid.js?v="+version,
		"layer":"libs/layer/layer.js?v="+version,
		"updateModel":"js/page/system/updateModel.js?v="+version,
		"addModel":"js/page/system/addModel.js?v="+version,
		"modelList":"js/page/system/modelList.js?v="+version,
		"addRegion":"js/page/system/addRegion.js?v="+version,
		"updateRegion":"js/page/system/updateRegion.js?v="+version,
		"regionList":"js/page/system/regionList.js?v="+version,
		"noticeList":"js/page/contents/noticeList.js?v="+version,
		"addNotice":"js/page/contents/addNotice.js?v="+version,
		"updateNotice":"js/page/contents/updateNotice.js?v="+version,
		"articleList":"js/page/contents/articleList.js?v="+version,
		"addArticle":"js/page/contents/addArticle.js?v="+version,
		"updateArticle":"js/page/contents/updateArticle.js?v="+version,
		"linkList":"js/page/contents/linkList.js?v="+version,
		"addLink":"js/page/contents/addLink.js?v="+version,
		"updateLink":"js/page/contents/updateLink.js?v="+version,
		"siteList":"js/page/web/siteList.js?v="+version,
		"addSite":"js/page/web/addSite.js?v="+version,
		"updateSite":"js/page/web/updateSite.js?v="+version,
		"addCategory":"js/page/section/addCategory.js?v="+version,
		"categoryList":"js/page/section/categoryList.js?t="+version,
		"updateCategory":"js/page/section/updateCategory.js?v="+version,
		"ajaxfileupload":"libs/ajaxfileupload.js?v="+version,
		"jqxcore":"libs/jqwidgets/jqxcore.js?v="+version,
		"jqxdata":"libs/jqwidgets/jqxdata.js?v="+version,
		"jqxbuttons":"libs/jqwidgets/jqxbuttons.js?v="+version,
		"jqxscrollbar":"libs/jqwidgets/jqxscrollbar.js?v="+version,
		"jqxdatatable":"libs/jqwidgets/jqxdatatable.js?v="+version,
		"jqxtreegrid":"libs/jqwidgets/jqxtreegrid.js?v="+version
	},
	preload: ['jquery']
});