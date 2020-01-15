package com.pface.admin.core.utils;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by liudong on 2017/9/25.
 */
@Slf4j
public class OKHttpUtil {

//    protected static final Logger log = LoggerFactory.getLogger(OKHttpUtil.class);
    
    public static String httpsGet(String url){
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor()
                .sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY)
                .build();
        
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @param url
     * @param para
     * @return
     */
    public static String httpPost(String url, Map<String, String> para) {
        if (para == null || para.size() == 0) {
            return null;
        }
        String result = null;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : para.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    public static void post_file(final String url, final Map<String, Object> map, File file) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
//            MediaType.parse() 里面是上传的文件类型。

            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            String filename = file.getName();
//            参数分别为， 请求key ，文件名称 ， RequestBody
//            requestBody.addFormDataPart("file", file.getName(), body);
            requestBody.addFormDataPart("file", filename, body);

        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }
        Request request = new Request.Builder().url(url).post(requestBody.build()).tag("pchj_"+System.currentTimeMillis()).build();

//    Request request = new Request.Builder().url("请求地址").post(requestBody.build()).tag(context).build();

        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                log.info("onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    log.info("lfq="+ response.message() + " , body = " + str);
                } else {
                    log.info("lfq="+ response.message() + " error : body = " + response.body().string());
                }
            }
        });
    }

    public static String httpsPost(String url, Map<String, String> para){

        if (para == null || para.size() == 0) {
            return null;
        }
        String result = null;
        //ssl认证重写
        OkHttpClient okHttpClient=new OkHttpClient.Builder().hostnameVerifier(
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                }
        ).build();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : para.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = builder.build();
        Request request=new Request.Builder()
                .url(url)
                .post(formBody)
                //.addHeader("Cookie","JSESSIONID=299571E0E40DA6E9962E41B87A669BBB")
                //.addHeader("content-type", "application/json")
                //.addHeader("cache-control", "no-cache")
                .build();
        Call call=okHttpClient.newCall(request);
        try {
            Response response=call.execute();
            result=response.body().string();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 发送httppost请求
     *
     * @param url
     * @param jsonData  提交的参数为key=value&key1=value1的形式
     * @return
     */
    public static String httpPostForJson(String url, String jsonData) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String httpPostForJson(String url, String jsonData,final String webName,final String webPwd) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient().newBuilder().authenticator(new Authenticator(){
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                log.info("Authenticating for response: " + response);
                log.info("Challenges: " + response.challenges());
                String credential = Credentials.basic(webName,webPwd);
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
        }).build();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static Map<String, String> getParameterToMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        @SuppressWarnings("rawtypes")
        Enumeration names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String paramName = (String) names.nextElement();
            String paramValue = request.getParameter(paramName);
            paramValue = (StringUtils.isBlank(paramValue) || ("null".equals(paramValue))) ? null : paramValue;
            params.put(paramName, paramValue);
        }
        return params;
    }


    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url,final String webName,final String webPwd) {

        String result = null;
        OkHttpClient httpClient = new OkHttpClient().newBuilder().authenticator(new Authenticator(){
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                log.info("Authenticating for response: " + response);
                log.info("Challenges: " + response.challenges());
                String credential = Credentials.basic(webName,webPwd);
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
        }).build();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
