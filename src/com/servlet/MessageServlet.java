package com.servlet;

import com.dao.MessageDao;
import com.dao.MessageDaoImpl;
import com.entity.Message;
import com.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 评论消息控制器
 * 处理评论相关的请求
 * 
 * @author Titan
 * @version 1.0
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
    private MessageDao messageDao = new MessageDaoImpl();
    
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
            action = "list";
        }
        
        switch (action) {
            case "add":
                handleAddMessage(request, response);
                break;
            case "delete":
                handleDeleteMessage(request, response);
                break;
            case "list":
                handleMessageList(request, response);
                break;
            case "search":
                handleMessageSearch(request, response);
                break;
            case "userMessages":
                handleUserMessages(request, response);
                break;
            default:
                response.sendRedirect("listbook.jsp");
        }
    }
    
    /**
     * 处理添加评论请求
     */
    private void handleAddMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            String bookIdStr = request.getParameter("bookId");
            String messageContent = request.getParameter("message");
            
            if (bookIdStr == null || messageContent == null) {
                response.sendRedirect("BookServlet?action=list&error=param");
                return;
            }
            
            int bookId = Integer.parseInt(bookIdStr);
            
            // 输入验证
            if (messageContent.trim().isEmpty()) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=empty");
                return;
            }
            
            if (messageContent.length() > 255) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=toolong");
                return;
            }
            
            // 创建评论对象
            Message message = new Message();
            message.setUid(user.getUid());
            message.setBid(bookId);
            message.setMmessage(messageContent.trim());
            
            // 保存到数据库
            if (messageDao.addMessage(message)) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&success=comment");
            } else {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=comment");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=list&error=format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("BookServlet?action=list&error=system");
        }
    }
    
    /**
     * 处理删除评论请求
     */
    private void handleDeleteMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            String midStr = request.getParameter("mid");
            String bookIdStr = request.getParameter("bookId");
            
            if (midStr == null) {
                response.sendRedirect("BookServlet?action=list&error=param");
                return;
            }
            
            int mid = Integer.parseInt(midStr);
            int bookId = bookIdStr != null ? Integer.parseInt(bookIdStr) : 0;
            
            // 检查权限：只有评论作者或管理员可以删除
            Message message = messageDao.getMessageById(mid);
            if (message == null) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=notfound");
                return;
            }
            
            if (message.getUid() != user.getUid() && !user.isAdmin()) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=permission");
                return;
            }
            
            // 删除评论
            if (messageDao.deleteMessage(mid)) {
                if (bookId > 0) {
                    response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&success=delete");
                } else {
                    response.sendRedirect("MessageServlet?action=list&success=delete");
                }
            } else {
                if (bookId > 0) {
                    response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=delete");
                } else {
                    response.sendRedirect("MessageServlet?action=list&error=delete");
                }
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=list&error=format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("BookServlet?action=list&error=system");
        }
    }
    
    /**
     * 处理评论列表请求（管理员功能）
     */
    private void handleMessageList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int page = 1;
            int pageSize = 20;
            
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            List<Message> messages = messageDao.getMessagesByPage(page, pageSize);
            int totalMessages = messageDao.getTotalMessagesCount();
            int totalPages = (int) Math.ceil((double) totalMessages / pageSize);
            
            request.setAttribute("messages", messages);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalMessages", totalMessages);
            
            request.getRequestDispatcher("messagemanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取评论列表失败");
            request.getRequestDispatcher("messagemanage.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理评论搜索请求（管理员功能）
     */
    private void handleMessageSearch(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            handleMessageList(request, response);
            return;
        }
        
        try {
            List<Message> messages = messageDao.searchMessages(keyword);
            request.setAttribute("messages", messages);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("messagemanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "搜索评论失败");
            request.getRequestDispatcher("messagemanage.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理用户评论列表请求
     */
    private void handleUserMessages(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            List<Message> messages = messageDao.getMessagesByUserId(user.getUid());
            
            request.setAttribute("userMessages", messages);
            request.getRequestDispatcher("user.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取用户评论失败");
            request.getRequestDispatcher("user.jsp").forward(request, response);
        }
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
