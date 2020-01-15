package com.pface.admin.modules.jiekou.Constants;

public class Faceconstant {
    public final static String uri = "http://192.168.2.10/api/json";
    public final static String websock_uri = "ws://192.168.2.10/ws/";

    //盒子设备默认的用户名和密码
    public final static String boxusername_default = "admin";
    public final static String boxuserpassword_default = "admin";

    //faceuser默认的登录密码
    public final static String faceuser_password_default = "123456";

    //sysuser默认的登录密码
    public final static String sysuser_password_default = "123456";
    //sysuser默认的角色ids
    public final static String sysuser_roleids_default = "6";
    //sysuser默认的组织id
    public final static Long sysuser_organizationId_default = 1L;
    //sysuser默认的分组
    public final static String sysuser_groupIds_default = "1";

    //登录智能盒子的session过期时间（单位：秒）：30分钟-1分钟
    public final static long face_login_expire = 1740;
    public final static String face_login_expire_key = "face_login_expire_key";

}
