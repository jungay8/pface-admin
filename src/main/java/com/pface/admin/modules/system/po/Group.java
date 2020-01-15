package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.type.JdbcType;
import com.pface.admin.modules.system.enums.GroupType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cjbi,daniel.liu
 */
@Table(name = "jmgo_sys_group")
public class Group {

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 组名称
     */
    @NotBlank(message = "组名称不能为空")
    private String name;

    /**
     * 组类型
     */
    @NotNull(message = "组类型不能为空")
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private GroupType type;

    /**
     * 描述
     */
    private String description;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    public Long getId() {
        return id;
    }

    public Group setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    public GroupType getType() {
        return type;
    }

    public Group setType(GroupType type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Group setDescription(String description) {
        this.description = description;
        return this;
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

}
