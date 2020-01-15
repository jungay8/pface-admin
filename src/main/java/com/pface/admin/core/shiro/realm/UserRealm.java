package com.pface.admin.core.shiro.realm;

import com.google.code.kaptcha.Constants;
import com.pface.admin.core.shiro.NamePwdCapToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.UserService;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.queryRoles(username));
        authorizationInfo.setStringPermissions(userService.queryPermissions(username));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        NamePwdCapToken token = (NamePwdCapToken) authcToken;

        doCaptchaValidate(token.getCaptcha());

        String username = (String) token.getPrincipal();
        User user = null;
        if (!StringUtils.isEmpty(username)) {
            user = userService.queryOne(new User().setUsername(username));
        }
        if (user == null) {
            throw new UnknownAccountException();//没找到帐号
        }

        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getUsername() + user.getSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    private void doCaptchaValidate(String captchaCode) throws AuthenticationException {
        // 从session获取正确的验证码
        Session session = SecurityUtils.getSubject().getSession();

        String validateCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);

        // 若验证码为空或匹配失败则返回false
        if(captchaCode==null || validateCode==null){
            //如果验证码失败了，存储失败key属性
            throw new AuthenticationException("kaptchaValidateFailed");
        }
        captchaCode = captchaCode.toLowerCase();
        validateCode = validateCode.toLowerCase();
        if(!captchaCode.equals(validateCode)) {
            //如果验证码失败了，存储失败key属性
            throw new AuthenticationException("kaptchaValidateFailed");
        }

    }

}
