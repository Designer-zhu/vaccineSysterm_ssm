package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IRequestMessMapper;
import com.designal.vacc.domain.RequestMess;
import com.designal.vacc.service.IRequestMessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:35
 */
@Service
@Transactional
public class RequestMessService implements IRequestMessService {

    @Autowired
    private IRequestMessMapper requestMessMapper;

    //查询库中所有
    @Override
    public List<RequestMess> viewAll(String query) {
        List<RequestMess> requestMessList = null;
        if(query==null || query.equals("")){
            requestMessList = requestMessMapper.viewAllOne();
        }else {
            requestMessList = requestMessMapper.viewAllTwo(query);
        }
        return requestMessList;
    }

    @Override
    public List<RequestMess> viewAllAllocation(String query) {
        List<RequestMess> requestMessList = null;
        if(query==null || query.equals("")){
            requestMessList = requestMessMapper.selectAllOne();
        }else {
            requestMessList = requestMessMapper.selectAllTwo(query);
        }
        return requestMessList;
    }

    //查询单个
    @Override
    public RequestMess viewOne(String vaccineName, String vaccineSpec) {
        return requestMessMapper.viewOne(vaccineName, vaccineSpec);
    }

    //根据id删除
    @Override
    public boolean dropById(int id) {
        return requestMessMapper.deleteById(id)>0;
    }

    //根据vaccineName,vaccineSpec删除
    @Override
    public boolean dropByNameAndSpec(String vaccineName, String vaccineSpec) {
        return requestMessMapper.deleteByNameAndSpec(vaccineName, vaccineSpec)>0;
    }

    //往allocation表中插入
    @Override
    public boolean addToAllocation(RequestMess requestMess) {
        return requestMessMapper.insertIntoAllocation(requestMess)>0;
    }

    //往requestmess表中插入
    @Override
    public boolean addToRequestMess(RequestMess requestMess) {
        return requestMessMapper.insert(requestMess)>0;
    }
}
