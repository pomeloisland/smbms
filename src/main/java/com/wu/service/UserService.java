package com.wu.service;

import com.wu.pojo.User;

import java.util.Date;
import java.util.List;


public interface UserService {
    User getLoginUser(String userCode, String userPassword);

    int updatePwd(int id, String userPassword);

    int getUserCount(String userName, int userRole);


    List<User> getUserList(String userName,
                           int userRole,
                           int currentPageNo,
                           int pageSize);

    User getUserById(int id);

    int modifyUserByID(
            String userName,
            int gender,
            String birthday,
            String phone,
            String address,
            int userRole,
            int modifyBy,
            Date date,
            int id
    );

    int deleteUserByID(int id);

    int selectUserCodeExist(String userCode);

    int addUser(User user);
}
