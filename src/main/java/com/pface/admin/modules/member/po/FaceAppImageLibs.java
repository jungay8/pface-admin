package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_app_image_libs")
public class FaceAppImageLibs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 客户编码
     */
    @Column(name = "sys_userid")
    private Integer sysUserid;

    /**
     * 设备ID，指盒子智能设备
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 设备ID，指盒子智能设备
     */
    @Column(name = "sys_sceneid")
    private Integer sysSceneid;

    /**
     * 设备授权开始
     */
    @Column(name = "auth_begindate")
    private Date authBegindate;
    /**
     * 设备授权结束
     */
    @Column(name = "auth_enddate")
    private Date authEnddate;

    /**
     * 人像库id
     */
    @Column(name = "lib_id")
    private Byte libId;

    /**
     * 库名
     */
    @Column(name = "lib_name")
    private String libName;

    /**
     * 图片数
     */
    @Column(name = "picture_no")
    private Integer pictureNo;

    /**
     * 在盒子设备中的更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 1黑名单
            2白名单
     */
    @Column(name = "lib_type")
    private Byte libType;

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
     * 获取设备ID，指盒子智能设备
     *
     * @return device_id - 设备ID，指盒子智能设备
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置设备ID，指盒子智能设备
     *
     * @param deviceId 设备ID，指盒子智能设备
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @return sys_sceneid
     */
    public Integer getSysSceneid() {
        return sysSceneid;
    }

    /**
     * @param sysSceneid
     */
    public void setSysSceneid(Integer sysSceneid) {
        this.sysSceneid = sysSceneid;
    }

    public Date getAuthBegindate() {
        return authBegindate;
    }

    public void setAuthBegindate(Date authBegindate) {
        this.authBegindate = authBegindate;
    }

    public Date getAuthEnddate() {
        return authEnddate;
    }

    public void setAuthEnddate(Date authEnddate) {
        this.authEnddate = authEnddate;
    }

    /**
     * 获取人像库id
     *
     * @return lib_id - 人像库id
     */
    public Byte getLibId() {
        return libId;
    }

    /**
     * 设置人像库id
     *
     * @param libId 人像库id
     */
    public void setLibId(Byte libId) {
        this.libId = libId;
    }

    /**
     * 获取库名
     *
     * @return lib_name - 库名
     */
    public String getLibName() {
        return libName;
    }

    /**
     * 设置库名
     *
     * @param libName 库名
     */
    public void setLibName(String libName) {
        this.libName = libName;
    }

    /**
     * 获取图片数
     *
     * @return picture_no - 图片数
     */
    public Integer getPictureNo() {
        return pictureNo;
    }

    /**
     * 设置图片数
     *
     * @param pictureNo 图片数
     */
    public void setPictureNo(Integer pictureNo) {
        this.pictureNo = pictureNo;
    }

    /**
     * 获取在盒子设备中的更新时间
     *
     * @return update_time - 在盒子设备中的更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置在盒子设备中的更新时间
     *
     * @param updateTime 在盒子设备中的更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取1黑名单
            2白名单
     *
     * @return lib_type - 1黑名单
            2白名单
     */
    public Byte getLibType() {
        return libType;
    }

    /**
     * 设置1黑名单
            2白名单
     *
     * @param libType 1黑名单
            2白名单
     */
    public void setLibType(Byte libType) {
        this.libType = libType;
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