package com.designal.vacc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description 抽象用户类
 * @Author designal
 * @Date 2021/2/27 10:28
 */
public class UserInfo {

    private Integer userId;
    private String username;
    private String password;
    private String sex;
    private String identity; //身份
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String telephone;
    private String photo;
    private int state;//0--未激活 1--激活
    private String code;//激活码

    private List<Role> roles; //一个用户关联多个角色

    public UserInfo() {
    }

    public UserInfo(Integer userId, String username, String password, String sex, String identity, String email, Date birthday, String telephone, String photo, int state, String code, List<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.identity = identity;
        this.email = email;
        this.birthday = birthday;
        this.telephone = telephone;
        this.photo = photo;
        this.state = state;
        this.code = code;
        this.roles = roles;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", identity='" + identity + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", telephone='" + telephone + '\'' +
                ", photo='" + photo + '\'' +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", roles=" + roles +
                '}';
    }
}
