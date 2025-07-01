-- 网上书店系统数据库初始化脚本
-- 使用方法: mysql -u root -p < database-setup.sql

-- 创建数据库
CREATE DATABASE IF NOT EXISTS bookshop DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE bookshop;

-- 显示当前数据库
SELECT DATABASE() AS '当前数据库';

-- 提示信息
SELECT '数据库创建完成，请继续导入bookshop.sql文件' AS '提示信息';
SELECT 'mysql -u root -p bookshop < bookshop.sql' AS '导入命令';
