package com.pface.admin.modules.member.vo;

import lombok.Data;

import javax.persistence.Column;

@Data
public class MemberMediaVo {

    private Long mediaFileId;

    private String coverOrigin;

    /**
     * 归属标签id
     */
    private String belongLabelid;

    /**
     * 媒体标题
     */
    private String mediaTitle;

    /**
     * 目录分类id
     */
    private Long catalogueId;


    /**
     * 关键词
     */
    private String mediaKeyword;


    /**
     * 价格标签id
     */
    private String priceLabelid;

    /**
     * 转载标签id
     */
    private String publishLabelid;


    private String mediaBrief;
}
