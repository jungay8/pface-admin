package com.pface.admin.modules.member.vo;

import lombok.Data;

import java.util.Date;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/17
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class AuditUserQueryVo {


    private Long uid;

    private String uname;

    private String mobile;

    private String memberType;

    private String isCert;

    private Date opDate;

    private Long certId;

    private String iconUrl;

    private String contactWay1;

    private String contactWay2;

    private String personId;

    private String companyName;

    private String personidFront;

    private String personidBack;

    private String companyFront;

    private String companyBack;

    private String companyCode;

    private String companyCert;
}
