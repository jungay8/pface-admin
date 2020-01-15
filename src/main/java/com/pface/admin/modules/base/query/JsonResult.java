package com.pface.admin.modules.base.query;

public class JsonResult<T> {
    private int code;
    private String msg;
    private T data;

    public JsonResult() {

    }

    public JsonResult(int code, String msg) {
        this(code, msg, null);
    }

    public JsonResult(T data) {
        this(1, "ok", data);
    }

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

//    public String toString() {
//        return new Gson().toJson(this);
//    }

}
