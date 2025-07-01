package com.util;

import java.sql.*;

/**
 * Database setup utility class
 * Used to create missing database tables and structures
 *
 * @author Titan
 * @version 1.0
 */
public class DatabaseSetup {

    public static void main(String[] args) {
        createSaleTable();
    }

    /**
     * Create sale table
     */
    public static void createSaleTable() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Get database connection
            conn = BasicJDBC.getConnection();
            stmt = conn.createStatement();

            System.out.println("Starting to create sale table...");

            // Drop existing sale table
            String dropSql = "DROP TABLE IF EXISTS `sale`";
            stmt.executeUpdate(dropSql);
            System.out.println("Dropped old sale table if exists");

            // Create sale table
            String createSql = "CREATE TABLE `sale` (" +
                    "`Sid` int NOT NULL AUTO_INCREMENT," +
                    "`Uid` int NOT NULL," +
                    "`Bid` int NOT NULL," +
                    "`Scount` int NULL DEFAULT NULL," +
                    "`Stime` timestamp NULL DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (`Sid`, `Uid`, `Bid`) USING BTREE," +
                    "INDEX `Bid`(`Bid`) USING BTREE," +
                    "INDEX `Uid`(`Uid`) USING BTREE," +
                    "CONSTRAINT `sale_bid_fk` FOREIGN KEY (`Bid`) REFERENCES `book` (`Bid`) ON DELETE RESTRICT ON UPDATE RESTRICT," +
                    "CONSTRAINT `sale_uid_fk` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT" +
                    ") ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";

            stmt.executeUpdate(createSql);
            System.out.println("Sale table created successfully!");

            // Check existing users and books first
            ResultSet userRs = stmt.executeQuery("SELECT Uid FROM user LIMIT 1");
            if (userRs.next()) {
                int uid1 = userRs.getInt("Uid");
                userRs.close();

                ResultSet bookRs = stmt.executeQuery("SELECT Bid FROM book LIMIT 1");
                if (bookRs.next()) {
                    int bid1 = bookRs.getInt("Bid");
                    bookRs.close();

                    // Insert test data with existing IDs
                    String insertSql1 = "INSERT INTO `sale` (`Uid`, `Bid`, `Scount`) VALUES (" + uid1 + ", " + bid1 + ", 2)";
                    stmt.executeUpdate(insertSql1);
                    System.out.println("Test data inserted successfully!");
                } else {
                    bookRs.close();
                    System.out.println("No existing books found, skipping test data insertion");
                }
            } else {
                userRs.close();
                System.out.println("No existing users found, skipping test data insertion");
            }

            // Create stored procedure
            createSaleProcedure(conn);

            // Verify table creation
            String verifySql = "SELECT COUNT(*) FROM sale";
            ResultSet rs = stmt.executeQuery(verifySql);
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("Sale table verified successfully, current record count: " + count);
            }
            rs.close();

            System.out.println("Database setup completed!");
            
        } catch (SQLException e) {
            System.err.println("Failed to create sale table: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            BasicJDBC.closeConnection(conn);
        }
    }

    /**
     * Create sale stored procedure
     */
    private static void createSaleProcedure(Connection conn) {
        CallableStatement cstmt = null;
        try {
            // Drop existing stored procedure
            String dropProcSql = "DROP PROCEDURE IF EXISTS `sale`";
            cstmt = conn.prepareCall(dropProcSql);
            cstmt.execute();
            cstmt.close();

            // Create stored procedure
            String createProcSql = "CREATE PROCEDURE `sale`(userid int, bookid int, salecount int) " +
                    "BEGIN " +
                    "UPDATE book SET Bstock=Bstock-salecount, Bsale=Bsale+salecount WHERE Bid=bookid; " +
                    "INSERT INTO sale(Uid, Bid, Scount) VALUES (userid, bookid, salecount); " +
                    "END";

            cstmt = conn.prepareCall(createProcSql);
            cstmt.execute();
            System.out.println("Sale stored procedure created successfully!");

        } catch (SQLException e) {
            System.err.println("Failed to create stored procedure: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cstmt != null) {
                try {
                    cstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
