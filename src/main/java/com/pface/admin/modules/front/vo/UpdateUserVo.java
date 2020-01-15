package com.pface.admin.modules.front.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/23
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class UpdateUserVo implements Serializable {

    private Long id;
    private String uname;
    private String pwd;
    private String newPwd;
    private String newPwd2;
    private String mobile;
    private String email;
    private String headUrl;


}
