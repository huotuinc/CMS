/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller;

import com.huotu.cms.manage.ManageTest;
import com.huotu.cms.manage.page.AdminPage;
import com.huotu.hotcms.service.entity.login.Owner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author CJ
 */
public class LoginTest extends ManageTest {


    /**
     * 以提交form的方式登录
     *
     * @throws Exception
     */
    @Test
    public void normal() throws Exception {
        // 1 是商户可登录
        // 2 是测试管理员可登录

        String password = UUID.randomUUID().toString();
        Owner owner = randomOwner(password);

        MvcResult result = mockMvc.perform(get("/manage/"))
                .andExpect(status().isFound())
                .andReturn();
        session = (MockHttpSession) result.getRequest().getSession(true);

        String loginPageURL = result.getResponse().getRedirectedUrl();

        Document document = Jsoup.parse(mockMvc.perform(get(loginPageURL).session(session))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        Elements forms = document.getElementsByTag("form");
        assertThat(forms)
                .hasSize(1);

        Element form = forms.get(0);

        String action = form.attr("action");
        assertThat(action)
                .isNotEmpty();

        // username password
        mockMvc.perform(post(action).param("username", owner.getLoginName()).param("password", password).session(session))
                .andDo(print());
//        http://localhost/manage
    }

    /**
     * 以管理员身份登录 cookie
     * @throws Exception
     */
    @Test
    public void manager() throws Exception {
        loginAsManage();

        AdminPage page = initPage(AdminPage.class);
    }

    /**
     * 以商户身份登录 cookie
     * @throws Exception
     */
    @Test
    public void customer() throws Exception {
        loginAsOwner(testOwner);
    }

}