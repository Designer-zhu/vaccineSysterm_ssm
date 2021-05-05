package com.designal.vacc.service;

import com.designal.vacc.domain.Pre_vacc;
import com.designal.vacc.domain.Vaccine;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IVaccineService {

    //条件+分页查询
    List<Vaccine> viewVaccineByQuery(String query1, String search, int pageNow, int limit);

    //修改疫苗信息
    boolean update(Vaccine vaccine);

    //删除疫苗1
    boolean drop(String v_id);

    //插入
    boolean addOne(Pre_vacc pre_vacc);

    //通过疫苗名称模糊查询
    List<Vaccine> viewByV_name(String v_name, String v_spec);
}
