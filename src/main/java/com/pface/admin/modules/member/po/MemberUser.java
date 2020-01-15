package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.enums.MemberTypeEnum;
import com.pface.admin.modules.member.enums.MemberTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "jmgo_member_user")
public class MemberUser {

    public static final String USER_SESSION_KEY="usersessionkey";
    /**
     * 会员id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 会员名称
     */
    private String uname;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    @Column(name = "head_url")
    private String headUrl;

    /**
     * 是否认证 0未认证 1已认证
     */
    @Column(name = "is_cert")
    @NotNull(message = "认证状态不能为空")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private CertTypeEnum isCert;


    /**
     * 会员类型 0个人，1企业
     */
    @Column(name = "member_type")
    @NotNull(message = "会员类型不能为空")
    @ColumnType(typeHandler = MemberTypeHandler.class)
    //@ColumnType(jdbcType = JdbcType.VARCHAR)
    private MemberTypeEnum memberType;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    /**
     * 获取会员id
     *
     * @return id - 会员id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置会员id
     *
     * @param id 会员id
     */
    public MemberUser setId(Long id) {
        this.id = id;
        return  this;
    }

    /**
     * 获取会员名称
     *
     * @return uname - 会员名称
     */
    public String getUname() {
        return uname;
    }

    /**
     * 设置会员名称
     *
     * @param uname 会员名称
     */
    public MemberUser setUname(String uname) {
        this.uname = uname;
        return this;
    }

    /**
     * 获取密码
     *
     * @return pwd - 密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置密码
     *
     * @param pwd 密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 获取加密盐
     *
     * @return salt - 加密盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置加密盐
     *
     * @param salt 加密盐
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCredentialsSalt() {
        return uname + salt;
    }


    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }


    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    /**
     * 获取是否认证 0未认证 1已认证
     *
     * @return is_cert - 是否认证 0未认证 1已认证
     */
    public CertTypeEnum getIsCert() {
        return isCert;
    }

    /**
     * 设置是否认证 0未认证 1已认证
     *
     * @param isCert 是否认证 0未认证 1已认证
     */
    public MemberUser setIsCert(CertTypeEnum isCert) {
        this.isCert = isCert;
        return this;
    }



    /**
     * 获取会员类型 0个人，1企业
     *
     * @return member_type - 会员类型 0个人，1企业
     */
    public MemberTypeEnum getMemberType() {
        return memberType;
    }

    /**
     * 设置会员类型 0个人，1企业
     *
     * @param memberType 会员类型 0个人，1企业
     */
    public MemberUser setMemberType(MemberTypeEnum memberType) {
        this.memberType = memberType;
        return this;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MemberUser{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", pwd='" + pwd + '\'' +
                ", salt='" + salt + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", isCert=" + isCert +
                ", memberType=" + memberType +
                ", opDate=" + opDate +
                '}';
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