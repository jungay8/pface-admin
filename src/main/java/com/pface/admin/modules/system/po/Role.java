package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cjbi,daniel.liu
 */
@Table(name = "jmgo_sys_role")
public class Role {

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 角色标识 程序中判断使用,如"admin"
     */
    @NotBlank(message = "角色标识不能为空")
    private String role;
    /**
     * 角色描述,UI界面显示使用
     */
    @NotBlank(message = "角色描述不能为空")
    private String description;
    /**
     * 拥有的资源
     */
    @NotBlank(message = "拥有的资源不能为空")
    private String resourceIds;
    /**
     * 是否可用,如果不可用将不会添加给用户
     */
    private Boolean available;

    public Role() {
    }

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Role setRole(String role) {
        this.role = role;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Role setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public Role setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
        return this;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Role setAvailable(Boolean available) {
        this.available = available;
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
}
