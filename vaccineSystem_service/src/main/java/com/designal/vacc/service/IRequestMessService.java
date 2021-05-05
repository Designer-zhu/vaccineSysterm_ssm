package com.designal.vacc.service;

import com.designal.vacc.domain.RequestMess;

import java.util.List;

public interface IRequestMessService {

    //查询库中所有
    List<RequestMess> viewAll(String query);

    List<RequestMess> viewAllAllocation(String query);

    //查询单个
    RequestMess viewOne(String vaccineName,String vaccineSpec);

    //根据id删除
    boolean dropById(int id);

    //根据vaccineName,vaccineSpec删除
    boolean dropByNameAndSpec(String vaccineName,String vaccineSpec);

    //往allocation表中插入
    boolean addToAllocation(RequestMess requestMess);

    //往requestmess表中插入
    boolean addToRequestMess(RequestMess requestMess);

}
