package com.pface.admin.core.utils;

/**
 * @author cjbi,daniel.liu
 */
public enum ResultCodeEnum {

    OK("200", "处理成功"),
    BAD_REQUEST("400", "请求参数有误"),
    UNAUTHORIZED("401", "未授权"),
    PARAMS_MISS("483", "缺少接口中必填参数"),
    PARAM_ERROR("484", "参数非法"),
    FAILED_DEL_OWN("485", "不能删除自己"),
    FAILED_USER_ALREADY_EXIST("486", "该用户已存在"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误"),
    KAPTCHA_VALIDATE_FAILED("666","验证码错误"),
    NOT_IMPLEMENTED("501", "业务异常"),
    SESENSE_BOX_COLLECTION_ERROR("502","连接智能盒子网络错误"),
    INVOKE_SENSE_BOX_INTERFACE_ERROR("503", "调用智能盒子接口错误");
    private String code;
    private String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
