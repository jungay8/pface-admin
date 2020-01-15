package com.pface.admin.modules.front.vo;

import com.pface.admin.common.MultipartFileParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

/**
 * 视频上传属性
 */
@Data
public class VideoMultipartFileParam  {

    private Long mediaId;
    private Long mediaFileId;

    private String mediaType;

    private Long userId;
    /**
     * 目录分类id
     */
    private Long catalogueId;

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

    /**
     * 价格标签id
     */
    private String priceLabelid;

    /**
     * 转载标签id
     */
    private String publishLabelid;

    /**
     * 封面来源：'0上传 1截取',
     */
    private String coverOrigin;

    /**
     * 媒体简介
     */
    private String mediaBrief;

    /**
     * 关联的媒体文件id
     */
    private String[] relationMediaIds;

/////////////////////////////////////

    // 用户id
    private String uid;
    //任务ID
    private String id;
    //总分片数量
    private int chunks;
    //当前为第几块分片
    private int chunk;
    //当前分片大小
    private long size = 0L;
    //文件名
    private String name;
    //分片对象
    private MultipartFile file;
    // MD5
    private String md5;

    //一个文件完成传完的标示，初始-1
    private Boolean uploaded;


//
//    /**
//     * 源文件地址
//     */
//    private String fileUrl;
//
//    /**
//     * 源文件文件名称
//     */
//    private String fileName;
//
//////////////////////////////////////////
//    /**
//     * 文件编码地址,std文件
//     */
//    private String fileEncodeUrl;
//
//    /**
//     * 转码后的std文件名称（标准）
//     */
//    private String fileEncodeName;
//
//    ////////////////////////////////////////
//    /**
//     * 转码后的视频文件，pre文件
//     */
//    private String fileViewUrl;
//
//    /**
//     * 转码后的std文件名称（预览）
//     */
//    private String fileViewName;
//
//    /**
//     * 文件大小
//     */
//    private Double fileSize;
//    private Double fileEncodeSize;
//    private Double fileViewSize;


}
