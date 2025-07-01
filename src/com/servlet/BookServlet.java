package com.servlet;

import com.dao.BookDao;
import com.dao.BookDaoImpl;
import com.dao.MessageDao;
import com.dao.MessageDaoImpl;
import com.entity.Book;
import com.entity.Message;
import com.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书控制器
 * 处理图书相关的请求
 * 
 * @author Titan
 * @version 1.0
 */
@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
    private BookDao bookDao = new BookDaoImpl();
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
            case "list":
                handleBookList(request, response);
                break;
            case "search":
                handleBookSearch(request, response);
                break;
            case "detail":
                handleBookDetail(request, response);
                break;
            case "add":
                handleAddBook(request, response);
                break;
            case "update":
                handleUpdateBook(request, response);
                break;
            case "delete":
                handleDeleteBook(request, response);
                break;
            case "manage":
                handleBookManage(request, response);
                break;
            case "type":
                handleBooksByType(request, response);
                break;
            default:
                handleBookList(request, response);
        }
    }
    
    /**
     * 处理图书列表请求
     */
    private void handleBookList(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int page = 1;
            int pageSize = 10;
            
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            }
            
            List<Book> books = bookDao.getBooksByPage(page, pageSize);
            int totalBooks = bookDao.getTotalBooksCount();
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);
            
            // 获取图书类型列表用于筛选
            List<String> bookTypes = bookDao.getBookTypes();
            
            request.setAttribute("books", books);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            request.setAttribute("bookTypes", bookTypes);
            
            request.getRequestDispatcher("listbook.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取图书列表失败");
            request.getRequestDispatcher("failure.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理图书搜索请求
     */
    private void handleBookSearch(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            String type = request.getParameter("type");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            
            float minPrice = 0;
            float maxPrice = 0;
            
            if (minPriceStr != null && !minPriceStr.isEmpty()) {
                minPrice = Float.parseFloat(minPriceStr);
            }
            
            if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                maxPrice = Float.parseFloat(maxPriceStr);
            }
            
            List<Book> books = bookDao.searchBooks(keyword, type, minPrice, maxPrice);
            List<String> bookTypes = bookDao.getBookTypes();
            
            request.setAttribute("books", books);
            request.setAttribute("bookTypes", bookTypes);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("searchType", type);
            request.setAttribute("searchMinPrice", minPriceStr);
            request.setAttribute("searchMaxPrice", maxPriceStr);
            
            request.getRequestDispatcher("listbook.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "搜索图书失败");
            request.getRequestDispatcher("failure.jsp").forward(request, response);
        }
    }

    /**
     * 处理按类型筛选图书请求
     */
    private void handleBooksByType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String bookType = request.getParameter("booktype");
            if (bookType == null || bookType.isEmpty()) {
                handleBookList(request, response);
                return;
            }

            // 获取指定类型的图书
            List<Book> books = bookDao.getBooksByType(bookType);
            List<String> bookTypes = bookDao.getBookTypes();

            request.setAttribute("books", books);
            request.setAttribute("bookTypes", bookTypes);
            request.setAttribute("selectedType", bookType);

            request.getRequestDispatcher("listbook.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "按类型筛选图书失败");
            request.getRequestDispatcher("failure.jsp").forward(request, response);
        }
    }

    /**
     * 处理图书详情请求
     */
    private void handleBookDetail(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String bidStr = request.getParameter("id");
            if (bidStr == null || bidStr.isEmpty()) {
                response.sendRedirect("BookServlet?action=list");
                return;
            }
            
            int bookId = Integer.parseInt(bidStr);
            Book book = bookDao.getBookById(bookId);
            
            if (book != null) {
                // 获取该图书的评论
                List<Message> messages = messageDao.getMessagesByBookId(bookId);
                
                request.setAttribute("book", book);
                request.setAttribute("messages", messages);
                request.getRequestDispatcher("salebook.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "图书不存在");
                request.getRequestDispatcher("failure.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=list");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取图书详情失败");
            request.getRequestDispatcher("failure.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理添加图书请求（管理员功能）
     */
    private void handleAddBook(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String bname = request.getParameter("bname");
            String bpriceStr = request.getParameter("bprice");
            String btype = request.getParameter("btype");
            String bstockStr = request.getParameter("bstock");
            String bdes = request.getParameter("bdes");
            
            // 输入验证
            List<String> errors = validateBookInput(bname, bpriceStr, btype, bstockStr, bdes);
            
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("bname", bname);
                request.setAttribute("bprice", bpriceStr);
                request.setAttribute("btype", btype);
                request.setAttribute("bstock", bstockStr);
                request.setAttribute("bdes", bdes);
                request.getRequestDispatcher("addbook.jsp").forward(request, response);
                return;
            }
            
            // 创建图书对象
            Book book = new Book();
            book.setBname(bname.trim());
            book.setBprice(Float.parseFloat(bpriceStr));
            book.setBtype(btype != null ? btype.trim() : "");
            book.setBstock(Integer.parseInt(bstockStr));
            book.setBdes(bdes != null ? bdes.trim() : "");
            
            // 保存到数据库
            if (bookDao.addBook(book)) {
                response.sendRedirect("BookServlet?action=manage&success=add");
            } else {
                request.setAttribute("error", "添加图书失败");
                request.getRequestDispatcher("addbook.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "添加图书时发生错误");
            request.getRequestDispatcher("addbook.jsp").forward(request, response);
        }
    }
    
    /**
     * 处理图书管理页面请求（管理员功能）
     */
    private void handleBookManage(HttpServletRequest request, HttpServletResponse response) 
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
            
            // {{TITAN:
            // Action: Modified
            // Reason: 修正属性名不匹配，JSP期望session中的booklist
            // Timestamp: 2025-06-28 14:40:00 +08:00
            // }}
            // {{START MODIFICATION}}
            List<Book> books = bookDao.getBooksByPage(page, pageSize);
            int totalBooks = bookDao.getTotalBooksCount();
            int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

            // 设置到session中，与JSP页面期望的属性名一致
            request.getSession().setAttribute("booklist", books);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            // {{END MODIFICATION}}
            
            request.getRequestDispatcher("bookmanage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "获取图书管理列表失败");
            request.getRequestDispatcher("bookmanage.jsp").forward(request, response);
        }
    }
    
    /**
     * 验证图书输入
     */
    private List<String> validateBookInput(String bname, String bpriceStr, String btype, 
                                         String bstockStr, String bdes) {
        List<String> errors = new ArrayList<>();
        
        if (bname == null || bname.trim().isEmpty()) {
            errors.add("书名不能为空");
        }
        
        try {
            float bprice = Float.parseFloat(bpriceStr);
            if (bprice <= 0) {
                errors.add("价格必须大于0");
            }
        } catch (NumberFormatException e) {
            errors.add("价格格式不正确");
        }
        
        try {
            int bstock = Integer.parseInt(bstockStr);
            if (bstock < 0) {
                errors.add("库存不能为负数");
            }
        } catch (NumberFormatException e) {
            errors.add("库存格式不正确");
        }
        
        return errors;
    }
    
    /**
     * 处理更新图书请求（管理员功能）
     */
    private void handleUpdateBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String bidStr = request.getParameter("bid");
            String bname = request.getParameter("bname");
            String bpriceStr = request.getParameter("bprice");
            String btype = request.getParameter("btype");
            String bstockStr = request.getParameter("bstock");
            String bdes = request.getParameter("bdes");

            // 输入验证
            List<String> errors = validateBookInput(bname, bpriceStr, btype, bstockStr, bdes);

            int bid = 0;
            try {
                bid = Integer.parseInt(bidStr);
            } catch (NumberFormatException e) {
                errors.add("图书ID格式不正确");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.setAttribute("bid", bidStr);
                request.setAttribute("bname", bname);
                request.setAttribute("bprice", bpriceStr);
                request.setAttribute("btype", btype);
                request.setAttribute("bstock", bstockStr);
                request.setAttribute("bdes", bdes);
                request.getRequestDispatcher("BookServlet?action=manage").forward(request, response);
                return;
            }

            // 创建图书对象
            Book book = new Book();
            book.setBid(bid);
            book.setBname(bname.trim());
            book.setBprice(Float.parseFloat(bpriceStr));
            book.setBtype(btype != null ? btype.trim() : "");
            book.setBstock(Integer.parseInt(bstockStr));
            book.setBdes(bdes != null ? bdes.trim() : "");

            // 更新到数据库
            if (bookDao.updateBook(book)) {
                response.sendRedirect("BookServlet?action=manage&success=update");
            } else {
                request.setAttribute("error", "更新图书失败");
                response.sendRedirect("BookServlet?action=manage&error=update");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "更新图书时发生错误");
            response.sendRedirect("BookServlet?action=manage&error=system");
        }
    }

    /**
     * 处理删除图书请求（管理员功能）
     */
    private void handleDeleteBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendRedirect("login.jsp");
            return;
        }

        String bidStr = request.getParameter("bid");
        try {
            int bid = Integer.parseInt(bidStr);
            if (bookDao.deleteBook(bid)) {
                response.sendRedirect("BookServlet?action=manage&success=delete");
            } else {
                response.sendRedirect("BookServlet?action=manage&error=delete");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("BookServlet?action=manage&error=format");
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
