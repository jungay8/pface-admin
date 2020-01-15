package com.pface.admin.modules.front.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MemberMediaQueryParams {

    /**
     * 内容媒体文件id
     */
    private Long mediaFileId;

    /**
     * 归属用户id
     */
    private Long uid;
    /**
     * 归属用户name
     */
    private String uname;
    /**
     * 媒体资源类型
     */
    private String[] mediaType;
    /**
     * 媒体是否有基本信息 0没有，1有
     */
    private String isMediaInfo;
    /**
     * 媒体是否发布 0没 1发布
     */
    private String isPublishMedia;
    /**
     * 媒体状态 0审核中，1审核没通过
     */
    private String mediaStatus;
    /**
     * 文件商品状态：0上架 1下架
     */
    private String goodsStatus;

    /**
     * 原始文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Double fileSize;
    /**
     * 转码标识：转码标识：0：初始；1：完成；2:错误；3：不需要转码
     */
    private Integer changcodeflag;
    /**
     * 上传日期
     */
//    private Date uploadDate;

    /**
     * 上传日期开始
     */
    private Date bUploadDate;

    /**
     * 上传日期结束
     */
    private Date eUploadDate;

    /////////////////////////////////////////////
    /**
     * 媒体标题
     */
    private String mediaTitle;

    /**
     * 关键词
     */
    private String mediaKeyword;

    /**
     * 归属标签id
     */
    private String belongLabelid;  //可能要多选的，先不考虑

    /**
     * 价格标签id
     */
    private String priceLabelid;

    /**
     * 转载标签id
     */
    private String publishLabelid;

    /**
     * 目录分类id
     */
    private Long catalogueId;

    /**
     * 0上传 1截取
     */
    private String coverOrigin;

}
