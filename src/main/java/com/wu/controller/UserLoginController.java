package com.wu.controller;

import com.wu.pojo.User;
import com.wu.service.UserServiceImpl;
import com.wu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


@Controller
public class UserLoginController {

    @Autowired
    private UserServiceImpl userService;


    /**
     * 登录请求
     *
     * @param userCode
     * @param userPassword
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/login.do")
    public String login(String userCode, String userPassword, HttpSession session, Model model) {
        User loginUser = userService.getLoginUser(userCode, userPassword);
        if (loginUser != null) { //登录成功
            //放入session
            session.setAttribute(Constants.USER_SESSION, loginUser);
            //页面重定向请求（frame.jsp）
            return "redirect:/jsp/frame";
        } else {
            //页面转发（login.jsp）带出提示信息--转发
            model.addAttribute("error", "用户名或密码不正确");
            return "login";
        }
    }

    /**
     * 登出请求
     *
     * @param session
     * @return
     */
    @RequestMapping("/jsp/logout.do")
    public String loginOut(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);
        return "redirect:/login.jsp";
    }


    /**
     * 登录成功后会跳转到主页面
     *
     * @return
     */
    @RequestMapping("/jsp/frame")
    public String loginSeccess() {
        return "WEB-INF/jsp/frame";
    }


}
