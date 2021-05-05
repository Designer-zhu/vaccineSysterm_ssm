package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IPre_vaccMapper;
import com.designal.vacc.domain.Pre_vacc;
import com.designal.vacc.service.IPre_vaccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:26
 */
@Service
@Transactional
public class Pre_vaccService implements IPre_vaccService {

    @Autowired
    private IPre_vaccMapper pre_vaccMapper;

    //添加
    @Override
    public boolean addNew(Pre_vacc pre_vacc) {
        return pre_vaccMapper.insert(pre_vacc)>0;
    }

    //查询
    @Override
    public List<Pre_vacc> viewAll(String query) {
        List<Pre_vacc> pre_vaccs = null;
        if(query == null || query.equals("")){
            pre_vaccs = pre_vaccMapper.selectAllOne();
        }else {
            pre_vaccs = pre_vaccMapper.selectAllTwo(query);
        }
        return pre_vaccs;
    }

    //执行删除
    @Override
    public boolean drop(String v_id) {
        return pre_vaccMapper.deleteByV_id(v_id)>0;
    }

    //通过v_id查询单个
    @Override
    public Pre_vacc viewOne(int v_id) {
        return pre_vaccMapper.selectOneByV_id(v_id);
    }
}
