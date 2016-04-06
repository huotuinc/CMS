package com.huotu.hotcms.web.controller;

import com.alibaba.fastjson.JSON;
import com.huotu.hotcms.service.common.ConfigInfo;
import com.huotu.hotcms.service.model.Bind.AccessToken;
import com.huotu.hotcms.service.model.Bind.WxUser;
import com.huotu.hotcms.service.thymeleaf.model.RequestModel;
import com.huotu.hotcms.service.thymeleaf.service.RequestService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
    private ConfigInfo configInfo;


    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request){
        ModelAndView modelAndView=new ModelAndView();
        try{
//            modelAndView.setViewName(PageErrorType.CALLBACK.getValue());
            modelAndView.setViewName("/template/0/product_introduce-s.html");
            RequestModel requestModel=requestService.ConvertRequestModelByError(request);
            modelAndView.addObject("localUrl",requestModel.getRoot());
            String code =request.getParameter("code");
            String state =request.getParameter("state");
            String appid = configInfo.getAppid();
            String secret = configInfo.getAppsecret();
            String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code"+"&state="+state;
            WxUser wxUser =getWxUser(url);
            modelAndView.addObject("wxUser",wxUser);
        }catch (Exception ex){
            log.error(ex);
        }
        return modelAndView;
    }

    public WxUser getWxUser(String url){
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URI uri=new URIBuilder(url).build();
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){
                AccessToken accessToken = JSON.parseObject(EntityUtils.toString(response.getEntity()), AccessToken.class);
                WxUser wxUser = getUserdetail(accessToken.getAccess_token(),accessToken.getOpenid());
               return wxUser;
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return null;
    }

    public WxUser getUserdetail(String accessToken,String openid) throws URISyntaxException, IOException {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openid;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri=new URIBuilder(url).build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        int code = response.getStatusLine().getStatusCode();
        if(code == 200){
            WxUser wxUser = JSON.parseObject(EntityUtils.toString(response.getEntity()), WxUser.class);
            return wxUser;
        }
        else {
            return  null;
        }

    }

}