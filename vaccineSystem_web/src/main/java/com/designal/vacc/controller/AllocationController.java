package com.designal.vacc.controller;

import com.designal.vacc.domain.AllocationCity;
import com.designal.vacc.service.IAllocationService;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/5/3 10:34
 */
@Controller
@RequestMapping("/allocation")
public class AllocationController {

    @Autowired
    private IAllocationService allocationService;

    //查看全部市疾控心
    @RequestMapping("/viewAllCity.action")
    @ResponseBody
    public void viewAllCity(HttpServletRequest request, HttpServletResponse response){

        List<AllocationCity> allocationCityList = allocationService.viewAll();
        if(allocationCityList.size() != 0){
            Rs result = new Rs(allocationCityList);
            RespWriterUtil.writer(response,result);
        }
    }

    //查询不同城市的疫苗库存
    @RequestMapping("/getCity_number.action")
    @ResponseBody
    public void getCity_number(HttpServletRequest request, HttpServletResponse response){

        String city_table = request.getParameter("city_table");
        String v_name = request.getParameter("v_name");
        String v_spec = request.getParameter("v_spec");

        int v_number = allocationService.viewV_number(city_table, v_name, v_spec);

        if(v_number>0){
            Rs result = new Rs(v_number);
            RespWriterUtil.writer(response,result);
        }
    }

    //确认调拨，修改相应表中的疫苗数量
    @RequestMapping("/updateV_number.action")
    @ResponseBody
    public void updateV_number(HttpServletRequest request, HttpServletResponse response){

        String v_name = request.getParameter("vaccineName");
        String v_spec = request.getParameter("vaccineSpec");
        String vaccineNumber = request.getParameter("vaccineNumber");  //请求量
        String city_table = request.getParameter("allocationCity");
        String v_number = request.getParameter("v_number");  //库存量

        //计算剩余量
        int a = Integer.parseInt(v_number);
        int b = Integer.parseInt(vaccineNumber);



        //删除
        boolean flag1 = allocationService.dropAllocation(v_name, v_spec);

        //修改
        int i = a - b;
        boolean flag2 = allocationService.updateV_number(city_table, v_name, v_spec, i);

        if(flag1 && flag2){
            Rs result = new Rs("");
            RespWriterUtil.writer(response,result);
        }
    }

    //拒绝
    @RequestMapping("/refuse.action")
    @ResponseBody
    public void refuse(HttpServletRequest request, HttpServletResponse response){

        String vaccineName = request.getParameter("vaccineName");
        String vaccineSpec = request.getParameter("vaccineSpec");

        boolean flag = allocationService.dropAllocation(vaccineName, vaccineSpec);

        if(flag){
            Rs result = new Rs("");
            RespWriterUtil.writer(response,result);
        }
    }
}
