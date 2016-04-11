package com.huotu.hotcms.service.widget.service.impl.huobanplus;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.huotu.hotcms.service.service.HttpService;
import com.huotu.hotcms.service.util.ApiResult;
import com.huotu.hotcms.service.widget.model.JsonModel;
import com.huotu.hotcms.service.widget.service.GoodsDetailService;
import com.huotu.huobanplus.common.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * 商品详情
 * Created by chendeyu on 2016/4/8.
 */
@Profile("container")
@Service
public class GoodsDetailServiceImpl implements GoodsDetailService {

    @Autowired
    private HttpService httpService;

    @Override
    public Goods getGoodsDetail(int goodsId) throws Exception {

        ApiResult<String> apiResult = invokeGoodsDetailProce(goodsId);
        if(apiResult.getCode()!=200) {
            throw new Exception(apiResult.getMsg());
        }
        return JSON.parseObject(apiResult.getData(),Goods.class);
    }

    @Override
    public JsonModel getGoodsPrice(int userId, int goodsId) throws Exception {
        ApiResult<String> apiResult = invokeGoodsPriceProce(userId,goodsId);
        if(apiResult.getCode()!=200) {
            throw new Exception(apiResult.getMsg());
        }
        return new Gson().fromJson(apiResult.getData(), JsonModel.class);
    }


    private ApiResult<String> invokeGoodsDetailProce(int goodsId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("goodsId",goodsId);
//        params.put("unionId",unionId);
        return httpService.httpGet_prod("http", "api.open.huobanplus.com", null, "/goodses/goodsId", params);
    }

    private ApiResult<String> invokeGoodsPriceProce(int userId,int goodsId) throws Exception{
        Map<String,Object> params = new TreeMap<>();
        params.put("userId",userId);
        params.put("goodsId",goodsId);
        return httpService.httpGet_prod("http", "api.open.huobanplus.com", null, "/users/[userid]/goodsPrices?goods=1,2,3", params);
    }

}