package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "jmgo_member_cert")
public class MemberCert {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员id
     */
    private Long uid;


    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户头像或企业标示
     */
    @Column(name = "icon_url")
    private String iconUrl;

    /**
     * 联系方式，商户客服人员或微信号
     */
    @Column(name = "contact_way1")
    private String contactWay1;

    /**
     * 联系方式，商户客服人员或微信号
     */
    @Column(name = "contact_way2")
    private String contactWay2;

    /**
     * 个人身份证号码
     */
    @Column(name = "person_id")
    private String personId;

    /**
     * 企业名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 身份证正面地址
     */
    @Column(name = "personid_front")
    private String personidFront;

    /**
     * 身份证反面地址
     */
    @Column(name = "personid_back")
    private String personidBack;

    /**
     * 身份证正面地址
     */
    @Column(name = "company_front")
    private String companyFront;

    /**
     * 身份证反面地址
     */
    @Column(name = "company_back")
    private String companyBack;

    /**
     * 企业证件代码
     */
    @Column(name = "company_code")
    private String companyCode;

    /**
     * 公司证件地址
     */
    @Column(name = "company_cert")
    private String companyCert;

    /**
     * 认证日期
     */
    @Column(name = "cert_date")
    private Date certDate;

    /**
     * 认证审核人id
     */
    @Column(name = "cert_uid")
    private Long certUid;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取会员id
     *
     * @return uid - 会员id
     */
    public Long getUid() {
        return uid;
    }

    /**
     * 设置会员id
     *
     * @param uid 会员id
     */
    public MemberCert setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户头像或企业标示
     *
     * @return icon_url - 用户头像或企业标示
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 设置用户头像或企业标示
     *
     * @param iconUrl 用户头像或企业标示
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * 获取联系方式，商户客服人员或微信号
     *
     * @return contact_way1 - 联系方式，商户客服人员或微信号
     */
    public String getContactWay1() {
        return contactWay1;
    }

    /**
     * 设置联系方式，商户客服人员或微信号
     *
     * @param contactWay1 联系方式，商户客服人员或微信号
     */
    public void setContactWay1(String contactWay1) {
        this.contactWay1 = contactWay1;
    }

    /**
     * 获取联系方式，商户客服人员或微信号
     *
     * @return contact_way2 - 联系方式，商户客服人员或微信号
     */
    public String getContactWay2() {
        return contactWay2;
    }

    /**
     * 设置联系方式，商户客服人员或微信号
     *
     * @param contactWay2 联系方式，商户客服人员或微信号
     */
    public void setContactWay2(String contactWay2) {
        this.contactWay2 = contactWay2;
    }

    /**
     * 获取个人身份证号码
     *
     * @return person_id - 个人身份证号码
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * 设置个人身份证号码
     *
     * @param personId 个人身份证号码
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * 获取企业名称
     *
     * @return company_name - 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置企业名称
     *
     * @param companyName 企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取身份证正面地址
     *
     * @return personid_front - 身份证正面地址
     */
    public String getPersonidFront() {
        return personidFront;
    }

    /**
     * 设置身份证正面地址
     *
     * @param personidFront 身份证正面地址
     */
    public void setPersonidFront(String personidFront) {
        this.personidFront = personidFront;
    }

    /**
     * 获取身份证反面地址
     *
     * @return personid_back - 身份证反面地址
     */
    public String getPersonidBack() {
        return personidBack;
    }

    /**
     * 设置身份证反面地址
     *
     * @param personidBack 身份证反面地址
     */
    public void setPersonidBack(String personidBack) {
        this.personidBack = personidBack;
    }

    /**
     * 获取企业证件代码
     *
     * @return company_code - 企业证件代码
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 设置企业证件代码
     *
     * @param companyCode 企业证件代码
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 获取公司证件地址
     *
     * @return company_cert - 公司证件地址
     */
    public String getCompanyCert() {
        return companyCert;
    }

    /**
     * 设置公司证件地址
     *
     * @param companyCert 公司证件地址
     */
    public void setCompanyCert(String companyCert) {
        this.companyCert = companyCert;
    }

    /**
     * 获取认证日期
     *
     * @return cert_date - 认证日期
     */
    public Date getCertDate() {
        return certDate;
    }

    /**
     * 设置认证日期
     *
     * @param certDate 认证日期
     */
    public void setCertDate(Date certDate) {
        this.certDate = certDate;
    }

    /**
     * 获取认证审核人id
     *
     * @return cert_uid - 认证审核人id
     */
    public Long getCertUid() {
        return certUid;
    }

    /**
     * 设置认证审核人id
     *
     * @param certUid 认证审核人id
     */
    public void setCertUid(Long certUid) {
        this.certUid = certUid;
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

    public String getCompanyFront() {
        return companyFront;
    }

    public void setCompanyFront(String companyFront) {
        this.companyFront = companyFront;
    }

    public String getCompanyBack() {
        return companyBack;
    }

    public void setCompanyBack(String companyBack) {
        this.companyBack = companyBack;
    }
}