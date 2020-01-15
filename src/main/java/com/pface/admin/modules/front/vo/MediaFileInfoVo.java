package com.pface.admin.modules.front.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.GoodsStatusEnum;
import com.pface.admin.modules.member.enums.MediaStatusEnum;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberMediaFile;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;


public class MediaFileInfoVo implements Serializable {


    private Long id;

    /**
     * 归属用户id
     */
    private Long uid;


    private MediaVo memberMedia;

    private MemberCert memberCert;

    /**
     * 媒体资源类型
     */

    private MediaTypeEnum mediaType;


    /**
     * 媒体是否有基本信息 0没有，1有
     */

    private MediaStatusEnum isMediaInfo;

    /**
     * 媒体是否发布 0没 1发布
     */

    private MediaStatusEnum isPublishMedia;

    /**
     * 媒体状态 0审核中，1审核没通过
     */

    private AuditStatusEnum mediaStatus;




    private GoodsStatusEnum goodsStatus;


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
     * 文件名称
     */

    private String fileName;

    /**
     * 文件大小
     */

    private Double fileSize;

    /**
     * 上传日期
     */

    private Date uploadDate;

    private Date startDate;

    private Date endDate;

    /**
     * 操作修改日期
     */

    private Date opDate;

    public MediaFileInfoVo(MemberMediaFile file) {
        this.id = file.getId();
        this.fileName = file.getFileName();
        this.fileSize = file.getFileSize();
        this.fileUrl = file.getFileUrl();
        this.fileEncodeUrl =file.getFileEncodeUrl();
        this.fileViewUrl = file.getFileViewUrl();
        this.uploadDate =file.getUploadDate();
        this.opDate = file.getOpDate();
        this.goodsStatus=file.getGoodsStatus();
        this.isMediaInfo=file.getIsMediaInfo();
        this.mediaStatus=file.getMediaStatus();
        this.isPublishMedia=file.getIsPublishMedia();
        this.mediaType=file.getMediaType();
    }

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

    /**
     * 获取上传日期
     *
     * @return upload_date - 上传日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    /**
     * 设置媒体状态 0审核中，1审核没通过
     *
     * @param mediaStatus 媒体状态 0审核中，1审核没通过
     */
    public void setMediaStatus(AuditStatusEnum mediaStatus) {
        this.mediaStatus = mediaStatus;
    }

    public MediaVo getMemberMedia() {
        return memberMedia;
    }

    public void setMemberMedia(MediaVo memberMedia) {
        this.memberMedia = memberMedia;
    }

    public MemberCert getMemberCert() {
        return memberCert;
    }

    public void setMemberCert(MemberCert memberCert) {
        this.memberCert = memberCert;
    }
}