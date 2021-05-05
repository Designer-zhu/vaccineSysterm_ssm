package com.designal.vacc.controller;

import com.designal.vacc.domain.City;
import com.designal.vacc.service.ICityService;
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
 * @Date 2021/5/3 10:39
 */
@Controller
@RequestMapping("/city")
public class CityController {

    @Autowired
    private ICityService cityService;

    //查询城市
    @RequestMapping("/viewCity.action")
    @ResponseBody
    public void viewCity(HttpServletRequest request, HttpServletResponse response){
        String p_pid = request.getParameter("p_id");

        if(p_pid!=null && !p_pid.equals("")){
            int p_id = Integer.parseInt(p_pid);
            List<City> cityList = cityService.viewAllCityByP_id(p_id);
            if(cityList!=null){
                //响应回页面
                Rs result = new Rs(cityList);
                RespWriterUtil.writer(response,result);
            }
        }
    }
}
