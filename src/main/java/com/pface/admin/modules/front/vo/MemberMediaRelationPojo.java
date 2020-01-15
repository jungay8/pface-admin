package com.pface.admin.modules.front.vo;

import lombok.Data;

@Data
public class MemberMediaRelationPojo {

    /**
     * 关联id，主键
     */
    private Long id;

    /**
     * 被关联媒体文件id，
     */
    private Long mediaFileId;

    /**
     * 关联的媒体文件id
     */
    private Long relationMediaFileId;

    /**
     * 关联的媒体文件标题
     */
    private String relationMediaFileTitle;
}
