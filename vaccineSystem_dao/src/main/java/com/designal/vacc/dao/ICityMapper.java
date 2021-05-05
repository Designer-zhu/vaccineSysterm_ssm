package com.designal.vacc.dao;

import com.designal.vacc.domain.City;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//持久化City层
public interface ICityMapper {

    //通过p_id查询所属城市
    @Select("select * from city where p_id = #{p_id}")
    @Results({
            @Result(id = true,property = "c_id",column = "c_id"),
            @Result(property = "c_name",column = "c_name"),
    })
    List<City> selectCityList(@Param("p_id") int p_id);

    //通过p_id和c_id获取省份+城市One
    @Select("select p_name from province where p_id = #{p_id}")
    String selectProvince_CityOne(@Param("p_id") int p_id);

    //通过p_id和c_id获取省份+城市Two
    @Select("select c_name from city where c_id = #{c_id}")
    String selectProvince_CityTwo(@Param("c_id") int c_id);
}
