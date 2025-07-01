package com.dao;

import com.entity.Book;
import java.util.List;

/**
 * 图书数据访问对象接口
 * 定义图书相关的数据库操作方法
 * 
 * @author Titan
 * @version 1.0
 */
public interface BookDao {
    
    /**
     * 获取所有图书列表
     * @return 图书列表
     */
    List<Book> getAllBooks();
    
    /**
     * 分页获取图书列表
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 图书列表
     */
    List<Book> getBooksByPage(int page, int pageSize);
    
    /**
     * 根据图书ID获取图书信息
     * @param bid 图书ID
     * @return Book对象，如果不存在返回null
     */
    Book getBookById(int bid);
    
    /**
     * 添加新图书
     * @param book 图书对象
     * @return true-添加成功, false-添加失败
     */
    boolean addBook(Book book);
    
    /**
     * 更新图书信息
     * @param book 图书对象
     * @return true-更新成功, false-更新失败
     */
    boolean updateBook(Book book);
    
    /**
     * 删除图书
     * @param bid 图书ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteBook(int bid);
    
    /**
     * 搜索图书
     * @param keyword 关键词（书名）
     * @param type 图书类型
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 符合条件的图书列表
     */
    List<Book> searchBooks(String keyword, String type, float minPrice, float maxPrice);
    
    /**
     * 根据图书名称搜索
     * @param bookName 图书名称（支持模糊查询）
     * @return 图书列表
     */
    List<Book> searchBooksByName(String bookName);
    
    /**
     * 根据图书类型获取图书
     * @param type 图书类型
     * @return 图书列表
     */
    List<Book> getBooksByType(String type);
    
    /**
     * 获取所有图书类型
     * @return 图书类型列表
     */
    List<String> getBookTypes();
    
    /**
     * 获取图书总数
     * @return 图书总数
     */
    int getTotalBooksCount();
    
    /**
     * 获取有库存的图书列表
     * @return 有库存的图书列表
     */
    List<Book> getBooksInStock();
    
    /**
     * 获取热门图书（按销量排序）
     * @param limit 返回数量限制
     * @return 热门图书列表
     */
    List<Book> getTopSellingBooks(int limit);
    
    /**
     * 获取最新添加的图书
     * @param limit 返回数量限制
     * @return 最新图书列表
     */
    List<Book> getLatestBooks(int limit);
    
    /**
     * 根据价格区间获取图书
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 图书列表
     */
    List<Book> getBooksByPriceRange(float minPrice, float maxPrice);
    
    /**
     * 更新图书库存
     * @param bid 图书ID
     * @param stock 新库存数量
     * @return true-更新成功, false-更新失败
     */
    boolean updateBookStock(int bid, int stock);
    
    /**
     * 更新图书销量
     * @param bid 图书ID
     * @param saleCount 销量增加数量
     * @return true-更新成功, false-更新失败
     */
    boolean updateBookSales(int bid, int saleCount);
    
    /**
     * 检查图书名称是否已存在
     * @param bookName 图书名称
     * @return true-已存在, false-不存在
     */
    boolean isBookNameExists(String bookName);
    
    /**
     * 获取库存不足的图书（库存小于指定数量）
     * @param threshold 库存阈值
     * @return 库存不足的图书列表
     */
    List<Book> getLowStockBooks(int threshold);
}
