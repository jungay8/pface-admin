package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cjbi,daniel.liu
 */
@Table(name = "jmgo_sys_user")
public class User {

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = UserUpdateChecks.class)
    private Long id;
    /**
     * 所属公司
     */
    @NotNull(message = "所属组织不能为空")
    private Long organizationId;
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Min(value = 3, message = "用户名不能低于3位")
    private String username;
    /**
     * 密码
     */
//    @NotBlank(message = "密码不能为空", groups = UserCreateChecks.class)
//    @Min(value = 6, message = "密码不能低于6位", groups = UserCreateChecks.class)
    private String password;

    /**
     * 密码
     */
    //@NotBlank(message = "旧密码不能为空", groups = UserCreateChecks.class)
    //@Min(value = 6, message = "旧密码不能低于6位", groups = UserCreateChecks.class)
    @Transient
    private String oldpassword;

    private String mobile;

    /**
     * 加密密码的盐
     */
    private String salt;
    /**
     * 拥有的角色列表
     */
    private String roleIds;

    private String groupIds;

    private Boolean locked;

    public interface UserCreateChecks {

    }

    public interface UserUpdateChecks {

    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getCredentialsSalt() {
        return username + salt;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public User setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSalt() {
        return salt;
    }

    public User setSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public User setRoleIds(String roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public User setGroupIds(String groupIds) {
        this.groupIds = groupIds;
        return this;
    }

    public Boolean getLocked() {
        return locked;
    }

    public User setLocked(Boolean locked) {
        this.locked = locked;
        return this;
    }

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;
    /**
     * 获取操作修改日期
     *
     * @return op_date - 操作修改日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getOpDate() {
        return opDate;
    }

    /**
     * 设置操作修改日期
     *
     * @param opDate 操作修改日期
     */
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }
}
