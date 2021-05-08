package com.designal.vacc.dao;

import com.designal.vacc.domain.Admin;
import com.designal.vacc.domain.Permission;
import com.designal.vacc.domain.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

//持久化Admin层
public interface IAdminMapper {

    //通过姓名和密码查询
    @Select("select * from admin where name = #{name} and password = #{password}")
    Admin selectOneByNameAndPassword(@Param(value = "name") String name,@Param(value = "password") String password);

    //查看所有角色列表
    @Select("select * from role")
    List<Role> viewAllRoles();

    //查看所有权限列表
    @Select("select * from permission")
    List<Permission> viewAllPermissions();

    //查看角色详情
    @Select("select * from role where id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IPermissionMapper.selectPermissionListByRoleId")),
    })
    Role viewRoleInfo(@Param(value = "id") Integer id);

    //添加角色
    @Insert("insert into role values(null,#{roleName},#{roleDesc})")
    int addRole(Role role);

    //删除用户-角色关系表中的数据
    @Delete("delete from user_role where roleId = #{roleId}")
    int deleteUserAndRole(@Param(value = "roleId")Integer roleId);

    //删除角色-权限关系表中的数据
    @Delete("delete from role_permission where roleId = #{roleId}")
    int deleteRoleAndPermission(@Param(value = "roleId")Integer roleId);

    //通过id删除角色表中的数据
    @Delete("delete from role where id = #{id}")
    int deleteRoleById(@Param(value = "id") Integer id);

    //新增权限
    @Insert("insert into permission values(null,#{permissionName},#{url})")
    int addPermission(Permission permission);

    //删除角色权限关系表中的数据
    @Delete("delete from role_permission where permissionId = #{permissionId}")
    int deleteRoleAndPermissionByPermissionId(@Param(value = "permissionId") Integer permissionId);

    //删除权限表中信息
    @Delete("delete from permission where id = #{id}")
    int deletePermissionById(Integer id);

    //查看剩余权限
    @Select("select * from role where id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "roleName",column = "roleName"),
            @Result(property = "roleDesc",column = "roleDesc"),
            @Result(property = "permissions",column = "id",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IPermissionMapper.selectOtherPermissionListByRoleId")),
    })
    Role viewOtherPermissions(@Param(value = "id")Integer id);

    //给角色添加权限
    @Insert("insert into role_permission values(#{roleId},#{permissionId})")
    int addNewPermissionToRole(@Param(value = "roleId")Integer roleId,@Param(value = "permissionId")Integer permissionId);

    //给用户添加角色
    @Insert("insert into user_role values(#{userId},#{roleId})")
    int addNewRoleToUser(@Param(value = "userId")Integer userId,@Param(value = "roleId")Integer roleId);
}
