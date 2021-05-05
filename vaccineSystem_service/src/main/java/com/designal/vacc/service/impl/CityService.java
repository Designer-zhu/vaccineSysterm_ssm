package com.designal.vacc.service.impl;

import com.designal.vacc.dao.ICityMapper;
import com.designal.vacc.domain.City;
import com.designal.vacc.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:23
 */
@Service
@Transactional
public class CityService implements ICityService {

    @Autowired
    private ICityMapper cityMapper;

    //查询省所属城市
    @Override
    public List<City> viewAllCityByP_id(int p_id) {
        return cityMapper.selectCityList(p_id);
    }

    //查询省和城市名
    @Override
    public String viewProCityName(int p_id, int c_id) {
        String s1 = cityMapper.selectProvince_CityOne(p_id);
        String s2 = cityMapper.selectProvince_CityTwo(c_id);
        return s1+s2;
    }
}
