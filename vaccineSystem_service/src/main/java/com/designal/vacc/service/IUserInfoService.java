package com.designal.vacc.service;

import com.designal.vacc.domain.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserInfoService /*extends UserDetailsService*/ {

    //注册
    boolean register(UserInfo userInfo);

    //激活验证
    boolean activeState(String code,String identity);

    //用户名唯一校验
    boolean validate(String username, String identity);

    //登录
    UserInfo login(String username, String password, String identity);

    //根据用户id查询用户信息
    UserInfo viewOneByUser_id(String user_id, String identity);

    //更新用户信息
    boolean updateUser(UserInfo userInfo);

    //更新密码
    boolean updatePassword(UserInfo userInfo);

    //查看省疾控中心人员列表
    List<UserInfo> viewProUserList();

    //查看市疾控中心人员列表
    List<UserInfo> viewCityUserList();


}
