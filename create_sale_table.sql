-- 创建sale表的SQL脚本
-- 用于修复网上书店销售管理系统

USE bookshop;

-- 删除已存在的sale表（如果存在）
DROP TABLE IF EXISTS `sale`;

-- 创建sale表
CREATE TABLE `sale`  (
  `Sid` int NOT NULL AUTO_INCREMENT,
  `Uid` int NOT NULL,
  `Bid` int NOT NULL,
  `Scount` int NULL DEFAULT NULL,
  `Stime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Sid`, `Uid`, `Bid`) USING BTREE,
  INDEX `Bid`(`Bid`) USING BTREE,
  INDEX `Uid`(`Uid`) USING BTREE,
  CONSTRAINT `sale_bid_fk` FOREIGN KEY (`Bid`) REFERENCES `book` (`Bid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `sale_uid_fk` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- 插入一些测试数据
INSERT INTO `sale` (`Uid`, `Bid`, `Scount`) VALUES 
(2, 1, 2),
(2, 2, 1),
(3, 1, 3),
(3, 3, 1);

-- 创建sale存储过程（如果不存在）
DROP PROCEDURE IF EXISTS `sale`;
DELIMITER ;;
CREATE PROCEDURE `sale`(userid int, bookid int, salecount int)
BEGIN
    UPDATE book SET Bstock=Bstock-salecount, Bsale=Bsale+salecount WHERE Bid=bookid;
    INSERT INTO sale(Uid, Bid, Scount) VALUES (userid, bookid, salecount);
END;;
DELIMITER ;

SELECT 'Sale table created successfully!' as Result;
