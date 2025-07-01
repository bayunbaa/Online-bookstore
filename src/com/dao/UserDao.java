package com.dao;

import com.entity.User;
import java.util.List;

/**
 * 用户数据访问对象接口
 * 定义用户相关的数据库操作方法
 * 
 * @author Titan
 * @version 1.0
 */
public interface UserDao {
    
    /**
     * 验证用户登录
     * @param username 用户名
     * @param password 密码
     * @return true-验证成功, false-验证失败
     */
    boolean validateLogin(String username, String password);
    
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return User对象，如果不存在返回null
     */
    User getUserByName(String username);
    
    /**
     * 根据用户ID获取用户信息
     * @param uid 用户ID
     * @return User对象，如果不存在返回null
     */
    User getUserById(int uid);
    
    /**
     * 创建新用户
     * @param user 用户对象
     * @return true-创建成功, false-创建失败
     */
    boolean userCreate(User user);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return true-更新成功, false-更新失败
     */
    boolean updateUser(User user);
    
    /**
     * 删除用户
     * @param uid 用户ID
     * @return true-删除成功, false-删除失败
     */
    boolean deleteUser(int uid);
    
    /**
     * 获取所有用户列表
     * @return 用户列表
     */
    List<User> getAllUsers();
    
    /**
     * 分页获取用户列表
     * @param page 页码（从1开始）
     * @param pageSize 每页大小
     * @return 用户列表
     */
    List<User> getUsersByPage(int page, int pageSize);
    
    /**
     * 获取用户总数
     * @return 用户总数
     */
    int getTotalUsersCount();
    
    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return true-已存在, false-不存在
     */
    boolean isUsernameExists(String username);
    
    /**
     * 检查电话号码是否已存在
     * @param phone 电话号码
     * @return true-已存在, false-不存在
     */
    boolean isPhoneExists(String phone);
    
    /**
     * 根据条件搜索用户
     * @param keyword 关键词（用户名或电话）
     * @return 用户列表
     */
    List<User> searchUsers(String keyword);
    
    /**
     * 修改用户密码
     * @param uid 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return true-修改成功, false-修改失败
     */
    boolean changePassword(int uid, String oldPassword, String newPassword);
    
    /**
     * 获取管理员用户列表
     * @return 管理员用户列表
     */
    List<User> getAdminUsers();
    
    /**
     * 获取普通用户列表
     * @return 普通用户列表
     */
    List<User> getRegularUsers();
}
