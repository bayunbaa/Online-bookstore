package com.servlet;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户控制器
 * 处理用户相关的请求
 * 
 * @author Titan
 * @version 1.0
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private UserDao userDao = new UserDaoImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "login";
        }
        
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "logout":
                handleLogout(request, response);
                break;
            case "update":
                handleUpdate(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            case "list":
                handleUserList(request, response);
                break;
            case "manage":
                // {{TITAN:
                // Action: Added
                // Reason: 修复用户管理按钮跳转到登录页面的问题，添加manage action处理
                // Timestamp: 2025-06-28 15:20:00 +08:00
                // }}
                // {{START MODIFICATION}}
                handleUserList(request, response);
                // {{END MODIFICATION}}
                break;
            case "search":
                handleUserSearch(request, response);
                break;
            case "changePassword":
                handleChangePassword(request, response);
                break;
            default:
                response.sendRedirect("login.jsp");
        }
    }
    
    /**
     * 处理用户登录
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // {{TITAN:
        // Action: Modified
        // Reason: 修复登录参数名与前端表单字段名不匹配的问题
        // Timestamp: 2025-06-28 14:31:00 +08:00
        // }}
        // {{START MODIFICATION}}
        String username = request.getParameter("uname");
        String password = request.getParameter("upassword");
        // {{END MODIFICATION}}
        
        // 输入验证
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "用户名和密码不能为空");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        // 验证登录
        if (userDao.validateLogin(username, password)) {
            User user = userDao.getUserByName(username);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("uid", user.getUid());  // 添加uid属性
                session.setAttribute("username", user.getUname());
                session.setAttribute("isAdmin", user.isAdmin());

                // 根据用户权限跳转
                if (user.isAdmin()) {
                    response.sendRedirect("manage.jsp");
                } else {
                    response.sendRedirect("BookServlet?action=list");  // 修改为通过Servlet访问
                }
            } else {
                request.setAttribute("error", "获取用户信息失败");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "用户名或密码错误");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理用户注册
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String ageStr = request.getParameter("age");
        String phone = request.getParameter("phone");
        
        // 输入验证
        List<String> errors = validateUserInput(username, password, confirmPassword, ageStr, phone);
        
        // 检查用户名是否已存在
        if (userDao.isUsernameExists(username)) {
            errors.add("用户名已存在，请选择其他用户名");
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("username", username);
            request.setAttribute("age", ageStr);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("usercreate.jsp").forward(request, response);
            return;
        }
        
        // 创建用户对象
        User user = new User();
        user.setUname(username);
        user.setUpassword(password); // 实际项目中应该加密
        user.setUage(Integer.parseInt(ageStr));
        user.setUphone(phone);
        user.setUd(1); // 普通用户权限
        
        // 保存到数据库
        if (userDao.userCreate(user)) {
            request.setAttribute("success", "注册成功，请登录");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "注册失败，请重试");
            request.getRequestDispatcher("usercreate.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理用户登出
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("login.jsp");
    }
    
    /**
     * 处理用户信息更新
     */
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查用户是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        String password = request.getParameter("password");
        String ageStr = request.getParameter("age");
        String phone = request.getParameter("phone");
        
        // 输入验证
        List<String> errors = new ArrayList<>();
        
        if (password != null && !password.trim().isEmpty()) {
            if (password.length() < 6 || password.length() > 20) {
                errors.add("密码长度必须在6-20字符之间");
            }
        }
        
        int age = 0;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 18 || age > 100) {
                errors.add("年龄必须在18-100之间");
            }
        } catch (NumberFormatException e) {
            errors.add("年龄必须是有效数字");
        }
        
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            errors.add("请输入有效的手机号码");
        }
        
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("age", ageStr);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("user.jsp").forward(request, response);
            return;
        }
        
        // 更新用户信息
        if (password != null && !password.trim().isEmpty()) {
            currentUser.setUpassword(password);
        }
        currentUser.setUage(age);
        currentUser.setUphone(phone);
        
        if (userDao.updateUser(currentUser)) {
            session.setAttribute("user", currentUser);
            request.setAttribute("success", "信息更新成功");
        } else {
            request.setAttribute("error", "信息更新失败");
        }
        
        request.getRequestDispatcher("user.jsp").forward(request, response);
    }
    
    /**
     * 验证用户输入
     */
    private List<String> validateUserInput(String username, String password, String confirmPassword, 
                                         String ageStr, String phone) {
        List<String> errors = new ArrayList<>();
        
        if (username == null || username.trim().isEmpty()) {
            errors.add("用户名不能为空");
        } else if (username.length() < 3 || username.length() > 20) {
            errors.add("用户名长度必须在3-20字符之间");
        } else if (!username.matches("^[a-zA-Z0-9_]+$")) {
            errors.add("用户名只能包含字母、数字和下划线");
        }
        
        if (password == null || password.trim().isEmpty()) {
            errors.add("密码不能为空");
        } else if (password.length() < 6 || password.length() > 20) {
            errors.add("密码长度必须在6-20字符之间");
        }
        
        if (confirmPassword != null && !password.equals(confirmPassword)) {
            errors.add("两次输入的密码不一致");
        }
        
        try {
            int age = Integer.parseInt(ageStr);
            if (age < 18 || age > 100) {
                errors.add("年龄必须在18-100之间");
            }
        } catch (NumberFormatException e) {
            errors.add("年龄必须是有效数字");
        }
        
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            errors.add("请输入有效的手机号码");
        }
        
        return errors;
    }

    /**
     * 处理用户删除（管理员功能）
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String uidStr = request.getParameter("uid");
        try {
            int uid = Integer.parseInt(uidStr);
            if (userDao.deleteUser(uid)) {
                response.sendRedirect("UserServlet?action=list&success=delete");
            } else {
                response.sendRedirect("UserServlet?action=list&error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("UserServlet?action=list&error=format");
        }
    }

    /**
     * 处理用户列表查询（管理员功能）
     */
    private void handleUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int page = 1;
            int pageSize = 10;

            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }

            List<User> users = userDao.getUsersByPage(page, pageSize);
            int totalUsers = userDao.getTotalUsersCount();
            int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

            // {{TITAN:
            // Action: Modified
            // Reason: 修复用户管理页面显示失败问题，将属性名从"users"改为"userlist"以匹配JSP页面期望
            // Timestamp: 2025-06-28 15:35:00 +08:00
            // }}
            // {{START MODIFICATION}}
            request.setAttribute("userlist", users);
            // {{END MODIFICATION}}
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalUsers", totalUsers);

            request.getRequestDispatcher("usermanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取用户列表失败");
            request.getRequestDispatcher("usermanage.jsp").forward(request, response);
        }
    }

    /**
     * 处理用户搜索（管理员功能）
     */
    private void handleUserSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            handleUserList(request, response);
            return;
        }

        try {
            List<User> users = userDao.searchUsers(keyword);
            // {{TITAN:
            // Action: Modified
            // Reason: 修复用户搜索功能，将属性名从"users"改为"userlist"以匹配JSP页面期望
            // Timestamp: 2025-06-28 15:37:00 +08:00
            // }}
            // {{START MODIFICATION}}
            request.setAttribute("userlist", users);
            // {{END MODIFICATION}}
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("usermanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "搜索用户失败");
            request.getRequestDispatcher("usermanage.jsp").forward(request, response);
        }
    }

    /**
     * 处理密码修改
     */
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查用户是否登录
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // 输入验证
        List<String> errors = new ArrayList<>();

        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            errors.add("原密码不能为空");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.add("新密码不能为空");
        } else if (newPassword.length() < 6 || newPassword.length() > 20) {
            errors.add("新密码长度必须在6-20字符之间");
        }

        if (!newPassword.equals(confirmPassword)) {
            errors.add("两次输入的新密码不一致");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("user.jsp").forward(request, response);
            return;
        }

        // 修改密码
        if (userDao.changePassword(currentUser.getUid(), oldPassword, newPassword)) {
            request.setAttribute("success", "密码修改成功");
        } else {
            request.setAttribute("error", "密码修改失败，请检查原密码是否正确");
        }

        request.getRequestDispatcher("user.jsp").forward(request, response);
    }

    /**
     * 检查是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && user.isAdmin();
        }
        return false;
    }
}
