package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.*;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "jmgo_member_media")
public class MemberMedia {

    /**
     * 资源id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "media_file_id")
    private Long mediaFileId;

    /**
     * 媒体标题
     */
    @Column(name = "media_title")
    private String mediaTitle;

    /**
     * 关键词
     */
    @Column(name = "media_keyword")
    private String mediaKeyword;

    /**
     * 归属标签id
     */
    @Column(name = "belong_labelid")
    private String belongLabelid;

    /**
     * 价格标签id
     */
    @Column(name = "price_labelid")
    private String priceLabelid;

    /**
     * 转载标签id
     */
    @Column(name = "publish_labelid")
    private String publishLabelid;

    /**
     * 目录分类id
     */
    @Column(name = "catalogue_id")
    private Long catalogueId;

    /**
     * 封面地址
     * 如 ： D:\\data0\\uploads\\1\audio\\timg561964641659036298.jpg
     */
    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "upload_cover_url")
    private String uploadCoverUrl;

    @Column(name = "file_cover_md5")
    private String fileCoverMd5;

    @Column(name = "file_cover_raw_name")
    private String fileCoverRawName;


    @Column(name = "cover_source_url")
    private String coverSourceUrl;


    /**
     * 0上传 1截取
     */
    @Column(name = "cover_origin")
    @NotNull(message = "封面类型不能为空")
   // @ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = CoverOriginHandler.class)
    private CoverOriginEnum coverOrigin;


    /**
     *  归属用户id
     */
    private Long uid;


    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    /**
     * 媒体简介
     */
    @Column(name = "media_brief")
    private String mediaBrief;

    /**
     * 获取资源id
     *
     * @return id - 资源id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置资源id
     *
     * @param id 资源id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取媒体标题
     *
     * @return media_title - 媒体标题
     */
    public String getMediaTitle() {
        return mediaTitle;
    }

    /**
     * 设置媒体标题
     *
     * @param mediaTitle 媒体标题
     */
    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    /**
     * 获取关键词
     *
     * @return media_keyword - 关键词
     */
    public String getMediaKeyword() {
        return mediaKeyword;
    }

    /**
     * 设置关键词
     *
     * @param mediaKeyword 关键词
     */
    public void setMediaKeyword(String mediaKeyword) {
        this.mediaKeyword = mediaKeyword;
    }

    /**
     * 获取归属标签id
     *
     * @return belong_labelid - 归属标签id
     */
    public String getBelongLabelid() {
        return belongLabelid;
    }

    /**
     * 设置归属标签id
     *
     * @param belongLabelid 归属标签id
     */
    public void setBelongLabelid(String belongLabelid) {
        this.belongLabelid = belongLabelid;
    }

    /**
     * 获取价格标签id
     *
     * @return price_labelid - 价格标签id
     */
    public String getPriceLabelid() {
        return priceLabelid;
    }

    /**
     * 设置价格标签id
     *
     * @param priceLabelid 价格标签id
     */
    public void setPriceLabelid(String priceLabelid) {
        this.priceLabelid = priceLabelid;
    }

    /**
     * 获取转载标签id
     *
     * @return publish_labelid - 转载标签id
     */
    public String getPublishLabelid() {
        return publishLabelid;
    }

    /**
     * 设置转载标签id
     *
     * @param publishLabelid 转载标签id
     */
    public void setPublishLabelid(String publishLabelid) {
        this.publishLabelid = publishLabelid;
    }

    /**
     * 获取目录分类id
     *
     * @return catalogue_id - 目录分类id
     */
    public Long getCatalogueId() {
        return catalogueId;
    }

    /**
     * 设置目录分类id
     *
     * @param catalogueId 目录分类id
     */
    public void setCatalogueId(Long catalogueId) {
        this.catalogueId = catalogueId;
    }

    /**
     * 获取封面地址
     *
     * @return cover_url - 封面地址
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * 设置封面地址
     *
     * @param coverUrl 封面地址
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getUploadCoverUrl() {
        return uploadCoverUrl;
    }

    public void setUploadCoverUrl(String uploadCoverUrl) {
        this.uploadCoverUrl = uploadCoverUrl;
    }

    /**
     * 获取0上传 1截取
     *
     * @return cover_origin - 0上传 1截取
     */
    public CoverOriginEnum getCoverOrigin() {
        return coverOrigin;
    }

    /**
     * 设置0上传 1截取
     *
     * @param coverOrigin 0上传 1截取
     */
    public void setCoverOrigin(CoverOriginEnum coverOrigin) {
        this.coverOrigin = coverOrigin;
    }



    /**
     * 获取 归属用户id
     *
     * @return uid -  归属用户id
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置 归属用户id
     *
     * @param uid  归属用户id
     */
    public void setUid(Long uid) {
        this.uid = uid;
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
     * 获取媒体简介
     *
     * @return media_brief - 媒体简介
     */
    public String getMediaBrief() {
        return mediaBrief;
    }

    /**
     * 设置媒体简介
     *
     * @param mediaBrief 媒体简介
     */
    public void setMediaBrief(String mediaBrief) {
        this.mediaBrief = mediaBrief;
    }

    public Long getMediaFileId() {
        return mediaFileId;
    }

    public MemberMedia setMediaFileId(Long mediaFileId) {
        this.mediaFileId = mediaFileId;
        return this;
    }

    @Column(name = "del_flag")
    private Integer delFlag;

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getCoverSourceUrl() {
        return coverSourceUrl;
    }


    public void setCoverSourceUrl(String coverSourceUrl) {
        this.coverSourceUrl = coverSourceUrl;
    }

    public String getFileCoverRawName() {
        return fileCoverRawName;
    }

    public String getFileCoverMd5() {
        return fileCoverMd5;
    }

    public void setFileCoverMd5(String fileCoverMd5) {
        this.fileCoverMd5 = fileCoverMd5;
    }

    public void setFileCoverRawName(String fileCoverRawName) {
        this.fileCoverRawName = fileCoverRawName;
    }
}