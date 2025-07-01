package com.dao;

import com.entity.Book;
import com.util.BasicJDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书数据访问对象实现类
 * 实现图书相关的数据库操作
 * 
 * @author Titan
 * @version 1.0
 */
public class BookDaoImpl implements BookDao {
    
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book ORDER BY Bid DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取所有图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }
    
    @Override
    public List<Book> getBooksByPage(int page, int pageSize) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book ORDER BY Bid DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("分页获取图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }
    
    @Override
    public Book getBookById(int bid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID获取图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return null;
    }
    
    @Override
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "INSERT INTO book (Bname, Bprice, Btype, Bstock, Bsale, Bdes) VALUES (?, ?, ?, ?, 0, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getBname());
            pstmt.setFloat(2, book.getBprice());
            pstmt.setString(3, book.getBtype());
            pstmt.setInt(4, book.getBstock());
            pstmt.setString(5, book.getBdes());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("添加图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "UPDATE book SET Bname=?, Bprice=?, Btype=?, Bstock=?, Bdes=? WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getBname());
            pstmt.setFloat(2, book.getBprice());
            pstmt.setString(3, book.getBtype());
            pstmt.setInt(4, book.getBstock());
            pstmt.setString(5, book.getBdes());
            pstmt.setInt(6, book.getBid());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean deleteBook(int bid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            // 检查是否有相关的销售记录
            String checkSql = "SELECT COUNT(*) FROM sale WHERE Bid=?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, bid);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                // 如果有销售记录，进行软删除（设置库存为0）
                String updateSql = "UPDATE book SET Bstock=0 WHERE Bid=?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setInt(1, bid);
                return pstmt.executeUpdate() > 0;
            } else {
                // 如果没有销售记录，可以直接删除
                String deleteSql = "DELETE FROM book WHERE Bid=?";
                pstmt = conn.prepareStatement(deleteSql);
                pstmt.setInt(1, bid);
                return pstmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("删除图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public List<Book> searchBooks(String keyword, String type, float minPrice, float maxPrice) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM book WHERE 1=1");
            List<Object> parameters = new ArrayList<>();
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                sql.append(" AND Bname LIKE ?");
                parameters.add("%" + keyword + "%");
            }
            
            if (type != null && !type.trim().isEmpty()) {
                sql.append(" AND Btype = ?");
                parameters.add(type);
            }
            
            if (minPrice > 0) {
                sql.append(" AND Bprice >= ?");
                parameters.add(minPrice);
            }
            
            if (maxPrice > 0) {
                sql.append(" AND Bprice <= ?");
                parameters.add(maxPrice);
            }
            
            sql.append(" ORDER BY Bid DESC");
            
            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }
            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("搜索图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }
    
    @Override
    public List<Book> searchBooksByName(String bookName) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bname LIKE ? ORDER BY Bid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + bookName + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据书名搜索图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }
    
    @Override
    public List<Book> getBooksByType(String type) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Btype=? ORDER BY Bid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据类型获取图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }
    
    @Override
    public List<String> getBookTypes() {
        List<String> types = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT DISTINCT Btype FROM book WHERE Btype IS NOT NULL ORDER BY Btype";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                types.add(rs.getString("Btype"));
            }
        } catch (SQLException e) {
            System.err.println("获取图书类型失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return types;
    }

    @Override
    public int getTotalBooksCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM book";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取图书总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public List<Book> getBooksInStock() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bstock > 0 ORDER BY Bid DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取有库存图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }

    @Override
    public List<Book> getTopSellingBooks(int limit) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bstock > 0 ORDER BY Bsale DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取热门图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }

    @Override
    public List<Book> getLatestBooks(int limit) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book ORDER BY Bid DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取最新图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }

    @Override
    public List<Book> getBooksByPriceRange(float minPrice, float maxPrice) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bprice >= ? AND Bprice <= ? ORDER BY Bprice";
            pstmt = conn.prepareStatement(sql);
            pstmt.setFloat(1, minPrice);
            pstmt.setFloat(2, maxPrice);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据价格区间获取图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }

    @Override
    public boolean updateBookStock(int bid, int stock) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "UPDATE book SET Bstock=? WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, stock);
            pstmt.setInt(2, bid);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新图书库存失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    @Override
    public boolean updateBookSales(int bid, int saleCount) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "UPDATE book SET Bsale=Bsale+? WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, saleCount);
            pstmt.setInt(2, bid);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新图书销量失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    @Override
    public boolean isBookNameExists(String bookName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM book WHERE Bname=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("检查图书名称是否存在失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return true; // 出错时返回true，避免重复书名
    }

    @Override
    public List<Book> getLowStockBooks(int threshold) {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM book WHERE Bstock < ? ORDER BY Bstock ASC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, threshold);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取库存不足图书失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return books;
    }

    /**
     * 从ResultSet中提取Book对象
     * @param rs ResultSet对象
     * @return Book对象
     * @throws SQLException SQL异常
     */
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBid(rs.getInt("Bid"));
        book.setBname(rs.getString("Bname"));
        book.setBprice(rs.getFloat("Bprice"));
        book.setBtype(rs.getString("Btype"));
        book.setBstock(rs.getInt("Bstock"));
        book.setBsale(rs.getInt("Bsale"));
        book.setBdes(rs.getString("Bdes"));
        return book;
    }
}
