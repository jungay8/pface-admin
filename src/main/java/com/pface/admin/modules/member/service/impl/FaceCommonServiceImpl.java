package com.pface.admin.modules.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.*;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.jiekou.service.FaceSenseboxInterfaceService;
import com.pface.admin.modules.jiekou.utils.BizUtils;
import com.pface.admin.modules.jiekou.utils.HttpClientHelper;
import com.pface.admin.modules.jiekou.utils.HttpException;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FaceCommonServiceImpl implements FaceCommonService {

    @Autowired
    FaceSenseboxService faceSenseboxService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private FaceSenseboxInterfaceService faceSenseboxInterfaceService;
    @Autowired
    private UserService userService;

    @Override
    public String invokeInterface(String sysDeviceId, Map<String, Object> mapParams) {
        try {
            String sessionId = getSessionid(Long.valueOf(sysDeviceId));
            String url = getAccessSenseboxUrl(Long.valueOf(sysDeviceId));
            String response = HttpClientHelper.postJson(url, JSON.toJSONString(mapParams),sessionId);
            return BizUtils.convertResponse(response);
        } catch (HttpException e) {
            throw new BizException(ResultCodeEnum.INVOKE_SENSE_BOX_INTERFACE_ERROR);
//            return e.toJsonStr();
        }
    }



    @Override
    public String invokeInterface(FaceSensebox faceSensebox, Map<String, Object> mapParams) {
        try {
            if (StringUtils.isEmpty(faceSensebox.getIp()) || StringUtils.isEmpty(faceSensebox.getPort())
                || StringUtils.isEmpty(faceSensebox.getLoginname()) || StringUtils.isEmpty(faceSensebox.getLoginpassword())){
                return null;
            }

            String url = BizUtils.getAccessSenseboxUrl(faceSensebox.getIp(), faceSensebox.getPort());
            String sessionId = faceSenseboxInterfaceService.getSessionid(faceSensebox);

            String response = HttpClientHelper.postJson(url, JSON.toJSONString(mapParams),sessionId);
            return BizUtils.convertResponse(response);
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.INVOKE_SENSE_BOX_INTERFACE_ERROR);
//            return e.toJsonStr();
        }

//        return null;
    }

    public <T> T setCommonField(T t, String action) {
        if (StringUtils.isEmpty(action)) {
            return t;
        }

        try {
            Date now = DateUtils.getNowDate();
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User user = null;
            if (StringUtils.isNotEmpty(username)){
                user = userService.queryOne(new User().setUsername(username));
            }

            Class<? extends Object> tClass = t.getClass();

            //updateBy字段
            if (user!=null){
                Field updateBy = tClass.getDeclaredField("updateBy");
                updateBy.setAccessible(true);
                String updateByname = updateBy.getName();
                updateByname = updateByname.replaceFirst(updateByname.substring(0, 1), updateByname.substring(0, 1).toUpperCase());
                Method setUpdateByMethod = tClass.getMethod("set" + updateByname, String.class);
                setUpdateByMethod.invoke(t, user.getId().toString());
            }
            //updateDate字段
            Field updateDate = tClass.getDeclaredField("updateDate");
            updateDate.setAccessible(true);
            String updateDatename = updateDate.getName();
            updateDatename = updateDatename.replaceFirst(updateDatename.substring(0, 1), updateDatename.substring(0, 1).toUpperCase());
            Method setUpdateDateMethod = tClass.getMethod("set"+updateDatename, Date.class);
            setUpdateDateMethod.invoke(t,now);

            if (action.equalsIgnoreCase("create")){
                //createBy字段
                if (user!=null){
                    Field createBy = tClass.getDeclaredField("createBy");
                    createBy.setAccessible(true);
                    String createByname = createBy.getName();
                    createByname = createByname.replaceFirst(createByname.substring(0, 1), createByname.substring(0, 1).toUpperCase());
                    Method setCreateByMethod = tClass.getMethod("set"+createByname, String.class);
                    setCreateByMethod.invoke(t, user.getId().toString());
                }

                //createDate字段
                Field createDate = tClass.getDeclaredField("createDate");
                createDate.setAccessible(true);
                String createDatename = createDate.getName();
                createDatename = createDatename.replaceFirst(createDatename.substring(0, 1), createDatename.substring(0, 1).toUpperCase());
                Method setCreateDateMethod = tClass.getMethod("set"+createDatename, Date.class);
                setCreateDateMethod.invoke(t, now);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
    public <T> T setAppCommonField(T t, String action, FaceAppUserPojo userinfo) {
        if (StringUtils.isEmpty(action)) {
            return t;
        }

        try {
            Date now = DateUtils.getNowDate();
//            String username = (String) SecurityUtils.getSubject().getPrincipal();
//            User user = null;
//            if (StringUtils.isNotEmpty(username)){
//                user = userService.queryOne(new User().setUsername(username));
//            }

            Class<? extends Object> tClass = t.getClass();

            //updateBy字段
            if (userinfo!=null){
                Field updateBy = tClass.getDeclaredField("updateBy");
                updateBy.setAccessible(true);
                String updateByname = updateBy.getName();
                updateByname = updateByname.replaceFirst(updateByname.substring(0, 1), updateByname.substring(0, 1).toUpperCase());
                Method setUpdateByMethod = tClass.getMethod("set" + updateByname, String.class);
                setUpdateByMethod.invoke(t, userinfo.getId().toString());
            }
            //updateDate字段
            Field updateDate = tClass.getDeclaredField("updateDate");
            updateDate.setAccessible(true);
            String updateDatename = updateDate.getName();
            updateDatename = updateDatename.replaceFirst(updateDatename.substring(0, 1), updateDatename.substring(0, 1).toUpperCase());
            Method setUpdateDateMethod = tClass.getMethod("set"+updateDatename, Date.class);
            setUpdateDateMethod.invoke(t,now);

            if (action.equalsIgnoreCase("create")){
                //createBy字段
                if (userinfo!=null){
                    Field createBy = tClass.getDeclaredField("createBy");
                    createBy.setAccessible(true);
                    String createByname = createBy.getName();
                    createByname = createByname.replaceFirst(createByname.substring(0, 1), createByname.substring(0, 1).toUpperCase());
                    Method setCreateByMethod = tClass.getMethod("set"+createByname, String.class);
                    setCreateByMethod.invoke(t, userinfo.getId().toString());
                }

                //createDate字段
                Field createDate = tClass.getDeclaredField("createDate");
                createDate.setAccessible(true);
                String createDatename = createDate.getName();
                createDatename = createDatename.replaceFirst(createDatename.substring(0, 1), createDatename.substring(0, 1).toUpperCase());
                Method setCreateDateMethod = tClass.getMethod("set"+createDatename, Date.class);
                setCreateDateMethod.invoke(t, now);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public FaceAppUserPojo appFaceUser(String faceserverip, String faceserverport, String operator, String id) {
        String url = "http://" + faceserverip + ":" + faceserverport + "/appapi/faceserverapi/userinfo";
        Map<String, String> param = new HashMap<>();
        param.put("operator", operator);
        param.put("id", id);
        String jsonstr = OKHttpUtil.httpPost(url, param);
        FaceAppUserPojo pojo = null;
        if (StringUtils.isNotEmpty(jsonstr)) {
            jsonstr = jsonstr.replaceAll("\\\\", "");
            jsonstr = jsonstr.substring(1, jsonstr.length() - 1);
            JSONObject jsonObject = JSON.parseObject(jsonstr);
            Boolean sucess = jsonObject.getBoolean("success");
            if (sucess) {
                JSONObject object = jsonObject.getJSONObject("data");
                pojo = object.toJavaObject(FaceAppUserPojo.class);
            }
        }
        return pojo;
    }

    public String getAccessSenseboxUrl(Long sysDeviceId){
        FaceSensebox faceSensebox = faceSenseboxService.queryById(sysDeviceId);
        if (faceSensebox == null){
            return null;
        }
        if (StringUtils.isEmpty(faceSensebox.getIp()) || StringUtils.isEmpty(faceSensebox.getPort())
                ||StringUtils.isEmpty(faceSensebox.getLoginname()) || StringUtils.isEmpty(faceSensebox.getLoginpassword())
                || !BizUtils.isipv4(faceSensebox.getIp())
        ){
            return null;
        }

        return BizUtils.getAccessSenseboxUrl(faceSensebox.getIp(), faceSensebox.getPort());
    }

    public String getAccessSenseboxUrlByForm(Long sysDeviceId){
        FaceSensebox faceSensebox = faceSenseboxService.queryById(sysDeviceId);
        if (faceSensebox == null){
            return null;
        }
        if (StringUtils.isEmpty(faceSensebox.getIp()) || StringUtils.isEmpty(faceSensebox.getPort())
                ||StringUtils.isEmpty(faceSensebox.getLoginname()) || StringUtils.isEmpty(faceSensebox.getLoginpassword())
                || !BizUtils.isipv4(faceSensebox.getIp())
        ){
            return null;
        }

        return BizUtils.getAccessSenseboxUrlByForm(faceSensebox.getIp(), faceSensebox.getPort());
    }

    public String getSessionid(Long sysDeviceId) {
        String key = Faceconstant.face_login_expire_key+"_"+sysDeviceId;
        String sessionIdFromRedis = redisUtils.get(key, Faceconstant.face_login_expire);
        if (StringUtils.isNotEmpty(sessionIdFromRedis)){
            log.info("sessionId = "+sessionIdFromRedis + ",from redis");
            return sessionIdFromRedis;
        }

        FaceSensebox faceSensebox = faceSenseboxService.queryById(sysDeviceId);
        if (faceSensebox == null){
            return null;
        }
        if (StringUtils.isEmpty(faceSensebox.getIp()) || StringUtils.isEmpty(faceSensebox.getPort())
                ||StringUtils.isEmpty(faceSensebox.getLoginname()) || StringUtils.isEmpty(faceSensebox.getLoginpassword())
                || !BizUtils.isipv4(faceSensebox.getIp())
        ){
            return null;
        }

        String sessionId = faceSenseboxInterfaceService.getSessionid(faceSensebox);
        redisUtils.set(key, sessionId, Faceconstant.face_login_expire);
        log.info("sessionId = "+sessionId + ",from login");

        return sessionId;
    }
}
