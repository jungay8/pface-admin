package com.pface.admin.otherdb.po;

import org.apache.ibatis.annotations.SelectKey;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "dbo.MT_AUTH")
public class MtAuth {
    @Id
    @Column(name = "UserId")
    //@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT SCOPE_IDENTITY()")
    private Integer userid;

    @Column(name = "Admin")
    private String admin;

    @Column(name = "AuthType")
    private Integer authtype;

    @Column(name = "CardBack")
    private String cardback;

    @Column(name = "CardFront")
    private String cardfront;

    @Column(name = "CardNO")
    private String cardno;

    @Column(name = "CheckDate")
    private Date checkdate;

    @Column(name = "CheckOk")
    private Integer checkok;

    @Column(name = "ComImage")
    private String comimage;

    @Column(name = "ComNO")
    private String comno;

    @Column(name = "Company")
    private String company;

    @Column(name = "UserLogin")
    private String userlogin;

    @Column(name = "ComLogo")
    private String comlogo;

    @Column(name = "Money")
    private BigDecimal money;

    @Column(name = "EnableShop")
    private Boolean enableshop;

    @Column(name = "TimeValue")
    private Integer timevalue;

    @Column(name = "Service")
    private String service;

    @Column(name = "WantBuy")
    private Boolean wantbuy;

    @Column(name = "WantSell")
    private Boolean wantsell;

    @Column(name = "Fee")
    private BigDecimal fee;

    @Column(name = "CheckMemo")
    private String checkmemo;

    @Column(name = "YiYuan")
    private Integer yiyuan;

    /**
     * @return UserId
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * @return Admin
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * @param admin
     */
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    /**
     * @return AuthType
     */
    public Integer getAuthtype() {
        return authtype;
    }

    /**
     * @param authtype
     */
    public void setAuthtype(Integer authtype) {
        this.authtype = authtype;
    }

    /**
     * @return CardBack
     */
    public String getCardback() {
        return cardback;
    }

    /**
     * @param cardback
     */
    public void setCardback(String cardback) {
        this.cardback = cardback;
    }

    /**
     * @return CardFront
     */
    public String getCardfront() {
        return cardfront;
    }

    /**
     * @param cardfront
     */
    public void setCardfront(String cardfront) {
        this.cardfront = cardfront;
    }

    /**
     * @return CardNO
     */
    public String getCardno() {
        return cardno;
    }

    /**
     * @param cardno
     */
    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    /**
     * @return CheckDate
     */
    public Date getCheckdate() {
        return checkdate;
    }

    /**
     * @param checkdate
     */
    public void setCheckdate(Date checkdate) {
        this.checkdate = checkdate;
    }

    /**
     * @return CheckOk
     */
    public Integer getCheckok() {
        return checkok;
    }

    /**
     * @param checkok
     */
    public void setCheckok(Integer checkok) {
        this.checkok = checkok;
    }

    /**
     * @return ComImage
     */
    public String getComimage() {
        return comimage;
    }

    /**
     * @param comimage
     */
    public void setComimage(String comimage) {
        this.comimage = comimage;
    }

    /**
     * @return ComNO
     */
    public String getComno() {
        return comno;
    }

    /**
     * @param comno
     */
    public void setComno(String comno) {
        this.comno = comno;
    }

    /**
     * @return Company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return UserLogin
     */
    public String getUserlogin() {
        return userlogin;
    }

    /**
     * @param userlogin
     */
    public void setUserlogin(String userlogin) {
        this.userlogin = userlogin;
    }

    /**
     * @return ComLogo
     */
    public String getComlogo() {
        return comlogo;
    }

    /**
     * @param comlogo
     */
    public void setComlogo(String comlogo) {
        this.comlogo = comlogo;
    }

    /**
     * @return Money
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * @param money
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * @return EnableShop
     */
    public Boolean getEnableshop() {
        return enableshop;
    }

    /**
     * @param enableshop
     */
    public void setEnableshop(Boolean enableshop) {
        this.enableshop = enableshop;
    }

    /**
     * @return TimeValue
     */
    public Integer getTimevalue() {
        return timevalue;
    }

    /**
     * @param timevalue
     */
    public void setTimevalue(Integer timevalue) {
        this.timevalue = timevalue;
    }

    /**
     * @return Service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return WantBuy
     */
    public Boolean getWantbuy() {
        return wantbuy;
    }

    /**
     * @param wantbuy
     */
    public void setWantbuy(Boolean wantbuy) {
        this.wantbuy = wantbuy;
    }

    /**
     * @return WantSell
     */
    public Boolean getWantsell() {
        return wantsell;
    }

    /**
     * @param wantsell
     */
    public void setWantsell(Boolean wantsell) {
        this.wantsell = wantsell;
    }

    /**
     * @return Fee
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * @return CheckMemo
     */
    public String getCheckmemo() {
        return checkmemo;
    }

    /**
     * @param checkmemo
     */
    public void setCheckmemo(String checkmemo) {
        this.checkmemo = checkmemo;
    }

    /**
     * @return YiYuan
     */
    public Integer getYiyuan() {
        return yiyuan;
    }

    /**
     * @param yiyuan
     */
    public void setYiyuan(Integer yiyuan) {
        this.yiyuan = yiyuan;
    }
}