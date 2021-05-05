package com.designal.vacc.dao;

import com.designal.vacc.domain.RequestMess;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//持久化RequestMess层
public interface IRequestMessMapper {

    //查询库中的所有请求One
    @Select("select * from requestmess")
    List<RequestMess> viewAllOne();

    //查询库中的所有请求Two
    @Select("select * from requestmess where id = #{query}")
    List<RequestMess> viewAllTwo(@Param("query") String query);

    //根据name和spec查询单条记录
    @Select("select * from requestmess where vaccineName = #{vaccineName} and vaccineSpec = #{vaccineSpec}")
    RequestMess viewOne(@Param("vaccineName") String vaccineName,@Param("vaccineSpec") String vaccineSpec);

    //插入
    @Insert("insert into requestmess values(null,#{vaccineName},#{location},#{requestDate},#{vaccineSpec},#{vaccineNumber})")
    int insert(@Param("requestMess") RequestMess requestMess);

    //根据id删除
    @Delete("delete from requestmess where id = #{id}")
    int deleteById(@Param("id") int id);

    //根据vaccineName 和 vaccineSpec删除
    @Delete("delete from requestmess where vaccineName = #{vaccineName} and vaccineSpec = #{vaccineSpec}")
    int deleteByNameAndSpec(@Param("vaccineName") String vaccineName,@Param("vaccineSpec") String vaccineSpec);

    //往allocation表中插入
    @Insert("insert into allocation values(#{id},#{vaccineName},#{location},#{requestDate},#{vaccineSpec},#{vaccineNumber})")
    int insertIntoAllocation(@Param("requestMess") RequestMess requestMess);

    //查询allocation中的所有调拨请求One
    @Select("select * from allocation")
    List<RequestMess> selectAllOne();

    //查询allocation中的所有调拨请求Two
    @Select("select * from allocation where id = #{query}")
    List<RequestMess> selectAllTwo(@Param("query") String query);
}
