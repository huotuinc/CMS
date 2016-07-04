/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.web.filter;

import com.huotu.hotcms.service.FilterBehavioral;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.widget.CMSContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.OrderComparator;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 解析过滤器
 * </p>
 *
 * @author xhl
 * @since 1.0.0
 */
public class RouteFilter extends OncePerRequestFilter {
    private static final Log log = LogFactory.getLog(RouteFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Site site = CMSContext.RequestContext().getSite();

            Collection<FilterBehavioral> behavioralCollection = WebApplicationContextUtils.findWebApplicationContext(getServletContext())
                    .getBeansOfType(FilterBehavioral.class).values();

            List<FilterBehavioral> behavioralList = behavioralCollection.stream()
                    .sorted(new OrderComparator()).collect(Collectors.toList());

            for (FilterBehavioral behavioral : behavioralList) {
                if (!behavioral.doSiteFilter(site, request, response)) {
                    return;
                }
            }

        } catch (Exception ex) {
            log.error("on RouteFilter", ex);
            response.sendError(404);
            return;
        }
        filterChain.doFilter(request, response);
    }

}
