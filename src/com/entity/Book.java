package com.entity;

/**
 * 图书实体类
 * 对应数据库book表
 * 
 * @author Titan
 * @version 1.0
 */
public class Book {
    private int bid;           // 图书ID
    private String bname;      // 书名
    private float bprice;      // 价格
    private String btype;      // 类型
    private int bstock;        // 库存数量
    private int bsale;         // 销售数量
    private String bdes;       // 图书描述
    
    // 默认构造方法
    public Book() {
    }
    
    // 带参构造方法（不包含ID，用于新增）
    public Book(String bname, float bprice, String btype, int bstock, String bdes) {
        this.bname = bname;
        this.bprice = bprice;
        this.btype = btype;
        this.bstock = bstock;
        this.bsale = 0; // 新书销量为0
        this.bdes = bdes;
    }
    
    // 完整构造方法
    public Book(int bid, String bname, float bprice, String btype, int bstock, int bsale, String bdes) {
        this.bid = bid;
        this.bname = bname;
        this.bprice = bprice;
        this.btype = btype;
        this.bstock = bstock;
        this.bsale = bsale;
        this.bdes = bdes;
    }
    
    // Getter和Setter方法
    public int getBid() {
        return bid;
    }
    
    public void setBid(int bid) {
        this.bid = bid;
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
    
    public String getBtype() {
        return btype;
    }
    
    public void setBtype(String btype) {
        this.btype = btype;
    }
    
    public int getBstock() {
        return bstock;
    }
    
    public void setBstock(int bstock) {
        this.bstock = bstock;
    }
    
    public int getBsale() {
        return bsale;
    }
    
    public void setBsale(int bsale) {
        this.bsale = bsale;
    }
    
    public String getBdes() {
        return bdes;
    }
    
    public void setBdes(String bdes) {
        this.bdes = bdes;
    }
    
    // 业务方法
    
    /**
     * 判断图书是否有库存
     * @return true-有库存, false-无库存
     */
    public boolean hasStock() {
        return this.bstock > 0;
    }
    
    /**
     * 判断库存是否足够
     * @param quantity 需要的数量
     * @return true-库存足够, false-库存不足
     */
    public boolean hasEnoughStock(int quantity) {
        return this.bstock >= quantity;
    }
    
    /**
     * 计算总销售额
     * @return 总销售额
     */
    public float getTotalSalesAmount() {
        return this.bprice * this.bsale;
    }
    
    // toString方法
    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bname='" + bname + '\'' +
                ", bprice=" + bprice +
                ", btype='" + btype + '\'' +
                ", bstock=" + bstock +
                ", bsale=" + bsale +
                ", bdes='" + bdes + '\'' +
                '}';
    }
    
    // equals方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return bid == book.bid;
    }
    
    // hashCode方法
    @Override
    public int hashCode() {
        return Integer.hashCode(bid);
    }
}
