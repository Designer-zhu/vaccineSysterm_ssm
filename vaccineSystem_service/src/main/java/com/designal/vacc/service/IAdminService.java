package com.designal.vacc.service;

import com.designal.vacc.domain.Admin;
import com.designal.vacc.domain.Permission;
import com.designal.vacc.domain.Role;

import java.util.List;

public interface IAdminService {

    //管理员登录
    Admin login(String name, String password);

    //查看所有角色列表
    List<Role> viewAllRoles();

    //查看所有权限列表
    List<Permission> viewAllPermissions();

    //查看角色详情
    Role viewRoleInfo(Integer id);

    //添加角色
    boolean addRole(Role role);

    //通过id删除角色
    void deleteRoleById(Integer id);

    //添加权限
    boolean addPermission(Permission permission);

    //删除权限
    void deletePermissionById(Integer id);

    //查看角色下的剩余权限
    Role viewOtherPermissionsById(int id);

    //给角色添加权限
    boolean addNewPermissionToRole(Integer id,int[] permissionIds);

    //给用户添加角色
    boolean addNewRoleToUser(int userId, int[] roleIds);
}
