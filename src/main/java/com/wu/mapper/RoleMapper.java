package com.wu.mapper;

import com.wu.pojo.Role;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface RoleMapper {

    /**
     * 获取角色列表
     *
     * @return
     */
    List<Role> getRoleList();

}
