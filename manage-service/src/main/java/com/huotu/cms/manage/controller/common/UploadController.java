/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.cms.manage.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huotu.hotcms.service.entity.login.Login;
import com.huotu.hotcms.service.util.ResultOptionEnum;
import com.huotu.hotcms.widget.CMSContext;
import me.jiangcai.lib.resource.service.ResourceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/manage/cms")
public class UploadController {

    private static final Log log = LogFactory.getLog(UploadController.class);

    @Autowired
    private ResourceService resourceService;


    /**
     * 上传永久资源,
     *
     * @param login
     * @param file
     * @return
     */
    @RequestMapping(value = "/resourceUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> resourceUpload(@AuthenticationPrincipal Login login
            , @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (login.siteManageable(CMSContext.RequestContext().getSite())) {
                String fileName = file.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String path = "page/resource/img/" + UUID.randomUUID().toString() + "." + suffix;
                URI uri = resourceService.uploadResource(path, file.getInputStream()).httpUrl().toURI();
                map.put("fileUri", uri);
                map.put("path", path);
                map.put("code", ResultOptionEnum.OK.getCode());
                map.put("msg", ResultOptionEnum.RESOURCE_PERMISSION_ERROR.getValue());
            } else {
                throw new IllegalStateException("上传失败，没有权限");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("code", ResultOptionEnum.RESOURCE_PERMISSION_ERROR.getCode());
            map.put("msg", e.getMessage());
        }
        return map;
    }


