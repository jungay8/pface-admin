package com.pface.admin.modules.member.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.po.FaceCamera;
import com.pface.admin.modules.member.po.FaceDeviceChannelRes;
import com.pface.admin.modules.member.po.FaceSensebox;
import com.pface.admin.modules.member.service.FaceCameraService;
import com.pface.admin.modules.member.service.FaceCommonService;
import com.pface.admin.modules.member.service.FaceDeviceChannelResService;
import com.pface.admin.modules.member.service.FaceSenseboxService;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相机相关业务Service实现
 */
@Slf4j
@Service
public class FaceCameraServiceImpl extends BaseService<FaceCamera> implements FaceCameraService {
    @Autowired
    private FaceCommonService faceCommonService;

    @Autowired
    private FaceSenseboxService faceSenseboxService;
    @Autowired
    private FaceDeviceChannelResService faceDeviceChannelResService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void pullCameraInfoAndSave(FaceCamera faceCamera) {
        if (StringUtils.isEmpty(faceCamera.getDeviceId())){//所有设备
            List<FaceSensebox> list = faceSenseboxService.queryAll();
            for (FaceSensebox faceSensebox:list){
                faceCamera.setDeviceId(faceSensebox.getDeviceId());
                saveCameraOfSensebox(faceCamera);
            }
        }else {//选择了一个设备
            saveCameraOfSensebox(faceCamera);
        }
    }
    @Override
    public List<FaceCamera> queryAll() {
        FaceCamera faceCamera = new FaceCamera();
        faceCamera.setDelFlag("1");
        return super.queryList(faceCamera);
    }
    /**
     * 刷新保存智能盒子下的相机信息
     *
     * @param faceCamera
     */
    private void saveCameraOfSensebox(FaceCamera faceCamera){
        if (StringUtils.isEmpty(faceCamera.getDeviceId())){
            log.info("智能设备盒子id为空");
            return;
        }

        //获取设备信息
        FaceSensebox faceSensebox_cond = new FaceSensebox();
        faceSensebox_cond.setDeviceId(faceCamera.getDeviceId());
        FaceSensebox faceSensebox = faceSenseboxService.queryOne(faceSensebox_cond);

        //4.1 获取相机信息
        Map<String, Object> params = new HashMap<>();
        params.put("msg_id", "516");
        String jsonstr = faceCommonService.invokeInterface(faceSensebox.getId() + "", params); //设备下有多个相机
        JSONObject obj = JSON.parseObject(jsonstr);
        int code = obj.getIntValue("code");
        if (code == 0){
            faceCamera.setMode(obj.getJSONObject("data").getString("mode"));
            JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("camera");
            JSONObject jsonObject = null;

            //删除设备下的所有相机
            FaceCamera faceCamera1_cond = new FaceCamera();
            faceCamera1_cond.setDeviceId(faceCamera.getDeviceId());
            delete(faceCamera1_cond);
            //再逐条创建
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                faceCamera.setChannel(jsonObject.getByteValue("channel"));
                faceCamera.setCameraName(jsonObject.getString("camera_name"));
                String status =jsonObject.getByteValue("status") == 0 ? "off" : "on";
                faceCamera.setStatus(status);
                faceCamera.setLibIds(jsonObject.getString("lib_ids"));
                faceCamera.setCameraNo("DEV"+faceCamera.getDeviceId() + "CH"+faceCamera.getChannel());
                create(faceCamera);

                FaceDeviceChannelRes res_cond_update = new FaceDeviceChannelRes();
                res_cond_update.setDeviceId(faceCamera.getDeviceId());
                res_cond_update.setChannel(faceCamera.getChannel());
                FaceDeviceChannelRes oneRes = faceDeviceChannelResService.queryOne(res_cond_update);
                if (oneRes!=null){
                    res_cond_update.setIssetup("yes");
                    res_cond_update.setId(oneRes.getId());
                    faceCommonService.setCommonField(res_cond_update, "update");
                    faceDeviceChannelResService.updateNotNull(res_cond_update);
                }
            }
        }
    }
    /**
     * 保存一条相机信息
     *
     * @param faceCamera
     * @return
     */
    private int saveonecamera(FaceCamera faceCamera){
        if (StringUtils.isEmpty(faceCamera.getDeviceId()) || faceCamera.getChannel()==null){
            return 0;
        }

        FaceCamera faceCamera1_cond = new FaceCamera();
        faceCamera1_cond.setDeviceId(faceCamera.getDeviceId());
        faceCamera1_cond.setChannel(faceCamera.getChannel());
        faceCamera1_cond.setDelFlag("1");
        List<FaceCamera> faceCamera_list = queryList(faceCamera1_cond);
        int ret = 0;
        if (faceCamera_list.isEmpty()){
           ret = create(faceCamera);
        }else{
           faceCamera.setId(faceCamera_list.get(0).getId());
           ret = updateNotNull(faceCamera);
        }
        return ret;
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        Date now = DateUtils.getNowDate();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.queryOne(new User().setUsername(username));

        FaceCamera faceCamera = null;

        for(int i=0;i<ids.length;i++) {
            faceCamera = new FaceCamera();
            if (user!=null && user.getLocked()!=null){
                faceCamera.setUpdateBy(user.getId().toString());
            }
            faceCamera.setUpdateDate(now);

            faceCamera.setId(Integer.parseInt(ids[i].toString()));
            faceCamera.setDelFlag("0");
            super.updateNotNull(faceCamera);
        }
    }

}
