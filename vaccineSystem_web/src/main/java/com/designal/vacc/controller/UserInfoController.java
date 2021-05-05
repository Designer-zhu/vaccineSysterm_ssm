package com.designal.vacc.controller;

import com.designal.vacc.domain.UserInfo;
import com.designal.vacc.service.IUserInfoService;
import com.designal.vacc.utils.MD5Utils;
import com.designal.vacc.vo.RespWriterUtil;
import com.designal.vacc.vo.Rs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

/**
 * @Description TODO
 * @Author designal
 * @Date 2021/3/25 21:31
 */
@Controller
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;

    //登录
    @RequestMapping("/login.action")
    public String login(String username, String password, String identity,
                        HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        UserInfo userInfo = userInfoService.login(username, password, identity);
        if(userInfo!=null){
            //邮件已激活，可登录
            if(userInfo.getState() == 1){

                //选择免登录
                String free = request.getParameter("free");
                //记住用户名
                String remember = request.getParameter("remember");

                //选择记住用户名：若点击了remember,则在Cookie中存储用户名
                if(remember!=null && "remember".equals(remember)){
                    Cookie usernameCookie = new Cookie("remember", URLEncoder.encode(username, "UTF-8"));
                    usernameCookie.setMaxAge(7*24*60*60);
                    response.addCookie(usernameCookie);
                }

                //若选择了自动登录，点击了free，则在Cookie中存储用户名和密码
                else if(free!=null && "free".equals(free)){
                    Cookie usernameCookie = new Cookie("username", URLEncoder.encode(username,"UTF-8"));
                    Cookie passwordCookie = new Cookie("password",password);
                    usernameCookie.setMaxAge(7*24*60*60);
                    passwordCookie.setMaxAge(7*24*60*60);
                    response.addCookie(usernameCookie);
                    response.addCookie(passwordCookie);
                }

                //将当前查询到的用户信息存储值会话中
                HttpSession session = request.getSession();

                //根据身份跳转至对应的页面
                if(userInfo.getIdentity().equals("pro_user")){
                    session.setAttribute("pro_user",userInfo);
                    //跳转
                    return "pro_index";
                }else {
                    session.setAttribute("city_user",userInfo);
                    //跳转
                    return "cityPage/city_index";
                }

            }else{
                request.setAttribute("msg","当前账户未激活，请尽快前往您的邮箱激活！");
                return "pro_login";
            }
        }else {
            request.setAttribute("msg","用户名与密码不匹配");
            return "pro_login";
        }
    }

    //登出
    @RequestMapping("/logout.action")
    public String logout(HttpServletRequest request,HttpServletResponse response){
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

        //跳转至登陆页面
        return "pro_login";
    }

    //基础资料
    @RequestMapping(value = "/updateUser.action",method = {RequestMethod.GET,RequestMethod.POST})
    public void updateUser(UserInfo userInfo,MultipartFile photo,HttpServletRequest request,HttpServletResponse response
    ) throws IOException {
        //根据userId查询数据库中的用户
        UserInfo oldPro_user = userInfoService.viewOneByUser_id(userInfo.getUserId(), "pro_user");
        oldPro_user.setUsername(userInfo.getUsername());
        oldPro_user.setSex(userInfo.getSex());
        if(photo!=null){
            //获取上传上传图片信息（包括图片名称）
            String oldName = photo.getOriginalFilename();

            if(oldName!=null && oldName.contains(".")){
                //真正成功上传图片
                //图片的新name = 随机数 + oldName的后缀
                String newName = UUID.randomUUID() +
                        oldName.substring(oldName.lastIndexOf("."),oldName.length()-1);

                //将当前图片信息存进items对象中
                oldPro_user.setPhoto("/pic/"+newName);
                //将图片信息传递至本地服务器上
                photo.transferTo(new File("D:\\IGeek\\Work-IDEA\\github--designal\\maven\\project\\temp\\"+newName));
            }
        }

        oldPro_user.setEmail(userInfo.getEmail());
        oldPro_user.setBirthday(userInfo.getBirthday());
        oldPro_user.setTelephone(userInfo.getTelephone());
        boolean flag = userInfoService.updateUser(oldPro_user);

        if(flag){
            //修改成功
            //更改会话中的pro_user信息
            HttpSession session = request.getSession();
            session.setAttribute("pro_user",oldPro_user);
            response.sendRedirect(request.getContextPath()+"/backstage/page/pro_personer/pro_updatePro.jsp");
        }
    }

    //安全设置
    @RequestMapping("/salfe.action")
    @ResponseBody
    public void salfe(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //原密码
        String password = request.getParameter("password");
        //password = MD5Utils.md5(password);
        //新密码
        String newPassword = request.getParameter("newPassword");
        //确认密码
        String rePassword = request.getParameter("rePassword");

        String username = request.getParameter("username");

        //通过username和原密码查询账户
        UserInfo user = userInfoService.login(username, password,"pro_user");

        if(user!=null){
            //可更改
            if(!"".equals(newPassword)&&!"".equals(rePassword)&&newPassword.equals(rePassword)){
                //两次密码输入一致,修改用户密码
                //密码加密
                //user.setPassword(MD5Utils.md5(newPassword));
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
}
