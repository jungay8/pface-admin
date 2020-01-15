package com.pface.admin.modules.jiekou.utils;

import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class HttpClientHelper {

    public static String postJson(String url,String jsonStr,String sessionid) throws HttpException{

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");

        httpPost.addHeader("sessionid",sessionid);
        StringEntity requestEntity = new StringEntity(jsonStr,"utf-8");
        requestEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(requestEntity);
       return getResponseStr(httpClient,httpPost);
    }

    public static String postForm(String url, Map<String, File> files, Map<String,String> params, String sessionid) throws HttpException {
        CloseableHttpResponse response = null;
        String responseStr = "";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("sessionid",sessionid);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//请设置Mode为BROWSER_COMPATIBLE
        for(Map.Entry<String,File> file : files.entrySet()){
            multipartEntityBuilder.addBinaryBody(file.getKey(), file.getValue());
        }
        for(Map.Entry<String,String> param : params.entrySet()){
            ContentType contentType = ContentType.create("text/plain", StandardCharsets.UTF_8);//解决中文乱码
            multipartEntityBuilder.addTextBody(param.getKey(), String.valueOf(param.getValue()), contentType);
        }
        HttpEntity httpEntity=multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        return getResponseStr(httpClient,httpPost);
    }

    private static String getResponseStr(CloseableHttpClient httpClient,HttpPost httpPost) throws HttpException{
        CloseableHttpResponse response = null;
        String responseStr = "";
        try {
            response  = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                HttpEntity responseEntity = response.getEntity();
                responseStr = EntityUtils.toString(responseEntity, "UTF-8");
            }else{
                throw new HttpException(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
            }
            return responseStr;
        } catch (IOException e) {
            throw new HttpException(600,e.getMessage());
        }
        finally{
            try {
                if (response != null)
                    response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}