package com.dao;

import com.entity.Message;
import com.util.BasicJDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论消息数据访问对象实现类
 * 实现评论相关的数据库操作
 * 
 * @author Titan
 * @version 1.0
 */
public class MessageDaoImpl implements MessageDao {
    
    @Override
    public boolean addMessage(Message message) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "INSERT INTO message (Uid, Bid, Mmessage) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, message.getUid());
            pstmt.setInt(2, message.getBid());
            pstmt.setString(3, message.getMmessage());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("添加评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public Message getMessageById(int mid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Mid = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractMessageFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据ID获取评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return null;
    }
    
    @Override
    public List<Message> getMessagesByBookId(int bookId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Bid = ? ORDER BY m.Mid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据图书ID获取评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }
    
    @Override
    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Uid = ? ORDER BY m.Mid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("根据用户ID获取评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }
    
    @Override
    public List<Message> getMessagesByBookId(int bookId, int page, int pageSize) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Bid = ? ORDER BY m.Mid DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, (page - 1) * pageSize);
            pstmt.setInt(3, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("分页获取图书评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }
    
    @Override
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "ORDER BY m.Mid DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取所有评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }
    
    @Override
    public List<Message> getMessagesByPage(int page, int pageSize) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "ORDER BY m.Mid DESC LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("分页获取评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }
    
    @Override
    public boolean updateMessage(Message message) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "UPDATE message SET Mmessage=? WHERE Mid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, message.getMmessage());
            pstmt.setInt(2, message.getMid());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean deleteMessage(int mid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM message WHERE Mid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mid);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("删除评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean deleteMessagesByUserAndBook(int userId, int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM message WHERE Uid=? AND Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("删除用户图书评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    @Override
    public int getTotalMessagesCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM message";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取评论总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public int getMessagesCountByBookId(int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM message WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取图书评论总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public int getMessagesCountByUserId(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM message WHERE Uid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取用户评论总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }

    @Override
    public List<Message> searchMessages(String keyword) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Mmessage LIKE ? ORDER BY m.Mid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("搜索评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }

    @Override
    public List<Message> getLatestMessages(int limit) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "ORDER BY m.Mid DESC LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取最新评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }

    @Override
    public List<Message> getUserMessagesForBook(int userId, int bookId) {
        List<Message> messages = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT m.*, u.Uname, b.Bname " +
                        "FROM message m " +
                        "JOIN user u ON m.Uid = u.Uid " +
                        "JOIN book b ON m.Bid = b.Bid " +
                        "WHERE m.Uid = ? AND m.Bid = ? ORDER BY m.Mid DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                messages.add(extractMessageFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取用户图书评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return messages;
    }

    @Override
    public boolean hasUserCommentedOnBook(int userId, int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM message WHERE Uid=? AND Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("检查用户是否评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return false;
    }

    @Override
    public boolean deleteMessages(List<Integer> messageIds) {
        // 简化实现，逐个删除
        boolean allSuccess = true;
        for (Integer mid : messageIds) {
            if (!deleteMessage(mid)) {
                allSuccess = false;
            }
        }
        return allSuccess;
    }

    @Override
    public boolean deleteMessagesByBookId(int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM message WHERE Bid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected >= 0; // 即使没有记录也算成功
        } catch (SQLException e) {
            System.err.println("删除图书相关评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    @Override
    public boolean deleteMessagesByUserId(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM message WHERE Uid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected >= 0; // 即使没有记录也算成功
        } catch (SQLException e) {
            System.err.println("删除用户相关评论失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    /**
     * 从ResultSet中提取Message对象
     * @param rs ResultSet对象
     * @return Message对象
     * @throws SQLException SQL异常
     */
    private Message extractMessageFromResultSet(ResultSet rs) throws SQLException {
        Message message = new Message();
        message.setMid(rs.getInt("Mid"));
        message.setUid(rs.getInt("Uid"));
        message.setBid(rs.getInt("Bid"));
        message.setMmessage(rs.getString("Mmessage"));
        message.setUname(rs.getString("Uname"));
        message.setBname(rs.getString("Bname"));
        return message;
    }
}
