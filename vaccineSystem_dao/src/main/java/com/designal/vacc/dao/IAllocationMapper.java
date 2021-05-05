package com.designal.vacc.dao;

import com.designal.vacc.domain.AllocationCity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//持久化Allocation层
public interface IAllocationMapper {

    //查询全部市疾控中心
    @Select("select * from allocation_city")
    List<AllocationCity> viewAll();

    //根据id查询市疾控中心的表名称
    @Select("select city_table from allocation_city where id = #{id}")
    String selectTableName(@Param("id") int id);

    //根据不同的表名获取指定疫苗的库存量One
    @Select("select v_number from xi_an where v_name = #{v_name} and v_spec = #{v_name}")
    int selectV_numberOne(@Param("v_name") String v_name,@Param("v_spec")String v_spec);

    //根据不同的表名获取指定疫苗的库存量Two
    @Select("select v_number from xian_yang where v_name = #{v_name} and v_spec = #{v_name}")
    int selectV_numberTwo(@Param("v_name") String v_name,@Param("v_spec") String v_spec);

    //修改相应表中的数据One
    @Select("update xi_an set v_number = #{v_number} where v_name = #{v_name} and v_spec = #{v_spec}")
    int setV_numberOne(@Param("v_number") int v_number,@Param("v_name") String v_name,@Param("v_spec") String v_spec);

    //修改相应表中的数据Two
    @Select("update xian_yang set v_number = #{v_number} where v_name = #{v_name} and v_spec = #{v_spec}")
    int setV_numberTwo(@Param("v_number") int v_number,@Param("v_name") String v_name,@Param("v_spec") String v_spec);

    //删除allocation中的请求记录
    @Select("delete from allocation where vaccineName = #{vaccineName} and vaccineSpec = #{vaccineSpec}")
    int deleteOne(@Param("vaccineName") String vaccineName,@Param("vaccineSpec") String vaccineSpec);
}
