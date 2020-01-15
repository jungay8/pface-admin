package com.pface.admin.modules.member.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class FaceAppImagesVo {
    /**
     * Face_app_images.id
     */
    private Integer id;

    /**
     * 客户编码:face_app_images.sys_userid
     */
    private Integer sysUserid;

    /**
     * 设备ID，指盒子智能设备---6.3:face_app_images.device_id
     */
    private String deviceId;

    /**
     * 人像库ID:face_app_images.lib_id
     */
    private Byte libId;

    /**
     * 图片ID:face_app_images.img_id
     */
    private String imgId;



    /**
     * 场景id：face_app_image_libs.sys_sceneid
     */
    private Integer sysSceneid;

    /**
     * 场景开始时间,如:会议开始时间，精确到天:face_app_usescene.start_date
     */
    private Date startDate;

    /**
     * 场景结束时间,如:会议结束时间:face_app_usescene.end_date
     */
    private Date endDate;

    /**
     * 场景签到开始时间:face_app_usescene.asign_start_date
     */
    private Date asignStarttime;

    /**
     * 场景签到结束时间:face_app_usescene.asign_end_date
     */
    private Date asignEndtime;

}
