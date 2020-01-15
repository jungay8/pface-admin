package com.pface.admin.modules.front.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FaceAppSnapListPojo {

    ////////////////////////////face_app_snap_list
    private Integer sysUserid;
    private String deviceId;
    private Byte libId;
    /**
     * 图片ID，上传的人脸ID
     */
    private String imgId;
    /**
     * 图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
     */
    private String imgPath;
    /**
     * 抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     */
    private String snapPath;
    private String snapId;

    /**
     * 地址
     */
    private String personAddr;

    /**
     * 年龄
     */
    private String personAge;

    /**
     * 性别
     */
    private String personGender;

    /**
     * 证件号
     */
    private String personIdcard;

    /**
     * 姓名
     */
    private String personName;

    private String cameraName;

    /**
     * 相机通道
     */
    private Byte channel;

    /**
     * 相机位置
     */
    private String position;
    /**
     * 抓拍图片时间
     */
    private Date triggerTime;

    ////////////////////////////face_app_image_libs
    private Integer sysSceneid;
    ////////////////////////face_app_usescene
    private String scenename;
    private Date startDate;
    /**
     * 结束时间,如:会议结束时间
     */
    private Date endDate;
    /**
     * 签到开始时间
     */
    private Date asignStarttime;
    /**
     * 签到结束时间
     */
    private Date asignEndtime;
    private String theme;
    private String address;
}
