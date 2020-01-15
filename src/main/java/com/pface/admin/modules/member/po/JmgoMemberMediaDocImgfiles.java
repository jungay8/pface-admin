package com.pface.admin.modules.member.po;

import javax.persistence.*;
import java.util.Date;

@Table(name = "jmgo_member_media_doc_imgfiles")
public class JmgoMemberMediaDocImgfiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //MemberMediaFile#id  类型为图文时扩展的表，1对多
    private Long docid;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_file_size")
    private Double imageFileSize;


    @Column(name = "image_url")
    private String imageUrl;

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
     * @return docid
     */
    public Long getDocid() {
        return docid;
    }

    /**
     * @param docid
     */
    public JmgoMemberMediaDocImgfiles setDocid(Long docid) {
        this.docid = docid;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(Double imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}