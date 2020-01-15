package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_camera")
public class FaceCamera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 设备ID，指盒子智能设备---6.3
     */
    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "camera_no")
    private String cameraNo;

    @Column(name = "install_location")
    private String installLocation;

    /**
     * 通道数由盒子型号决定，一般有4、8、16路的
     */
    private Byte channel;

    @Column(name = "camera_name")
    private String cameraName;

    private String mode;

    /**
     * 相机状态：off离线；on在线
     */
    private String status;

    /**
     * 人像库ids
     */
    @Column(name = "lib_ids")
    private String libIds;

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
     * @return camera_no
     */
    public String getCameraNo() {
        return cameraNo;
    }

    /**
     * @param cameraNo
     */
    public void setCameraNo(String cameraNo) {
        this.cameraNo = cameraNo;
    }

    /**
     * @return install_location
     */
    public String getInstallLocation() {
        return installLocation;
    }

    /**
     * @param installLocation
     */
    public void setInstallLocation(String installLocation) {
        this.installLocation = installLocation;
    }

    /**
     * 获取通道数由盒子型号决定，一般有4、8、16路的
     *
     * @return channel - 通道数由盒子型号决定，一般有4、8、16路的
     */
    public Byte getChannel() {
        return channel;
    }

    /**
     * 设置通道数由盒子型号决定，一般有4、8、16路的
     *
     * @param channel 通道数由盒子型号决定，一般有4、8、16路的
     */
    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    /**
     * @return camera_name
     */
    public String getCameraName() {
        return cameraName;
    }

    /**
     * @param cameraName
     */
    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    /**
     * @return mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * 获取相机状态：off离线；on在线
     *
     * @return status - 相机状态：off离线；on在线
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置相机状态：off离线；on在线
     *
     * @param status 相机状态：off离线；on在线
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取人像库ids
     *
     * @return lib_ids - 人像库ids
     */
    public String getLibIds() {
        return libIds;
    }

    /**
     * 设置人像库ids
     *
     * @param libIds 人像库ids
     */
    public void setLibIds(String libIds) {
        this.libIds = libIds;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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