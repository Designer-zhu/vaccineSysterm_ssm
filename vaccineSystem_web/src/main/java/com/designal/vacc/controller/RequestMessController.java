package com.designal.vacc.controller;

import com.designal.vacc.domain.RequestMess;
import com.designal.vacc.service.IRequestMessService;
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
 * @Date 2021/5/3 10:48
 */
@Controller
@RequestMapping("/requestMess")
public class RequestMessController {

    @Autowired
    private IRequestMessService requestMessService;

    //查看库中所有
    @RequestMapping("/viewAll.action")
    @ResponseBody
    public void viewAll(HttpServletRequest request, HttpServletResponse response){

        List<RequestMess> requestMessList = requestMessService.viewAll(request.getParameter("query"));

        if(requestMessList!=null){

            //响应回页面
            Rs result = new Rs(requestMessList);
            RespWriterUtil.writer(response,result);
        }

    }

    //添加入库请求
    @RequestMapping("/insertRequest.action")
    @ResponseBody
    public void insertRequest(final HttpServletRequest request, HttpServletResponse response){

        Map<String, String[]> parameterMap = request.getParameterMap();

        RequestMess requestMess = new RequestMess();

        //处理时间问题
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class clazz, Object o) {
                Date requestDate = null;
                if(o instanceof String){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        requestDate = sdf.parse(request.getParameter("requestDate"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return requestDate;
            }
        }, Date.class);

        //通过BeanUtils工具类，给pre_vacc对象赋值属性
        try {
            BeanUtils.populate(requestMess,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        boolean flag = requestMessService.addToRequestMess(requestMess);
        if(flag){
            Rs result = new Rs("成功");
            RespWriterUtil.writer(response,result);
        }
    }

    //删除
    @RequestMapping("/delete.action")
    @ResponseBody
    public void delete(HttpServletRequest request, HttpServletResponse response){
        String i = request.getParameter("id");

        if(i!=null){
            String[] ids = i.split("@");
            for (String id : ids) {
                boolean flag = requestMessService.dropById(Integer.parseInt(id));
            }

            Rs result = new Rs("成功");
            RespWriterUtil.writer(response,result);
        }


    }
}
