package com.designal.vacc.controller;

import com.designal.vacc.domain.Record;
import com.designal.vacc.domain.RequestMess;
import com.designal.vacc.domain.Vaccine;
import com.designal.vacc.service.IRecordService;
import com.designal.vacc.service.IRequestMessService;
import com.designal.vacc.service.IVaccineService;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/5/3 10:41
 */
@Controller
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private IRecordService recordService;

    @Autowired
    private IRequestMessService requestMessService;

    @Autowired
    private IVaccineService vaccineService;

    //查询
    @RequestMapping("/viewAll.action")
    @ResponseBody
    public void viewAll(HttpServletRequest request, HttpServletResponse response){

        List<Record> recordList = recordService.viewAll();

        if(recordList!=null){
            //封装结果集数据
            Rs result = new Rs(recordList);

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }

    }

    //处理下发记录
    @RequestMapping("/addNew.action")
    @ResponseBody
    public void addNew(HttpServletRequest request, HttpServletResponse response){

        Map<String, String[]> parameterMap = request.getParameterMap();
        //获取下发记录的id
        String id = request.getParameter("id");
        //查询表中对应的下发记录
        List<RequestMess> requestMessList = requestMessService.viewAll(id);
        RequestMess requestMess = requestMessList.get(0);
        Record record = new Record();

        record.setId(requestMess.getId());
        record.setVaccineName(requestMess.getVaccineName());
        record.setLocation(requestMess.getLocation());
        record.setRequestDate(requestMess.getRequestDate());
        record.setResult("同意");
        record.setProcessDate(new Date());
        record.setVaccineSpec(requestMess.getVaccineSpec());
        record.setVaccineNumber(requestMess.getVaccineNumber());

        //System.out.println(record);


        //处理日期 String -> Date
        //处理String -> Data类型
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class aClass, Object o) {
                Date v_date = null;
                if(o instanceof  String){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        v_date = simpleDateFormat.parse(request.getParameter("v_date"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return v_date;
            }
        }, Date.class);

        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class aClass, Object o) {
                Date v_term = null;
                if(o instanceof  String){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        v_term = simpleDateFormat.parse(request.getParameter("v_term"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return v_term;
            }
        }, Date.class);

        //创建空实例
        Vaccine vaccine = new Vaccine();

        //通过BeanUtils工具类，给vaccine对象对应的属性根据键值对赋值
        try {
            BeanUtils.populate(vaccine,parameterMap);
            //System.out.println(vaccine);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //执行添加下发记录的方法
        boolean flag = recordService.addNew(record);
        //删除vaccine库中的疫苗
        vaccineService.drop(vaccine.getV_id());

        //删除请求库中的记录
        boolean b = requestMessService.dropById(record.getId());

        if(flag && b){
            //封装结果集数据
            Rs result = new Rs("操作成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }

    }

    //查询Vaccine中的疫苗信息
    @RequestMapping("/viewVaccine.action")
    @ResponseBody
    public void viewVaccine(HttpServletRequest request, HttpServletResponse response){
        String v_name = request.getParameter("v_name");
        String v_spec = request.getParameter("v_spec");
        //System.out.println(v_name);

        List<Vaccine> vaccineList = vaccineService.viewByV_name(v_name,v_spec);
        if(vaccineList.size() != 0){
            Vaccine vaccine = vaccineList.get(0);
            //System.out.println(vaccine);
            //封装结果集数据
            Rs result = new Rs(vaccine);

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }else {

            //先查询再删除
            RequestMess requestMess = requestMessService.viewOne(v_name, v_spec);

            //删除requestmess表中的记录
            boolean flag1 = requestMessService.dropByNameAndSpec(v_name,v_spec);

            //往allocation表中插入
            boolean flag2 = requestMessService.addToAllocation(requestMess);

            //返回ERROR响应
            /*RtnError rtnError = new RtnError();
            RespWriterUtil.writerError(response,rtnError);*/

        }
    }

    //下发拒绝操作
    @RequestMapping("/refuse.action")
    @ResponseBody
    public void refuse(HttpServletRequest request, HttpServletResponse response){

        //获取id
        String id = request.getParameter("id");

        //1.创建一条record记录
        Record record = new Record();

        //2.先查RequestMess表中的该请求记录
        List<RequestMess> requestMessList = requestMessService.viewAll(id);
        RequestMess requestMess = requestMessList.get(0);
        //封装record
        record.setId(requestMess.getId());
        record.setVaccineName(requestMess.getVaccineName());
        record.setLocation(requestMess.getLocation());
        record.setRequestDate(requestMess.getRequestDate());
        record.setResult("拒绝");
        record.setProcessDate(new Date());

        //3.删除请求表中的记录
        boolean flag1 = requestMessService.dropById(Integer.parseInt(id));

        //4.往record表中插入记录
        boolean flag2 = recordService.addNew(record);

        if(flag1 && flag2){
            //封装结果集数据
            Rs result = new Rs("操作成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }


    }

    //调拨
    @RequestMapping("/allocationView.action")
    @ResponseBody
    public void allocationView(HttpServletRequest request, HttpServletResponse response){

        String query = request.getParameter("query");

        //查询调拨表中的请求
        List<RequestMess> requestMessList = requestMessService.viewAllAllocation(query);

        if(requestMessList != null){
            //封装结果集数据
            Rs result = new Rs(requestMessList);

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }
    }
}
