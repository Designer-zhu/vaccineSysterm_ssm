package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IUserInfoMapper;
import com.designal.vacc.domain.Role;
import com.designal.vacc.domain.UserInfo;
import com.designal.vacc.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:41
 */
@Service(value = "userInfoService")
@Transactional
public class UserInfoService implements IUserInfoService {

    @Autowired
    private IUserInfoMapper userInfoMapper;

   /* @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;*/

    //注册
    /*@Override
    public boolean register(UserInfo userInfo) {
        //密码加密存储
        userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
        //判断用户类型
        if(userInfo.getIdentity().equals("pro_user")){
            return userInfoMapper.insertOneOne(userInfo)>0;
        }else if(userInfo.getIdentity().equals("city_user")){
            return userInfoMapper.insertOneTwo(userInfo)>0;
        }
        return false;
    }*/

    /*public static void main(String[] args) {
        BCryptPasswordEncoder by = new BCryptPasswordEncoder();
        String encode = by.encode("111111");
        System.out.println(encode);
    }*/

    @Override
    public boolean register(UserInfo userInfo) {
        return false;
    }

    //激活验证
    @Override
    public boolean activeState(String code, String identity) {
        //判断用户类型
        if(identity.equals("pro_user")){
            return userInfoMapper.updateStateOne(code)>0;
        }else if(identity.equals("city_user")){
            return userInfoMapper.updateStateTwo(code)>0;
        }
        return false;
    }

    //用户名唯一校验
    @Override
    public boolean validate(String username, String identity) {
        UserInfo userInfo = null;
        //判断用户类型
        if(identity.equals("pro_user")){
            userInfo = userInfoMapper.selectOneByUsernameOne(username);
        }else if(identity.equals("city_user")){
            userInfo = userInfoMapper.selectOneByUsernameTwo(username);
        }
        return userInfo !=null;
    }

    //登录(city_user)
    @Override
    public UserInfo login(String username, String password, String identity) {
        //判断用户类型
        if(identity.equals("pro_user")){
            return userInfoMapper.selectOneOne1(username, password);
        }else if(identity.equals("city_user")){
            return userInfoMapper.selectOneTwo1(username, password);
        }
        return null;
    }

    //登录(pro_user)
    //认证
    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = userInfoMapper.selectProOneByUsername(username);
        User user = new User(username, userInfo.getPassword(),
                userInfo.getState() == 1 ? true : false,
                true, true, true,
                getAuthorities(userInfo.getRoles()));
        return user;

    }*/

    //根据当前用户所有角色的名称，拼接
    public Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName());
            authorities.add(authority);
        }
        return authorities;
    }

    //根据用户id查询用户信息
    @Override
    public UserInfo viewOneByUser_id(String user_id, String identity) {
        //判断用户类型
        if(identity.equals("pro_user")){
            return userInfoMapper.selectOneOne2(user_id);
        }else if(identity.equals("city_user")){
            return userInfoMapper.selectOneTwo2(user_id);
        }
        return null;
    }

    //更新用户信息
    @Override
    public boolean updateUser(UserInfo userInfo) {
        return userInfoMapper.updateOne(userInfo)>0;
    }

    //更新密码
    @Override
    public boolean updatePassword(UserInfo userInfo) {
        return userInfoMapper.updatePassword(userInfo)>0;
    }

    //查看省疾控中心人员列表
    @Override
    public List<UserInfo> viewProUserList() {
        return userInfoMapper.selectProUserList();
    }

    //查看市疾控中心人员列表
    @Override
    public List<UserInfo> viewCityUserList() {
        return userInfoMapper.selectCityUserList();
    }
}
