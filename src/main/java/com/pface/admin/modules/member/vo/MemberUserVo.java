package com.pface.admin.modules.member.vo;

import com.pface.admin.modules.member.enums.MemberTypeEnum;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.annotation.sql.DataSourceDefinition;
import java.io.Serializable;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/11
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Data
public class MemberUserVo implements Serializable {

    /**
     * 会员名称
     */
    private String uname;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    private String smsCode;


    private MemberTypeEnum memberType;


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public MemberTypeEnum getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberTypeEnum memberType) {
        this.memberType = memberType;
    }
}
