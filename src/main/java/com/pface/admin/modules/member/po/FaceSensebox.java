package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_sensebox")
public class FaceSensebox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ip;

    private String port;

    /**
     * 设备web登录用户名
     */
    private String loginname;

    /**
     * 设备web端登陆密码
     */
    private String loginpassword;

    /**
     * 设备ID，指盒子智能设备---6.3
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 序列号--只读---6.3
     */
    @Column(name = "serial_id")
    private String serialId;

    /**
     * 版本--只读---6.3
     */
    private String version;

    /**
     * 版本--只读---6.3
     */
    @Column(name = "web_version")
    private String webVersion;

    /**
     * 总容量--只读---6.4
     */
    @Column(name = "max_storage")
    private Integer maxStorage;

    /**
     * 剩余--只读---6.4
     */
    @Column(name = "cur_storage")
    private Integer curStorage;

    /**
     * 1满覆盖
            2满即停  ---6.4
     */
    @Column(name = "storage_strategy")
    private Byte storageStrategy;

    /**
     * 对接http_url--7.10
     */
    @Column(name = "http_url")
    private String httpUrl;

    /**
     * 对接http_keyl--7.10
     */
    @Column(name = "http_key")
    private String httpKey;

    /**
     * 相机工作模式--只读--4.5
            对于盒子,其相机的工作模式只有一个确定的.
     */
    @Column(name = "camera_mode")
    private String cameraMode;

    private Integer startno;

    private Integer endno;

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
     * @return ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * 获取设备web登录用户名
     *
     * @return loginname - 设备web登录用户名
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * 设置设备web登录用户名
     *
     * @param loginname 设备web登录用户名
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    /**
     * 获取设备web端登陆密码
     *
     * @return loginpassword - 设备web端登陆密码
     */
    public String getLoginpassword() {
        return loginpassword;
    }

    /**
     * 设置设备web端登陆密码
     *
     * @param loginpassword 设备web端登陆密码
     */
    public void setLoginpassword(String loginpassword) {
        this.loginpassword = loginpassword;
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
     * 获取序列号--只读---6.3
     *
     * @return serial_id - 序列号--只读---6.3
     */
    public String getSerialId() {
        return serialId;
    }

    /**
     * 设置序列号--只读---6.3
     *
     * @param serialId 序列号--只读---6.3
     */
    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    /**
     * 获取版本--只读---6.3
     *
     * @return version - 版本--只读---6.3
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本--只读---6.3
     *
     * @param version 版本--只读---6.3
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取版本--只读---6.3
     *
     * @return web_version - 版本--只读---6.3
     */
    public String getWebVersion() {
        return webVersion;
    }

    /**
     * 设置版本--只读---6.3
     *
     * @param webVersion 版本--只读---6.3
     */
    public void setWebVersion(String webVersion) {
        this.webVersion = webVersion;
    }

    /**
     * 获取总容量--只读---6.4
     *
     * @return max_storage - 总容量--只读---6.4
     */
    public Integer getMaxStorage() {
        return maxStorage;
    }

    /**
     * 设置总容量--只读---6.4
     *
     * @param maxStorage 总容量--只读---6.4
     */
    public void setMaxStorage(Integer maxStorage) {
        this.maxStorage = maxStorage;
    }

    /**
     * 获取剩余--只读---6.4
     *
     * @return cur_storage - 剩余--只读---6.4
     */
    public Integer getCurStorage() {
        return curStorage;
    }

    /**
     * 设置剩余--只读---6.4
     *
     * @param curStorage 剩余--只读---6.4
     */
    public void setCurStorage(Integer curStorage) {
        this.curStorage = curStorage;
    }

    /**
     * 获取1满覆盖
            2满即停  ---6.4
     *
     * @return storage_strategy - 1满覆盖
            2满即停  ---6.4
     */
    public Byte getStorageStrategy() {
        return storageStrategy;
    }

    /**
     * 设置1满覆盖
            2满即停  ---6.4
     *
     * @param storageStrategy 1满覆盖
            2满即停  ---6.4
     */
    public void setStorageStrategy(Byte storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    /**
     * 获取对接http_url--7.10
     *
     * @return http_url - 对接http_url--7.10
     */
    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * 设置对接http_url--7.10
     *
     * @param httpUrl 对接http_url--7.10
     */
    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    /**
     * 获取对接http_keyl--7.10
     *
     * @return http_key - 对接http_keyl--7.10
     */
    public String getHttpKey() {
        return httpKey;
    }

    /**
     * 设置对接http_keyl--7.10
     *
     * @param httpKey 对接http_keyl--7.10
     */
    public void setHttpKey(String httpKey) {
        this.httpKey = httpKey;
    }

    /**
     * 获取相机工作模式--只读--4.5
            对于盒子,其相机的工作模式只有一个确定的.
     *
     * @return camera_mode - 相机工作模式--只读--4.5
            对于盒子,其相机的工作模式只有一个确定的.
     */
    public String getCameraMode() {
        return cameraMode;
    }

    /**
     * 设置相机工作模式--只读--4.5
            对于盒子,其相机的工作模式只有一个确定的.
     *
     * @param cameraMode 相机工作模式--只读--4.5
            对于盒子,其相机的工作模式只有一个确定的.
     */
    public void setCameraMode(String cameraMode) {
        this.cameraMode = cameraMode;
    }

    /**
     * @return startno
     */
    public Integer getStartno() {
        return startno;
    }

    /**
     * @param startno
     */
    public void setStartno(Integer startno) {
        this.startno = startno;
    }

    /**
     * @return endno
     */
    public Integer getEndno() {
        return endno;
    }

    /**
     * @param endno
     */
    public void setEndno(Integer endno) {
        this.endno = endno;
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