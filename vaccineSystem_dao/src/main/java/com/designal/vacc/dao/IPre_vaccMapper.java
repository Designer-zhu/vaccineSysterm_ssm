package com.designal.vacc.dao;

import com.designal.vacc.domain.Pre_vacc;
import org.apache.ibatis.annotations.*;

import java.util.List;

//持久化Pre_vacc层
public interface IPre_vaccMapper {
    
    //预插入
    @Insert("insert into vacc_pre_storage values(#{v_name},#{v_detail},#{v_site},#{v_date}," +
            "#{v_term},#{v_spec},#{v_batch},#{c_id},null)")
    int insert (Pre_vacc pre_vacc);

    //查询预存库One
    @Select("select * from vacc_pre_storage")
    @Results({
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "c_id",column = "c_id"),
            @Result(id = true,property = "v_id",column = "v_id"),
    })
    List<Pre_vacc> selectAllOne();

    //查询预存库Two
    @Select("select * from vacc_pre_storage where v_id = #{query}")
    @Results({
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "c_id",column = "c_id"),
            @Result(id = true,property = "v_id",column = "v_id"),
    })
    List<Pre_vacc> selectAllTwo(@Param("query") String query);

    //通过v_id进行删除
    @Delete("delete from vacc_pre_storage where v_id = #{v_id}")
    int deleteByV_id(@Param("v_id") String v_id);

    //通过v_id查询单个
    @Select("select * from vacc_pre_storage where v_id = #{v_id}")
    @Results({
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "c_id",column = "c_id"),
            @Result(id = true,property = "v_id",column = "v_id"),
    })
    Pre_vacc selectOneByV_id(@Param("v_id") int v_id);


}
