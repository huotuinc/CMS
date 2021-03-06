package com.huotu.hotcms.service.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.Property;

import javax.activation.UnsupportedDataTypeException;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/17.
 */
public class HttpUtils {
    /**
     * 通过图片url返回图片InputStream
     * @param url
     * @return
     */
    public static InputStream getInputStreamByUrl(URL url) throws IOException {
        InputStream is = null;
        if (url != null) {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream(); //得到网络返回的输入流
        }
        return is;
    }

    public static String getHtmlByUrl(URL url) throws IOException {
        InputStream inputStream=getInputStreamByUrl(url);
        byte[] getData = readInputStream(inputStream); //获得网站的二进制数据
        String html = new String(getData,"utf-8");
        return html;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static ApiResult<String> httpGet(String url,Map<String, Object> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        URI uri=new URIBuilder(url).setParameters(nameValuePairs).build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        ApiResult<String> apiResult = new ApiResult();
        apiResult.setCode(response.getStatusLine().getStatusCode());
        apiResult.setMsg(response.getStatusLine().getReasonPhrase());
        apiResult.setData(EntityUtils.toString(response.getEntity()));
        return apiResult;
    }

    public static ApiResult<String> httpPost(String url,Map<String, Object> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        URI uri=new URIBuilder(url).build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        ApiResult<String> apiResult = new ApiResult();
        apiResult.setCode(response.getStatusLine().getStatusCode());
        apiResult.setMsg(response.getStatusLine().getReasonPhrase());
        apiResult.setData(EntityUtils.toString(response.getEntity()));
        return apiResult;
    }

    public static ApiResult<String> httpGet(String scheme,String host,Integer port,String path, Map<String, Object> params) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        params.forEach((key,value)->{
            if(value != null) {
                nameValuePairs.add(new BasicNameValuePair(key,String.valueOf(value)));
            }
        });
        URI uri = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port == null ? 80 : port)
                .setPath(path)
                .setParameters(nameValuePairs)
                .build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        ApiResult<String> apiResult = new ApiResult();
        apiResult.setCode(response.getStatusLine().getStatusCode());
        apiResult.setMsg(response.getStatusLine().getReasonPhrase());
        apiResult.setData(EntityUtils.toString(response.getEntity()));
        return apiResult;
    }

    public static <T>T getRequestParam(HttpServletRequest request, Class<T> t) throws Exception{
        T obj = t.newInstance();
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        for(PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            String paramValue = request.getParameter(propertyName);
            if(paramValue == null || paramValue.length() == 0) {
                continue;
            }
            Class<?> propertyType = descriptor.getPropertyType();
            if(Double.class == propertyType) {
                beanWrapper.setPropertyValue(propertyName,Double.parseDouble(paramValue));
            }else if(Integer.class == propertyType) {
                beanWrapper.setPropertyValue(propertyName,Integer.parseInt(paramValue));
            }else if(Long.class == propertyType) {
                beanWrapper.setPropertyValue(propertyName,Long.parseLong(paramValue));
            }else if(String.class == propertyType) {
                beanWrapper.setPropertyValue(propertyName, paramValue);
            }else if(String[].class == propertyType) {
                beanWrapper.setPropertyValue(propertyName,request.getParameterValues(propertyName));
            } else {
                throw new UnsupportedDataTypeException("不支持的字段类型");
            }
        }
        return obj;
    }


}
