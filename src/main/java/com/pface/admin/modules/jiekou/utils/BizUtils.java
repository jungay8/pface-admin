package com.pface.admin.modules.jiekou.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import sun.net.util.URLUtil;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BizUtils {

//    @Value("${face.serverip}")
//    private String faceserverip;
//
//    @Value("${face.serverport}")
//    private String faceserverport;

    /**
     * 模拟登录，获取sessionid
     * @return
     */
//    public static String getSessionid(String user_name, String user_pwd){
//        try {
//            Map<String, Object> params = new HashMap<>();
//            params.put("msg_id", "257");
//            if (StringUtils.isEmpty(user_name)){
//                user_name = Faceconstant.boxusername_default;
//            }
//            if (StringUtils.isEmpty(user_pwd)){
//                user_pwd = Faceconstant.boxuserpassword_default;
//            }
//            params.put("user_name", user_name);
//            params.put("user_pwd",user_pwd);
//            String response = null;
//            response = HttpClientHelper.postJson(Faceconstant.uri, JSON.toJSONString(params),"");
//            response = convertResponse(response);
//            JSONObject obj = JSON.parseObject(response);
//            return obj.getString("data");
//        } catch (HttpException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static String getWebsockKey(){
//        try {
//            Map<String, Object> params = new HashMap<>();
//            params.put("msg_id", "1286");
//            String response = HttpClientHelper.postJson(Faceconstant.uri, JSON.toJSONString(params),
//                    getSessionid(null, null));
//
//            response = convertResponse(response);
//            JSONObject obj = JSON.parseObject(response);
//            return obj.getJSONObject("data").getString("key");
//        } catch (HttpException e) {
//            return e.toJsonStr();
//        }
//
//    }


    public static String convertResponse(String response) throws HttpException{
        JSONObject obj = JSON.parseObject(response);
        if (obj.getIntValue("code") == 0) {
            log.info(response);
            return response;
        } else {
            throw new HttpException(200, obj.getIntValue("code"), obj.getString("msg"));
        }
    }

    public static String getAccessSenseboxUrl(String ip, String port){
        if (StringUtils.isEmpty(port)){
            port = "80";
        }
        if (isipv4(ip)){
            return "http://" + ip + ":"+port + "/api/json";
        }
        return null;
    }

    public static String getAccessSenseboxUrlByForm(String ip, String port){
        if (StringUtils.isEmpty(port)){
            port = "80";
        }
        if (isipv4(ip)){
            return "http://" + ip + ":"+port + "/api/form";
        }
        return null;
    }

//    public String getFaceServerUrl(){
//        return "http://"+faceserverip + ":" + faceserverport + "/appapi/faceserverapi/";
//    }
    /**
     * 获取预览底图、抓拍图的url
     * @param ip
     * @param port
     * @return
     */
    public static String getAccessImageUrl(String ip, String port){
        if (StringUtils.isEmpty(port)){
            port = "80";
        }
        if (isipv4(ip)){
            return "http://" + ip + ":"+port + "/ws/";
        }
        return null;
    }
    public static boolean isipv4(String ipv4){
        if(ipv4==null || ipv4.length()==0){
            return false;//字符串为空或者空串
        }
        String[] parts=ipv4.split("\\.");//因为java doc里已经说明, split的参数是reg, 即正则表达式, 如果用"|"分割, 则需使用"\\|"
        if(parts.length!=4){
            return false;//分割开的数组根本就不是4个数字
        }
        for(int i=0;i<parts.length;i++){
            try{
                int n=Integer.parseInt(parts[i]);
                if(n<0 || n>255){
                    return false;//数字不在正确范围内
                }
            }catch (NumberFormatException e) {
                return false;//转换数字不正确
            }
        }
        return true;
    }

    public static void main(String[] args) {
//        String key = getWebsockKey();
//        log.info(key);
    }
}
