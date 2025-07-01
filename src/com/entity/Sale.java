package com.entity;

import java.sql.Timestamp;

/**
 * 销售记录实体类
 * 对应数据库sale表
 * 
 * @author Titan
 * @version 1.0
 */
public class Sale {
    private int sid;           // 销售ID
    private int uid;           // 用户ID
    private int bid;           // 图书ID
    private int scount;        // 销售数量
    private Timestamp stime;   // 销售时间
    
    // 关联信息（用于显示）
    private String uname;      // 用户名
    private String bname;      // 书名
    private float bprice;      // 图书价格
    private float totalAmount; // 总金额
    
    // 默认构造方法
    public Sale() {
    }
    
    // 基本构造方法
    public Sale(int uid, int bid, int scount) {
        this.uid = uid;
        this.bid = bid;
        this.scount = scount;
    }
    
    // 完整构造方法
    public Sale(int sid, int uid, int bid, int scount, Timestamp stime) {
        this.sid = sid;
        this.uid = uid;
        this.bid = bid;
        this.scount = scount;
        this.stime = stime;
    }
    
    // Getter和Setter方法
    public int getSid() {
        return sid;
    }
    
    public void setSid(int sid) {
        this.sid = sid;
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
    
    public int getScount() {
        return scount;
    }
    
    public void setScount(int scount) {
        this.scount = scount;
    }
    
    public Timestamp getStime() {
        return stime;
    }
    
    public void setStime(Timestamp stime) {
        this.stime = stime;
    }
    
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
    
    public float getBprice() {
        return bprice;
    }
    
    public void setBprice(float bprice) {
        this.bprice = bprice;
    }
    
    public float getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // 业务方法
    
    /**
     * 计算总金额
     * @return 总金额
     */
    public float calculateTotalAmount() {
        this.totalAmount = this.bprice * this.scount;
        return this.totalAmount;
    }
    
    // toString方法
    @Override
    public String toString() {
        return "Sale{" +
                "sid=" + sid +
                ", uid=" + uid +
                ", bid=" + bid +
                ", scount=" + scount +
                ", stime=" + stime +
                ", uname='" + uname + '\'' +
                ", bname='" + bname + '\'' +
                ", bprice=" + bprice +
                ", totalAmount=" + totalAmount +
                '}';
    }
    
    // equals方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sale sale = (Sale) obj;
        return sid == sale.sid;
    }
    
    // hashCode方法
    @Override
    public int hashCode() {
        return Integer.hashCode(sid);
    }
}
