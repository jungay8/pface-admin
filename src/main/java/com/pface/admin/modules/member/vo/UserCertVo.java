package com.pface.admin.modules.member.vo;

import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;

import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/4
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class UserCertVo  implements Serializable {

    private MemberUser memberUser;

    private MemberCert memberCert;

    /**
     * 内容最近的审核说明
     */
    private String lastedAuditMsg;

    public MemberUser getMemberUser() {
        return memberUser;
    }

    public void setMemberUser(MemberUser memberUser) {
        this.memberUser = memberUser;
    }

    public MemberCert getMemberCert() {
        return memberCert;
    }

    public void setMemberCert(MemberCert memberCert) {
        this.memberCert = memberCert;
    }

    public String getLastedAuditMsg() {
        return lastedAuditMsg;
    }

    public void setLastedAuditMsg(String lastedAuditMsg) {
        this.lastedAuditMsg = lastedAuditMsg;
    }
}
