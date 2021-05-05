package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IRecordMapper;
import com.designal.vacc.domain.Record;
import com.designal.vacc.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:32
 */
@Service
@Transactional
public class RecordService implements IRecordService {

    @Autowired
    private IRecordMapper recordMapper;

    //插入
    @Override
    public boolean addNew(Record record) {
        return recordMapper.insert(record)>0;
    }

    //查询
    @Override
    public List<Record> viewAll() {
        return recordMapper.selectAll();
    }

    //查询下发记录表中所有带 ‘同意’ 字段的处理结果
    @Override
    public List<Record> selectRecord(String result) {
        return recordMapper.selectRecord(result);
    }

    //提交运输操作
    @Override
    public boolean submit(int id) {
        return recordMapper.updateStatusById(id)>0;
    }
}
