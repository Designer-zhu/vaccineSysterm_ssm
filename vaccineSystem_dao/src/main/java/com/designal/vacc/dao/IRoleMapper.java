package com.designal.vacc.dao;

import com.designal.vacc.domain.Role;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IRoleMapper {

    //根据用户id查询角色详情
    @Select("select * from role where id in (select roleId from user_role where userId = #{userId})")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IPermissionMapper.selectPermissionListByRoleId"))
    })
    List<Role> selectRolesListByUserId(int userId);

    //根据用户id查询其它剩余角色
    @Select("select * from role where id not in (select roleId from user_role where userId = #{userId})")
    List<Role> selectOtherRoleListByUserId(int userId);
}
