package com.designal.vacc.dao;

import com.designal.vacc.domain.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

//持久化Record层
public interface IRecordMapper {

    //插入
    @Insert("insert into record values(#{id},#{vaccineName},#{location},#{requestDate}," +
            "#{result},#{processDate},#{vaccineSpec},#{vaccineNumber},1)")
    int insert(Record record);

    //查询
    @Select("select * from record")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "vaccineName",column = "vaccineName"),
            @Result(property = "location",column = "location"),
            @Result(property = "requestDate",column = "requestDate"),
            @Result(property = "result",column = "result"),
            @Result(property = "processDate",column = "processDate"),
            @Result(property = "vaccineSpec",column = "vaccineSpec"),
            @Result(property = "vaccineNumber",column = "vaccineNumber")
    })
    List<Record> selectAll();

    //查询下发记录表中所有带 ‘同意’ 字段的处理结果
    @Select("select * from record where result like concat('%',#{result},'%') and status = 1")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "vaccineName",column = "vaccineName"),
            @Result(property = "location",column = "location"),
            @Result(property = "requestDate",column = "requestDate"),
            @Result(property = "result",column = "result"),
            @Result(property = "processDate",column = "processDate"),
            @Result(property = "vaccineSpec",column = "vaccineSpec"),
            @Result(property = "vaccineNumber",column = "vaccineNumber"),
    })
    List<Record> selectRecord(@Param("result") String result);

    //将status字段改为0
    @Update("update record set status = 0 where id = #{id}")
    int updateStatusById(@Param("id") int id);
}
