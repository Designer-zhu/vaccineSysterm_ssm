package com.designal.vacc.service;

import com.designal.vacc.domain.Record;

import java.util.List;

public interface IRecordService {

    //插入
    boolean addNew(Record record);

    //查询
    List<Record> viewAll();

    //查询下发记录表中所有带 ‘同意’ 字段的处理结果
    List<Record> selectRecord(String result);

    //提交运输操作
    boolean submit(int id);

}
