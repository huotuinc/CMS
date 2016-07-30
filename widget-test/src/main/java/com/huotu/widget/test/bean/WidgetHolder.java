/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.widget.test.bean;

import com.huotu.hotcms.service.entity.support.WidgetIdentifier;
import com.huotu.hotcms.widget.InstalledWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetLocateService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
@Component
public class WidgetHolder implements WidgetLocateService {

    private final Set<Widget> widgetSet;


    public WidgetHolder() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        ClassPathResource resource = new ClassPathResource("META-INF/widget.properties");
        try (InputStream inputStream = resource.getInputStream()) {
            properties.load(inputStream);
        }

        String classes = properties.getProperty("widgetClasses");
        assertThat(classes)
                .isNotNull();

        HashSet<Widget> widgetArrayList = new HashSet<>();
        for (String clazz : classes.split(",")) {
            Widget widget = (Widget) Class.forName(clazz.trim()).newInstance();
            widgetArrayList.add(widget);
        }

        widgetSet = Collections.unmodifiableSet(widgetArrayList);
//        widget = (Widget) Class.forName(properties.getProperty("widgetClasses")).newInstance();
    }

    public Set<Widget> getWidgetSet() {
        return widgetSet;
    }

    @Override
    public InstalledWidget findWidget(String groupId, String widgetId, String version) {
//        return null;
        return findWidget(new WidgetIdentifier(groupId, widgetId, version).toString());
    }

    @Override
    public InstalledWidget findWidget(String identifier) {
        for (Widget widget : widgetSet) {
            if (Widget.WidgetIdentity(widget).equals(identifier)) {
                InstalledWidget widget1 = new InstalledWidget(widget);
                widget1.setType("test");
                widget1.setIdentifier(WidgetIdentifier.valueOf(Widget.WidgetIdentity(widget)));
                return widget1;
            }
        }
        return null;
    }
}
