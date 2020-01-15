package com.pface.admin.core.shiro;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/11
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class NamePwdCapToken extends org.apache.shiro.authc.UsernamePasswordToken{

    private String captcha;

    public NamePwdCapToken() {
        super();
    }

    public NamePwdCapToken(String username, char[] password,
                                 boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;

    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }


}
