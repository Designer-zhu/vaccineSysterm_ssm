package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IAllocationMapper;
import com.designal.vacc.domain.AllocationCity;
import com.designal.vacc.service.IAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:16
 */
@Service
@Transactional
public class AllocationService implements IAllocationService {

    @Autowired
    private IAllocationMapper allocationMapper;


    //查询全部市疾控中心
    @Override
    public List<AllocationCity> viewAll() {
        return allocationMapper.viewAll();
    }

    //根据id查询市疾控中心的表名称
    @Override
    public String viewCity_table(int id) {
        return allocationMapper.selectTableName(id);
    }

    //根据不同的表名获取指定疫苗的库存量
    @Override
    public int viewV_number(String city_table, String v_name, String v_spec) {
        if(city_table!=null && "xi_an".equals(city_table)){
            return allocationMapper.selectV_numberOne(v_name, v_spec);
        }else if(city_table!=null && "xian_yang".equals(city_table)){
            return allocationMapper.selectV_numberTwo(v_name, v_spec);
        }
        return 0;
    }

    //修改相应表中的数据
    @Override
    public boolean updateV_number(String city_table, String v_name, String v_spec, int v_number) {
        if(city_table!=null && "xi_an".equals(city_table)){
            return allocationMapper.setV_numberOne(v_number, v_name, v_spec)>0;
        }else if(city_table!=null && "xian_yang".equals(city_table)){
            return allocationMapper.setV_numberTwo(v_number, v_name, v_spec)>0;
        }
        return false;
    }

    //删除allocation中的记录
    @Override
    public boolean dropAllocation(String vaccineName, String vaccineSpec) {
        return allocationMapper.deleteOne(vaccineName, vaccineSpec)>0;
    }
}
