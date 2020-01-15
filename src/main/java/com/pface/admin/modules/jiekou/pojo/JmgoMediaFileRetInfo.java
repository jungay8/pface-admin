package com.pface.admin.modules.jiekou.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文件地址信息")
public class JmgoMediaFileRetInfo {

    @ApiModelProperty("内容id")
    private Long contentId;  //jmgo_member_media_file.id

    @ApiModelProperty("时长，对“视频”有意义")
    private Long fileDuration;

    @ApiModelProperty("文件大小")
    private Double fileSize;

    @ApiModelProperty("文件源地址")
    private String fileUrl;

    @ApiModelProperty("文件预览地址")
    private String fileViewUrl;

    @ApiModelProperty("媒体标题")
    private String mediaTitle;

    @ApiModelProperty("媒体简介")
    private String mediaBrief;

    @ApiModelProperty("分类名称")
    private String catalogueName;

    @ApiModelProperty("媒体类型名称：如：视频、音频、图文")
    private String mediaTypeText;

    @ApiModelProperty("关键词")
    private String mediaKeyword;

    @ApiModelProperty("归属标签名称")
    private String belongLabel;

    @ApiModelProperty("转载,为空：原创")
    private String publishLabel;

    @ApiModelProperty("图文类型的图片列表，mediaTypeText 为 图文 时有意义")
    private JmgoMemberMediaDocImgfilesPartRet docImgfiles;

    @ApiModelProperty("关联文件")
    private JmgoMemberMediaRelationPartRet relation;


}
