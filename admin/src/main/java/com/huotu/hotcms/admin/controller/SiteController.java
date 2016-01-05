package com.huotu.hotcms.admin.controller;

import com.huotu.hotcms.admin.util.web.CookieUser;
import com.huotu.hotcms.service.entity.Host;
import com.huotu.hotcms.service.entity.Region;
import com.huotu.hotcms.service.entity.Site;
import com.huotu.hotcms.service.repository.RegionRepository;
import com.huotu.hotcms.service.service.HostService;
import com.huotu.hotcms.service.service.SiteService;
import com.huotu.hotcms.service.util.PageData;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.service.util.ResultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Created by chendeyu on 2015/12/24.
 */
@Controller
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private HostService hostService;

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CookieUser cookieUser;

    @RequestMapping("/siteList")
    public ModelAndView siteList(HttpServletRequest request) throws Exception
    {
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/siteList.html");

        return  modelAndView;
    }



    @RequestMapping(value = "/addSite")
    public ModelAndView addSite(HttpServletRequest request) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/addSite.html");
        List<Region> regions =regionRepository.findAll();
        modelAndView.addObject("regions",regions);
        return  modelAndView;
    }


    /*
      * 更新地区
      * */
    @RequestMapping(value = "/saveSite",method = RequestMethod.POST)
    @Transactional(value = "transactionManager")
    @ResponseBody
    public ResultView updateSite(Site site,Long regionId,String...domains){
        ResultView result=null;
        try {
            if (domains.length != 0) {
                for (String domain : domains) {
                    Host flag = hostService.getHost(domain);
                    if (flag == null) {
                        Host host = new Host();
                        host.setCustomerId(site.getCustomerId());
                        host.setDomain(domain);
                        site.addHost(host);
                    } else {
                        if (flag.getCustomerId() .equals(site.getCustomerId())) {
                            siteService.save(site);
                            site.addHost(flag);

                        } else {
                            result = new ResultView(ResultOptionEnum.DOMAIN_EXIST.getCode(), ResultOptionEnum.DOMAIN_EXIST.getValue(), null);
                            return result;
                        }
                    }
                }
                Long siteId = site.getSiteId();
                Region region = regionRepository.findOne(regionId);
                if (siteId == null) {
                    site.setCreateTime(LocalDateTime.now());
                    site.setUpdateTime(LocalDateTime.now());
                    site.setRegion(region);
                    site.setDeleted(false);
                    siteService.save(site);
                } else {
                    site.setUpdateTime(LocalDateTime.now());
                    siteService.save(site);
                }
                result = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), null);
            }
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


    @RequestMapping("/updateSite")
    public ModelAndView updateSite(@RequestParam(value = "id",defaultValue = "0") Long id,int customerId) throws Exception{
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/view/web/updateSite.html");

        if(id!=0) {
            Site site = siteService.findBySiteIdAndCustomerId(id,customerId);
            if (site != null) {
                modelAndView.addObject("site", site);
                Set<Host> hosts =site.getHosts();
                String domains="";
                for(Host host : hosts){
                    String domain= host.getDomain();
                    domains=domains+domain+",";
                }
                Region region= site.getRegion();
                modelAndView.addObject("region",region);
                modelAndView.addObject("domains", domains.substring(0, domains.length() - 1));
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getSiteList")
    @ResponseBody
    public PageData<Site> getModelList(@RequestParam(name="customerId",required = false) Integer customerId,
                            @RequestParam(name="name",required = false) String name,
                           @RequestParam(name = "page",required = true,defaultValue = "1") int page,
                           @RequestParam(name = "pagesize",required = true,defaultValue = "20") int pageSize){
        PageData<Site> pageModel=siteService.getPage(customerId,name, page, pageSize);
        return pageModel;
    }

    @RequestMapping(value = "/deleteSite",method = RequestMethod.POST)
    @ResponseBody
    public ResultView deleteModel(@RequestParam(name = "id",required = true,defaultValue = "0") Long id,int customerId,HttpServletRequest request) {
        ResultView result=null;
            try{
            if(cookieUser.isSupper(request)) {
               Site site = siteService.findBySiteIdAndCustomerId(id, customerId);
                site.setDeleted(true);
                siteService.save(site);
                result=new ResultView(ResultOptionEnum.OK.getCode(),ResultOptionEnum.OK.getValue(),null);
            }
            else {
                result=new ResultView(ResultOptionEnum.NO_LIMITS.getCode(),ResultOptionEnum.NO_LIMITS.getValue(),null);
            }
        }
        catch (Exception ex)
        {
            result=new ResultView(ResultOptionEnum.FAILE.getCode(),ResultOptionEnum.FAILE.getValue(),null);
        }
        return  result;
    }


}
