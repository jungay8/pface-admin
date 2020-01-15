package com.pface.admin.modules.member.vo;

import lombok.Data;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/20
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class UserListVo {
    private Long uid;
    private String uname;
    private String userType;
    private String memType;
    private String mobile;
    private String certType;
}
