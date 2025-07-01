package com.dao;

import com.entity.Message;
import java.util.List;

/**
 * 评论消息数据访问对象接口
 * 定义评论相关的数据库操作方法
 * 
 * @author Titan
 * @version 1.0
 */
public interface MessageDao {
    
    /**
     * 添加评论
     * @param message 评论对象
     * @return true-添加成功, false-添加失败
     */
    boolean addMessage(Message message);
    
    /**
     * 根据评论ID获取评论
     * @param mid 评论ID
     * @return Message对象，如果不存在返回null
     */
    Message getMessageById(int mid);
    
    /**
     * 根据图书ID获取评论列表
     * @param bookId 图书ID
     * @return 评论列表
     */
    List<Message> getMessagesByBookId(int bookId);
    
    /**
     * 根据用户ID获取评论列表
     * @param userId 用户ID
     * @return 评论列表
     */
    List<Message> getMessagesByUserId(int userId);
    
    /**
     * 分页获取图书评论
     * @param bookId 图书ID
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 评论列表
     */
    List<Message> getMessagesByBookId(int bookId, int page, int pageSize);
    
    /**
     * 获取所有评论
     * @return 评论列表
     */
    List<Message> getAllMessages();
    
    /**
     * 分页获取所有评论
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 评论列表
     */
    List<Message> getMessagesByPage(int page, int pageSize);
    
    /**
     * 更新评论内容
     * @param message 评论对象
     * @return true-更新成功, false-更新失败
     */
    boolean updateMessage(Message message);
    
    /**
     * 删除评论
     * @param mid 评论ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteMessage(int mid);
    
    /**
     * 根据用户ID和图书ID删除评论
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteMessagesByUserAndBook(int userId, int bookId);
    
    /**
     * 获取评论总数
     * @return 评论总数
     */
    int getTotalMessagesCount();
    
    /**
     * 获取指定图书的评论总数
     * @param bookId 图书ID
     * @return 评论总数
     */
    int getMessagesCountByBookId(int bookId);
    
    /**
     * 获取指定用户的评论总数
     * @param userId 用户ID
     * @return 评论总数
     */
    int getMessagesCountByUserId(int userId);
    
    /**
     * 搜索评论内容
     * @param keyword 关键词
     * @return 评论列表
     */
    List<Message> searchMessages(String keyword);
    
    /**
     * 获取最新评论
     * @param limit 返回数量限制
     * @return 最新评论列表
     */
    List<Message> getLatestMessages(int limit);
    
    /**
     * 获取用户对指定图书的评论
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 评论列表
     */
    List<Message> getUserMessagesForBook(int userId, int bookId);
    
    /**
     * 检查用户是否已对图书评论
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return true-已评论, false-未评论
     */
    boolean hasUserCommentedOnBook(int userId, int bookId);
    
    /**
     * 批量删除评论
     * @param messageIds 评论ID列表
     * @return true-删除成功, false-删除失败
     */
    boolean deleteMessages(List<Integer> messageIds);
    
    /**
     * 根据图书ID删除所有相关评论
     * @param bookId 图书ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteMessagesByBookId(int bookId);
    
    /**
     * 根据用户ID删除所有相关评论
     * @param userId 用户ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteMessagesByUserId(int userId);
}
