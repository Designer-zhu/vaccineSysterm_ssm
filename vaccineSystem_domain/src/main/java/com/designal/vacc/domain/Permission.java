package com.designal.vacc.domain;

import java.util.List;

/**
 * @Description 权限
 * @Author designal
 * @Date 2021/3/25 21:47
 */
public class Permission {

    private Integer id;
    private String permissionName;
    private List<Role> roles;


    public Permission() {
    }

    public Permission(Integer id, String permissionName, List<Role> roles) {
        this.id = id;
        this.permissionName = permissionName;
        this.roles = roles;
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
     * @return permissionName
     */
    public String getPermissionName() {
        return permissionName;
    }

    /**
     * 设置
     * @param permissionName
     */
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    /**
     * 获取
     * @return roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * 设置
     * @param roles
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String toString() {
        return "Permission{id = " + id + ", permissionName = " + permissionName + ", roles = " + roles + "}";
    }
}
