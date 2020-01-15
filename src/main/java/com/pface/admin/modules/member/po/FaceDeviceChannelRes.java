package com.pface.admin.modules.member.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`face_device_channel_res`")
public class FaceDeviceChannelRes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 设备ID，指盒子智能设备---6.3
     */
    @Column(name = "device_id")
    private String deviceId;

    private Byte channel;

    /**
     * yes:已安装
            no:未安装
     */
    private String issetup;

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
     * @return channel
     */
    public Byte getChannel() {
        return channel;
    }

    /**
     * @param channel
     */
    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    /**
     * 获取yes:已安装
            no:未安装
     *
     * @return issetup - yes:已安装
            no:未安装
     */
    public String getIssetup() {
        return issetup;
    }

    /**
     * 设置yes:已安装
            no:未安装
     *
     * @param issetup yes:已安装
            no:未安装
     */
    public void setIssetup(String issetup) {
        this.issetup = issetup;
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