package com.designal.vacc.controller;

import com.designal.vacc.domain.Vaccine;
import com.designal.vacc.service.IVaccineService;
import com.designal.vacc.vo.PageVo;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/5/2 12:39
 */
@Controller
@RequestMapping("/vaccine")
public class vaccineController {

    @Autowired
    private IVaccineService vaccineService;

    //查看库中所有疫苗
    @RequestMapping("/showVaccineByCategory.action")
    @ResponseBody
    public void showVaccineByCategory(HttpServletRequest request, HttpServletResponse response){
        //获取请求中的查询条件
        String query1 = request.getParameter("query1");

        //搜索框
        String search = request.getParameter("search");

        //获取请求中的当前页
        String page = request.getParameter("page");
        //获取请求中的每页行数
        String limit = request.getParameter("limit");

        List<Vaccine> vaccineList = vaccineService.viewVaccineByQuery(query1, search, Integer.parseInt(page), Integer.parseInt(limit));

        if(vaccineList != null){

            //封装PageInfo
            PageInfo<Vaccine> pageInfo = new PageInfo<>(vaccineList);

            //封装结果集数据
            Rs result = new Rs(pageInfo);

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }

    }

    //修改疫苗信息
    @RequestMapping("/editVaccine.action")
    @ResponseBody
    public void editVaccine(HttpServletRequest request,HttpServletResponse response){

        String v_id = request.getParameter("v_id");
        String v_name = request.getParameter("v_name");
        String v_detail = request.getParameter("v_detail");
        String v_site = request.getParameter("v_site");

        //处理String -> Date
        Date v_date = null;
        Date v_term = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            v_date = sdf.parse(request.getParameter("v_date"));
            v_term = sdf.parse(request.getParameter("v_term"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String v_spec = request.getParameter("v_spec");
        String v_batch = request.getParameter("v_batch");

        //创建实例
        Vaccine vaccine = new Vaccine(v_id,v_name,v_detail,v_site,v_date,v_term,v_spec,v_batch);
        //System.out.println(v_id+" "+v_name+" "+v_detail+" "+v_site+" "+v_date+" "+v_term+" "+v_spec+" "+v_batch);

        boolean flag = vaccineService.update(vaccine);
        if(flag){
            //修改成功
            //封装结果集数据
            Rs result = new Rs("修改成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }else {
            //修改失败
            //封装结果集数据
            Rs result = new Rs("修改失败");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }
    }

    //通过v_id删除疫苗
    @RequestMapping("/deleteByV_id.action")
    @ResponseBody
    public void deleteByV_id(HttpServletRequest request,HttpServletResponse response){
        String v_id = request.getParameter("v_id");
        boolean flag = vaccineService.drop(v_id);
        if(flag){
            //删除成功
            //封装结果集数据
            Rs result = new Rs("修改成功");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }else {
            //删除失败
            //封装结果集数据
            Rs result = new Rs("修改失败");

            //通过json数据的格式响应回页面
            RespWriterUtil.writer(response,result);
        }
    }

}
