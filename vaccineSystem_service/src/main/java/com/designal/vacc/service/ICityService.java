package com.designal.vacc.service;

import com.designal.vacc.domain.City;

import java.util.List;

public interface ICityService {

    //查询省所属城市
    List<City> viewAllCityByP_id(int p_id);

    //查询省和城市名
    String viewProCityName(int p_id,int c_id);
}
