package com.pface.admin.modules.member.service;

import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.po.FaceAppImageLibs;
import com.pface.admin.modules.member.po.FaceAppImages;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FaceAppImagesService extends IService<FaceAppImages> {
    /**
     * 远程确认人像图片是否存在
     *
     * @param faceserverip
     * @param faceserverport
     * @param deviceId
     * @param imgId
     * @param libId
     * @return
     */
    Boolean remoteEnsureImageExists(String faceserverip, String faceserverport, String deviceId, String imgId, String libId);

    /**
     * 远程修改人像图片属性
     *
     * @param faceserverip
     * @param faceserverport
     * @param deviceId
     * @param imgId
     * @param libId
     * @param personIdcard
     * @param personName
     * @param personGender
     * @param personAge
     * @param personAddr
     * @return
     */
    boolean remoteUpdateImage(String faceserverip, String faceserverport, String deviceId, String imgId, String libId, String personIdcard, String personName, String personGender, String personAge, String personAddr);

    /**
     * 远程删除人脸图片
     *
     * @param faceserverip
     * @param faceserverport
     * @param deviceId
     * @param imgId
     * @param libId
     * @return
     */
    boolean remoteDeleteImage(String faceserverip, String faceserverport, String deviceId, String imgId, Byte libId);

    /**
     * 上传单张人脸图片
     *
     * @param sysLibId
     * @param file
     * @return
     */
    boolean uploadoneimage(String faceserverip, String faceserverport, String sysLibId, String imgId, FaceAppImageLibs faceAppImageLibs, File file);
}
