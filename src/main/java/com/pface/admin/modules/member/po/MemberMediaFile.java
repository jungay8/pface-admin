package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "jmgo_member_media_file")
public class MemberMediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 归属用户id
     */
    private Long uid;


    /**
     * 媒体资源类型
     */
    @Column(name = "media_type")
    @NotNull(message = "媒体类型不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = MediaTypeHandler.class)
    private MediaTypeEnum mediaType;


    /**
     * 目录分类id
     */
    @Column(name = "catalogue_id")
    private Long catalogueId;

    /**
     * 媒体是否有基本信息 0没有，1有
     */
    @Column(name = "is_media_info")
    @NotNull(message = "媒体是否有基本信息不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = MediaStatusHandler.class)
    private MediaStatusEnum isMediaInfo;

    /**
     * 媒体是否发布 0没 1发布
     */
    @Column(name = "is_publish_media")
    @NotNull(message = "媒体是否发布不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = MediaStatusHandler.class)
    private MediaStatusEnum isPublishMedia;

    /**
     * 媒体状态 0审核中，1审核没通过
     */
    @Column(name = "media_status")
    @NotNull(message = "审核状态不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = AuditStatusHandler.class)
    private AuditStatusEnum mediaStatus;



    @Column(name = "goods_status")
    @NotNull(message = "商品状态不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = GoodsStatusHandler.class)
    private GoodsStatusEnum goodsStatus;


    /**
     * 文件源地址
     */
    @Column(name = "file_url")
    private String fileUrl;

    /**
     * 文件编码地址
     */
    @Column(name = "file_encode_url")
    private String fileEncodeUrl;

    /**
     * 文件预览地址
     */
    @Column(name = "file_view_url")
    private String fileViewUrl;

    /**
     * 原始文件名称
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 上传后的文件名称
     */
    @Column(name = "file_new_name")
    private String fileNewName;

    /**
     * 文件名称
     */
    @Column(name = "file_encode_name")
    private String fileEncodeName;

    /**
     * 文件名称
     */
    @Column(name = "file_view_name")
    private String fileViewName;

    @Column(name = "file_cover_name")
    private String fileCoverName;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private Double fileSize;

    @Column(name = "file_view_size")
    private Double fileViewSize;

    @Column(name = "file_encode_size")
    private Double fileEncodeSize;

    /**
     * 转码标识：：初始；1：完成；2:错误；3：不需要转码
     */
    @Column(name = "changcodeflag")
    private Integer changcodeflag;

    /**
     * 上传日期
     */
    @Column(name = "upload_date")
    private Date uploadDate;

    /**
     * 转码返回
     */
    @Column(name = "changcodeResult")
    private String changcodeResult;

    /**
     * 转码任务id
     */
    @Column(name = "changcodetaskid")
    private String changcodetaskid;

    @Column(name = "file_md5")
    private String fileMd5;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_duration")
    private Long fileDuration;

    @Column(name = "file_bitrate")
    private String fileBitrate;

    @Column(name = "file_codec")
    private String fileCodec;

    @Column(name = "file_resolution")
    private String fileResolution;

    @Column(name = "file_aspect_ration")
    private String fileAspectRation;



    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    @Column(name = "del_flag")
    private Integer delFlag;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取归属用户id
     *
     * @return uid - 归属用户id
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置归属用户id
     *
     * @param uid 归属用户id
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }



    /**
     * 获取媒体资源类型
     *
     * @return media_type - 媒体资源类型
     */
    public MediaTypeEnum getMediaType() {
        return mediaType;
    }

    /**
     * 设置媒体资源类型
     *
     * @param mediaType 媒体资源类型
     */
    public void setMediaType(MediaTypeEnum mediaType) {
        this.mediaType = mediaType;
    }


    public GoodsStatusEnum getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(GoodsStatusEnum goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    /**
     * 获取文件源地址
     *
     * @return file_url - 文件源地址
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置文件源地址
     *
     * @param fileUrl 文件源地址
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取文件编码地址
     *
     * @return file_encode_url - 文件编码地址
     */
    public String getFileEncodeUrl() {
        return fileEncodeUrl;
    }

    /**
     * 设置文件编码地址
     *
     * @param fileEncodeUrl 文件编码地址
     */
    public void setFileEncodeUrl(String fileEncodeUrl) {
        this.fileEncodeUrl = fileEncodeUrl;
    }

    /**
     * 获取文件预览地址
     *
     * @return file_view_url - 文件预览地址
     */
    public String getFileViewUrl() {
        return fileViewUrl;
    }

    /**
     * 设置文件预览地址
     *
     * @param fileViewUrl 文件预览地址
     */
    public void setFileViewUrl(String fileViewUrl) {
        this.fileViewUrl = fileViewUrl;
    }

    /**
     * 获取文件名称
     *
     * @return file_name - 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置文件名称
     *
     * @param fileName 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件大小
     *
     * @return file_size - 文件大小
     */
    public Double getFileSize() {
        return fileSize;
    }

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小
     */
    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }


    public String getFileEncodeName() {
        return fileEncodeName;
    }

    public void setFileEncodeName(String fileEncodeName) {
        this.fileEncodeName = fileEncodeName;
    }

    public String getFileViewName() {
        return fileViewName;
    }

    public void setFileViewName(String fileViewName) {
        this.fileViewName = fileViewName;
    }

    public Double getFileViewSize() {
        return fileViewSize;
    }

    public void setFileViewSize(Double fileViewSize) {
        this.fileViewSize = fileViewSize;
    }

    public Double getFileEncodeSize() {
        return fileEncodeSize;
    }

    public void setFileEncodeSize(Double fileEncodeSize) {
        this.fileEncodeSize = fileEncodeSize;
    }


    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    /**
     * 获取上传日期
     *
     * @return upload_date - 上传日期
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * 设置上传日期
     *
     * @param uploadDate 上传日期
     */
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * 获取操作修改日期
     *
     * @return op_date - 操作修改日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getOpDate() {
        return opDate;
    }

    /**
     * 设置操作修改日期
     *
     * @param opDate 操作修改日期
     */
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    /**
     * 获取媒体是否有基本信息 0没有，1有
     *
     * @return is_media_info - 媒体是否有基本信息 0没有，1有
     */
    public MediaStatusEnum getIsMediaInfo() {
        return isMediaInfo;
    }

    /**
     * 设置媒体是否有基本信息 0没有，1有
     *
     * @param isMediaInfo 媒体是否有基本信息 0没有，1有
     */
    public void setIsMediaInfo(MediaStatusEnum isMediaInfo) {
        this.isMediaInfo = isMediaInfo;
    }

    /**
     * 获取媒体是否发布 0没 1发布
     *
     * @return is_publish_media - 媒体是否发布 0没 1发布
     */
    public MediaStatusEnum getIsPublishMedia() {
        return isPublishMedia;
    }

    public Integer getChangcodeflag() {
        return changcodeflag;
    }

    public void setChangcodeflag(Integer changcodeflag) {
        this.changcodeflag = changcodeflag;
    }

    /**
     * 设置媒体是否发布 0没 1发布
     *
     * @param isPublishMedia 媒体是否发布 0没 1发布
     */
    public void setIsPublishMedia(MediaStatusEnum isPublishMedia) {
        this.isPublishMedia = isPublishMedia;
    }

    /**
     * 获取媒体状态 0审核中，1审核没通过
     *
     * @return media_status - 媒体状态 0审核中，1审核没通过
     */
    public AuditStatusEnum getMediaStatus() {
        return mediaStatus;
    }

    public String getChangcodeResult() {
        return changcodeResult;
    }

    public void setChangcodeResult(String changcodeResult) {
        this.changcodeResult = changcodeResult;
    }

    public String getChangcodetaskid() {
        return changcodetaskid;
    }

    public void setChangcodetaskid(String changcodetaskid) {
        this.changcodetaskid = changcodetaskid;
    }

    /**
     * 设置媒体状态 0审核中，1审核没通过
     *
     * @param mediaStatus 媒体状态 0审核中，1审核没通过
     */
    public void setMediaStatus(AuditStatusEnum mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Long getCatalogueId() {
        return catalogueId;
    }

    public void setCatalogueId(Long catalogueId) {
        this.catalogueId = catalogueId;
    }

    public String getFileNewName() {
        return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public Long getFileDuration() {
        return fileDuration;
    }

    public void setFileDuration(Long fileDuration) {
        this.fileDuration = fileDuration;
    }

    public String getFileBitrate() {
        return fileBitrate;
    }

    public void setFileBitrate(String fileBitrate) {
        this.fileBitrate = fileBitrate;
    }

    public String getFileCodec() {
        return fileCodec;
    }

    public void setFileCodec(String fileCodec) {
        this.fileCodec = fileCodec;
    }

    public String getFileResolution() {
        return fileResolution;
    }

    public void setFileResolution(String fileResolution) {
        this.fileResolution = fileResolution;
    }

    public String getFileAspectRation() {
        return fileAspectRation;
    }

    public String getFileCoverName() {
        return fileCoverName;
    }

    public void setFileCoverName(String fileCoverName) {
        this.fileCoverName = fileCoverName;
    }

    public void setFileAspectRation(String fileAspectRation) {
        this.fileAspectRation = fileAspectRation;
    }
}