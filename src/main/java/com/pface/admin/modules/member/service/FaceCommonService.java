package com.pface.admin.modules.member.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.OKHttpUtil;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.po.FaceSensebox;

import java.util.HashMap;
import java.util.Map;

public interface FaceCommonService {
    /**
     * 接口调起通用方法
     *
     * @param sysDeviceId
     * @param mapParams
     * @return
     */
    public String invokeInterface(String sysDeviceId, Map<String, Object> mapParams);

    /**
     *
     * @param faceSensebox (ip,port,loginname, loginpassword 必须的4个属性）
     * @param mapParams
     * @return
     */
    public String invokeInterface(FaceSensebox faceSensebox, Map<String, Object> mapParams);

    /**
     * 获取智能设备登录的sessionid
     * @param sysDeviceId
     * @return
     */
    public String getSessionid(Long sysDeviceId);

    /**
     * 获取访问只能设备的URL
     * @param sysDeviceId
     * @return
     */
    public String getAccessSenseboxUrl(Long sysDeviceId);
    public String getAccessSenseboxUrlByForm(Long sysDeviceId);//用于人脸图片上传
    /**
     * 设置实体的通用字段值:updateBy,updateDate, createBy,createDate
     *
     * @param t
     * @param action :create or update
     * @param <T>
     */
    public <T> T setCommonField(T t, String action);
    public <T> T setAppCommonField(T t, String action, FaceAppUserPojo faceAppUserPojo); //前端用

    public FaceAppUserPojo appFaceUser(String faceserverip, String faceserverport, String operator, String id) ;//前端用
}
