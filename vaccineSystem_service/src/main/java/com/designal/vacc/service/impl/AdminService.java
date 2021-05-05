package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IAdminMapper;
import com.designal.vacc.domain.Admin;
import com.designal.vacc.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
