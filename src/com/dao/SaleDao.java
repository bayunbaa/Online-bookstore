package com.dao;

import com.entity.Sale;
import java.util.List;
import java.util.Map;

/**
 * 销售数据访问对象接口
 * 定义销售相关的数据库操作方法
 * 
 * @author Titan
 * @version 1.0
 */
public interface SaleDao {
    
    /**
     * 购买图书（调用存储过程）
     * @param userId 用户ID
     * @param bookId 图书ID
     * @param quantity 购买数量
     * @return true-购买成功, false-购买失败
     */
    boolean purchaseBook(int userId, int bookId, int quantity);
    
    /**
     * 添加销售记录
     * @param sale 销售记录对象
     * @return true-添加成功, false-添加失败
     */
    boolean addSaleRecord(Sale sale);
    
    /**
     * 根据销售ID获取销售记录
     * @param sid 销售ID
     * @return Sale对象，如果不存在返回null
     */
    Sale getSaleById(int sid);
    
    /**
     * 获取所有销售记录
     * @return 销售记录列表
     */
    List<Sale> getAllSales();
    
    /**
     * 分页获取销售记录
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 销售记录列表
     */
    List<Sale> getSalesByPage(int page, int pageSize);
    
    /**
     * 根据用户ID获取销售记录
     * @param userId 用户ID
     * @return 销售记录列表
     */
    List<Sale> getSalesByUserId(int userId);
    
    /**
     * 根据图书ID获取销售记录
     * @param bookId 图书ID
     * @return 销售记录列表
     */
    List<Sale> getSalesByBookId(int bookId);
    
    /**
     * 根据条件搜索销售记录
     * @param userId 用户ID（可为null）
     * @param bookId 图书ID（可为null）
     * @param startDate 开始日期（可为null）
     * @param endDate 结束日期（可为null）
     * @return 销售记录列表
     */
    List<Sale> searchSales(Integer userId, Integer bookId, String startDate, String endDate);
    
    /**
     * 分页搜索销售记录
     * @param page 页码
     * @param pageSize 每页大小
     * @param userId 用户ID（可为null）
     * @param bookId 图书ID（可为null）
     * @param startDate 开始日期（可为null）
     * @param endDate 结束日期（可为null）
     * @return 销售记录列表
     */
    List<Sale> searchSalesByPage(int page, int pageSize, Integer userId, Integer bookId, 
                                String startDate, String endDate);
    
    /**
     * 获取销售记录总数
     * @return 销售记录总数
     */
    int getTotalSalesCount();
    
    /**
     * 根据条件获取销售记录总数
     * @param userId 用户ID（可为null）
     * @param bookId 图书ID（可为null）
     * @param startDate 开始日期（可为null）
     * @param endDate 结束日期（可为null）
     * @return 销售记录总数
     */
    int getTotalSalesCount(Integer userId, Integer bookId, String startDate, String endDate);
    
    /**
     * 获取用户购买历史
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页大小
     * @return 购买历史列表
     */
    List<Sale> getUserPurchaseHistory(int userId, int page, int pageSize);
    
    /**
     * 获取销售统计信息
     * @return 统计信息Map
     */
    Map<String, Object> getSalesStatistics();
    
    /**
     * 根据条件获取销售统计信息
     * @param userId 用户ID（可为null）
     * @param bookId 图书ID（可为null）
     * @param startDate 开始日期（可为null）
     * @param endDate 结束日期（可为null）
     * @return 统计信息Map
     */
    Map<String, Object> getSalesStatistics(Integer userId, Integer bookId, String startDate, String endDate);
    
    /**
     * 获取每日销售统计
     * @param days 统计天数
     * @return 每日销售统计列表
     */
    List<Map<String, Object>> getDailySalesStatistics(int days);
    
    /**
     * 获取图书销售排行榜
     * @param limit 返回数量限制
     * @return 图书销售排行榜
     */
    List<Map<String, Object>> getBookSalesRanking(int limit);
    
    /**
     * 获取用户购买排行榜
     * @param limit 返回数量限制
     * @return 用户购买排行榜
     */
    List<Map<String, Object>> getUserPurchaseRanking(int limit);
    
    /**
     * 删除销售记录
     * @param sid 销售ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteSale(int sid);
}
