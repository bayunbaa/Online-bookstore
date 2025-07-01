package com.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 字符编码过滤器
 * 统一设置请求和响应的字符编码为UTF-8
 *
 * {{TITAN:
 * Action: Modified
 * Reason: 移除@WebFilter注解避免与web.xml配置冲突，增强编码设置和调试功能
 * Timestamp: 2025-06-28 20:35:00 +08:00
 * }}
 *
 * @author Titan
 * @version 2.0
 */
public class EncodingFilter implements Filter {
    private String encoding = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.trim().isEmpty()) {
            this.encoding = encodingParam;
        }
        System.out.println("字符编码过滤器初始化完成，编码设置为: " + this.encoding);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                        FilterChain chain) throws IOException, ServletException {

        // {{START MODIFICATION}}
        // 强制设置请求编码，即使已经设置过也要重新设置
        if (request.getCharacterEncoding() == null || !encoding.equals(request.getCharacterEncoding())) {
            request.setCharacterEncoding(encoding);
        }

        // 强制设置响应编码和内容类型
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html;charset=" + encoding);

        // 如果是HTTP请求，进行增强处理
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // 添加调试日志
            String requestURI = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            System.out.println("[编码过滤器] 处理请求: " + method + " " + requestURI +
                             " | 请求编码: " + request.getCharacterEncoding() +
                             " | 响应编码: " + response.getCharacterEncoding());

            // 设置额外的响应头确保编码正确
            httpResponse.setHeader("Content-Type", "text/html;charset=" + encoding);
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
        }
        
        // 继续过滤器链
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        System.out.println("字符编码过滤器销毁");
    }
}
