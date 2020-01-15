package com.pface.admin.core.filter;

import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.member.enums.CertTypeEnum;
import com.pface.admin.modules.member.po.MemberUser;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/3
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Component
public class FrontLoginInterceptor implements HandlerInterceptor {

        // 在请求处理之前调用,只有返回true才会执行请求
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {

        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo)session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
       //判断对象是否存在
        if(user!=null){
//            String url= request.getRequestURI();
//            if(url.contains("/front/certification")
//                    ||url.contains("/front/logout")
//                    ||url.contains("/front/cert/submitCert")){
//                return true;
//            }
//            if(!CertTypeEnum.CERTIFIED.getName().equalsIgnoreCase(user.getIsCert().getName())){
//               // 没认证全部导向认证界面
//                httpServletResponse.sendRedirect(request.getContextPath()+"/front/certification");
//                return false;
//            }else{
                return true;
//            }

        }else{
            String url= request.getRequestURI();
            if(url.contains("/front/user/login")){
                return true;
            }
          // 不存在则跳转到登录页
            httpServletResponse.sendRedirect(request.getContextPath()+"/front/login");
            return false;
        }
    }

    //    试图渲染之后执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //    在请求处理之后,视图渲染之前执行
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }




}
