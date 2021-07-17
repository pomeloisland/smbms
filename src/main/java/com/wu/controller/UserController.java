package com.wu.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.wu.pojo.User;
import com.wu.service.UserServiceImpl;
import com.wu.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/jsp")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 跳转到修改密码的页面请求
     *
     * @return
     */
    @RequestMapping("/pwdmodify")
    public String toPasswordModify() {
        return "WEB-INF/jsp/pwdmodify";
    }


    /**
     * 利用ajax来判断旧密码是否正确
     *
     * @param session
     * @param oldpassword
     * @return
     */
    @RequestMapping("/oldpassword")
    @ResponseBody
    public String oldPasswordByAiax(HttpSession session, String oldpassword) {
        Object user = session.getAttribute(Constants.USER_SESSION);

        Map<String, String> resultMap = new HashMap();
        if (null == user) {//session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {//旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) user).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {//旧密码输入不正确
                resultMap.put("result", "false");
            }
        }

        return JSONArray.toJSONString(resultMap);
    }

    /**
     * 点击修改密码请求来修改密码，并移除session
     *
     * @param session
     * @param newpassword
     * @param model
     * @return
     */
    @RequestMapping("/savepwd")
    public String savePassword(HttpSession session, String newpassword, Model model) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        int flag = 0;
        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            flag = userService.updatePwd(((User) o).getId(), newpassword);
            if (flag > 0) {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
            } else {
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        //修改成功后转发就行了
        return "WEB-INF/jsp/pwdmodify";
    }
    

}
