package com.pface.admin.modules.member.po;

import javax.persistence.*;

@Table(name = "jmgo_file_path")
public class JmgoFilePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 地址映射名称
     */
    private String pathname;

    /**
     * 映射路径
     */
    private String virtualpath;

    /**
     * 真实路径
     */
    private String realpath;

    /**
     * 启用标识：0：没有；1启用，同一时刻只允许一条记录为1
     */
    private Integer useflag;

    /**
     * 备注
     */
    private String remark;

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
     * 获取地址映射名称
     *
     * @return pathname - 地址映射名称
     */
    public String getPathname() {
        return pathname;
    }

    /**
     * 设置地址映射名称
     *
     * @param pathname 地址映射名称
     */
    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    /**
     * 获取映射路径
     *
     * @return virtualpath - 映射路径
     */
    public String getVirtualpath() {
        return virtualpath;
    }

    /**
     * 设置映射路径
     *
     * @param virtualpath 映射路径
     */
    public void setVirtualpath(String virtualpath) {
        this.virtualpath = virtualpath;
    }

    /**
     * 获取真实路径
     *
     * @return realpath - 真实路径
     */
    public String getRealpath() {
        return realpath;
    }

    /**
     * 设置真实路径
     *
     * @param realpath 真实路径
     */
    public void setRealpath(String realpath) {
        this.realpath = realpath;
    }

    /**
     * 获取启用标识：0：没有；1启用，同一时刻只允许一条记录为1
     *
     * @return useflag - 启用标识：0：没有；1启用，同一时刻只允许一条记录为1
     */
    public Integer getUseflag() {
        return useflag;
    }

    /**
     * 设置启用标识：0：没有；1启用，同一时刻只允许一条记录为1
     *
     * @param useflag 启用标识：0：没有；1启用，同一时刻只允许一条记录为1
     */
    public void setUseflag(Integer useflag) {
        this.useflag = useflag;
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
}