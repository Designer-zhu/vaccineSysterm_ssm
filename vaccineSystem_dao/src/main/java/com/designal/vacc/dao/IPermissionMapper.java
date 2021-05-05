package com.designal.vacc.dao;

import com.designal.vacc.domain.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IPermissionMapper {

    //根据角色Id查询对象的权限信息表
    @Select("select * from permission where id in(select permissionId from role_permission where roleId = #{roleId})")
    List<Permission> selectPermissionListByRoleId(int roleId);
}
