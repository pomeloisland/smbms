package com.wu.interceptor;

import com.wu.utils.Constants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //放行：判断什么情况下登录
        //session不为空说明登录过没注销，并且没有关闭过窗口
        HttpSession session = request.getSession();
        if (session.getAttribute(Constants.USER_SESSION) != null) {
            return true;
        }

        //登录页面也要放行
        if (request.getRequestURI().contains("login")) {
            return true;
        }

        // 用户没有登陆跳转到登陆页面
        response.sendRedirect("/login.jsp");
        return false;
    }

}
