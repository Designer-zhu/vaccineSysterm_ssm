package com.designal.vacc.dao;

import com.designal.vacc.domain.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//持久化Admin层
public interface IAdminMapper {

    //通过姓名和密码查询
    @Select("select * from admin where name = #{name} and password = #{password}")
    Admin selectOneByNameAndPassword(@Param(value = "name") String name,@Param(value = "password") String password);

}
