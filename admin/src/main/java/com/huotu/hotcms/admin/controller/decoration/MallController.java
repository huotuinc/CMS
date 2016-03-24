package com.huotu.hotcms.admin.controller.decoration;

import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.util.ResultView;
import com.huotu.hotcms.service.widget.service.MallApiEnvironmentService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     对接伙伴商城MallApi 接口
 * </p>
 * @since xhl
 *
 * @version 1.2
 */
@Controller
@RequestMapping("/mallapi")
public class MallController {
    private static final Log log = LogFactory.getLog(MallController.class);

    @Autowired
    private MallApiEnvironmentService mallApiEnvironmentService;

    @RequestMapping(value = "/uploadPhoto",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult<String> getPagesList(@RequestParam("customerId") Integer customerId,@RequestParam("fileId") Integer fileId, @RequestParam(value = "fileName", required = false) MultipartFile files) {
        ApiResult<String> apiResult = null;
        try{
            String fileName = files.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if("jpg, jpeg,png,gif,bmp".contains(suffix)){
                BASE64Encoder base64Encoder=new BASE64Encoder();
                String base64=base64Encoder.encodeBuffer(files.getBytes());
                BufferedImage sourceImg = javax.imageio.ImageIO.read(files.getInputStream());
                String size=sourceImg.getWidth()+"x"+sourceImg.getHeight();
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("customid",customerId);
                map.put("base64Image",base64);
                map.put("size",size);
                map.put("extenName","."+suffix);
                map.put("fileid",fileId);
                String path="gallery/uploadPhoto";
                apiResult= mallApiEnvironmentService.HttpPost(path,map);
            }
        }catch (Exception ex){
            log.error(ex);
        }
        return apiResult;
    }
}