    /**
     * 删除永久资源
     *
     * @param login
     * @param json
     * @return
     */
    @RequestMapping(value = "/deleteResource", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteResource(@AuthenticationPrincipal Login login
            , @RequestBody String json) throws IOException {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map jsonMap = objectMapper.readValue(json, Map.class);
        String path = (String) jsonMap.get("path");
        try {
            if (login.siteManageable(CMSContext.RequestContext().getSite())) {
                resourceService.deleteResource(path);
                map.put("path", path);
                map.put("code", ResultOptionEnum.OK.getCode());
                map.put("msg", ResultOptionEnum.OK.getValue());
            } else {
                throw new IllegalStateException("删除失败，没有权限");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("code", ResultOptionEnum.RESOURCE_PERMISSION_ERROR.getCode());
            map.put("msg", e.getMessage());
        }
        return map;
    }

//    @RequestMapping(value = "/imgUpLoad", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultView imgUpLoad(@RequestParam(value = "btnFile", required = false) MultipartFile files) {
//        ResultView resultView = null;
//        try {
//            Date now = new Date();
//            String fileName = files.getOriginalFilename();
//            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//            if ("jpg, jpeg,png,gif,bmp".contains(suffix)) {
//                String path = configInfo.getResourceWidgetImg() + "/" + StringUtil.DateFormat(now, "yyyyMMddHHmmSS")
//                        + "." + suffix;
//                URI uri = resourceService.uploadResource(path, files.getInputStream()).httpUrl().toURI();
//                BufferedImage sourceImg = javax.imageio.ImageIO.read(files.getInputStream());
//                Map<String, Object> map = new HashMap<>();
//                map.put("fileUrl", uri);
//                map.put("fileUri", path);
//                map.put("wide", sourceImg.getWidth());
//                map.put("height", sourceImg.getHeight());
//                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
//            } else {
//                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode()
//                        , ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), e.getMessage(), null);
//        }
//        return resultView;
//    }
//
//    @RequestMapping(value = "/widgetUpLoadRead", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultView widgetUpLoadRead(Long id, @RequestParam(value = "btnFile1", required = false) MultipartFile files) {
//        ResultView resultView = null;
//        try {
//            Date now = new Date();
//            String fileName = files.getOriginalFilename();
//            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//            if ("html".contains(suffix)) {
//                String path = configInfo.getResourcesWidget() + "/template_" + id + "." + suffix;
//                URI uri = resourceService.uploadResource(path, files.getInputStream()).httpUrl().toURI();
//                String content = HttpUtils.getHtmlByUrl(uri.toURL());
//                Map<String, Object> map = new HashMap<String, Object>();
////                WidgetMains widgetMains = widgetService.findWidgetMainsById(widgetId);//修改
////                widgetMains.setResourceUri(path);
////                widgetService.saveWidgetMains(widgetMains);
//                map.put("fileContent", content);
//                map.put("fileUri", uri);
//                map.put("fileUrl", path);
//                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
//            } else {
//                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode()
//                        , ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), e.getMessage(), null);
//        }
//        return resultView;
//    }
//
//    @RequestMapping(value = "/downloadUpLoad", method = RequestMethod.POST)
//    @ResponseBody
//    public ResultView downloadUpLoad(long ownerId, @RequestParam(value = "btnFile", required = false) MultipartFile files) {
//        ResultView resultView = null;
//        try {
//            Date now = new Date();
//            String fileName = files.getOriginalFilename();
//            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//            if ("txt,zip,jar,docx,doc,xlsx".contains(suffix)) {
//                String path = configInfo.getResourcesDownload(ownerId) + "/" +
//                        StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + suffix;
//                URI uri = resourceService.uploadResource(path, files.getInputStream()).httpUrl().toURI();
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("fileUrl", uri);
//                map.put("fileUri", path);
//                resultView = new ResultView(ResultOptionEnum.OK.getCode(), ResultOptionEnum.OK.getValue(), map);
//            } else {
//                resultView = new ResultView(ResultOptionEnum.FILE_FORMATTER_ERROR.getCode()
//                        , ResultOptionEnum.FILE_FORMATTER_ERROR.getValue(), null);
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            resultView = new ResultView(ResultOptionEnum.SERVERFAILE.getCode(), e.getMessage(), null);
//        }
//        return resultView;
//    }
//
//
//    @RequestMapping(value = "/kindeditorUpload", method = RequestMethod.POST)
//    @ResponseBody
//    public Result fileUploadUeImage(long ownerId, MultipartHttpServletRequest multipartHttpServletRequest)
//            throws Exception {
//        Result result = new Result();
//        Date now = new Date();
//        MultipartFile file = multipartHttpServletRequest.getFile("imgFile");
////        String[] img =configInfo.getResourcesUeditor().split("/");
//        //取得扩展名
//        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
//                .toLowerCase();
////        String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + fileExt;
//
//        String path = configInfo.getResourcesUeditor(ownerId) + "/" + StringUtil.DateFormat(now, "yyyyMMddHHmmSS")
//                + "." + fileExt;
//
//        URI uri = resourceService.uploadResource(path, file.getInputStream()).httpUrl().toURI();
//        result.setError(0);
//        result.setUrl(uri.toString());
//        return result;
//    }
//
//
//    @RequestMapping("/ajaxEditorFileUpload")
//    @ResponseBody
//    public Result ajaxEditorFileUpload(long ownerId, String imgsrc) throws Exception {
//        Result result = new Result();
//        //去掉字符串前面多余的字符"data:image/png;base64,"，获得纯粹的二进制地址
//        imgsrc = imgsrc.substring(22);
//        try {
//            //将String转换成InputStream流
//            byte[] bytes1 = Base64.getDecoder().decode(imgsrc);
//            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
////            String[] img =configInfo.getResourcesImg().split("/");
////            Date now = new Date();
////            String path =img[0]+"/"+3447+"/"+img[2]+"/"+ StringUtil.DateFormat(now, "yyyyMMddHHmmSS") + "." + "png";
//            Date now = new Date();
////            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
//            String path = configInfo.getResourcesUeditor(ownerId) + "/" + StringUtil.DateFormat(now, "yyyyMMddHHmmSS")
//                    + ".png";
//            //上传至服务器
////            String fileName = StaticResourceService.RICHTEXT_UPLOAD + UUID.randomUUID().toString() + ".png";
//            URI uri = resourceService.uploadResource(path, bais).httpUrl().toURI();
//
//            result.setStatus(0);
//            result.setMessage(path);
//            result.setUrl(uri.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

}