package com.util;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

/**
 * 数据库连接工具类
 * 提供数据库连接的获取和关闭功能
 * 支持配置文件和默认配置
 *
 * @author Titan
 * @version 2.0
 */
public class BasicJDBC {
    // 默认数据库连接参数
    private static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String URL = "jdbc:mysql://localhost:3306/bookshop?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static String USERNAME = "root";
    private static String PASSWORD = "root";

// {{TITAN:
// Action: Modified
// Reason: 添加配置文件支持，提高部署灵活性
// Timestamp: 2025-06-28 15:10:00 +08:00
// }}
// {{START MODIFICATION}}
    // 配置文件加载标志
    private static boolean configLoaded = false;
// {{END MODIFICATION}}
    
// {{TITAN:
// Action: Modified
// Reason: 添加配置文件加载逻辑
// Timestamp: 2025-06-28 15:10:00 +08:00
// }}
// {{START MODIFICATION}}
    // 静态代码块，加载数据库驱动和配置
    static {
        loadConfiguration();
        try {
            Class.forName(DRIVER);
            System.out.println("数据库驱动加载成功: " + DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("数据库驱动加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 加载数据库配置文件
     */
    private static void loadConfiguration() {
        if (configLoaded) return;

        try {
            InputStream is = BasicJDBC.class.getClassLoader().getResourceAsStream("database.properties");
            if (is == null) {
                // 尝试从WEB-INF目录加载
                is = BasicJDBC.class.getResourceAsStream("/WEB-INF/database.properties");
            }

            if (is != null) {
                Properties props = new Properties();
                props.load(is);

                DRIVER = props.getProperty("db.driver", DRIVER);
                URL = props.getProperty("db.url", URL);
                USERNAME = props.getProperty("db.username", USERNAME);
                PASSWORD = props.getProperty("db.password", PASSWORD);

                System.out.println("数据库配置文件加载成功");
                is.close();
            } else {
                System.out.println("未找到配置文件，使用默认配置");
            }
            configLoaded = true;
        } catch (Exception e) {
            System.err.println("加载配置文件失败，使用默认配置: " + e.getMessage());
            configLoaded = true;
        }
    }
// {{END MODIFICATION}}
    
    /**
     * 获取数据库连接
     * @return Connection 数据库连接对象
     * @throws SQLException SQL异常
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("数据库连接成功");
            return conn;
        } catch (SQLException e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 关闭数据库连接（只关闭Connection）
     * @param conn 数据库连接对象
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接已关闭");
            } catch (SQLException e) {
                System.err.println("关闭数据库连接失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 关闭数据库连接（Connection + PreparedStatement）
     * @param conn 数据库连接对象
     * @param pstmt 预编译语句对象
     */
    public static void closeConnection(Connection conn, PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("关闭PreparedStatement失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        closeConnection(conn);
    }
    
    /**
     * 关闭数据库连接（Connection + PreparedStatement + ResultSet）
     * @param conn 数据库连接对象
     * @param pstmt 预编译语句对象
     * @param rs 结果集对象
     */
    public static void closeConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("关闭ResultSet失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        closeConnection(conn, pstmt);
    }
    
    /**
     * 关闭数据库连接（Connection + CallableStatement）
     * @param conn 数据库连接对象
     * @param cstmt 可调用语句对象
     */
    public static void closeConnection(Connection conn, CallableStatement cstmt) {
        if (cstmt != null) {
            try {
                cstmt.close();
            } catch (SQLException e) {
                System.err.println("关闭CallableStatement失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        closeConnection(conn);
    }
    
    /**
     * 测试数据库连接
     * @return true-连接成功, false-连接失败
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * 执行查询并返回结果数量
     * @param sql SQL查询语句
     * @return 查询结果数量
     */
    public static int executeCountQuery(String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("执行计数查询失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(conn, pstmt, rs);
        }
        return 0;
    }
}
