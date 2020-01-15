package com.pface.admin.modules.member.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_app_usescene")
public class FaceAppUsescene {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户编码
     */
    @Column(name = "sys_userid")
    private Integer sysUserid;

    /**
     * 场景名称,如：会议名称
     */
    private String scenename;

    /**
     * 开始时间,如:会议开始时间，精确到天
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 结束时间,如:会议结束时间
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 签到开始时间
     */
    @Column(name = "asign_starttime")
    private Date asignStarttime;

    /**
     * 签到结束时间
     */
    @Column(name = "asign_endtime")
    private Date asignEndtime;

    /**
     * 状态
            0未开始
            1进行中
            2已结束
     */
//    private Byte status;

    /**
     * 场景主题
     */
    private String theme;

    private String address;

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
     * 获取场景名称,如：会议名称
     *
     * @return name - 场景名称,如：会议名称
     */
    public String getScenename() {
        return scenename;
    }

    /**
     * 设置场景名称,如：会议名称
     *
     * @param scenename 场景名称,如：会议名称
     */
    public void setScenename(String scenename) {
        this.scenename = scenename;
    }

    /**
     * 获取开始时间,如:会议开始时间，精确到天
     *
     * @return start_date - 开始时间,如:会议开始时间，精确到天
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始时间,如:会议开始时间，精确到天
     *
     * @param startDate 开始时间,如:会议开始时间，精确到天
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束时间,如:会议结束时间
     *
     * @return end_date - 结束时间,如:会议结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束时间,如:会议结束时间
     *
     * @param endDate 结束时间,如:会议结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取签到开始时间
     *
     * @return asign_starttime - 签到开始时间
     */
    public Date getAsignStarttime() {
        return asignStarttime;
    }

    /**
     * 设置签到开始时间
     *
     * @param asignStarttime 签到开始时间
     */
    public void setAsignStarttime(Date asignStarttime) {
        this.asignStarttime = asignStarttime;
    }

    /**
     * 获取签到结束时间
     *
     * @return asign_endtime - 签到结束时间
     */
    public Date getAsignEndtime() {
        return asignEndtime;
    }

    /**
     * 设置签到结束时间
     *
     * @param asignEndtime 签到结束时间
     */
    public void setAsignEndtime(Date asignEndtime) {
        this.asignEndtime = asignEndtime;
    }

    /**
     * 获取状态
            0未开始
            1进行中
            2已结束
     *
     * @return status - 状态
            0未开始
            1进行中
            2已结束
     */
//    public Byte getStatus() {
//        return status;
//    }

    /**
     * 设置状态
            0未开始
            1进行中
            2已结束
     *
     * @param status 状态
            0未开始
            1进行中
            2已结束
     */
//    public void setStatus(Byte status) {
//        this.status = status;
//    }

    /**
     * 获取场景主题
     *
     * @return theme - 场景主题
     */
    public String getTheme() {
        return theme;
    }

    /**
     * 设置场景主题
     *
     * @param theme 场景主题
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
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