package com.designal.vacc.controller;

import com.designal.vacc.domain.Pre_vacc;
import com.designal.vacc.service.ICityService;
import com.designal.vacc.service.IPre_vaccService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/5/3 10:22
 */
@Controller
@RequestMapping("/pre")
public class PreController {

    @Autowired
    private IPre_vaccService pre_vaccService;

    @Autowired
    private ICityService cityService;

    @Autowired
    private IVaccineService vaccineService;

    //查询预存库存
    @RequestMapping("/viewAll.action")
    @ResponseBody
    public void viewAll(HttpServletRequest request, HttpServletResponse response){

        String query = request.getParameter("query");

        List<Pre_vacc> pre_vaccList = pre_vaccService.viewAll(query);

        if(pre_vaccList != null){
            Rs result = new Rs(pre_vaccList);
            RespWriterUtil.writer(response,result);
        }
    }

    //删除
    @RequestMapping("/delete.action")
    @ResponseBody
    public void delete(HttpServletRequest request,HttpServletResponse response){
        String v_idStr = request.getParameter("v_id");

        String[] split = v_idStr.split("@");

        if(split!=null){
            //循环执行删除
            for (String v_id : split) {
                pre_vaccService.drop(v_id);
            }
            //封装结果集数据
            Rs result = new Rs("删除成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }else {
            //封装结果集数据
            Rs result = new Rs("删除失败");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }
    }

    //添加初期前账
    @RequestMapping("/addNew.action")
    @ResponseBody
    public void addNew(HttpServletRequest request,HttpServletResponse response){
        //获取form表单中的值
        Map<String, String[]> parameterMap = request.getParameterMap();

        Pre_vacc pre_vacc = new Pre_vacc();

        //处理日期问题1
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class clazz, Object o) {
                Date v_date = null;
                if(o instanceof String){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        v_date = sdf.parse(request.getParameter("v_date"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return v_date;
            }
        }, Date.class);

        //处理日期问题2
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class clazz, Object o) {
                Date v_term = null;
                if(o instanceof String){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        v_term = sdf.parse(request.getParameter("v_term"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return v_term;
            }
        }, Date.class);

        //通过BeanUtils工具类，给pre_vacc对象赋值属性
        try {
            BeanUtils.populate(pre_vacc,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //处理出厂商地址问题
        String province = request.getParameter("province");
        String city = request.getParameter("city");

        //从数据库中获取省和对应城市的信息
        String v_site = "";
        if(province!=null && !"".equals(province) && city !=null && !"".equals(city)){
            int p_id = Integer.parseInt(province);
            int c_id = Integer.parseInt(city);
            v_site = cityService.viewProCityName(p_id, c_id);
        }
        //给pre_vacc装配出厂商
        pre_vacc.setV_site(v_site);

        //System.out.println(pre_vacc);

        //添加至数据库
        boolean flag = pre_vaccService.addNew(pre_vacc);
        if(flag){
            //插入成功
            //封装结果集数据
            Rs result = new Rs("插入成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }else {
            //封装结果集数据
            Rs result = new Rs("插入失败");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }
    }

    //入库操作
    @RequestMapping("/warehousing.action")
    @ResponseBody
    public void warehousing(HttpServletRequest request, HttpServletResponse response){
        String v_idStr = request.getParameter("v_id");

        if(v_idStr != null && !"".equals(v_idStr)){

            int v_id = Integer.parseInt(v_idStr);

            //入库操作具体执行步骤：
            /**
             * 1.通过v_id获取初期建账库中的疫苗并创建实例对象
             * 2.先删除初期建账库中的该疫苗
             * 3.往疫苗库存中插入该疫苗
             */

            //获取疫苗实例
            Pre_vacc pre_vacc = pre_vaccService.viewOne(v_id);

            //删除初期建账中的该疫苗
            boolean flag = pre_vaccService.drop(v_idStr);

            //完成前面两步后
            if(pre_vacc != null && flag){
                boolean b = vaccineService.addOne(pre_vacc);
                if(b){
                    //入库成功
                    //封装结果集数据
                    Rs result = new Rs("入库成功");

                    //通过json数据的格式响应回页面
                    RespWriterUtil.writer(response,result);
                }else {
                    //封装结果集数据
                    Rs result = new Rs("入库失败");

                    //通过json数据的格式响应回页面
                    RespWriterUtil.writer(response,result);
                }
            }


        }
    }
}
