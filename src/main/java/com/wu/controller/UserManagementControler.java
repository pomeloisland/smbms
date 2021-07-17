package com.wu.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.wu.pojo.Role;
import com.wu.pojo.User;
import com.wu.service.RoleServiceImpl;
import com.wu.service.UserServiceImpl;
import com.wu.utils.Constants;
import com.wu.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/jsp")
public class UserManagementControler {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private PageSupport pageSupport;


    /**
     * 跳转到用户管理界面
     *
     * @param queryname     查询的名字
     * @param queryUserRole 查询的角色id
     * @param pageIndex     当前页
     * @param model
     * @return
     */
    @RequestMapping("/userManagement")
    public String queryUser(String queryname, String queryUserRole, String pageIndex, Model model) {

        //为以第一次访问做容错处理，默认角色为 0
        int queryRole = 0;
        //第一次走这个请求，一定是第一页，页面大小是固定的
        int pageSize = Constants.pageSize; //设置页面容量
        int currentPageNo = 1; //默认当前页码


        if (queryname == null) {
            queryname = "";
        }
        if (queryUserRole != null) {
            queryRole = Integer.parseInt(queryUserRole);
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "WEB-INF/jsp/error";
            }
        }

        //按照条件查询的总数量
        int totalCount = userService.getUserCount(queryname, queryRole);

        pageSupport.setCurrentPageNo(currentPageNo);    //当前页
        pageSupport.setPageSize(pageSize);              //页面大小
        pageSupport.setTotalCount(totalCount);          //总数量
        int totalPageCount = pageSupport.getTotalPageCount();  //总页面数量

        //控制首页和尾页
        //如果页面小于第一页，就显示第一页
        if (currentPageNo < 1) {
            currentPageNo = 1;
            //如果当前页面大于最后一页，当前页等于最后一页即可
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        //获取用户列表展示
        List<User> userList = null;
        // 名字  角色  下标  页面大小
        userList = userService.getUserList(queryname, queryRole, (currentPageNo - 1) * pageSize, pageSize);
        model.addAttribute("userList", userList);
        //获取角色列表
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);

        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);

        return "WEB-INF/jsp/userlist";
    }


    @RequestMapping("/viewUser")
    public String getUserById(String uid, Model model) {
        if (!StringUtils.isNullOrEmpty(uid)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(Integer.parseInt(uid));
            model.addAttribute("user", user);
        }
        return "WEB-INF/jsp/userview";
    }

    @RequestMapping("/toMdfUser")
    public String getUserById2(Model model, String uid) throws ParseException {
        if (!StringUtils.isNullOrEmpty(uid)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(Integer.parseInt(uid));
            model.addAttribute("user", user);
        }

        return "WEB-INF/jsp/usermodify";
    }

    @RequestMapping("/modifyuser")
    public String modifyUser(User user, HttpSession session) {
        System.out.println(user);
        user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        int i = userService.modifyUserByID(
                user.getUserName(),
                user.getGender(),
                user.getBirthday(),
                user.getPhone(),
                user.getAddress(),
                user.getUserRole(),
                user.getModifyBy(),
                user.getModifyDate(),
                user.getId());
        if (i > 0) {
            return "redirect:/jsp/userManagement";
        } else {
            return "WEB-INF/jsp/usermodify";
        }
    }

    @RequestMapping("/deleteuser")
    @ResponseBody
    public String deleteUser(int id) {
        HashMap<String, String> resultMap = new HashMap();
        if (id <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            if (userService.deleteUserByID(id) > 0) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }


    @RequestMapping("/adduser")
    public String toAddUserPage() {
        return "WEB-INF/jsp/useradd";
    }


    @RequestMapping("/ucexist")
    @ResponseBody
    public String selectUserisExists(String userCode) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            resultMap.put("userCode", "exist");
        } else {
            int i = userService.selectUserCodeExist(userCode);
            if (i != 0) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "notexist");
            }
        }
        //把resultMap转为json字符串 输出
        return JSONArray.toJSONString(resultMap);
    }


    @RequestMapping("/addusersave")
    public String addUser(
            @RequestParam("userCode") String userCode,
            @RequestParam("userName") String userName,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") String birthday,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("userRole") String userRole,
            HttpSession session
    ) {
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        user.setBirthday(birthday);
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());

        if (userService.addUser(user) > 0) {
            return "redirect:/jsp/userManagement";
        } else {
            return "WEB-INF/jsp/useradd";
        }
    }

}
