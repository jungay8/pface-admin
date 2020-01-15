package com.pface.admin.modules.base.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.jiekou.pojo.JmgoMemberUserParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonResultUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, int code, String msg, Object data, boolean isplainText) {
        return produceJsonResult(userInfo, new JsonResult(code, msg, data), isplainText);
    }

    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, int code, String msg, Object data) {

        return produceJsonResult(userInfo, new JsonResult(code, msg, data), false);
    }

    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, Object data) {

        return produceJsonResult(userInfo, new JsonResult(1, "请求成功", data), false);
    }
    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, int code, String msg) {

        return produceJsonResult(userInfo, new JsonResult(code, msg, null), false);
    }

    /**
     * @param userInfo    登录信息
     * @param jsonResult  返回的json
     * @param isPlainText 是否为明文封装 true 是  false 否
     */
    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, JsonResult jsonResult, boolean isPlainText) {

        if (isPlainText == true)
            return jsonResult;

        if (userInfo == null)
            return jsonResult;

//        if (userInfo.getLoginisencrypt() != 1)
//            return jsonResult;

        if (jsonResult.getData() == null)
            return jsonResult;

        //getData为空也需要加密
        String data = "{}";

        if (jsonResult.getData() != null) {
            try {
                //Include.Include.ALWAYS 默认
                //Include.NON_DEFAULT 属性为默认值不序列化
                //Include.NON_EMPTY 属性为 空（“”） 或者为 NULL 都不序列化
                //Include.NON_NULL 属性为NULL 不序列化
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//为空不序列化，只对VO起作用，Map List不起作用
                data = mapper.writeValueAsString(jsonResult.getData());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        jsonResult.setData(CryptoUtils.encryptAndEncodeByDes3AndBase64(userInfo.getAppSecret(), data));

        return jsonResult;
    }

    /**
     * 将Map<String,Object>封装成加密后的JsonResult对象
     *
     * @param userInfo
     * @param map
     * @return
     */
    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, Map<String, Object> map) {
        return produceJsonResult(userInfo, map, false);
    }

    /**
     * 将Map<String,Object>封装成JsonResult对象
     *
     * @param userInfo
     * @param map
     * @param isPlainText
     * @return
     */
    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, Map<String, Object> map, boolean isPlainText) {
        return produceJsonResult(userInfo, 0, null, map, isPlainText);
    }

    /**
     * 将Map<String,Object>封装成加密后的JsonResult对象
     *
     * @param userInfo
     * @param map
     * @param isPlainText 是否为明文封装 true 是  false 否
     * @return
     */
    public static JsonResult produceJsonResult(JmgoMemberUserParams userInfo, int code, String msg, Map<String, Object> map, boolean isPlainText) {

        System.out.println("产生结果数据传入参数：userInfo=" + userInfo + ",code= " + code + ",msg=" + msg + ",map=" + map + ",isPlainText=" + isPlainText);
        if (!isPlainText && userInfo == null) {
            return new JsonResult(-4, "登录超时，请重新登录");
        }
        int result = Integer.MAX_VALUE;
        String message = null;
        //先进行空字段的过滤
        if (map != null && !map.isEmpty()) {
            List<String> removeList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("result")) {
                    result = (Integer) entry.getValue();
                    removeList.add(entry.getKey());
                } else if (entry.getKey().equals("message")) {
                    message = (String) entry.getValue();
                    removeList.add(entry.getKey());
                } else if (entry.getValue() == null) {
                    removeList.add(entry.getKey());
                }
            }
            for (String key : removeList) {
                map.remove(key);
            }
        }
        if (code != 0 && StringUtils.isNotEmpty(msg)) { //都传了，不用处理

        } else if (code == 0 && StringUtils.isNotEmpty(msg)) { // 传了msg没传code，表示成功
            code = 1;
        } else if (code == 0 && StringUtils.isEmpty(msg)) {// 都没传，则优先去map里面取
            if (result != Integer.MAX_VALUE && StringUtils.isNotEmpty(message)) {
                code = result;
                msg = message;
            } else { //都没有，就采用默认返回
                code = 1;
                msg = "OK";
            }
        }

//        if (userInfo.getLoginisencrypt() != 1)
//            isPlainText = true;

        if (isPlainText) {
            return new JsonResult(code, msg, map);
        } else {
            if (map != null) {
                String json = "";
                try {
                    json = mapper.writeValueAsString(map);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return new JsonResult(code, msg, CryptoUtils.encryptAndEncodeByDes3AndBase64(userInfo.getAppSecret(), json));
            } else {
                return new JsonResult(code, msg);
            }
        }
    }
}