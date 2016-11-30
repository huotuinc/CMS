/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.service.service;

import com.huotu.hotcms.service.TestBase;
import com.huotu.hotcms.service.entity.login.Owner;
import com.huotu.hotcms.service.exception.LoginException;
import com.huotu.hotcms.service.exception.RegisterException;
import com.huotu.huobanplus.common.entity.User;
import com.huotu.huobanplus.sdk.common.repository.UserRestRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class MallServiceTest extends TestBase {

    @Autowired
    private MallService mallService;
    private int testCustomerId = 3447;
    @Autowired
    private UserRestRepository userRestRepository;

    @Test
    public void test() throws IOException, RegisterException, LoginException {
        Owner owner = new Owner();
        owner.setCustomerId(testCustomerId);
        String domain = mallService.getMallDomain(owner);
        System.out.println(domain);

        String username = randomMobile();
        String password = UUID.randomUUID().toString();

        try {
            mallService.mallLogin(owner, username, password, null);
            throw new AssertionError("登录需要失败");
        } catch (LoginException ignored) {

        }

        User user = mallService.mallRegister(owner, username, password, null);
        assertThat(user)
                .isNotNull();
        try {

            mallService.mallLogin(owner, username, password, null);

        } finally {
            userRestRepository.deleteByPK(user.getId());
        }


    }

}