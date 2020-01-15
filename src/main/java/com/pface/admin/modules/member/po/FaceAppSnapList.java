package com.pface.admin.modules.member.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_app_snap_list")
public class FaceAppSnapList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户编码
     */
    @Column(name = "sys_userid")
    private Integer sysUserid;

    /**
     * 盒子设备ID
     */
    @Column(name = "device_id")
    private String deviceId;

    /**
     * 人像库ID
     */
    @Column(name = "lib_id")
    private Byte libId;

    /**
     * 人像库名称
     */
    @Column(name = "lib_name")
    private String libName;

    /**
     * 人像库类型
     */
    @Column(name = "lib_type")
    private Byte libType;

    /**
     * 相机名称
     */
    @Column(name = "camera_name")
    private String cameraName;

    /**
     * 相机通道
     */
    private Byte channel;

    /**
     * 相机位置
     */
    private String position;

    /**
     * 图片ID
     */
    @Column(name = "img_id")
    private String imgId;

    /**
     * 图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
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
     * 人脸位置--左上Y
     */
    @Column(name = "pos_top")
    private Integer posTop;

    /**
     * 人脸位置--左上X
     */
    @Column(name = "pos_left")
    private Integer posLeft;

    /**
     * 人脸位置--右下Y
     */
    @Column(name = "pos_bottom")
    private Integer posBottom;

    /**
     * 人脸位置--右下X
     */
    @Column(name = "pos_right")
    private Integer posRight;

    /**
     * 质量分数
     */
    private Integer quality;

    /**
     * 与底片对比的分数
     */
    private Integer similarity;

    /**
     * 抓拍图片ID
     */
    @Column(name = "snap_id")
    private String snapId;

    /**
     * 抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     */
    @Column(name = "snap_path")
    private String snapPath;

    /**
     * 抓拍图片时间
     */
    @Column(name = "trigger_time")
    private Date triggerTime;

    @Column(name = "snap_mask")
    private String snapMask;

    @Column(name = "snap_race")
    private String snapRace;

    @Column(name = "snap_zangjiang")
    private String snapZangjiang;

    private String threshold;

    /**
     * 抓拍的sdk年龄
     */
    @Column(name = "snap_age")
    private Integer snapAge;

    /**
     * 抓拍的有无眼镜
     */
    @Column(name = "snap_glasses")
    private Integer snapGlasses;

    /**
     * 抓拍的性别
     */
    @Column(name = "snap_gender")
    private Integer snapGender;

    /**
     * 抓拍的有无胡子
     */
    @Column(name = "snap_beard")
    private Integer snapBeard;

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
     * 获取盒子设备ID
     *
     * @return device_id - 盒子设备ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置盒子设备ID
     *
     * @param deviceId 盒子设备ID
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
     * 获取人像库名称
     *
     * @return lib_name - 人像库名称
     */
    public String getLibName() {
        return libName;
    }

    /**
     * 设置人像库名称
     *
     * @param libName 人像库名称
     */
    public void setLibName(String libName) {
        this.libName = libName;
    }

    /**
     * 获取人像库类型
     *
     * @return lib_type - 人像库类型
     */
    public Byte getLibType() {
        return libType;
    }

    /**
     * 设置人像库类型
     *
     * @param libType 人像库类型
     */
    public void setLibType(Byte libType) {
        this.libType = libType;
    }

    /**
     * 获取相机名称
     *
     * @return camera_name - 相机名称
     */
    public String getCameraName() {
        return cameraName;
    }

    /**
     * 设置相机名称
     *
     * @param cameraName 相机名称
     */
    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    /**
     * 获取相机通道
     *
     * @return channel - 相机通道
     */
    public Byte getChannel() {
        return channel;
    }

    /**
     * 设置相机通道
     *
     * @param channel 相机通道
     */
    public void setChannel(Byte channel) {
        this.channel = channel;
    }

    /**
     * 获取相机位置
     *
     * @return position - 相机位置
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置相机位置
     *
     * @param position 相机位置
     */
    public void setPosition(String position) {
        this.position = position;
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
     * 获取图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
     *
     * @return img_path - 图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * 设置图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
     *
     * @param imgPath 图片路径，下载图片需拼接：http://<ip>:<port>/ws/img_path
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
     * 获取人脸位置--左上Y
     *
     * @return pos_top - 人脸位置--左上Y
     */
    public Integer getPosTop() {
        return posTop;
    }

    /**
     * 设置人脸位置--左上Y
     *
     * @param posTop 人脸位置--左上Y
     */
    public void setPosTop(Integer posTop) {
        this.posTop = posTop;
    }

    /**
     * 获取人脸位置--左上X
     *
     * @return pos_left - 人脸位置--左上X
     */
    public Integer getPosLeft() {
        return posLeft;
    }

    /**
     * 设置人脸位置--左上X
     *
     * @param posLeft 人脸位置--左上X
     */
    public void setPosLeft(Integer posLeft) {
        this.posLeft = posLeft;
    }

    /**
     * 获取人脸位置--右下Y
     *
     * @return pos_bottom - 人脸位置--右下Y
     */
    public Integer getPosBottom() {
        return posBottom;
    }

    /**
     * 设置人脸位置--右下Y
     *
     * @param posBottom 人脸位置--右下Y
     */
    public void setPosBottom(Integer posBottom) {
        this.posBottom = posBottom;
    }

    /**
     * 获取人脸位置--右下X
     *
     * @return pos_right - 人脸位置--右下X
     */
    public Integer getPosRight() {
        return posRight;
    }

    /**
     * 设置人脸位置--右下X
     *
     * @param posRight 人脸位置--右下X
     */
    public void setPosRight(Integer posRight) {
        this.posRight = posRight;
    }

    /**
     * 获取质量分数
     *
     * @return quality - 质量分数
     */
    public Integer getQuality() {
        return quality;
    }

    /**
     * 设置质量分数
     *
     * @param quality 质量分数
     */
    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    /**
     * 获取与底片对比的分数
     *
     * @return similarity - 与底片对比的分数
     */
    public Integer getSimilarity() {
        return similarity;
    }

    /**
     * 设置与底片对比的分数
     *
     * @param similarity 与底片对比的分数
     */
    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    /**
     * 获取抓拍图片ID
     *
     * @return snap_id - 抓拍图片ID
     */
    public String getSnapId() {
        return snapId;
    }

    /**
     * 设置抓拍图片ID
     *
     * @param snapId 抓拍图片ID
     */
    public void setSnapId(String snapId) {
        this.snapId = snapId;
    }

    /**
     * 获取抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     *
     * @return snap_path - 抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     */
    public String getSnapPath() {
        return snapPath;
    }

    /**
     * 设置抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     *
     * @param snapPath 抓拍图片路径，下载图片需拼接：http://<ip>:<port>/ws/snap_path
     */
    public void setSnapPath(String snapPath) {
        this.snapPath = snapPath;
    }

    /**
     * 获取抓拍图片时间
     *
     * @return trigger_time - 抓拍图片时间
     */
    public Date getTriggerTime() {
        return triggerTime;
    }

    /**
     * 设置抓拍图片时间
     *
     * @param triggerTime 抓拍图片时间
     */
    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    /**
     * @return snap_mask
     */
    public String getSnapMask() {
        return snapMask;
    }

    /**
     * @param snapMask
     */
    public void setSnapMask(String snapMask) {
        this.snapMask = snapMask;
    }

    /**
     * @return snap_race
     */
    public String getSnapRace() {
        return snapRace;
    }

    /**
     * @param snapRace
     */
    public void setSnapRace(String snapRace) {
        this.snapRace = snapRace;
    }

    /**
     * @return snap_zangjiang
     */
    public String getSnapZangjiang() {
        return snapZangjiang;
    }

    /**
     * @param snapZangjiang
     */
    public void setSnapZangjiang(String snapZangjiang) {
        this.snapZangjiang = snapZangjiang;
    }

    /**
     * @return threshold
     */
    public String getThreshold() {
        return threshold;
    }

    /**
     * @param threshold
     */
    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    /**
     * 获取抓拍的sdk年龄
     *
     * @return snap_age - 抓拍的sdk年龄
     */
    public Integer getSnapAge() {
        return snapAge;
    }

    /**
     * 设置抓拍的sdk年龄
     *
     * @param snapAge 抓拍的sdk年龄
     */
    public void setSnapAge(Integer snapAge) {
        this.snapAge = snapAge;
    }

    /**
     * 获取抓拍的有无眼镜
     *
     * @return snap_glasses - 抓拍的有无眼镜
     */
    public Integer getSnapGlasses() {
        return snapGlasses;
    }

    /**
     * 设置抓拍的有无眼镜
     *
     * @param snapGlasses 抓拍的有无眼镜
     */
    public void setSnapGlasses(Integer snapGlasses) {
        this.snapGlasses = snapGlasses;
    }

    /**
     * 获取抓拍的性别
     *
     * @return snap_gender - 抓拍的性别
     */
    public Integer getSnapGender() {
        return snapGender;
    }

    /**
     * 设置抓拍的性别
     *
     * @param snapGender 抓拍的性别
     */
    public void setSnapGender(Integer snapGender) {
        this.snapGender = snapGender;
    }

    /**
     * 获取抓拍的有无胡子
     *
     * @return snap_beard - 抓拍的有无胡子
     */
    public Integer getSnapBeard() {
        return snapBeard;
    }

    /**
     * 设置抓拍的有无胡子
     *
     * @param snapBeard 抓拍的有无胡子
     */
    public void setSnapBeard(Integer snapBeard) {
        this.snapBeard = snapBeard;
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