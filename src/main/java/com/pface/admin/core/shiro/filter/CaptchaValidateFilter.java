package com.pface.admin.core.shiro.filter;

import com.google.code.kaptcha.Constants;
import com.pface.admin.core.shiro.NamePwdCapToken;
import com.pface.admin.core.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/11
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
public class CaptchaValidateFilter  extends FormAuthenticationFilter {

    private String captchaParam = "captchaCode"; //前台提交的验证码参数名

    private String failureKeyAttribute = "shiroLoginFailure";  //验证失败后存储到的属性名


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password==null){
            password = "";
        }
        boolean rememberMe = isRememberMe(request);
        String host = StringUtils.getRemoteAddr((HttpServletRequest)request);
        String captcha = getCaptchaCode(request);

        return new NamePwdCapToken(username, password.toCharArray(), rememberMe, host, captcha);
    }

    /**
     * 获取登录用户名
     */
    @Override
    protected String getUsername(ServletRequest request) {
        String username = super.getUsername(request);
        if (StringUtils.isBlank(username)){
            username = StringUtils.toString(request.getAttribute(getUsernameParam()), StringUtils.EMPTY);
        }
        return username;
    }

    /**
     * 获取登录密码
     */
    @Override
    protected String getPassword(ServletRequest request) {
        String password = super.getPassword(request);
        if (StringUtils.isBlank(password)){
            password = StringUtils.toString(request.getAttribute(getPasswordParam()), StringUtils.EMPTY);
        }
        return password;
    }

    /**
     * 获取记住我
     */
    @Override
    protected boolean isRememberMe(ServletRequest request) {
        String isRememberMe = WebUtils.getCleanParam(request, getRememberMeParam());
        if (StringUtils.isBlank(isRememberMe)){
            isRememberMe = StringUtils.toString(request.getAttribute(getRememberMeParam()), StringUtils.EMPTY);
        }
        return StringUtils.toBoolean(isRememberMe);
    }

    /**
     * 登录成功之后跳转URL
     */
    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }



    private void doCaptchaValidate(ServletRequest request, ServletResponse response) throws AuthenticationException {
        // 从session获取正确的验证码
        Session session = SecurityUtils.getSubject().getSession();
        //页面输入的验证码
        String captchaCode = getCaptchaCode(request);
        String validateCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //判断验证码是否表单提交（允许访问）
        /*if ( !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return true;
        }*/
        // 若验证码为空或匹配失败则返回false
        if(captchaCode==null || validateCode==null){
            //如果验证码失败了，存储失败key属性
            request.setAttribute(failureKeyAttribute, "kaptchaValidateFailed");
            throw new AuthenticationException("kaptchaValidateFailed");
        }
        captchaCode = captchaCode.toLowerCase();
        validateCode = validateCode.toLowerCase();
         if(!captchaCode.equals(validateCode)) {
            //如果验证码失败了，存储失败key属性
            request.setAttribute(failureKeyAttribute, "kaptchaValidateFailed");
             throw new AuthenticationException("kaptchaValidateFailed");
          }

    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request,
                                        ServletResponse response) throws Exception {
//		Principal p = UserUtils.getPrincipal();
//		if (p != null && !p.isMobileLogin()){
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
//		}else{
//			super.issueSuccessRedirect(request, response);
//		}
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request, ServletResponse response) {
        String className = e.getClass().getName(), message = "";
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)){
            message = "用户或密码错误, 请重试.";
        }else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")){
            message = StringUtils.replace(e.getMessage(), "msg:", "");
        }else{
            message = "系统出现点问题，请稍后再试！";
            e.printStackTrace(); // 输出到控制台
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute("message", message);
        return true;
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }


    public String getCaptchaCode(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

}
