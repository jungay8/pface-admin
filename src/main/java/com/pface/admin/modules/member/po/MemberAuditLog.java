package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.AuditStatusEnum;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "jmgo_member_audit_log")
public class MemberAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 审核状态 0通过 1不通过
     */
    @Column(name = "audit_status")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private AuditStatusEnum auditStatus;

    /**
     * 归属用户id
     */
    @Column(name = "belong_uid")
    private Long belongUid;

    /**
     * 审核用户id
     */
    @Column(name = "audit_uid")
    private Long auditUid;

    /**
     * 信息来源关联Id
     */
    @Column(name = "msg_origin_id")
    private Long msgOriginId;

    /**
     * 审核日期
     */
    @Column(name = "audit_date")
    private Date auditDate;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    /**
     * 审核信息
     */
    @Column(name = "audit_msg")
    private String auditMsg;



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
     * 获取审核状态 0通过 1不通过
     *
     * @return audit_status - 审核状态 0通过 1不通过
     */
    public AuditStatusEnum getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置审核状态 0通过 1不通过
     *
     * @param auditStatus 审核状态 0通过 1不通过
     */
    public void setAuditStatus(AuditStatusEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取归属用户id
     *
     * @return belong_uid - 归属用户id
     */
    public Long getBelongUid() {
        return belongUid;
    }

    /**
     * 设置归属用户id
     *
     * @param belongUid 归属用户id
     */
    public void setBelongUid(Long belongUid) {
        this.belongUid = belongUid;
    }

    /**
     * 获取审核用户id
     *
     * @return audit_uid - 审核用户id
     */
    public Long getAuditUid() {
        return auditUid;
    }

    /**
     * 设置审核用户id
     *
     * @param auditUid 审核用户id
     */
    public void setAuditUid(Long auditUid) {
        this.auditUid = auditUid;
    }

    /**
     * 获取信息来源关联Id
     *
     * @return msg_origin_id - 信息来源关联Id
     */
    public Long getMsgOriginId() {
        return msgOriginId;
    }

    /**
     * 设置信息来源关联Id
     *
     * @param msgOriginId 信息来源关联Id
     */
    public void setMsgOriginId(Long msgOriginId) {
        this.msgOriginId = msgOriginId;
    }

    /**
     * 获取审核日期
     *
     * @return audit_date - 审核日期
     */
    public Date getAuditDate() {
        return auditDate;
    }

    /**
     * 设置审核日期
     *
     * @param auditDate 审核日期
     */
    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
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
     * 获取审核信息
     *
     * @return audit_msg - 审核信息
     */
    public String getAuditMsg() {
        return auditMsg;
    }

    /**
     * 设置审核信息
     *
     * @param auditMsg 审核信息
     */
    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
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