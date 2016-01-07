package com.huotu.hotcms.web.service.factory;

import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Route;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.impl.ArticleServiceImpl;
import com.huotu.hotcms.web.service.BaseProcessorService;
import com.huotu.hotcms.web.service.RoutResolverService;
import com.huotu.hotcms.web.thymeleaf.expression.VariableExpression;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import com.huotu.hotcms.web.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.standard.expression.Assignation;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator xhl 2016/1/6.
 */
public class ArticleHrefProcessorFactory extends BaseProcessorService {
    private static final String regexp="\\$\\{([^\\}]+)}";//匹配${key}模式的正则表达式

    private static final Log log = LogFactory.getLog(CategoryForeachProcessorFactory.class);

    @Override
    public String resolveLinkData(List<Assignation> assignations, String LinkExpression, ITemplateContext context) {
        try {
            if(!StringUtils.isEmpty(LinkExpression)) {
                IExpressionObjects expressContent = context.getExpressionObjects();
                HttpServletRequest request = (HttpServletRequest) expressContent.getObject("request");
                WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
                Site site = (Site) VariableExpression.getVariable(context, "site");
                RoutResolverService routResolverService = (RoutResolverService) applicationContext.getBean("routResolverService");
                Route routeRule = routResolverService.getRout(site, PatternMatchUtil.getUrl(request));//获得路由规则
                if (routeRule != null) {
                    Integer articleId = PatternMatchUtil.getUrlId(PatternMatchUtil.getUrl(request), routeRule.getRule());//获得文章Id对象
                    if (articleId != null) {
                        ArticleServiceImpl articleService = (ArticleServiceImpl) applicationContext.getBean("articleServiceImpl");
                        Article article = articleService.findById(Long.valueOf(articleId));
                        for (Assignation assignation : assignations) {
                            String left = "{"+assignation.getLeft().toString()+"}";
                            String right = assignation.getRight().toString();

                            String attributeName = PatternMatchUtil.getMatchVal(right, regexp);
                            attributeName = StringUtil.toUpperCase(attributeName);
                            Object object = article.getClass().getMethod("get" + attributeName).invoke(article);

                            LinkExpression=LinkExpression.replace(left,object.toString());
                        }
                    }
                }
            }
        }
        catch (Exception ex){
           log.equals(ex.getMessage());
        }
        return LinkExpression;
    }
}
