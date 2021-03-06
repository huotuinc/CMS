/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.service.impl;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.entity.Link;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.entity.WidgetInfo;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.service.exception.PageNotFoundException;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.repository.ContentRepository;
import com.huotu.hotcms.service.repository.OwnerRepository;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.Component;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.exception.FormatException;
import com.huotu.hotcms.widget.page.Layout;
import com.huotu.hotcms.widget.page.PageElement;
import com.huotu.hotcms.widget.page.PageLayout;
import com.huotu.hotcms.widget.page.PageModel;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.repository.WidgetInfoRepository;
import com.huotu.hotcms.widget.service.PageService;
import com.huotu.hotcms.widget.service.WidgetFactoryService;
import com.huotu.hotcms.widget.test.TestBase;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class WidgetFactoryServiceTest extends TestBase {

    @Autowired(required = false)
    HttpServletResponse response;
    @Autowired
    private WidgetFactoryService widgetFactoryService;
    @Autowired
    private WidgetInfoRepository widgetInfoRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private SiteService siteService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private PageInfoRepository pageInfoRepository;
    @Autowired
    private PageService pageService;


    @Test
    public void testInstallWidget() throws IOException, FormatException, IllegalAccessException
            , InstantiationException, InterruptedException, PageNotFoundException {
        //*********************************case1 安装控件校验控件是否存在控件列表中******************************
        String randomType = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "1.0-SNAPSHOT", randomType);
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);


        //*********************************case2 检验重新加载后，之前安装的控件有没有被正确加载*********************
        widgetFactoryService.reloadWidgets();
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);

        //*********************************case3 设置主控件包不忽略错误 ，安装新版本控件****************************
        List<InstalledWidget> installedWidgets = widgetFactoryService.widgetList(null);
        InstalledWidget installedWidget = getInstalledWidget(installedWidgets, "1.0-SNAPSHOT");
        assertThat(installedWidget).as("等于null").isNotNull();
        String pagePath = "testPagePath";
        ComponentProperties properties = new ComponentProperties();
        properties.put("maxImgUrl", Arrays.asList(new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"}));
        properties.put("minImgUrl", Arrays.asList(new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"}));
        properties.put("styleTemplate", "html");
        pageInitData(pagePath, installedWidget, properties);


        String randomType2 = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "2.0-SNAPSHOT", randomType2);
        assertWidgetListContainWidgetName("picCarousel", "2.0-SNAPSHOT", randomType2);
        installedWidgets = widgetFactoryService.widgetList(null);
        installedWidget = getInstalledWidget(installedWidgets, "2.0-SNAPSHOT");
        assert installedWidget != null;
        WidgetInfo widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
        widgetFactoryService.primary(widgetInfo, false);
        PageInfo pageInfo = pageInfoRepository.findByPagePath(pagePath);
        Layout[] pageElements = PageLayout.NoNullLayout(pageInfo.getLayout());
        for (PageElement element : pageElements) {
            validPageElements(element, widgetInfo);
        }
        assertThat(1).as("验证成功，没有出现异常，并找到页面中修改的组件").isEqualTo(1);


        //控件的新版本参数肯定会满足旧版本控件

        //**********************************case4 设置主控件包不忽略错误 ，安装新版本控件****************************
//        String pagePath2 = "testPagePath2";
//        ComponentProperties properties2 = new ComponentProperties();
//        properties2.put("styleTemplate", "html");
//        pageInitData(pagePath2, installedWidget, properties2);
//        try {
//            installedWidget = getInstalledWidget(installedWidgets, "2.0-SNAPSHOT");
//            widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
//            widgetFactoryService.primary(widgetInfo, false);
//            assertThat(0).as("参数验证失败，应当出现异常").isEqualTo(1);
//        } catch (IllegalStateException e) {
//            assertThat(0).as("参数验证不忽略错误时抛出异常").isEqualTo(0);
//        }

        //***************************************case5 安装主控件包，忽略错误***************************************
//        installedWidget = getInstalledWidget(installedWidgets, "1.0-SNAPSHOT");
//        assertThat(installedWidget).as("等于null").isNotNull();
//        pagePath = "testPagePath3";
//        ComponentProperties properties3 = new ComponentProperties();
//
//        properties3.put("maxImgUrl",  Arrays.asList(new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"}));
//        properties3.put("minImgUrl",  Arrays.asList(new String[]{"1.jpg", "2.jpg", "3.jpg", "4.jpg"}));
//        properties3.put("styleTemplate", "html");
//        pageInitData(pagePath, installedWidget, properties3);
//        installedWidget = getInstalledWidget(installedWidgets, "2.0-SNAPSHOT");
//        widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
//        widgetFactoryService.primary(widgetInfo, true);
//
//        installedWidget = getInstalledWidget(installedWidgets, "1.0-SNAPSHOT");
//        widgetInfo = widgetInfoRepository.findOne(installedWidget.getIdentifier());
//        pageInfo = pageInfoRepository.findByPagePath(pagePath);
//
//        pageElements = PageLayout.NoNullLayout(pageInfo.getLayout());
//
//        for (PageElement element : pageElements) {
//            validPageElements(element, widgetInfo);
//        }
//        assertThat(1).as("错误被忽略，组件被忽略更新").isEqualTo(1);
//
//        List<WidgetInfo> list = widgetInfoRepository.findByGroupIdAndArtifactIdAndEnabledTrue(
//                "com.huotu.hotcms.widget.picCarousel", "picCarousel");
//        assertThat(list.size()).as("新版本未能满足低版本所以没有删除低版本").isEqualTo(2);

    }

    private InstalledWidget getInstalledWidget(List<InstalledWidget> installedWidgets, String version) {
        for (InstalledWidget installedWidget1 : installedWidgets) {
            if (installedWidget1.getWidget().version().equals(version)) {
                return installedWidget1;
            }
        }
        return null;
    }

    @Test
    public void testDownloadJar() throws IOException {
        String groupId = "com.huotu.widget.friendshipLink";
        String id = "friendshipLink";
        String version = "1.0-SNAPSHOT";
        testDownloadJar(groupId, id, version);
        testDownloadJar("commons-codec", "commons-codec", "1.10");
    }

    private void testDownloadJar(String groupId, String id, String version) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        widgetFactoryService.downloadJar(groupId, id, version, buffer);
        assertThat(buffer.toByteArray())
                .isNotEmpty();
        // 而且是一个zip格式
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(buffer.toByteArray()))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            System.out.println(entry.getName() + entry.getCreationTime());
        }
    }

    @Test
    public void testInstallWidgetInfo() throws IOException, FormatException, IllegalAccessException, InstantiationException {
        //*********************************case1 安装控件校验控件是否存在控件列表中******************************
        String randomType = UUID.randomUUID().toString();
        widgetFactoryService.installWidgetInfo(null, "com.huotu.hotcms.widget.picCarousel", "picCarousel"
                , "1.0-SNAPSHOT", randomType);
        assertWidgetListContainWidgetName("picCarousel", "1.0-SNAPSHOT", randomType);
    }

    public void validPageElements(PageElement pageElement, WidgetInfo widgetInfo) {
        if (pageElement instanceof Layout) {
            for (PageElement element : ((Layout) pageElement).elements())
                validPageElements(element, widgetInfo);
        } else if (pageElement instanceof Component) {
            Component component = (Component) pageElement;
            assertThat(new WidgetIdentifier(widgetInfo.getGroupId(), widgetInfo.getArtifactId()
                    , widgetInfo.getVersion()).toString()).as("控件被更新成新控件包Identifier")
                    .isEqualToIgnoringCase(component.getWidgetIdentity());
        }
    }

    private void assertWidgetListContainWidgetName(String widgetId, String version, String type) throws IOException
            , FormatException, InstantiationException, IllegalAccessException {
        assertThat(widgetFactoryService.widgetList(null))
                .haveAtLeastOne(new Condition<>(installedWidget
                        -> installedWidget.getWidget().widgetId().equals(widgetId), "找不到添加的控件"));
    }

    public void pageInitData(String pagePath, InstalledWidget installedWidget, ComponentProperties properties) {
        Owner owner = ownerRepository.findOne(1L);
        Site site = new Site();
        site.setOwner(owner);
        site.setName(UUID.randomUUID().toString());
        site.setTitle(UUID.randomUUID().toString());
        site.setCreateTime(LocalDateTime.now());
        site.setEnabled(true);
        site.setDescription(UUID.randomUUID().toString());
        String[] domains = new String[]{"localhost"};
        site = siteService.newSite(domains, domains[0], site, Locale.CHINA);

        Category category = new Category();
        category.setParent(null);
        category.setSite(site);
        category.setSerial(UUID.randomUUID().toString());
        category.setContentType(ContentType.Article);
        categoryRepository.saveAndFlush(category);

        Link link = new Link();
        link.setCategory(category);
        contentRepository.saveAndFlush(link);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTitle("test");
        pageInfo.setCategory(category);
        pageInfo.setPagePath(pagePath);
        pageInfo.setPageType(PageType.DataContent);
        pageInfo.setSite(category.getSite());
        Layout layoutElement = new Layout();
        layoutElement.setValue("12");
        Component component = new Component();
        try {
            String styleId = installedWidget.getWidget().styles() != null
                    ? installedWidget.getWidget().styles()[0].id() : null;
            component.setInstalledWidget(installedWidget);
            component.setWidgetIdentity(installedWidget.getIdentifier().toString());
            component.setStyleId(styleId);
            component.setId(UUID.randomUUID().toString());
            component.setProperties(properties);
            layoutElement.setParallelElements(new PageElement[]{component});

            pageInfo.setLayout(new PageLayout(new Layout[]{layoutElement}, randomStyleSheet()));
            pageInfo = pageInfoRepository.saveAndFlush(pageInfo);
            PageModel pageModel = new PageModel();
            pageModel.setTitle("testPicCarousel");
            pageModel.setPageIdentity(pageInfo.getId());
            pageModel.setRoot(new Layout[]{layoutElement});
            CMSContext.PutContext(request, response, site);
            pageService.savePage(pageInfo, pageModel, false);
        } catch (Exception e) {
            throw new IllegalStateException("查找控件列表失败", e);
        }
    }

}
