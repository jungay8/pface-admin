package com.pface.admin.modules.member.service;


import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.FaceCamera;

/**
 * 相机相关业务Service
 */
public interface FaceCameraService extends IService<FaceCamera> {
    /**
     * 拉取相机信息
     *
     * @param faceCamera
     */
    void pullCameraInfoAndSave(FaceCamera faceCamera);


}
