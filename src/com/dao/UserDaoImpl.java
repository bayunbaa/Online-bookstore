package com.dao;

import com.entity.User;
import com.util.BasicJDBC;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象实现类
 * 实现用户相关的数据库操作
 * 
 * @author Titan
 * @version 1.0
 */
public class UserDaoImpl implements UserDao {
    
    @Override
    public boolean validateLogin(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE Uname=? AND Upassword=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("验证用户登录失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return false;
    }
    
    @Override
    public User getUserByName(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user WHERE Uname=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据用户名获取用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return null;
    }
    
    @Override
    public User getUserById(int uid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user WHERE Uid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("根据用户ID获取用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return null;
    }
    
    @Override
    public boolean userCreate(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "INSERT INTO user (Uname, Upassword, Uage, Uphone, Ud) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUname());
            pstmt.setString(2, user.getUpassword());
            pstmt.setInt(3, user.getUage());
            pstmt.setString(4, user.getUphone());
            pstmt.setInt(5, user.getUd());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("创建用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "UPDATE user SET Uname=?, Upassword=?, Uage=?, Uphone=?, Ud=? WHERE Uid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUname());
            pstmt.setString(2, user.getUpassword());
            pstmt.setInt(3, user.getUage());
            pstmt.setString(4, user.getUphone());
            pstmt.setInt(5, user.getUd());
            pstmt.setInt(6, user.getUid());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("更新用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public boolean deleteUser(int uid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "DELETE FROM user WHERE Uid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, uid);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("删除用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }
    
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user ORDER BY Uid";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取所有用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return users;
    }
    
    @Override
    public List<User> getUsersByPage(int page, int pageSize) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user ORDER BY Uid LIMIT ?, ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, (page - 1) * pageSize);
            pstmt.setInt(2, pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("分页获取用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return users;
    }
    
    @Override
    public int getTotalUsersCount() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM user";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("获取用户总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return 0;
    }
    
    @Override
    public boolean isUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE Uname=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("检查用户名是否存在失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return true; // 出错时返回true，避免重复用户名
    }
    
    @Override
    public boolean isPhoneExists(String phone) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT COUNT(*) FROM user WHERE Uphone=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("检查电话号码是否存在失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return true; // 出错时返回true，避免重复电话号码
    }

    @Override
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user WHERE Uname LIKE ? OR Uphone LIKE ? ORDER BY Uid";
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("搜索用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return users;
    }

    @Override
    public boolean changePassword(int uid, String oldPassword, String newPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = BasicJDBC.getConnection();
            // 先验证旧密码
            String checkSql = "SELECT COUNT(*) FROM user WHERE Uid=? AND Upassword=?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, uid);
            pstmt.setString(2, oldPassword);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // 旧密码正确，更新新密码
                String updateSql = "UPDATE user SET Upassword=? WHERE Uid=?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setString(1, newPassword);
                pstmt.setInt(2, uid);

                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("修改密码失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt);
        }
        return false;
    }

    @Override
    public List<User> getAdminUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user WHERE Ud=0 ORDER BY Uid";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取管理员用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return users;
    }

    @Override
    public List<User> getRegularUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = BasicJDBC.getConnection();
            String sql = "SELECT * FROM user WHERE Ud=1 ORDER BY Uid";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("获取普通用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            BasicJDBC.closeConnection(conn, pstmt, rs);
        }
        return users;
    }

    /**
     * 从ResultSet中提取User对象
     * @param rs ResultSet对象
     * @return User对象
     * @throws SQLException SQL异常
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUid(rs.getInt("Uid"));
        user.setUname(rs.getString("Uname"));
        user.setUpassword(rs.getString("Upassword"));
        user.setUage(rs.getInt("Uage"));
        user.setUphone(rs.getString("Uphone"));
        user.setUd(rs.getInt("Ud"));
        return user;
    }
}
