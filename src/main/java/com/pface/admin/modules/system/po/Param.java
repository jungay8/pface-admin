package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author cjbi,daniel.liu
 */
@Table(name = "jmgo_sys_param")
public class Param {

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
    @NotBlank(message = "参数名称不能为空")
    @Column(name = "param_name")
    private String paramName;

    /**
     * 角色描述,UI界面显示使用
     */
    @NotBlank(message = "参数值不能为空")
    @Column(name = "param_value")
    private String paramValue;


    /**
     * 描述
     */
    @Column(name = "param_desc")
    private String paramDesc;

    /**
     * 操作修改日期
     */
    @Column(name = "op_date")
    private Date opDate;

    public Param() { }




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
