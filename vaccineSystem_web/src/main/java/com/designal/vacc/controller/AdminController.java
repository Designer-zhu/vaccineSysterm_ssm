package com.designal.vacc.controller;

import com.designal.vacc.domain.Admin;
import com.designal.vacc.domain.UserInfo;
import com.designal.vacc.service.IAdminService;
import com.designal.vacc.service.IUserInfoService;
import com.designal.vacc.utils.CommonUtils;
import com.designal.vacc.utils.MD5Utils;
import com.designal.vacc.utils.MailUtils;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
 * @Date 2021/3/23 21:25
 */
@Controller
@RequestMapping("/admin")
@MultipartConfig//以二进制方式获取属性值
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IUserInfoService userInfoService;

    //登录
    @RequestMapping("/login.action")
    public void login(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Admin admin = adminService.login(name, password);
        if(admin!=null){
            request.setAttribute("admin",admin);
            request.getRequestDispatcher(request.getContextPath()+"/backstage/page/adminPage/admin_index.jsp").forward(request,response);
        }
    }

    //查看省疾控中心人员列表
    @RequestMapping("/viewAllPro.action")
    @ResponseBody
    public void viewAllPro(HttpServletRequest request,HttpServletResponse response){
        List<UserInfo> userList = userInfoService.viewProUserList();
        if(userList!=null){
            Rs result = new Rs(userList);
            RespWriterUtil.writer(response,result);
        }
    }

    //查看市疾控中心人员列表
    @RequestMapping("/viewAllCity.action")
    @ResponseBody
    public void viewAllCity(HttpServletRequest request,HttpServletResponse response){

        List<UserInfo> userList = userInfoService.viewCityUserList();
        if(userList!=null){
            Rs result = new Rs(userList);
            RespWriterUtil.writer(response,result);
        }
    }

    //注册
    @RequestMapping("/register.action")
    @ResponseBody
    public void register(HttpServletRequest request,HttpServletResponse response){

        //根据name = value 进行获取键值对，例：name=username  value=文本和输入框中的内容
        Map<String, String[]> parameterMap = request.getParameterMap();

        //处理String -> Data类型
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class aClass, Object o) {
                Date birthday = null;
                if(o instanceof  String){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        birthday = simpleDateFormat.parse(request.getParameter("birthday"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return birthday;
            }
        }, Date.class);

        //创建空用户
        UserInfo user = new UserInfo();

        //通过BeanUtils工具类，给user对象对应的属性根据键值对赋值
        try {
            BeanUtils.populate(user,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //处理头像
        /*Part part = request.getPart("photo");
        if(part!=null){
            //获取上传上传图片信息（包括图片名称）
            String oldName = part.getHeader("content-disposition");

            if(oldName!=null && oldName.contains(".")){
                //真正成功上传图片
                //图片的新name = 随机数 + oldName的后缀
                String newName = UUID.randomUUID() +
                        oldName.substring(oldName.lastIndexOf("."),oldName.length()-1);

                //将当前图片信息存进items对象中
                user.setPhoto("/pic/"+newName);
                //将图片信息传递至本地服务器上
                part.write("D:\\IGeek\\Work-IDEA\\github--designal\\maven\\project\\temp\\"+newName);
            }
        }*/

        //设置pro_id
        user.setUserId(CommonUtils.getUUID().replaceAll("-",""));
        //设置code
        String code = CommonUtils.getUUID().replaceAll("-","");
        user.setCode(code);
        //密码加密
        //user.setPassword(MD5Utils.md5(user.getPassword()));

        //注册
        boolean flag = userInfoService.register(user);
        if(flag){
            //注册成功
            //发送邮件
            //获取user的id
            String userId = user.getUserId();
            String emailMess = "恭喜您注册成功，这是一封激活邮件，请点击<a href='http://localhost:8080/admin/active.action?identity="+user.getIdentity()+"&userId="+userId+"&code="+code+"'>"+code+"</a>激活";
            try {
                MailUtils.sendMail(user.getEmail(),"注册邮件激活",emailMess);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            Rs result = new Rs("激活成功");
            RespWriterUtil.writer(response,result);
            //跳转至注册成功 前往邮件激活界面
            //request.getRequestDispatcher(request.getContextPath()+"/backstage/page/pro_registerSuccess.jsp").forward(request,response);
        }else {
            //注册失败
            //request.getRequestDispatcher(request.getContextPath()+"/backstage/page/pro_registerFail.jsp").forward(request,response);
        }
    }

    //用户激活
    @RequestMapping("/active.action")
    public void active(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String code = request.getParameter("code");
        String identity = request.getParameter("identity");
        boolean flag = userInfoService.activeState(code,identity);

        //激活成功后跳转至相应页面
        if(flag && identity.equals("pro_user")){
            //激活成功 跳转至province首页
            response.sendRedirect(request.getContextPath()+"/backstage/page/adminPage/admin_index.jsp");
        }else if(flag && identity.equals("city_user")){
            //激活成功 跳转至province首页
            response.sendRedirect(request.getContextPath()+"/backstage/page/adminPage/admin_index.jsp");
        }else {
            //激活失败
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_error.jsp");
        }


        /*//根据user_id查询user对象
        String user_id = request.getParameter("user_id");
        User user = userService.viewOneBvUser_id(user_id, identity);

        if(flag && identity.equals("pro_user")){

            //存储user信息至会话中
            //将当前查询到的用户信息存储值会话中
            HttpSession session = request.getSession();
            session.setAttribute("pro_user",user);

            //激活成功 跳转至province首页
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_index.jsp");

        }else if(flag && identity.equals("city_user")){

            //存储user信息至会话中
            //将当前查询到的用户信息存储值会话中
            HttpSession session = request.getSession();
            session.setAttribute("city_user",user);

            //激活成功 跳转至city首页
            response.sendRedirect(request.getContextPath()+"/backstage/page/cityPage/city_index.jsp");

        }else {
            //激活失败
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_error.jsp");
        }*/
    }

}
