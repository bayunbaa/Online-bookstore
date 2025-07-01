package com.entity;

/**
 * 评论消息实体类
 * 对应数据库message表
 *
 * @author Titan
 * @version 1.0
 */
public class Message {
    private int mid;           // 消息ID
    private int uid;           // 用户ID
    private int bid;           // 图书ID
    private String mmessage;   // 评论内容

// {{TITAN:
// Action: Modified
// Reason: 移除mtime字段，因为数据库message表中没有时间字段
// Timestamp: 2025-06-28 14:45:00 +08:00
// }}
// {{START MODIFICATION}}
    // 注意：数据库message表没有时间字段，如需要可以通过ALTER TABLE添加
// {{END MODIFICATION}}
    
    // 关联信息（用于显示）
    private String uname;      // 用户名
    private String bname;      // 书名
    
    // 默认构造方法
    public Message() {
    }
    
    // 基本构造方法
    public Message(int uid, int bid, String mmessage) {
        this.uid = uid;
        this.bid = bid;
        this.mmessage = mmessage;
    }

// {{TITAN:
// Action: Modified
// Reason: 移除mtime参数，因为数据库表中没有该字段
// Timestamp: 2025-06-28 14:45:00 +08:00
// }}
// {{START MODIFICATION}}
    // 完整构造方法
    public Message(int mid, int uid, int bid, String mmessage) {
        this.mid = mid;
        this.uid = uid;
        this.bid = bid;
        this.mmessage = mmessage;
    }
// {{END MODIFICATION}}
    
    // Getter和Setter方法
    public int getMid() {
        return mid;
    }
    
    public void setMid(int mid) {
        this.mid = mid;
    }
    
    public int getUid() {
        return uid;
    }
    
    public void setUid(int uid) {
        this.uid = uid;
    }
    
    public int getBid() {
        return bid;
    }
    
    public void setBid(int bid) {
        this.bid = bid;
    }
    
    public String getMmessage() {
        return mmessage;
    }
    
    public void setMmessage(String mmessage) {
        this.mmessage = mmessage;
    }
    
// {{TITAN:
// Action: Removed
// Reason: 移除mtime相关的getter和setter方法，因为数据库表中没有该字段
// Timestamp: 2025-06-28 14:45:00 +08:00
// }}
// {{START MODIFICATION}}
    // mtime字段已移除，如需要时间功能，请在数据库中添加Mtime字段
// {{END MODIFICATION}}
    
    public String getUname() {
        return uname;
    }
    
    public void setUname(String uname) {
        this.uname = uname;
    }
    
    public String getBname() {
        return bname;
    }
    
    public void setBname(String bname) {
        this.bname = bname;
    }
    
    // 业务方法
    
    /**
     * 判断评论内容是否为空
     * @return true-为空, false-不为空
     */
    public boolean isEmpty() {
        return this.mmessage == null || this.mmessage.trim().isEmpty();
    }
    
    /**
     * 获取评论内容长度
     * @return 评论内容长度
     */
    public int getMessageLength() {
        return this.mmessage != null ? this.mmessage.length() : 0;
    }
    
    /**
     * 验证评论内容是否有效
     * @return true-有效, false-无效
     */
    public boolean isValidMessage() {
        return !isEmpty() && getMessageLength() <= 255;
    }
    
// {{TITAN:
// Action: Modified
// Reason: 从toString方法中移除mtime字段引用
// Timestamp: 2025-06-28 14:45:00 +08:00
// }}
// {{START MODIFICATION}}
    // toString方法
    @Override
    public String toString() {
        return "Message{" +
                "mid=" + mid +
                ", uid=" + uid +
                ", bid=" + bid +
                ", mmessage='" + mmessage + '\'' +
                ", uname='" + uname + '\'' +
                ", bname='" + bname + '\'' +
                '}';
    }
// {{END MODIFICATION}}
    
    // equals方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return mid == message.mid;
    }
    
    // hashCode方法
    @Override
    public int hashCode() {
        return Integer.hashCode(mid);
    }
}
