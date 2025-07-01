package com.entity;

/**
 * 用户实体类
 * 对应数据库user表
 * 
 * @author Titan
 * @version 1.0
 */
public class User {
    private int uid;           // 用户ID
    private String uname;      // 用户名
    private String upassword;  // 密码
    private int uage;          // 年龄
    private String uphone;     // 电话号码
    private int ud;            // 用户权限标识 (0-管理员, 1-普通用户)
    
    // 默认构造方法
    public User() {
    }
    
    // 带参构造方法
    public User(String uname, String upassword, int uage, String uphone, int ud) {
        this.uname = uname;
        this.upassword = upassword;
        this.uage = uage;
        this.uphone = uphone;
        this.ud = ud;
    }
    
    // 完整构造方法
    public User(int uid, String uname, String upassword, int uage, String uphone, int ud) {
        this.uid = uid;
        this.uname = uname;
        this.upassword = upassword;
        this.uage = uage;
        this.uphone = uphone;
        this.ud = ud;
    }
    
    // Getter和Setter方法
    public int getUid() {
        return uid;
    }
    
    public void setUid(int uid) {
        this.uid = uid;
    }
    
    public String getUname() {
        return uname;
    }
    
    public void setUname(String uname) {
        this.uname = uname;
    }
    
    public String getUpassword() {
        return upassword;
    }
    
    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }
    
    public int getUage() {
        return uage;
    }
    
    public void setUage(int uage) {
        this.uage = uage;
    }
    
    public String getUphone() {
        return uphone;
    }
    
    public void setUphone(String uphone) {
        this.uphone = uphone;
    }
    
    public int getUd() {
        return ud;
    }
    
    public void setUd(int ud) {
        this.ud = ud;
    }
    
    // 判断是否为管理员
    public boolean isAdmin() {
        return this.ud == 0;
    }
    
    // toString方法
    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", uage=" + uage +
                ", uphone='" + uphone + '\'' +
                ", ud=" + ud +
                '}';
    }
    
    // equals方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return uid == user.uid;
    }
    
    // hashCode方法
    @Override
    public int hashCode() {
        return Integer.hashCode(uid);
    }
}
