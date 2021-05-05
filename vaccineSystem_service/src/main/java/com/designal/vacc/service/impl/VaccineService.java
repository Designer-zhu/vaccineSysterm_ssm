package com.designal.vacc.service.impl;

import com.designal.vacc.dao.IVaccineMapper;
import com.designal.vacc.domain.Pre_vacc;
import com.designal.vacc.domain.Vaccine;
import com.designal.vacc.service.IVaccineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/23 20:56
 */
@Service
@Transactional
public class VaccineService implements IVaccineService {

    @Autowired
    private IVaccineMapper vaccineMapper;

    //条件+分页查询
    @Override
    public List<Vaccine> viewVaccineByQuery(String query1, String search, int pageNow, int limit) {

        //使用场景：调用执行dao层查询数据之前 PageHelper的静态方法startPage(当前页，每页记录数)
        PageHelper.startPage(pageNow,limit);

        List<Vaccine> vaccineList = null;
        if((query1==null || query1.equals("")) && (search==null || search.equals(""))){
            //无搜索条件
            vaccineList = vaccineMapper.viewAllVaccineByPageOne();
        }else if(query1!=null && (search==null || search.equals(""))){
            //有query1搜索条件
            vaccineList = vaccineMapper.viewAllVaccineByPageTwo(Integer.parseInt(query1));
        }else if((query1==null || query1.equals(""))){
            //有search搜索条件
            vaccineList = vaccineMapper.viewAllVaccineByPageThree(Integer.parseInt(search));
        }else {
            //有search+query1搜索条件
            vaccineList = vaccineMapper.viewAllVaccineByPageFour(Integer.parseInt(query1),Integer.parseInt(search));
        }

        return vaccineList;

    }


    //修改疫苗信息
    @Override
    public boolean update(Vaccine vaccine) {
        Vaccine vaccineTest = vaccineMapper.selectOneByV_id(Integer.parseInt(vaccine.getV_id()));
        if(vaccineTest != null){
            return vaccineMapper.update(vaccine)>0;
        }
        return false;

    }

    //删除疫苗1
    @Override
    public boolean drop(String v_id) {
        return vaccineMapper.deleteOneByV_id(v_id)>0;
    }

    //插入
    @Override
    public boolean addOne(Pre_vacc pre_vacc) {
        return vaccineMapper.insertOne(pre_vacc)>0;
    }

    //通过疫苗名称模糊查询
    @Override
    public List<Vaccine> viewByV_name(String v_name, String v_spec) {
        return vaccineMapper.selectByV_name(v_name, v_spec);
    }
}
