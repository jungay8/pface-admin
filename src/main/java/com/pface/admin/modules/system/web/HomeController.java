package com.pface.admin.modules.system.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pface.admin.core.utils.BaseController;
import com.pface.admin.core.utils.Constants;
import com.pface.admin.modules.system.dto.ResourceDto;
import com.pface.admin.modules.system.service.ResourceService;
import com.pface.admin.modules.system.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/admin")
public class HomeController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/login"},method = {RequestMethod.POST,RequestMethod.GET})
    public String showLoginForm(HttpServletRequest req, Model model) {
        //doCaptchaValidate(req);
        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
        logger.info("begin to login");
        String error = null;
        if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } /*else if(ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
            error = "登陆失败次数过多";
        }*/ else if("kaptchaValidateFailed".equals(exceptionClassName)) {
            error = "验证码错误";
        }else if(AuthenticationException.class.getName().equals(exceptionClassName)) {
            error = "验证码错误8"; //todo
        }else if(exceptionClassName != null) {
            error = "其他错误：" + exceptionClassName;
        }
        model.addAttribute("error", error);
        return "system/login";
    }

    @RequestMapping(value={"/logout"},method = {RequestMethod.POST,RequestMethod.GET})
    public String logout(Model model) {
        SecurityUtils.getSubject().logout(); // session删除、RememberMe cookie
        return "system/login";
    }


    @RequestMapping(value={"/",""},method = {RequestMethod.POST,RequestMethod.GET})
    public String index(Model model) {

        String username = (String) SecurityUtils.getSubject().getPrincipal();

        Set<String> permissions = userService.queryPermissions(username);
        List<ResourceDto> menus = resourceService.findMenus(permissions);
        StringBuilder dom = new StringBuilder();
        getMenuTree(menus, Constants.MENU_ROOT_ID, dom);
        model.addAttribute(Constants.MENU_TREE, dom);
        return "base/main";
    }

    private List<ResourceDto> getMenuTree(List<ResourceDto> source, Long pid, StringBuilder dom) {
        List<ResourceDto> target = getChildResourceByPId(source, pid);
        target.forEach(res -> {
            dom.append("<li class='treeview'>");
            dom.append("<a href='" + res.getUrl() + "'>");
            dom.append("<i class='" + res.getIcon() + "'></i>");
            dom.append("<span>" + res.getName() + "</span>");
            if (Constants.SHARP.equals(res.getUrl())) {
                dom.append("<span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i> </span>");
            }
            dom.append("</a>");
            dom.append("<ul class='treeview-menu'>");
            res.setChildren(getMenuTree(source, res.getId(), dom));
            dom.append("</ul>");
            dom.append("</li>");
        });
        return target;
    }

    private List<ResourceDto> getChildResourceByPId(List<ResourceDto> source, Long pId) {
        List<ResourceDto> child = new ArrayList<>();
        source.forEach(res -> {
            if (pId.equals(res.getParentId())) {
                child.add(res);
            }
        });
        return child;
    }

    private void doCaptchaValidate(ServletRequest request) throws AuthenticationException {
        // 从session获取正确的验证码
        Session session = SecurityUtils.getSubject().getSession();
        //页面输入的验证码
        String captchaCode = WebUtils.getCleanParam(request, "captchaCode");
        String validateCode = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //判断验证码是否表单提交（允许访问）
       if ( !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            return ;
        }
        // 若验证码为空或匹配失败则返回false
        if(captchaCode==null || validateCode==null){
            //如果验证码失败了，存储失败key属性
            request.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");
            //throw new AuthenticationException("kaptchaValidateFailed");
        }
        captchaCode = captchaCode.toLowerCase();
        validateCode = validateCode.toLowerCase();
        if(!captchaCode.equals(validateCode)) {
            //如果验证码失败了，存储失败key属性
            request.setAttribute("shiroLoginFailure", "kaptchaValidateFailed");
            //throw new AuthenticationException("kaptchaValidateFailed");
        }

    }
}
