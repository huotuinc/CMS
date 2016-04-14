package com.huotu.hotcms.web.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import com.huotu.hotcms.service.thymeleaf.service.SiteResolveService;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.hotcms.service.widget.service.RegisterByWeixinService;
import com.huotu.hotcms.web.util.web.CookieUser;
import com.huotu.huobanplus.sdk.mall.model.RegisterWeixinUserData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * PC官网、商城第三方授权绑定操作
 * </p>
 *
 * @since xhl
 */
@Controller
@RequestMapping(value = "/bind")
public class BindController {
    private static final Log log = LogFactory.getLog(ShopController.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private RegisterByWeixinService registerByWeixinService;

    @Autowired
    private GoodsDetailService goodsDetailService;

    @Autowired
    private SiteResolveService siteResolveService;


    @Autowired
    private ConfigInfo configInfo;

    @Autowired
    private CookieUser cookieUser;

    /**
     *
     * 微信二维码注册
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request,HttpServletResponse response, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = null;
            try {
                String code = request.getParameter("code");
                String state = request.getParameter("state");//state为goodsId
                String appid = configInfo.getAppid();
                String secret = configInfo.getAppsecret();
                int customerId = siteResolveService.getCurrentSite(request).getCustomerId();
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code" + "&state=" + state;
                WxUser wxUser = registerByWeixinService.getWxUser(url);//返回用户信息
                RegisterWeixinUserData registerWeixinUserData =registerByWeixinService.RegisterByWeixin(customerId,wxUser);
                cookieUser.setUserID(response, registerWeixinUserData.getUserId().toString());
                modelAndView = new ModelAndView("redirect:/shop/product/"+state);//redirect模式.重定向到商品详情页
            } catch (Exception ex) {
                log.error(ex);
            }
        return modelAndView;
    }


    /**
     *
     * 二维码购买
     */
    @RequestMapping(value = "/qrCode", method = { RequestMethod.POST, RequestMethod.GET })
    public void qrCode(HttpServletResponse resp, String id) throws IOException {
        String url = "要生成二维码的地址"+id;
        if (url != null && !"".equals(url)) {
            ServletOutputStream stream = null;
            try {
                int width = 400;//图片的宽度
                int height = 400;//高度
                stream = resp.getOutputStream();
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix m = writer.encode(url, BarcodeFormat.QR_CODE, height, width);
                MatrixToImageWriter.writeToStream(m, "png", stream);
            } catch (WriterException e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            }
        }
    }


}
