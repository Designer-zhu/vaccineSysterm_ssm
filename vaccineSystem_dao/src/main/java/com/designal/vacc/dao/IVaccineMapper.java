package com.designal.vacc.dao;

import com.designal.vacc.domain.Pre_vacc;
import com.designal.vacc.domain.Vaccine;
import org.apache.ibatis.annotations.*;

import java.util.List;

//持久化Vaccine层
public interface IVaccineMapper {

    //通过疫苗分类查询所属疫苗
    @Select("select * from vaccine where c_id = #{c_id}")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> selectVaccineListByC_id(String c_id);

    //条件 + 分页查询One
    @Select("select * from vaccine")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> viewAllVaccineByPageOne();

    //条件 + 分页查询Two
    @Select("select * from vaccine where c_id = #{query1}")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> viewAllVaccineByPageTwo(int query1);

    //条件 + 分页查询Three
    @Select("select * from vaccine where v_id = #{search}")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> viewAllVaccineByPageThree(int search);

    //条件 + 分页查询Four
    @Select("select * from vaccine where v_id = #{search} and c_id = #{query1}")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> viewAllVaccineByPageFour(@Param("query1") int query1,@Param("search") int search);

    //修改商品
    @Update("update vaccine set v_name = #{v_name},v_detail = #{v_detail},v_site = #{v_site},v_date = #{v_date},v_term = #{v_term}," +
            "v_spec = #{v_spec},v_batch = #{v_batch} where v_id = #{v_id}")
    int update(Vaccine vaccine);

    //通过v_id查询单个疫苗
    @Select("select * from vaccine where v_id = #{v_id}")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    Vaccine selectOneByV_id(int v_id);

    //通过v_id删除疫苗
    @Delete("delete from vaccine where v_id = #{v_id}")
    int deleteOneByV_id(String v_id);

    //插入
    @Insert("insert into vaccine values(null,#{v_name},#{v_detail},#{v_site},#{v_date}," +
            "#{v_term},#{v_spec},#{v_batch},#{c_id})")
    int insertOne(Pre_vacc pre_vacc);

    //通过疫苗名称和规格模糊查询
    @Select("select * from vaccine where v_name like concat('%',#{v_name},'%') and v_spec like concat('%',#{v_spec},'%')")
    @Results({
            @Result(id = true,property = "v_id",column = "v_id"),
            @Result(property = "v_name",column = "v_name"),
            @Result(property = "v_detail",column = "v_detail"),
            @Result(property = "v_site",column = "v_site"),
            @Result(property = "v_date",column = "v_date"),
            @Result(property = "v_term",column = "v_term"),
            @Result(property = "v_spec",column = "v_spec"),
            @Result(property = "v_batch",column = "v_batch"),
    })
    List<Vaccine> selectByV_name(@Param("v_name") String v_name,@Param("v_spec") String v_spec);
}
