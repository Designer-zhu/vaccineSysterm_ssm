package com.designal.vacc.controller;

import com.designal.vacc.domain.Record;
import com.designal.vacc.service.IRecordService;
import com.designal.vacc.service.ITransportService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/5/3 10:52
 */
@Controller
@RequestMapping("/transport")
public class TransportController {

    @Autowired
    private IRecordService recordService;

    //显示所有同意的下发记录
    @RequestMapping("/sto_tans.action")
    @ResponseBody
    public void sto_tans(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String res = "同意";
        List<Record> recordList = recordService.selectRecord(res);
        if(recordList!=null){
            //通过json数据传递商品类别的集合
            Gson gson = new Gson();
            String json = gson.toJson(recordList);

            //解决中文乱码
            response.setContentType("text/html;charset=UTF-8");
            //将json数据响应至客户端
            response.getWriter().write(json);
        }
    }

    //运输提交操作
    @RequestMapping("/submit.action")
    @ResponseBody
    public void submit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String i = request.getParameter("id");
        int id = 0;
        if(i!=null){
            id = Integer.parseInt(i);
        }
        boolean submit = recordService.submit(id);
        if(submit){
            response.sendRedirect(request.getContextPath()+"/backstage/page/vacc_transport/vacc_sto_trans.jsp");
        }
    }

}
