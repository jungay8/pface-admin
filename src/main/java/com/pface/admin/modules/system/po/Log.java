package com.pface.admin.modules.system.po;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author cjbi,daniel.liu
 */
@Table(name="jmgo_sys_log")
public class Log {

    /**
     * 编号
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户ip
     */
    private String ip;

    /**
     * 请求方法
     */
    private String reqMethod;

    /**
     * 执行方法
     */
    private String execMethod;

    /**
     * 响应时间
     */
    private Long execTime;

    /**
     * 描述
     */
    private String execDesc;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 请求URL
     */
    private String reqUri;

    /**
     * 参数
     */
    private String args;

    /**
     * 返回值
     */
    private String returnVal;



    public Long getId() {
        return id;
    }

    public Log setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Log setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public Log setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public Log setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
        return this;
    }

    public String getExecMethod() {
        return execMethod;
    }

    public Log setExecMethod(String execMethod) {
        this.execMethod = execMethod;
        return this;
    }

    public Long getExecTime() {
        return execTime;
    }

    public Log setExecTime(Long execTime) {
        this.execTime = execTime;
        return this;
    }

    public String getExecDesc() {
        return execDesc;
    }

    public Log setExecDesc(String execDesc) {
        this.execDesc = execDesc;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Log setStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreateTime() {
        return createTime;
    }

    public Log setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getReqUri() {
        return reqUri;
    }

    public Log setReqUri(String reqUri) {
        this.reqUri = reqUri;
        return this;
    }

    public String getArgs() {
        return args;
    }

    public Log setArgs(String args) {
        this.args = args;
        return this;
    }

    public String getReturnVal() {
        return returnVal;
    }

    public Log setReturnVal(String returnVal) {
        this.returnVal = returnVal;
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