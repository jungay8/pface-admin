package com.pface.admin.modules.member.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "jmgo_member_catalogue")
public class MemberCatalogue {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 父编号
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 父编号列表
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 是否有效
     */
    private Boolean available;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 叶子节点
     */
    private Boolean leaf;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    /**
     * 获取编号
     *
     * @return id - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public MemberCatalogue setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * 获取组织名称
     *
     * @return name - 组织名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置组织名称
     *
     * @param name 组织名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父编号
     *
     * @return parent_id - 父编号
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父编号
     *
     * @param parentId 父编号
     */
    public MemberCatalogue setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * 获取父编号列表
     *
     * @return parent_ids - 父编号列表
     */
    public String getParentIds() {
        return parentIds;
    }

    /**
     * 设置父编号列表
     *
     * @param parentIds 父编号列表
     */
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    /**
     * 获取是否有效
     *
     * @return available - 是否有效
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * 设置是否有效
     *
     * @param available 是否有效
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * 获取优先级
     *
     * @return priority - 优先级
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 设置优先级
     *
     * @param priority 优先级
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 获取叶子节点
     *
     * @return leaf - 叶子节点
     */
    public Boolean getLeaf() {
        return leaf;
    }

    /**
     * 设置叶子节点
     *
     * @param leaf 叶子节点
     */
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

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

    public String makeSelfAsParentIds() {
        return getParentIds() + getId() + "/";
    }

    @Column(name = "del_flag")
    private Integer delFlag;

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}