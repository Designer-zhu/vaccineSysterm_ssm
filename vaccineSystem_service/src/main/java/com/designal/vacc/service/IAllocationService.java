package com.designal.vacc.service;

import com.designal.vacc.domain.AllocationCity;

import java.util.List;

public interface IAllocationService {

    //查询全部市疾控中心
    List<AllocationCity> viewAll();

    //根据id查询市疾控中心的表名称
    String viewCity_table(int id);

    //根据不同的表名获取指定疫苗的库存量
    int viewV_number(String city_table,String v_name,String v_spec);

    //修改相应表中的数据
    boolean updateV_number(String city_table,String v_name,String v_spec,int v_number);

    //删除allocation中的记录
    boolean dropAllocation(String vaccineName,String vaccineSpec);
}
