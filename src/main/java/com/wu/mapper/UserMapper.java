package com.wu.mapper;

import com.wu.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;



@Component
public interface UserMapper {

    /**
     * 登录的时候用来获取用户信息
     *
     * @param userCode
     * @param userPassword
     * @return
     */
    User getLoginUser(@Param("userCode") String userCode, @Param("userPassword") String userPassword);

    /**
     * 修改密码
     *
     * @param id
     * @param userPassword
     * @return
     */
    int updatePwd(@Param("id") int id, @Param("userPassword") String userPassword);


    int getUserCount(@Param("userName") String userName, @Param("userRole") int userRole);


    List<User> getUserList(@Param("userName") String userName,
                           @Param("userRole") int userRole,
                           @Param("currentPageNo") int currentPageNo,
                           @Param("pageSize") int pageSize);

    User getUserById(@Param("id") int id);

    int modifyUserByID(
            @Param("userName") String userName,
            @Param("gender") int gender,
            @Param("birthday") String birthday,
            @Param("phone") String phone,
            @Param("address") String address,
            @Param("userRole") int userRole,
            @Param("modifyBy") int modifyBy,
            @Param("modifyDate") Date date,
            @Param("id") int id
    );

    int deleteUserByID(@Param("id") int id);


    int selectUserCodeExist(@Param("userCode") String userCode);

    int addUser(User user);

}
