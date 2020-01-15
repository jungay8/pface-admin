package com.pface.admin.modules.system.vo;

import lombok.Data;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/21
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class UserUpdateVo {
    private Long id;
    private String password;
    private String oldpassword;
}
