package com.pface.admin.modules.front.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class MemberMediaPojo {

    private Long id;

    /**
     * 归属用户id
     */
    private Long uid;

    private String uname;

    /**
     * 媒体资源类型
     */
    private String mediaType;


    /**
     * 目录分类id,使用jmgo_member_media.catalogue_id
     */
//    private Long catalogueId;

    /**
     * 媒体是否有基本信息 0没有，1有
     */
    private String isMediaInfo;

    /**
     * 媒体是否发布 0没 1发布 用goods_status代替,所以这里注释
     */
//    private String isPublishMedia;

    /**
     * 媒体状态 0审核中，1审核没通过
     */
    private String mediaStatus;

    /**
     * 文件商品状态：on上架 off下架
     */
    private String goodsStatus;

    /**
     * 文件源地址
     */
    private String fileUrl;

    /**
     * 文件编码地址
     */
    private String fileEncodeUrl;

    /**
     * 文件预览地址
     */
    private String fileViewUrl;

    /**
     * 原始文件名称
     */
    private String fileName;

    /**
     * 上传后的文件名称
     */
    private String fileNewName;

    /**
     * 文件名称
     */
    private String fileEncodeName;

    /**
     * 文件名称
     */
    private String fileViewName;

    /**
     * 封面文件名称，仅用于转码截图修正转码接口的文件名称，在定时器中实现
     */
    private String fileCoverName;

    /**
     * 文件大小
     */
    private Double fileSize;

    private Double fileViewSize;

    private Double fileEncodeSize;

    /**
     * 转码标识：转码标识：0：初始；1：完成；2:错误；3：不需要转码
     */
    private Integer changcodeflag;

    /**
     * 上传日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date uploadDate;

    /**
     * 转码任务id
     */
    private String changcodetaskid;
    private String fileMd5;
    private String fileExt;
    private Long fileDuration;
    private String fileBitrate;
    private String fileCodec;
    private String fileResolution;
    private String fileAspectRation;

    /**
     * 操作修改日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date opDate;

    private String delFlag;

    //////////////////////////////////////////////jmgo_member_media/////////////////////////

    /**
     * 对应：jmgo_member_media.id
     */
    private Long mediaId;

    /**
     * jmgo_member_media_file.id
     */
    private Long mediaFileId;

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
    private String belongLabelid;
    private String belongLabelNames;


    /**
     * 价格标签id
     */
    private String priceLabelid;
    private String priceLabelName;
    private double price;

    /**
     * 转载标签id
     */
    private String publishLabelid;
    private String publishLabelName;

    /**
     * 目录分类id
     */
    private Long catalogueId;
    private String catalogueName;

    /**
     * 封面地址
     * 如 ： D:\\data0\\uploads\\1\audio\\timg561964641659036298.jpg
     */
    private String coverUrl;

    private String uploadCoverUrl;
    /**
     * 暂时没什么用20190708
     */
    private String coverSourceUrl;

    /**
     * 0上传 1截取
     */
    private String coverOrigin;

    /**
     * 内容最近的审核说明
     */
    private String lastedAuditMsg;

    /**
     * 图文的图片ids，以，分割
     */
    private String imgfileIds;
    /**
     * 图文的图片image_path,以，分割
     */
    private String imgfilePaths;

    /**
     *  归属用户id,使用jmgo_member_media_file.uid
     */
//    private Long uid;


    /**
     * 操作修改日期,使用jmgo_member_media_file.op_date
     */
//    private Date opDate;

    /**
     * 媒体简介
     */
    private String mediaBrief;

    /**多图文,多张以逗号分隔*/
//    private String imagePath;

}
