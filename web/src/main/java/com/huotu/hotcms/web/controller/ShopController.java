package com.huotu.hotcms.web.controller;

import com.huotu.hotcms.service.common.PageErrorType;
import com.huotu.hotcms.service.entity.CustomPages;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.service.CustomPagesService;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.hotcms.web.util.web.CookieUser;
import com.huotu.huobanplus.common.entity.Goods;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 商城入口Controller
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/shop")
public class ShopController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @Autowired
    private CustomPagesService customPagesService;

    @Autowired
    private GoodsDetailService goodsDetailService;
    @Autowired
    SiteResolveService siteResolveService;


    @Autowired
    private CookieUser cookieUser;


    @Autowired
    private RequestService requestService;

    /**
     * 商城首页/shop/
     * */
    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        try{
            Site site = siteResolveService.getCurrentSite(request);
            CustomPages customPages=customPagesService.findHomePages(site);
            if(customPages!=null){
                modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),customPages.getId()));
            }else{
                modelAndView.setViewName(PageErrorType.BUDDING_500.getValue());
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }

    /**
     * 商城其他页面-->/shop/{id}/........
     * **/
    @RequestMapping("/{name}")
    public ModelAndView page(HttpServletRequest request, @PathVariable("name") String id){
        ModelAndView modelAndView = new ModelAndView();
        try {
            Site site = siteResolveService.getCurrentSite(request);
            modelAndView.setViewName(String.format("%s_%s.cshtml", site.getSiteId(),id));
        } catch (Exception ex) {
            log.error(ex);
        }
        return modelAndView;
    }

    /**
     * 商品详情页面
     * **/
    @RequestMapping("/product/{id}")
    public ModelAndView defaultsPage(HttpServletRequest request, @PathVariable("id") String id){
        ModelAndView modelAndView = new ModelAndView();
        try{
            Goods goods = goodsDetailService.getGoodsDetail(100);
            goods.setId(Long.valueOf(id));
            modelAndView.addObject("unlogin", cookieUser.checkLogin(request));//未登录为false，登录了则为true
            modelAndView.setViewName("/template/0/goodsDetail.html");
            modelAndView.addObject("goods",goods);

        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }
}
