package com.designal.vacc.domain;

import java.util.List;

/**
 * @Description 角色
 * @Author designal
 * @Date 2021/3/25 21:46
 */
public class Role {

    private Integer id;
    private String roleName;
    private String roleDesc;
    private List<Permission> permissions;
    private List<UserInfo> users;

    public Role() {
    }

    public Role(Integer id, String roleName, String roleDesc, List<Permission> permissions, List<UserInfo> users) {
        this.id = id;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
        this.permissions = permissions;
        this.users = users;
    }

    /**
     * 获取
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取
     * @return roleName
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    /**
     * 获取
     * @return roleDesc
     */
    public String getRoleDesc() {
        return roleDesc;
    }

    /**
     * 设置
     * @param roleDesc
     */
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    /**
     * 获取
     * @return permissions
     */
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     * 设置
     * @param permissions
     */
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    /**
     * 获取
     * @return users
     */
    public List<UserInfo> getUsers() {
        return users;
    }

    /**
     * 设置
     * @param users
     */
    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public String toString() {
        return "Role{id = " + id + ", roleName = " + roleName + ", roleDesc = " + roleDesc + ", permissions = " + permissions + ", users = " + users + "}";
    }
}
