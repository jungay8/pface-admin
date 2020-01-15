package com.pface.admin.modules.member.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/4
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class AuditLogVo implements Serializable {

    /**
     * 归属用户id
     */
    private Long belongUid;

    /**
     * 审核用户id
     */
    private Long auditUid;

    /**
     * 信息来源关联Id
     */
    private Long msgOriginId;

    /**
     * 审核信息
     */
    private String auditMsg;

    private Long catalogueId;
    private String mediaTitle;
    private String mediaKeyword;
    private String mediaBrief;
    private String priceid;
    private String pubLabel;
    private String belongLabels;
//
//    public Long getBelongUid() {
//        return belongUid;
//    }
//
//    public void setBelongUid(Long belongUid) {
//        this.belongUid = belongUid;
//    }
//
//    public Long getAuditUid() {
//        return auditUid;
//    }
//
//    public void setAuditUid(Long auditUid) {
//        this.auditUid = auditUid;
//    }
//
//    public Long getMsgOriginId() {
//        return msgOriginId;
//    }
//
//    public void setMsgOriginId(Long msgOriginId) {
//        this.msgOriginId = msgOriginId;
//    }
//
//    public String getAuditMsg() {
//        return auditMsg;
//    }
//
//    public void setAuditMsg(String auditMsg) {
//        this.auditMsg = auditMsg;
//    }
}
