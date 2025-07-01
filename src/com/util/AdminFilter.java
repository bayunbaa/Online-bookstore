package com.util;

import com.entity.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 管理员权限过滤器
 * 拦截需要管理员权限的请求
 * 
 * @author Titan
 * @version 1.0
 */
@WebFilter(urlPatterns = {"/manage.jsp", "/bookmanage.jsp", "/usermanage.jsp", "/salemanage.jsp", "/addbook.jsp"})
public class AdminFilter implements Filter {
    
    // 需要管理员权限的页面列表
    private List<String> adminPages;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminPages = Arrays.asList(
            "/manage.jsp",
            "/bookmanage.jsp", 
            "/usermanage.jsp",
            "/salemanage.jsp",
            "/addbook.jsp"
        );
        System.out.println("管理员权限过滤器初始化完成");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                        FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // 获取相对路径
        String relativePath = requestURI.substring(contextPath.length());
        
        // 检查是否是需要管理员权限的页面
        if (adminPages.contains(relativePath)) {
            HttpSession session = httpRequest.getSession(false);
            
            if (session == null || session.getAttribute("user") == null) {
                // 用户未登录，重定向到登录页面
                httpResponse.sendRedirect(contextPath + "/login.jsp?error=notlogin");
                return;
            }
            
            User user = (User) session.getAttribute("user");
            if (!user.isAdmin()) {
                // 用户不是管理员，重定向到无权限页面
                httpResponse.sendRedirect(contextPath + "/failure.jsp?error=permission");
                return;
            }
            
            System.out.println("管理员 " + user.getUname() + " 访问: " + relativePath);
        }
        
        // 继续过滤器链
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("管理员权限过滤器销毁");
    }
}
