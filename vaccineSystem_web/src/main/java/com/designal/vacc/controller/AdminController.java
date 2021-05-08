package com.designal.vacc.controller;

import com.designal.vacc.domain.Admin;
import com.designal.vacc.domain.Permission;
import com.designal.vacc.domain.Role;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.io.UnsupportedEncodingException;
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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //登录
    @RequestMapping("/login.action")
    public void login(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");

        Admin admin = adminService.login(name, password);
        if(admin!=null){
            request.setAttribute("admin",admin);
            request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/admin_index.jsp").forward(request,response);
        }
    }

    //注册疾控中心用户人员
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

        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //设置code
        String code = CommonUtils.getUUID().replaceAll("-","");
        user.setCode(code);

        //注册
        boolean flag = userInfoService.register(user);
        if(flag){
            //注册成功
            //发送邮件
            //获取user的id
            Integer userId = user.getUserId();
            String emailMess = "恭喜您注册成功，这是一封激活邮件，请点击<a href='http://localhost:8080/admin/active.action?identity="+user.getIdentity()+"&userId="+userId+"&code="+code+"'>"+code+"</a>激活";
            try {
                MailUtils.sendMail(user.getEmail(),"注册邮件激活",emailMess);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            Rs result = new Rs("激活成功");
            RespWriterUtil.writer(response,result);
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
            //激活成功 跳转查看用户列表页
            response.sendRedirect(request.getContextPath()+"/backstage/adminPage/admin_index.jsp");
        }else if(flag && identity.equals("city_user")){
            //激活成功 跳转查看用户列表页
            response.sendRedirect(request.getContextPath()+"/backstage/adminPage/admin_index.jsp");
        }else {
            //激活失败
            response.sendRedirect(request.getContextPath()+"/backstage/adminPage/admin_index.jsp");
        }
    }

    //查看省疾控中心人员列表
    @RequestMapping("/viewAllPro.action")
    @ResponseBody
    public void viewAllPro(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        List<UserInfo> userList = userInfoService.viewProUserList();
        request.setCharacterEncoding("UTF-8");
        if(userList!=null){
            request.setAttribute("userList",userList);
            request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/user-list.jsp").forward(request,response);
        }
    }

    //查看市疾控中心人员列表
    @RequestMapping("/viewAllCity.action")
    @ResponseBody
    public void viewAllCity(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        List<UserInfo> userList = userInfoService.viewCityUserList();
        request.setCharacterEncoding("UTF-8");
        if(userList!=null){
            request.setAttribute("cityUserList",userList);
            request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/cityUser-list.jsp").forward(request,response);
        }
    }

    //查看全部角色
    @RequestMapping("/viewAllRoles.action")
    public void viewAllRoles(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        List<Role> rolesList = adminService.viewAllRoles();
        request.setAttribute("rolesList",rolesList);
        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/role-list.jsp").forward(request,response);
    }

    //查看全部权限
    @RequestMapping("/viewAllPermission.action")
    public void viewAllPermission(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        List<Permission> permissionsList = adminService.viewAllPermissions();
        request.setAttribute("permissionsList",permissionsList);
        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/permission-list.jsp").forward(request,response);
    }

    //查看角色详情
    @RequestMapping("/showRoleInfo.action")
    public void showRoleInfo(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        Role role = adminService.viewRoleInfo(Integer.parseInt(id));

        request.setAttribute("role",role);
        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/role-show.jsp").forward(request,response);

    }

    //新建角色
    @RequestMapping("/addNewRole.action")
    public void addNewRole(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Role role = new Role();
        try {
            BeanUtils.populate(role,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        boolean flag = adminService.addRole(role);
        //添加成功
        if(flag){
            //跳转查看角色列表
            request.getRequestDispatcher(request.getContextPath()+"/admin/viewAllRoles.action").forward(request,response);

        }

    }

    //删除角色(用户-角色关系表删除、角色-权限关系表删除、角色表删除)
    @RequestMapping("/deleteRoleById.action")
    public void deleteRoleById(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        adminService.deleteRoleById(Integer.parseInt(id));
        request.getRequestDispatcher(request.getContextPath()+"/admin/viewAllRoles.action").forward(request,response);
    }

    //查看角色下剩余权限
    @RequestMapping("/viewOtherPermissionsById.action")
    public void viewOtherPermissionsById(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        Role role = adminService.viewOtherPermissionsById(Integer.parseInt(id));

        request.setAttribute("role",role);
        request.getRequestDispatcher(request.getContextPath()+"/backstage/adminPage/role-permission-add.jsp").forward(request,response);
    }

    //给角色添加权限
    @RequestMapping("/addNewPermissionsToRole.action")
    public void addNewPermissionsToRole(
            @RequestParam(value = "id",required = true) int id,
            @RequestParam(value = "ids",required = true) int[] ids
            , HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        boolean flag = adminService.addNewPermissionToRole(id, ids);
        if(flag){
            request.getRequestDispatcher(request.getContextPath()+"/admin/viewOtherPermissionsById.action" +
                    "?id="+id).forward(request,response);
        }

    }

    //新建权限
    @RequestMapping("/addNewPermission.action")
    public void addNewPermission(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Permission permission = new Permission();
        try {
            BeanUtils.populate(permission,parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        boolean flag = adminService.addPermission(permission);
        //添加成功
        if(flag){
            //跳转查看权限列表
            request.getRequestDispatcher(request.getContextPath()+"/admin/viewAllPermission.action").forward(request,response);

        }

    }

    //删除权限（角色-权限关系表删除、权限表删除）
    @RequestMapping("/deletePermission.action")
    public void deletePermission(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        adminService.deletePermissionById(Integer.parseInt(id));
        //跳转查看全部权限列表
        request.getRequestDispatcher(request.getContextPath()+"/admin/viewAllPermission.action").forward(request,response);

    }

    //给用户添加角色
    @RequestMapping("/addNewRoleToUser.action")
    public void addNewRoleToUser(
            @RequestParam(value = "userId",required = true) int userId,
            @RequestParam(value = "ids",required = true) int[] ids
            ,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        boolean flag = adminService.addNewRoleToUser(userId, ids);
        if(flag){
            request.getRequestDispatcher(request.getContextPath()+"/user/selectUserHasOtherRoles.action" +
                    "?userId="+userId).forward(request,response);
        }

    }
}
