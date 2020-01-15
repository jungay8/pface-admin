package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "face_user")
public class FaceUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String username;

    private String operator;

    private String password;

    /**
     * off禁用
            on启用
     */
    private String status;

    /**
     * 应用端ip或域名
     */
    private String ip;

    /**
     * 应用端port
     */
    private String port;

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
     * 获取名称
     *
     * @return username - 名称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置名称
     *
     * @param username 名称
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取off禁用
            on启用
     *
     * @return status - off禁用
            on启用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置off禁用
            on启用
     *
     * @param status off禁用
            on启用
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取应用端ip或域名
     *
     * @return ip - 应用端ip或域名
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置应用端ip或域名
     *
     * @param ip 应用端ip或域名
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取应用端port
     *
     * @return port - 应用端port
     */
    public String getPort() {
        return port;
    }

    /**
     * 设置应用端port
     *
     * @param port 应用端port
     */
    public void setPort(String port) {
        this.port = port;
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