package com.pface.admin.modules.jiekou.utils;

public class HttpException extends  Exception {
    private int statusCode;
    private int apiErrorCode;
    public HttpException(){
        super();
    }
    public HttpException(int statusCode,String msg){
        super(msg);
        this.statusCode = statusCode;
    }
    public HttpException(int statusCode,int apiErrorCode,String msg){
        super(msg);
        this.statusCode = statusCode;
        this.apiErrorCode = apiErrorCode;
    }

    public String toJsonStr(){
        return "{\"statusCode\":"+ statusCode + ",\"apiErrorCode\":"+apiErrorCode+",\"message\":"+ getMessage() + "}";
    }
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getApiErrorCode() {
        return apiErrorCode;
    }

    public void setApiErrorCode(int apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }
}
