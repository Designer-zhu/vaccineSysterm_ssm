package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IAdminMapper;
import com.designal.vacc.domain.Admin;
import com.designal.vacc.domain.Permission;
import com.designal.vacc.domain.Role;
import com.designal.vacc.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:12
 */
@Service
@Transactional
public class AdminService implements IAdminService {

    @Autowired
    private IAdminMapper adminMapper;

    //管理员登录
    @Override
    public Admin login(String name, String password) {
        return adminMapper.selectOneByNameAndPassword(name, password);
    }

    //查看所有角色列表
    @Override
    public List<Role> viewAllRoles() {
        return adminMapper.viewAllRoles();
    }

    //查看所有权限列表
    @Override
    public List<Permission> viewAllPermissions() {
        return adminMapper.viewAllPermissions();
    }

    //查看角色详情
    @Override
    public Role viewRoleInfo(Integer id) {
        return adminMapper.viewRoleInfo(id);
    }

    //添加角色
    @Override
    public boolean addRole(Role role) {
        return adminMapper.addRole(role)>0;
    }

    //通过角色id删除角色
    @Override
    public void deleteRoleById(Integer id) {
        //删除用户-角色关系表中的数据
        adminMapper.deleteUserAndRole(id);
        //删除角色-权限关系表中的数据
        adminMapper.deleteRoleAndPermission(id);
        //通过id删除角色表中的数据
        adminMapper.deleteRoleById(id);
    }

    //添加权限
    @Override
    public boolean addPermission(Permission permission) {
        return adminMapper.addPermission(permission)>0;
    }

    //根据权限id删除权限
    @Override
    public void deletePermissionById(Integer id) {
        //删除角色-权限关系表中的数据
        adminMapper.deleteRoleAndPermissionByPermissionId(id);
        //通过id删除权限表中的数据
        adminMapper.deletePermissionById(id);
    }

    //查看角色剩余权限
    @Override
    public Role viewOtherPermissionsById(int id) {
        return adminMapper.viewOtherPermissions(id);
    }

    //给角色添加权限
    @Override
    public boolean addNewPermissionToRole(Integer roleId, int[] permissionIds) {
        if(permissionIds!=null && permissionIds.length!=0){
            for (int permissionId : permissionIds) {
                adminMapper.addNewPermissionToRole(roleId,permissionId);
            }
            return true;
        }
        return false;
    }

    //给用户添加角色
    @Override
    public boolean addNewRoleToUser(int userId, int[] roleIds) {
        if(roleIds!=null && roleIds.length!=0){
            for (int roleId : roleIds) {
                adminMapper.addNewRoleToUser(userId,roleId);
            }
            return true;
        }
        return false;
    }
}
