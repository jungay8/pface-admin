package com.pface.admin.modules.front.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FaceAppSnapListAssemblePojo {

    /**
     * 统计字段：应到
     */
    private Integer totalcnt;
    /**
     * 统计字段：实到
     */
    private Integer partcnt;

//////////////////////////////////////////////////face_app_usescene
    private Integer sysSceneid;
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

    //////////////////////////////////////////////////face_app_usescene
}
