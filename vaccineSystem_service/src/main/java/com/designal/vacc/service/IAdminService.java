package com.designal.vacc.service;

import com.designal.vacc.domain.Admin;

public interface IAdminService {

    //管理员登录
    Admin login(String name, String password);

}
