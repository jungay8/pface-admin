package com.pface.admin.modules.member.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_app_images")
public class FaceAppImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户编码
     */
    @Column(name = "sys_userid")
    private Integer sysUserid;

    /**
     * 设备ID，指盒子智能设备---6.3
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 人像库ID
     */
    @Column(name = "lib_id")
    private Byte libId;

    /**
     * 图片ID
     */
    @Column(name = "img_id")
    private String imgId;

    /**
     * 图片路径
     */
    @Column(name = "img_path")
    private String imgPath;

    /**
     * 地址
     */
    @Column(name = "person_addr")
    private String personAddr;

    /**
     * 年龄
     */
    @Column(name = "person_age")
    private String personAge;

    /**
     * 性别
     */
    @Column(name = "person_gender")
    private String personGender;

    /**
     * 证件号
     */
    @Column(name = "person_idcard")
    private String personIdcard;

    /**
     * 姓名
     */
    @Column(name = "person_name")
    private String personName;

    /**
     * 图片在盒子设备中的创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建日期
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新日期
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记，0删除；1正常，默认1
     */
    @Column(name = "del_flag")
    private String delFlag;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取客户编码
     *
     * @return sys_userid - 客户编码
     */
    public Integer getSysUserid() {
        return sysUserid;
    }

    /**
     * 设置客户编码
     *
     * @param sysUserid 客户编码
     */
    public void setSysUserid(Integer sysUserid) {
        this.sysUserid = sysUserid;
    }

    /**
     * 获取设备ID，指盒子智能设备---6.3
     *
     * @return device_id - 设备ID，指盒子智能设备---6.3
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置设备ID，指盒子智能设备---6.3
     *
     * @param deviceId 设备ID，指盒子智能设备---6.3
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 获取人像库ID
     *
     * @return lib_id - 人像库ID
     */
    public Byte getLibId() {
        return libId;
    }

    /**
     * 设置人像库ID
     *
     * @param libId 人像库ID
     */
    public void setLibId(Byte libId) {
        this.libId = libId;
    }

    /**
     * 获取图片ID
     *
     * @return img_id - 图片ID
     */
    public String getImgId() {
        return imgId;
    }

    /**
     * 设置图片ID
     *
     * @param imgId 图片ID
     */
    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    /**
     * 获取图片路径
     *
     * @return img_path - 图片路径
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * 设置图片路径
     *
     * @param imgPath 图片路径
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * 获取地址
     *
     * @return person_addr - 地址
     */
    public String getPersonAddr() {
        return personAddr;
    }

    /**
     * 设置地址
     *
     * @param personAddr 地址
     */
    public void setPersonAddr(String personAddr) {
        this.personAddr = personAddr;
    }

    /**
     * 获取年龄
     *
     * @return person_age - 年龄
     */
    public String getPersonAge() {
        return personAge;
    }

    /**
     * 设置年龄
     *
     * @param personAge 年龄
     */
    public void setPersonAge(String personAge) {
        this.personAge = personAge;
    }

    /**
     * 获取性别
     *
     * @return person_gender - 性别
     */
    public String getPersonGender() {
        return personGender;
    }

    /**
     * 设置性别
     *
     * @param personGender 性别
     */
    public void setPersonGender(String personGender) {
        this.personGender = personGender;
    }

    /**
     * 获取证件号
     *
     * @return person_idcard - 证件号
     */
    public String getPersonIdcard() {
        return personIdcard;
    }

    /**
     * 设置证件号
     *
     * @param personIdcard 证件号
     */
    public void setPersonIdcard(String personIdcard) {
        this.personIdcard = personIdcard;
    }

    /**
     * 获取姓名
     *
     * @return person_name - 姓名
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * 设置姓名
     *
     * @param personName 姓名
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * 获取图片在盒子设备中的创建时间
     *
     * @return create_time - 图片在盒子设备中的创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置图片在盒子设备中的创建时间
     *
     * @param createTime 图片在盒子设备中的创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建日期
     *
     * @return create_date - 创建日期
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建日期
     *
     * @param createDate 创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新日期
     *
     * @return update_date - 更新日期
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新日期
     *
     * @param updateDate 更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取删除标记，0删除；1正常，默认1
     *
     * @return del_flag - 删除标记，0删除；1正常，默认1
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * 设置删除标记，0删除；1正常，默认1
     *
     * @param delFlag 删除标记，0删除；1正常，默认1
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}