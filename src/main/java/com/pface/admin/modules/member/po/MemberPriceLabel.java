package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import com.pface.admin.modules.member.enums.AuditStatusHandler;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "jmgo_member_price_label")
public class MemberPriceLabel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标签名称
     */
    private String label;

    private String labelDesc;

    /**
     * 价格
     */
    private Double price;

    /**
     * 标签归属用户id
     */
    private Long uid;

    private String uname;

    /**
     * 审核人id
     */
    @Column(name = "audit_uid")
    private Long auditUid;

    /**
     * 标签状态 0审核中，1审核通过 2.审核不通过
     */
    @Column(name = "label_status")
    //@NotNull(message = "标签状态不能为空")
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    @ColumnType(typeHandler = AuditStatusHandler.class)
    private AuditStatusEnum labelStatus;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;




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
     * 获取标签名称
     *
     * @return label - 标签名称
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置标签名称
     *
     * @param label 标签名称
     */
    public void setLabel(String label) {
        this.label = label;
    }


    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取标签归属用户id
     *
     * @return uid - 标签归属用户id
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置标签归属用户id
     *
     * @param uid 标签归属用户id
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    /**
     * 获取审核人id
     *
     * @return audit_uid - 审核人id
     */
    public Long getAuditUid() {
        return auditUid;
    }

    /**
     * 设置审核人id
     *
     * @param auditUid 审核人id
     */
    public void setAuditUid(Long auditUid) {
        this.auditUid = auditUid;
    }

    /**
     * 获取标签状态 0审核中，1审核通过 2.审核不通过
     *
     * @return label_status - 标签状态 0审核中，1审核通过 2.审核不通过
     */
    public AuditStatusEnum getLabelStatus() {
        return labelStatus;
    }

    /**
     * 设置标签状态 0审核中，1审核通过 2.审核不通过
     *
     * @param labelStatus 标签状态 0审核中，1审核通过 2.审核不通过
     */
    public void setLabelStatus(AuditStatusEnum labelStatus) {
        this.labelStatus = labelStatus;
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

    @Column(name = "del_flag")
    private Integer delFlag;

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}