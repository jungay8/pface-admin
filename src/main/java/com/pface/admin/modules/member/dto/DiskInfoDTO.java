package com.pface.admin.modules.member.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/24
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class DiskInfoDTO implements Serializable {

    private Long uid;

    private String uname;

    private Long catalogueId;

    private String catalogueName;

    private String mediaType;

    private long totalSize;

}
