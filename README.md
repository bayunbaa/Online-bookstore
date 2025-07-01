# 📚 网上书店管理系统

![Java](https://img.shields.io/badge/Java-8-orange)
![Maven](https://img.shields.io/badge/Maven-3.x-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue)
![Servlet](https://img.shields.io/badge/Servlet-4.0-green)
![JSP](https://img.shields.io/badge/JSP-2.3-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

一个基于Java Web技术栈开发的完整网上书店管理系统，采用经典MVC架构，提供图书管理、用户管理、销售管理和评论系统等完整电商功能。

## 🌟 项目特色

- 🏗️ **经典MVC架构** - 清晰的三层架构设计，易于维护和扩展
- 🔐 **完整权限系统** - 支持管理员和普通用户的权限分离
- 📦 **库存管理** - 实时库存更新和销量统计
- 💬 **评论系统** - 用户可对图书发表和查看评论
- 🔍 **高级搜索** - 支持多条件图书搜索和分页展示
- 🛡️ **安全特性** - SQL注入防护、会话管理、过滤器保护
- 📊 **数据统计** - 销售数据统计和分析功能

## 🛠️ 技术栈

### 后端技术
- **Java 8** - 核心开发语言
- **Servlet 4.0** - Web请求处理
- **JSP 2.3** - 视图层技术
- **MySQL 8.0+** - 数据库存储
- **Maven 3.x** - 项目构建管理
- **JDBC** - 数据库连接和操作

### 前端技术
- **HTML5 + CSS3** - 页面结构和样式
- **JavaScript** - 前端交互逻辑
- **Bootstrap** - 响应式UI框架
- **jQuery** - JavaScript库

### 开发工具
- **IntelliJ IDEA** - 推荐IDE
- **Tomcat 7+** - Web服务器
- **Git** - 版本控制

## 📁 项目结构

```
网上书店/
├── src/com/                    # Java源代码
│   ├── entity/                 # 实体类
│   │   ├── User.java          # 用户实体
│   │   ├── Book.java          # 图书实体
│   │   ├── Sale.java          # 销售实体
│   │   └── Message.java       # 评论实体
│   ├── dao/                   # 数据访问层
│   │   ├── UserDao.java       # 用户数据访问接口
│   │   ├── UserDaoImpl.java   # 用户数据访问实现
│   │   ├── BookDao.java       # 图书数据访问接口
│   │   ├── BookDaoImpl.java   # 图书数据访问实现
│   │   ├── SaleDao.java       # 销售数据访问接口
│   │   ├── SaleDaoImpl.java   # 销售数据访问实现
│   │   ├── MessageDao.java    # 评论数据访问接口
│   │   └── MessageDaoImpl.java # 评论数据访问实现
│   ├── servlet/               # 控制器层
│   │   ├── UserServlet.java   # 用户管理控制器
│   │   ├── BookServlet.java   # 图书管理控制器
│   │   ├── SaleServlet.java   # 销售管理控制器
│   │   └── MessageServlet.java # 评论管理控制器
│   └── util/                  # 工具类
│       ├── BasicJDBC.java     # 数据库连接工具
│       ├── EncodingFilter.java # 编码过滤器
│       ├── LoginFilter.java   # 登录验证过滤器
│       ├── AdminFilter.java   # 管理员权限过滤器
│       └── DatabaseSetup.java # 数据库初始化工具
├── webapp/                    # Web资源
│   ├── WEB-INF/
│   │   ├── web.xml           # Web应用配置
│   │   └── database.properties # 数据库配置
│   ├── css/                  # 样式文件
│   ├── js/                   # JavaScript文件
│   ├── images/               # 图片资源
│   ├── login.jsp            # 登录页面
│   ├── listbook.jsp         # 图书列表页面
│   ├── salebook.jsp         # 图书详情页面
│   ├── manage.jsp           # 管理员主页
│   ├── bookmanage.jsp       # 图书管理页面
│   ├── usermanage.jsp       # 用户管理页面
│   ├── salemanage.jsp       # 销售管理页面
│   ├── addbook.jsp          # 添加图书页面
│   ├── usercreate.jsp       # 用户注册页面
│   └── failure.jsp          # 错误页面
├── bookshop.sql             # 数据库初始化脚本
├── pom.xml                  # Maven配置文件
└── README.md               # 项目说明文档
```

## 🗄️ 数据库设计

### 核心数据表

#### 用户表 (user)
| 字段 | 类型 | 说明 |
|------|------|------|
| Uid | INT | 用户ID (主键) |
| Uname | VARCHAR(255) | 用户名 |
| Upassword | VARCHAR(255) | 密码 |
| Uage | INT | 年龄 |
| Uphone | VARCHAR(255) | 电话 |
| Ud | INT | 权限标识 (0-管理员, 1-普通用户) |

#### 图书表 (book)
| 字段 | 类型 | 说明 |
|------|------|------|
| Bid | INT | 图书ID (主键) |
| Bname | VARCHAR(255) | 书名 |
| Bprice | FLOAT | 价格 |
| Btype | VARCHAR(255) | 图书类型 |
| Bstock | INT | 库存数量 |
| Bsale | INT | 销售数量 |
| Bdes | VARCHAR(255) | 图书描述 |

#### 销售表 (sale)
| 字段 | 类型 | 说明 |
|------|------|------|
| Sid | INT | 销售ID (主键) |
| Uid | INT | 用户ID (外键) |
| Bid | INT | 图书ID (外键) |
| Scount | INT | 购买数量 |

#### 评论表 (message)
| 字段 | 类型 | 说明 |
|------|------|------|
| Mid | INT | 评论ID (主键) |
| Uid | INT | 用户ID (外键) |
| Bid | INT | 图书ID (外键) |
| Mmessage | VARCHAR(255) | 评论内容 |

## 🚀 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.x
- MySQL 8.0+
- Tomcat 7.x+
- IntelliJ IDEA (推荐)

### 安装步骤

1. **克隆项目**
   ```bash
   git clone [项目地址]
   cd 网上书店
   ```

2. **数据库配置**
   ```sql
   # 创建数据库
   CREATE DATABASE bookshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   # 导入数据
   mysql -u root -p bookshop < bookshop.sql
   ```

3. **修改数据库配置**
   编辑 `webapp/WEB-INF/database.properties`：
   ```properties
   db.url=jdbc:mysql://localhost:3306/bookshop?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false
   db.username=你的数据库用户名
   db.password=你的数据库密码
   ```

4. **Maven构建**
   ```bash
   mvn clean compile
   mvn package
   ```

5. **部署运行**
   ```bash
   # 使用Maven Tomcat插件
   mvn tomcat7:run
   
   # 或者部署到Tomcat服务器
   cp target/bookshop.war $TOMCAT_HOME/webapps/
   ```

6. **访问系统**
   打开浏览器访问：`http://localhost:8080/bookshop`

### 默认账户

- **管理员账户**: admin / admin
- **普通用户**: user1 / user1

## 📖 功能模块

### 🔐 用户管理
- 用户注册和登录
- 权限控制（管理员/普通用户）
- 用户信息管理
- 会话管理

### 📚 图书管理
- 图书列表展示（分页）
- 图书搜索（按名称、类型、价格）
- 图书详情查看
- 图书CRUD操作（管理员）
- 库存管理

### 🛒 销售管理
- 图书购买功能
- 库存自动更新
- 销售记录管理
- 销售数据统计

### 💬 评论系统
- 用户评论发表
- 评论展示和管理
- 评论与图书关联

## 🎯 使用指南

### 普通用户操作流程
1. 注册账户或使用现有账户登录
2. 浏览图书列表，使用搜索功能查找图书
3. 查看图书详情和用户评论
4. 购买图书（自动扣减库存）
5. 对购买的图书发表评论

### 管理员操作流程
1. 使用管理员账户登录
2. 进入管理后台
3. 管理图书：添加、修改、删除图书信息
4. 管理用户：查看和管理用户信息
5. 查看销售数据和统计信息

## 🔧 开发指南

### 在IntelliJ IDEA中开发

1. **导入项目**
   - File → Open → 选择项目根目录
   - 选择"Import as Maven project"

2. **配置Tomcat**
   - Run → Edit Configurations
   - 添加Tomcat Server → Local
   - 配置Deployment：添加bookshop.war

3. **数据库连接**
   - 配置Database工具窗口
   - 连接到MySQL数据库

### 代码规范
- 遵循Java命名规范
- 使用中文注释说明业务逻辑
- 每个类和方法都要有完整的JavaDoc
- 使用PreparedStatement防止SQL注入

## 🤝 贡献指南

欢迎提交Issue和Pull Request来改进项目！

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情


---

⭐ 如果这个项目对你有帮助，请给它一个星标！
