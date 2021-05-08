package com.designal.vacc.controller;

import com.designal.vacc.domain.UserInfo;
import com.designal.vacc.service.IUserInfoService;
import com.designal.vacc.utils.MD5Utils;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/25 21:31
 */
@Controller
@RequestMapping("/user")
@MultipartConfig
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //用户登出
    @RequestMapping("/logout.action")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        //销毁会话
        session.invalidate();

        //清除Cookie
        Cookie usernameCookie = new Cookie("username", "");
        Cookie passwordCookie = new Cookie("password","");

        usernameCookie.setMaxAge(0);
        passwordCookie.setMaxAge(0);

        response.addCookie(usernameCookie);
        response.addCookie(passwordCookie);

        request.getRequestDispatcher(request.getContextPath()+"/backstage/page/pro_login.jsp").forward(request,response);
    }

    //登录介质
    @RequestMapping("/login.action")
    public void login(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ServletException, IOException {
        UserDetails userInfo = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userInfo.getUsername();
        UserInfo pro_user = userInfoService.selectProOneByUsername(username);

        //将用户信息储至会话中
        session.setAttribute("pro_user",pro_user);

        request.getRequestDispatcher(request.getContextPath()+"/backstage/page/pro_index.jsp").forward(request,response);
    }

    //基础资料
    @RequestMapping(value = "/updateUser.action",method = {RequestMethod.GET,RequestMethod.POST})
    public void updateUser(MultipartFile photo,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
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
        if(photo!=null){
            String oldName = photo.getOriginalFilename();
            if(oldName!=null && oldName.length()>0){
                String newName = UUID.randomUUID()
                        +oldName.substring(oldName.lastIndexOf("."));

                photo.transferTo(new File("D:\\IGeek\\Work-IDEA\\github--designal\\maven\\project\\temp\\"+newName));
                user.setPhoto("/pic/"+newName);
            }
        }

        //System.out.println(user);
        boolean flag = userInfoService.updateUser(user);
        if(flag){
            //修改成功
            UserInfo user1 = userInfoService.viewOneByUserId(user.getUserId(), "pro_user");
            //存储user信息至会话中
            //将当前查询到的用户信息存储值会话中
            HttpSession session = request.getSession();
            session.setAttribute("pro_user",user1);

            //跳转
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_personer/pro_updatePro.jsp");

        }
    }

    //安全设置
    @RequestMapping("/salfe.action")
    @ResponseBody
    public void salfe(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //新密码
        String newPassword = request.getParameter("newPassword");
        //确认密码
        String rePassword = request.getParameter("rePassword");

        String username = request.getParameter("username");

        //通过username和原密码查询账户
        UserInfo user = userInfoService.selectProOneByUsername(username);

        if(user!=null){
            //可更改
            if(!"".equals(newPassword)&&!"".equals(rePassword)&&newPassword.equals(rePassword)){
                //两次密码输入一致,修改用户密码
                //密码加密
                newPassword = bCryptPasswordEncoder.encode(newPassword);
                user.setPassword(newPassword);
                //更新密码
                boolean flag = userInfoService.updatePassword(user);
                if(flag){
                    //System.out.println("密码修改成功");
                    //更新成功，返回登陆页面
                    //记住用户名则在Cookie中存储用户名
                    Cookie usernameCookie = new Cookie("remember", URLEncoder.encode(username, "UTF-8"));
                    usernameCookie.setMaxAge(7*24*60*60);
                    response.addCookie(usernameCookie);

                    Rs result = new Rs("成功");
                    RespWriterUtil.writer(response,result);

                }else {
                    response.sendRedirect(request.getContextPath()+"/backstage/page/pro_error.jsp");
                }
            }else {
                response.sendRedirect(request.getContextPath()+"/backstage/page/pro_error.jsp");
            }
        }else {
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_error.jsp");
        }
    }

    //查看当前用户下的所属角色以及权限
    @RequestMapping("/viewUserInfo.action")
    public void viewUserInfo(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String userId = request.getParameter("userId");
        UserInfo userInfo = userInfoService.viewOneByUserId(Integer.parseInt(userId), "pro_user");
        request.setAttribute("userInfo",userInfo);

        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/user-show.jsp").forward(request,response);


    }

    //查看当前用户下剩余角色
    @RequestMapping("/selectUserHasOtherRoles.action")
    public void selectUserHasOtherRoles(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        UserInfo userInfo = userInfoService.selectOtherRoleListByUserId(Integer.parseInt(userId));
        request.setAttribute("userInfo",userInfo);

        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/user-role-add.jsp").forward(request,response);
    }

    //市疾控中心人员登录
    @RequestMapping("/city_userLogin.action")
    public void city_userLogin(String username,String password,HttpSession session,Model model,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        UserInfo city_user = userInfoService.login(username, password, "city_user");
        if(city_user!=null){
            session.setAttribute("city_user",city_user);
            request.getRequestDispatcher(request.getContextPath()+"/backstage/cityPage/city_index.jsp").forward(request,response);
        }
    }

}
