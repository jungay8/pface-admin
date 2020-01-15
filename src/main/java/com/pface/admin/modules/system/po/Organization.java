package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cjbi,daniel.liu
 */
@Table(name="jmgo_sys_organization")
public class Organization {

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 组织机构名称
     */
    @NotBlank(message = "组织机构名称不能为空")
    private String name;
    /**
     * 父编号
     */
    @NotNull(message = "父编号不能为空")
    private Long parentId;
    /**
     * 父编号列表，如1/2/
     */
    private String parentIds;
    private Boolean available;

    /**
     * 叶子节点
     */
    private Boolean leaf;
    /**
     * 排序
     */
    private Long priority;

    public Long getId() {
        return id;
    }

    public Organization setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Organization setName(String name) {
        this.name = name;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public Organization setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getParentIds() {
        return parentIds;
    }

    public Organization setParentIds(String parentIds) {
        this.parentIds = parentIds;
        return this;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Organization setAvailable(Boolean available) {
        this.available = available;
        return this;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public Organization setLeaf(Boolean leaf) {
        this.leaf = leaf;
        return this;
    }

    public Long getPriority() {
        return priority;
    }

    public Organization setPriority(Long priority) {
        this.priority = priority;
        return this;
    }

    public String makeSelfAsParentIds() {
        return getParentIds() + getId() + "/";
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
