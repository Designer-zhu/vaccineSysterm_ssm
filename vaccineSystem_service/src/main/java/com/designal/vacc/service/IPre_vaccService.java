package com.designal.vacc.service;

import com.designal.vacc.domain.Pre_vacc;

import java.util.List;

public interface IPre_vaccService {

    //添加
    boolean addNew(Pre_vacc pre_vacc);

    //查询
    List<Pre_vacc> viewAll(String query);

    //执行删除
    boolean drop(String v_id);

    //通过v_id查询单个
    Pre_vacc viewOne(int v_id);
}
