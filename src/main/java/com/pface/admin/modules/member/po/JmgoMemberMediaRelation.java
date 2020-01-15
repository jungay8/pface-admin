package com.pface.admin.modules.member.po;

import javax.persistence.*;

@Table(name = "jmgo_member_media_relation")
public class JmgoMemberMediaRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 被关联的媒体id
     */
    @Column(name = "media_file_id")
    private Integer mediaFileId;

    /**
     * 关联的媒体id
     */
    @Column(name = "relation_media_file_id")
    private Integer relationMediaFileId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取被关联的媒体id
     *
     * @return media_file_id - 被关联的媒体id
     */
    public Integer getMediaFileId() {
        return mediaFileId;
    }

    /**
     * 设置被关联的媒体id
     *
     * @param mediaFileId 被关联的媒体id
     */
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }

    /**
     * 获取关联的媒体id
     *
     * @return relation_media_file_id - 关联的媒体id
     */
    public Integer getRelationMediaFileId() {
        return relationMediaFileId;
    }

    /**
     * 设置关联的媒体id
     *
     * @param relationMediaFileId 关联的媒体id
     */
    public void setRelationMediaFileId(Integer relationMediaFileId) {
        this.relationMediaFileId = relationMediaFileId;
    }
}