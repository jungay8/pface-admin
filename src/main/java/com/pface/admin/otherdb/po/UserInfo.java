package com.pface.admin.otherdb.po;

import org.apache.ibatis.annotations.SelectKey;

import java.util.Date;
import javax.persistence.*;

@Table(name = "dbo.USER_INFO")
public class UserInfo {
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "USER_LOGIN")
    private String userLogin;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_MAIL")
    private String userMail;

    @Column(name = "USER_PHONE")
    private String userPhone;

    @Column(name = "USER_COMPANY")
    private String userCompany;

    @Column(name = "USER_ADDRESS")
    private String userAddress;

    @Column(name = "USER_CREATETIME")
    private Date userCreatetime;

    @Column(name = "USER_PONITS")
    private Integer userPonits;

    @Column(name = "USER_LOGO")
    private String userLogo;

    @Column(name = "USER_STATUS")
    private Boolean userStatus;

    @Column(name = "USER_TYPE")
    private Integer userType;

    @Column(name = "USER_QQ")
    private String userQq;

    @Column(name = "USER_JOB")
    private String userJob;

    @Column(name = "USER_HANDPHONE")
    private String userHandphone;

    @Column(name = "USER_TYPE_MEMO")
    private String userTypeMemo;

    @Column(name = "USER_CONTRACT")
    private String userContract;

    @Column(name = "USER_LIMIT_LVL")
    private Integer userLimitLvl;

    @Column(name = "USER_LIMIT_PROVINCE")
    private String userLimitProvince;

    @Column(name = "USER_LIMIT_CITY")
    private String userLimitCity;

    @Column(name = "USER_LIMIT_COUNTRY")
    private String userLimitCountry;

    @Column(name = "USER_BIZ_TYPE")
    private String userBizType;

    @Column(name = "USER_TIME_VALUE")
    private Integer userTimeValue;

    @Column(name = "USER_MTR_BIZ_TYPE")
    private Integer userMtrBizType;

    @Column(name = "USER_TVMID")
    private String userTvmid;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "CITY_ID")
    private Integer cityId;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    /**
     * @return USER_ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return USER_LOGIN
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param userLogin
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return USER_PASSWORD
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**
     * @return USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return USER_MAIL
     */
    public String getUserMail() {
        return userMail;
    }

    /**
     * @param userMail
     */
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    /**
     * @return USER_PHONE
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * @param userPhone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * @return USER_COMPANY
     */
    public String getUserCompany() {
        return userCompany;
    }

    /**
     * @param userCompany
     */
    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    /**
     * @return USER_ADDRESS
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * @param userAddress
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * @return USER_CREATETIME
     */
    public Date getUserCreatetime() {
        return userCreatetime;
    }

    /**
     * @param userCreatetime
     */
    public void setUserCreatetime(Date userCreatetime) {
        this.userCreatetime = userCreatetime;
    }

    /**
     * @return USER_PONITS
     */
    public Integer getUserPonits() {
        return userPonits;
    }

    /**
     * @param userPonits
     */
    public void setUserPonits(Integer userPonits) {
        this.userPonits = userPonits;
    }

    /**
     * @return USER_LOGO
     */
    public String getUserLogo() {
        return userLogo;
    }

    /**
     * @param userLogo
     */
    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    /**
     * @return USER_STATUS
     */
    public Boolean getUserStatus() {
        return userStatus;
    }

    /**
     * @param userStatus
     */
    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return USER_TYPE
     */
    public Integer getUserType() {
        return userType;
    }

    /**
     * @param userType
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    /**
     * @return USER_QQ
     */
    public String getUserQq() {
        return userQq;
    }

    /**
     * @param userQq
     */
    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    /**
     * @return USER_JOB
     */
    public String getUserJob() {
        return userJob;
    }

    /**
     * @param userJob
     */
    public void setUserJob(String userJob) {
        this.userJob = userJob;
    }

    /**
     * @return USER_HANDPHONE
     */
    public String getUserHandphone() {
        return userHandphone;
    }

    /**
     * @param userHandphone
     */
    public void setUserHandphone(String userHandphone) {
        this.userHandphone = userHandphone;
    }

    /**
     * @return USER_TYPE_MEMO
     */
    public String getUserTypeMemo() {
        return userTypeMemo;
    }

    /**
     * @param userTypeMemo
     */
    public void setUserTypeMemo(String userTypeMemo) {
        this.userTypeMemo = userTypeMemo;
    }

    /**
     * @return USER_CONTRACT
     */
    public String getUserContract() {
        return userContract;
    }

    /**
     * @param userContract
     */
    public void setUserContract(String userContract) {
        this.userContract = userContract;
    }

    /**
     * @return USER_LIMIT_LVL
     */
    public Integer getUserLimitLvl() {
        return userLimitLvl;
    }

    /**
     * @param userLimitLvl
     */
    public void setUserLimitLvl(Integer userLimitLvl) {
        this.userLimitLvl = userLimitLvl;
    }

    /**
     * @return USER_LIMIT_PROVINCE
     */
    public String getUserLimitProvince() {
        return userLimitProvince;
    }

    /**
     * @param userLimitProvince
     */
    public void setUserLimitProvince(String userLimitProvince) {
        this.userLimitProvince = userLimitProvince;
    }

    /**
     * @return USER_LIMIT_CITY
     */
    public String getUserLimitCity() {
        return userLimitCity;
    }

    /**
     * @param userLimitCity
     */
    public void setUserLimitCity(String userLimitCity) {
        this.userLimitCity = userLimitCity;
    }

    /**
     * @return USER_LIMIT_COUNTRY
     */
    public String getUserLimitCountry() {
        return userLimitCountry;
    }

    /**
     * @param userLimitCountry
     */
    public void setUserLimitCountry(String userLimitCountry) {
        this.userLimitCountry = userLimitCountry;
    }

    /**
     * @return USER_BIZ_TYPE
     */
    public String getUserBizType() {
        return userBizType;
    }

    /**
     * @param userBizType
     */
    public void setUserBizType(String userBizType) {
        this.userBizType = userBizType;
    }

    /**
     * @return USER_TIME_VALUE
     */
    public Integer getUserTimeValue() {
        return userTimeValue;
    }

    /**
     * @param userTimeValue
     */
    public void setUserTimeValue(Integer userTimeValue) {
        this.userTimeValue = userTimeValue;
    }

    /**
     * @return USER_MTR_BIZ_TYPE
     */
    public Integer getUserMtrBizType() {
        return userMtrBizType;
    }

    /**
     * @param userMtrBizType
     */
    public void setUserMtrBizType(Integer userMtrBizType) {
        this.userMtrBizType = userMtrBizType;
    }

    /**
     * @return USER_TVMID
     */
    public String getUserTvmid() {
        return userTvmid;
    }

    /**
     * @param userTvmid
     */
    public void setUserTvmid(String userTvmid) {
        this.userTvmid = userTvmid;
    }

    /**
     * @return PROVINCE_ID
     */
    public Integer getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId
     */
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * @return CITY_ID
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * @param cityId
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    /**
     * @return COUNTRY_ID
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * @param countryId
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }


    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userCompany='" + userCompany + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", userCreatetime=" + userCreatetime +
                ", userPonits=" + userPonits +
                ", userLogo='" + userLogo + '\'' +
                ", userStatus=" + userStatus +
                ", userType=" + userType +
                ", userQq='" + userQq + '\'' +
                ", userJob='" + userJob + '\'' +
                ", userHandphone='" + userHandphone + '\'' +
                ", userTypeMemo='" + userTypeMemo + '\'' +
                ", userContract='" + userContract + '\'' +
                ", userLimitLvl=" + userLimitLvl +
                ", userLimitProvince='" + userLimitProvince + '\'' +
                ", userLimitCity='" + userLimitCity + '\'' +
                ", userLimitCountry='" + userLimitCountry + '\'' +
                ", userBizType='" + userBizType + '\'' +
                ", userTimeValue=" + userTimeValue +
                ", userMtrBizType=" + userMtrBizType +
                ", userTvmid='" + userTvmid + '\'' +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", countryId=" + countryId +
                '}';
    }
}