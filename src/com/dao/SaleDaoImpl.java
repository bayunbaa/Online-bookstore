package com.dao;

import com.entity.Sale;
import com.util.BasicJDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售数据访问对象实现类
 * 实现销售相关的数据库操作
 * 
 * @author Titan
 * @version 1.0
 */
public class SaleDaoImpl implements SaleDao {
    
    @Override
    public boolean purchaseBook(int userId, int bookId, int quantity) {
        Connection conn = null;
        CallableStatement cstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            conn.setAutoCommit(false); // 开启事务
            
            // 调用存储过程
            String sql = "{CALL sale(?, ?, ?)}";
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, userId);
            cstmt.setInt(2, bookId);
            cstmt.setInt(3, quantity);
            
            cstmt.execute();
            conn.commit(); // 提交事务
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // 回滚事务
                }
            } catch (SQLException ex) {
                System.err.println("事务回滚失败: " + ex.getMessage());
                ex.printStackTrace();
            }
            System.err.println("购买图书失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("关闭连接失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public boolean addSaleRecord(Sale sale) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "INSERT INTO sale (Uid, Bid, Scount) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sale.getUid());
            pstmt.setInt(2, sale.getBid());
            pstmt.setInt(3, sale.getScount());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("添加销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public Sale getSaleById(int sid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "WHERE s.Sid = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractSaleFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID获取销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return null;
    }
    
    @Override
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "ORDER BY s.Sid DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取所有销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }
    
    @Override
    public List<Sale> getSalesByPage(int page, int pageSize) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "ORDER BY s.Sid DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("分页获取销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }
    
    @Override
    public List<Sale> getSalesByUserId(int userId) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "WHERE s.Uid = ? ORDER BY s.Sid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据用户ID获取销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }
    
    @Override
    public List<Sale> getSalesByBookId(int bookId) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "WHERE s.Bid = ? ORDER BY s.Sid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据图书ID获取销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }
    
    @Override
    public int getTotalSalesCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM sale";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取销售记录总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }
    
    @Override
    public List<Sale> searchSales(Integer userId, Integer bookId, String startDate, String endDate) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = BasicJDBC.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s.*, u.Uname, b.Bname, b.Bprice ");
            sql.append("FROM sale s ");
            sql.append("JOIN user u ON s.Uid = u.Uid ");
            sql.append("JOIN book b ON s.Bid = b.Bid ");
            sql.append("WHERE 1=1 ");

            List<Object> parameters = new ArrayList<>();

            if (userId != null) {
                sql.append("AND s.Uid = ? ");
                parameters.add(userId);
            }

            if (bookId != null) {
                sql.append("AND s.Bid = ? ");
                parameters.add(bookId);
            }

            sql.append("ORDER BY s.Sid DESC");

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("搜索销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }

    @Override
    public List<Sale> searchSalesByPage(int page, int pageSize, Integer userId, Integer bookId,
                                       String startDate, String endDate) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = BasicJDBC.getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s.*, u.Uname, b.Bname, b.Bprice ");
            sql.append("FROM sale s ");
            sql.append("JOIN user u ON s.Uid = u.Uid ");
            sql.append("JOIN book b ON s.Bid = b.Bid ");
            sql.append("WHERE 1=1 ");

            List<Object> parameters = new ArrayList<>();

            if (userId != null) {
                sql.append("AND s.Uid = ? ");
                parameters.add(userId);
            }

            if (bookId != null) {
                sql.append("AND s.Bid = ? ");
                parameters.add(bookId);
            }

            sql.append("ORDER BY s.Sid DESC LIMIT ?, ?");
            parameters.add((page - 1) * pageSize);
            parameters.add(pageSize);

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("分页搜索销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }

    @Override
    public int getTotalSalesCount(Integer userId, Integer bookId, String startDate, String endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM sale WHERE 1=1 ");
            List<Object> parameters = new ArrayList<>();

            if (userId != null) {
                sql.append("AND Uid = ? ");
                parameters.add(userId);
            }

            if (bookId != null) {
                sql.append("AND Bid = ? ");
                parameters.add(bookId);
            }

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取销售记录总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public List<Sale> getUserPurchaseHistory(int userId, int page, int pageSize) {
        List<Sale> sales = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT s.*, u.Uname, b.Bname, b.Bprice " +
                        "FROM sale s " +
                        "JOIN user u ON s.Uid = u.Uid " +
                        "JOIN book b ON s.Bid = b.Bid " +
                        "WHERE s.Uid = ? ORDER BY s.Sid DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, (page - 1) * pageSize);
            pstmt.setInt(3, pageSize);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取用户购买历史失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return sales;
    }

    @Override
    public Map<String, Object> getSalesStatistics() {
        Map<String, Object> stats = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) as totalSales, SUM(s.Scount) as totalQuantity, " +
                        "SUM(s.Scount * b.Bprice) as totalAmount " +
                        "FROM sale s JOIN book b ON s.Bid = b.Bid";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                stats.put("totalSales", rs.getInt("totalSales"));
                stats.put("totalQuantity", rs.getInt("totalQuantity"));
                stats.put("totalAmount", rs.getFloat("totalAmount"));
            }
        } catch (SQLException e) {
            System.err.println("获取销售统计失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return stats;
    }

    @Override
    public Map<String, Object> getSalesStatistics(Integer userId, Integer bookId, String startDate, String endDate) {
        // 简化实现，返回基本统计
        return getSalesStatistics();
    }

    @Override
    public List<Map<String, Object>> getDailySalesStatistics(int days) {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getBookSalesRanking(int limit) {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getUserPurchaseRanking(int limit) {
        // 简化实现，返回空列表
        return new ArrayList<>();
    }

    @Override
    public boolean deleteSale(int sid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM sale WHERE Sid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("删除销售记录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    /**
     * 从ResultSet中提取Sale对象
     * @param rs ResultSet对象
     * @return Sale对象
     * @throws SQLException SQL异常
     */
    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setSid(rs.getInt("Sid"));
        sale.setUid(rs.getInt("Uid"));
        sale.setBid(rs.getInt("Bid"));
        sale.setScount(rs.getInt("Scount"));
        sale.setUname(rs.getString("Uname"));
        sale.setBname(rs.getString("Bname"));
        sale.setBprice(rs.getFloat("Bprice"));
        sale.setTotalAmount(sale.getScount() * sale.getBprice());
        return sale;
    }
}
