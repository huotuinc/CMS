package com.huotu.hotcms.web.model;


import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.web.util.PatternMatchUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * Created by Administrator xhl 2016/1/7.
 */
public class RequestModel{
    private String hosts;

    /**
     * 当前请求的相对目录Url
     * */
    private String url;

    private List<PageModel> pages;

    private int currentPage;

    private boolean hasNextPage;

    private String nextPageHref;

    private boolean hasPrevPage;

    private String prevPageHref;

    /**
     * 当前请求的根路径并且加上语言版本参数(如果有则加上,没有相同于root属性)
     * */
    private String rootUri;

    public String getRootUri() {
        return rootUri;
    }

    public void setRootUri(Site site,HttpServletRequest request) {
        String rootUrl="";
        String langParam=PatternMatchUtil.getEffecterLangParam(request, site);
        Integer port=request.getServerPort();
        if(site.isCustom()){
            if(this.getContextPath()!=null) {
                rootUrl = site.getCustomTemplateUrl()+this.getContextPath();
            }else{
                rootUrl = site.getCustomTemplateUrl();
            }
        }else {
            if(StringUtils.isEmpty(langParam)){
                if(this.getContextPath()!=null) {
                    if(port.equals(80)){
                        rootUrl = "http://" + request.getServerName() + this.getContextPath();
                    }else {
                        rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + this.getContextPath();
                    }
                }else{
                    if(port.equals(80)){
                        rootUrl = "http://" + request.getServerName() ;
                    }else {
                        rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
                    }
                }
            }else{
                if(this.getContextPath()!=null) {
                    if(port.equals(80)) {
                        rootUrl = "http://" + request.getServerName() + this.getContextPath() + "/" + langParam;
                    }else {
                        rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + this.getContextPath() + "/" + langParam;
                    }
                }else{
                    if(port.equals(80)) {
                        rootUrl = "http://" + request.getServerName() + "/" + langParam;
                    }else {
                        rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/" + langParam;
                    }
                }
            }
        }
        this.rootUri = rootUrl;
    }

    /**
     * 当前请求的根路径
     * */
    private String root;

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * 虚拟目录
     * */
    private String contextPath;


    /**
     * CMS图片资源（可以配置CDN）
     * **/
    private String resourcesUri;

    public String getResourcesUri() {
        return resourcesUri;
    }

    public void setResourcesUri(String resourcesUri) {
        this.resourcesUri = resourcesUri;
    }

    public String getContextPath() {
        return contextPath;
    }


    public String getRoot() {
        return root;
    }

    public void setRoot(Site site,HttpServletRequest request) {
        String rootUrl="";
        Integer port=request.getServerPort();
        if(site.isCustom()){
            if(this.getContextPath()!=null) {
                rootUrl = site.getCustomTemplateUrl()+this.getContextPath();
            }else{
                rootUrl = site.getCustomTemplateUrl();
            }
        }else {
            if(this.getContextPath()!=null) {
                if(port.equals(80)) {
                    rootUrl = "http://" + request.getServerName()  + this.getContextPath();
                }else {
                    rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + this.getContextPath();
                }
            }else{
                if(port.equals(80)) {
                    rootUrl = "http://" + request.getServerName();
                }else {
                    rootUrl = "http://" + request.getServerName() + ":" + request.getServerPort();
                }
            }

        }
        this.root = rootUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request;

    public String get(String param) {
        if (request != null) {
            return request.getParameter(param);
        }
        return null;
    }

    public List<PageModel> getPages() {
        return pages;
    }

    public void setPages(List<PageModel> pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }

    public String getNextPageHref() {
        return nextPageHref;
    }

    public void setNextPageHref(String nextPageHref) {
        this.nextPageHref = nextPageHref;
    }

    public String getPrevPageHref() {
        return prevPageHref;
    }

    public void setPrevPageHref(String prevPageHref) {
        this.prevPageHref = prevPageHref;
    }
}
