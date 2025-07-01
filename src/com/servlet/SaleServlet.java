package com.servlet;

import com.dao.BookDao;
import com.dao.BookDaoImpl;
import com.dao.SaleDao;
import com.dao.SaleDaoImpl;
import com.entity.Book;
import com.entity.Sale;
import com.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 销售控制器
 * 处理图书销售相关的请求
 * 
 * @author Titan
 * @version 1.0
 */
@WebServlet("/SaleServlet")
public class SaleServlet extends HttpServlet {
    private SaleDao saleDao = new SaleDaoImpl();
    private BookDao bookDao = new BookDaoImpl();
    
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
            case "buy":
                handleBookPurchase(request, response);
                break;
            case "booklist":
                handleBooklistPurchase(request, response);
                break;
            case "list":
                handleSaleList(request, response);
                break;
            case "search":
                handleSaleSearch(request, response);
                break;
            case "userHistory":
                handleUserHistory(request, response);
                break;
            case "statistics":
                handleSaleStatistics(request, response);
                break;
            default:
                response.sendRedirect("listbook.jsp");
        }
    }
    
    /**
     * 处理图书购买请求
     */
    private void handleBookPurchase(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            String bookIdStr = request.getParameter("bookId");
            String quantityStr = request.getParameter("quantity");
            
            if (bookIdStr == null || quantityStr == null) {
                response.sendRedirect("BookServlet?action=list&error=param");
                return;
            }
            
            int bookId = Integer.parseInt(bookIdStr);
            int quantity = Integer.parseInt(quantityStr);
            
            // 输入验证
            if (quantity <= 0) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=quantity");
                return;
            }
            
            // 检查图书是否存在
            Book book = bookDao.getBookById(bookId);
            if (book == null) {
                response.sendRedirect("BookServlet?action=list&error=notfound");
                return;
            }
            
            // 检查库存
            if (book.getBstock() < quantity) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + 
                    "&error=stock&available=" + book.getBstock());
                return;
            }
            
            // 执行购买操作
            boolean success = saleDao.purchaseBook(user.getUid(), bookId, quantity);
            
            if (success) {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&success=purchase");
            } else {
                response.sendRedirect("BookServlet?action=detail&id=" + bookId + "&error=purchase");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=list&error=format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("BookServlet?action=list&error=system");
        }
    }

    /**
     * 处理从书籍列表页面的购买请求
     */
    private void handleBooklistPurchase(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            String bookIdStr = request.getParameter("bid");
            String userIdStr = request.getParameter("uid");

            if (bookIdStr == null || userIdStr == null) {
                response.sendRedirect("BookServlet?action=list&error=param");
                return;
            }

            int bookId = Integer.parseInt(bookIdStr);
            int userId = Integer.parseInt(userIdStr);
            int quantity = 1; // 从书籍列表购买默认数量为1

            // 验证用户ID匹配
            if (user.getUid() != userId) {
                response.sendRedirect("login.jsp");
                return;
            }

            // 检查图书是否存在
            Book book = bookDao.getBookById(bookId);
            if (book == null) {
                response.sendRedirect("BookServlet?action=list&error=notfound");
                return;
            }

            // 检查库存
            if (book.getBstock() < quantity) {
                response.sendRedirect("BookServlet?action=list&error=stock");
                return;
            }

            // 执行购买操作
            boolean success = saleDao.purchaseBook(userId, bookId, quantity);

            if (success) {
                response.sendRedirect("BookServlet?action=list&success=purchase");
            } else {
                response.sendRedirect("BookServlet?action=list&error=purchase");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=list&error=format");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("BookServlet?action=list&error=system");
        }
    }

    /**
     * 处理销售记录查询请求（管理员功能）
     */
    private void handleSaleList(HttpServletRequest request, HttpServletResponse response) 
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
            
            List<Sale> sales = saleDao.getSalesByPage(page, pageSize);
            int totalSales = saleDao.getTotalSalesCount();
            int totalPages = (int) Math.ceil((double) totalSales / pageSize);
            
            // 获取销售统计信息
            Map<String, Object> statistics = saleDao.getSalesStatistics();
            
            request.setAttribute("sales", sales);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalSales", totalSales);
            request.setAttribute("statistics", statistics);
            
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取销售记录失败");
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理销售记录搜索请求（管理员功能）
     */
    private void handleSaleSearch(HttpServletRequest request, HttpServletResponse response) 
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
            
            // 获取搜索条件
            String userIdStr = request.getParameter("userId");
            String bookIdStr = request.getParameter("bookId");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            
            Integer userId = null;
            Integer bookId = null;
            
            if (userIdStr != null && !userIdStr.trim().isEmpty()) {
                userId = Integer.parseInt(userIdStr);
            }
            
            if (bookIdStr != null && !bookIdStr.trim().isEmpty()) {
                bookId = Integer.parseInt(bookIdStr);
            }
            
            List<Sale> sales = saleDao.searchSalesByPage(page, pageSize, userId, bookId, startDate, endDate);
            int totalSales = saleDao.getTotalSalesCount(userId, bookId, startDate, endDate);
            int totalPages = (int) Math.ceil((double) totalSales / pageSize);
            
            // 获取销售统计信息
            Map<String, Object> statistics = saleDao.getSalesStatistics(userId, bookId, startDate, endDate);
            
            request.setAttribute("sales", sales);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalSales", totalSales);
            request.setAttribute("statistics", statistics);
            request.setAttribute("searchUserId", userIdStr);
            request.setAttribute("searchBookId", bookIdStr);
            request.setAttribute("searchStartDate", startDate);
            request.setAttribute("searchEndDate", endDate);
            
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "搜索销售记录失败");
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理用户购买历史请求
     */
    private void handleUserHistory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            User user = (User) session.getAttribute("user");
            int page = 1;
            int pageSize = 10;
            
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            List<Sale> sales = saleDao.getUserPurchaseHistory(user.getUid(), page, pageSize);
            int totalSales = saleDao.getTotalSalesCount(user.getUid(), null, null, null);
            int totalPages = (int) Math.ceil((double) totalSales / pageSize);
            
            request.setAttribute("sales", sales);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalSales", totalSales);
            
            request.getRequestDispatcher("user.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取购买历史失败");
            request.getRequestDispatcher("user.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理销售统计请求（管理员功能）
     */
    private void handleSaleStatistics(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            Map<String, Object> statistics = saleDao.getSalesStatistics();
            request.setAttribute("statistics", statistics);
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取销售统计失败");
            request.getRequestDispatcher("salemanage.jsp").forward(request, response);
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
