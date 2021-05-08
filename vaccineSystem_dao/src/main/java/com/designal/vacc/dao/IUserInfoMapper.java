package com.designal.vacc.dao;

import com.designal.vacc.domain.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

//持久化User层
public interface IUserInfoMapper {

    //插入用户至用户表One
    @Insert("insert into pro_user values(null,#{username},#{password},#{sex},#{email}," +
            "#{birthday},#{telephone},'',0,#{code},#{identity})")
    int insertOneOne(UserInfo userInfo);

    //插入用户至用户表Two
    @Insert("insert into city_user values(null,#{username},#{password},#{sex},#{email}," +
            "#{birthday},#{telephone},'',0,#{code},#{identity})")
    int insertOneTwo(UserInfo userInfo);

    //验证用户激活码 更新状态One
    @Update("update pro_user set state = 1 where code = #{code}")
    int updateStateOne(@Param("code") String code);

    //验证用户激活码 更新状态Two
    @Update("update city_user set state = 1 where code = #{code}")
    int updateStateTwo(@Param("code") String code);

    //通过username和password查询用户One
    @Select("select * from pro_user where username = #{username} and password = #{password}")
    UserInfo selectOneOne1(@Param("username") String username, @Param("password") String password);

    //通过username和password查询用户Two
    @Select("select * from city_user where username = #{username} and password = #{password}")
    UserInfo selectOneTwo1(@Param("username") String username,@Param("password") String password);

    //通过username查询用户One
    @Select("select * from pro_user where username = #{username}")
    UserInfo selectOneByUsernameOne(@Param("username") String username);

    //通过username查询用户Two
    @Select("select * from city_user where username = #{username}")
    UserInfo selectOneByUsernameTwo(@Param("username") String username);

    //根据用户id查询用户信息One
    @Select("select * from pro_user where userId = #{userId}")
    @Results({
            @Result(id = true,property = "userId",column = "userId"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "identity",column = "identity"),
            @Result(property = "email",column = "email"),
            @Result(property = "birthday",column = "birthday"),
            @Result(property = "telephone",column = "telephone"),
            @Result(property = "photo",column = "photo"),
            @Result(property = "state",column = "state"),
            @Result(property = "code",column = "code"),
            @Result(property = "roles",column = "userId",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IRoleMapper.selectRolesListByUserId")),
    })
    UserInfo selectOneOne2(@Param("userId") Integer userId);

    //根据用户id查询用户信息Two
    @Select("select * from city_user where userId = #{userId}")
    UserInfo selectOneTwo2(@Param("userId") Integer userId);

    //修改信息
    @Update("update pro_user " +
            "set username = #{username},sex = #{sex},email = #{email},birthday = #{birthday}," +
            "telephone = #{telephone},photo = #{photo} where userId = #{userId}")
    int updateOne(UserInfo userInfo);

    //更新密码
    @Update("update pro_user set password = #{password} where userId = #{userId}")
    int updatePassword(UserInfo userInfo);

    //查看省疾控中心人员列表
    @Select("select * from pro_user")
    List<UserInfo> selectProUserList();

    //查看市疾控中心人员列表
    @Select("select * from city_user")
    List<UserInfo> selectCityUserList();

    //根据姓名查询pro_user
    @Select("select * from pro_user where username = #{username}")
    @Results({
            @Result(id = true,property = "userId",column = "userId"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "identity",column = "identity"),
            @Result(property = "email",column = "email"),
            @Result(property = "birthday",column = "birthday"),
            @Result(property = "telephone",column = "telephone"),
            @Result(property = "photo",column = "photo"),
            @Result(property = "state",column = "state"),
            @Result(property = "code",column = "code"),
            @Result(property = "roles",column = "userId",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IRoleMapper.selectRolesListByUserId")),
    })
    UserInfo selectProOneByUsername(@Param("username") String username);

    //查看剩余角色用户信息
    @Select("select * from pro_user where userId = #{userId}")
    @Results({
            @Result(id = true,property = "userId",column = "userId"),
            @Result(property = "username",column = "username"),
            @Result(property = "password",column = "password"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "identity",column = "identity"),
            @Result(property = "email",column = "email"),
            @Result(property = "birthday",column = "birthday"),
            @Result(property = "telephone",column = "telephone"),
            @Result(property = "photo",column = "photo"),
            @Result(property = "state",column = "state"),
            @Result(property = "code",column = "code"),
            @Result(property = "roles",column = "userId",javaType = List.class,many = @Many(select = "com.designal.vacc.dao.IRoleMapper.selectOtherRoleListByUserId")),
    })
    UserInfo selectOtherRoleListByUserId(Integer userId);
}
