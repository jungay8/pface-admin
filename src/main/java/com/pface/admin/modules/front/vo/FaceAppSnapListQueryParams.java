package com.pface.admin.modules.front.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FaceAppSnapListQueryParams {

    /**
     * 综合查询的多字段参数，如person_name\person_idcard
     */
    private String personFlexParam;

    /**
     * 场景id
     */
    private Integer sysSceneid;

    /**
     * 用户id
     */
    private Integer sysUserid;

    /**
     * 开始时间,如:会议开始时间，精确到天
     */
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

}
