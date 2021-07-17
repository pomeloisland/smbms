package com.wu.service;

import com.wu.mapper.UserMapper;
import com.wu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User getLoginUser(String userCode, String userPassword) {
        return userMapper.getLoginUser(userCode, userPassword);
    }

    @Override
    public int updatePwd(int id, String userPassword) {
        return userMapper.updatePwd(id, userPassword);
    }

    @Override
    public int getUserCount(String userName, int userRole) {
        return userMapper.getUserCount(userName, userRole);
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        return userMapper.getUserList(userName, userRole, currentPageNo, pageSize);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int modifyUserByID(String userName, int gender, String birthday, String phone, String address, int userRole, int modifyBy, Date date, int id) {
        return userMapper.modifyUserByID(userName, gender, birthday, phone, address, userRole, modifyBy, date, id);
    }

    @Override
    public int deleteUserByID(int id) {
        return userMapper.deleteUserByID(id);
    }

    @Override
    public int selectUserCodeExist(String userCode) {
        return userMapper.selectUserCodeExist(userCode);
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }


}
