package com.util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 登录检查过滤器
 * 拦截需要登录才能访问的页面
 * 
 * @author Titan
 * @version 1.0
 */
@WebFilter(urlPatterns = {"/user.jsp", "/salebook.jsp"})
public class LoginFilter implements Filter {
    
    // 需要登录的页面列表
    private List<String> loginRequiredPages;
    
    // 公开访问的页面列表（不需要登录）
    private List<String> publicPages;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        loginRequiredPages = Arrays.asList(
            "/user.jsp",
            "/salebook.jsp"
        );
        
        publicPages = Arrays.asList(
            "/login.jsp",
            "/usercreate.jsp",
            "/listbook.jsp",
            "/failure.jsp"
        );
        
        System.out.println("登录检查过滤器初始化完成");
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
        
        // 检查是否是需要登录的页面
        if (loginRequiredPages.contains(relativePath)) {
            HttpSession session = httpRequest.getSession(false);
            
            if (session == null || session.getAttribute("user") == null) {
                // 用户未登录，重定向到登录页面
                String returnUrl = httpRequest.getRequestURL().toString();
                String queryString = httpRequest.getQueryString();
                if (queryString != null) {
                    returnUrl += "?" + queryString;
                }
                
                // 保存原始请求URL，登录后可以重定向回来
                HttpSession newSession = httpRequest.getSession(true);
                newSession.setAttribute("returnUrl", returnUrl);
                
                httpResponse.sendRedirect(contextPath + "/login.jsp?error=needlogin");
                return;
            }
            
            System.out.println("用户 " + session.getAttribute("username") + " 访问: " + relativePath);
        }
        
        // 继续过滤器链
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("登录检查过滤器销毁");
    }
}
