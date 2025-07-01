/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : bookshop

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 10/12/2020 23:40:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user (移到最前面，因为其他表需要引用)
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `Uid` int NOT NULL AUTO_INCREMENT,
  `Uname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Upassword` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Uage` int NULL DEFAULT NULL,
  `Uphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Ud` int NULL DEFAULT NULL,
  PRIMARY KEY (`Uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 22, '19919991111', 0);
INSERT INTO `user` VALUES (2, 'user1', 'user1', 23, '12312345678', 0);

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `Bid` int NOT NULL AUTO_INCREMENT,
  `Bname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Bprice` float(10, 2) NOT NULL DEFAULT 0.00,
  `Btype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `Bstock` int NOT NULL DEFAULT 0,
  `Bsale` int NOT NULL DEFAULT 0,
  `Bdes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Bid`, `Bprice`) USING BTREE,
  INDEX `Bprice`(`Bprice`) USING BTREE,
  INDEX `Bid`(`Bid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, 'JavaWeb应用开发教程', 28.00, '计算机', 17, 15, 'JavaWeb');
INSERT INTO `book` VALUES (2, '大学英语1', 25.80, '英语', 51, 10, '大学英语');
INSERT INTO `book` VALUES (3, 'Linux教程', 36.00, '计算机', 29, 12, 'Linux');
INSERT INTO `book` VALUES (4, '大学英语2', 36.00, '英语', 16, 12, '大学英语2');
INSERT INTO `book` VALUES (5, 'Python', 34.00, '计算机', 0, 35, 'Python教程');
INSERT INTO `book` VALUES (6, '大学物理', 45.00, '理科', 41, 9, '理工必修');
INSERT INTO `book` VALUES (7, '数据结构', 32.80, '计算机', 49, 1, '数据结构');
INSERT INTO `book` VALUES (8, '高等数学1', 23.00, '数学', 34, 0, '高等数学');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `Mid` int NOT NULL AUTO_INCREMENT,
  `Uid` int NOT NULL,
  `Bid` int NOT NULL,
  `Mmessage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`Mid`, `Uid`, `Bid`) USING BTREE,
  INDEX `Uid`(`Uid`) USING BTREE,
  INDEX `Bid`(`Bid`) USING BTREE,
  CONSTRAINT `message_ibfk_1` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `message_ibfk_2` FOREIGN KEY (`Bid`) REFERENCES `book` (`Bid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, 1, 1, 'javaWeb很不错');
INSERT INTO `message` VALUES (2, 1, 3, 'Linux很不错');
INSERT INTO `message` VALUES (3, 1, 3, 'Linux教程');
INSERT INTO `message` VALUES (4, 1, 3, 'Linux');
INSERT INTO `message` VALUES (5, 1, 1, 'JavaWeb');
INSERT INTO `message` VALUES (6, 1, 5, 'Python很不错');
INSERT INTO `message` VALUES (7, 1, 1, '112312');
INSERT INTO `message` VALUES (8, 1, 6, '大学物理很不错');
INSERT INTO `message` VALUES (9, 1, 3, 'Linux very good');
INSERT INTO `message` VALUES (10, 2, 5, '这是Python');
INSERT INTO `message` VALUES (11, 2, 5, '');
INSERT INTO `message` VALUES (12, 1, 6, '大学物理');
INSERT INTO `message` VALUES (13, 2, 1, 'Java+Web');
INSERT INTO `message` VALUES (14, 1, 3, 'Linux admin');
INSERT INTO `message` VALUES (15, 1, 8, 'math');
INSERT INTO `message` VALUES (16, 2, 8, '高等数学');

-- ----------------------------
-- Table structure for sale
-- ----------------------------
DROP TABLE IF EXISTS `sale`;
CREATE TABLE `sale`  (
  `Sid` int NOT NULL AUTO_INCREMENT,
  `Uid` int NOT NULL,
  `Bid` int NOT NULL,
  `Scount` int NULL DEFAULT NULL,
  PRIMARY KEY (`Sid`, `Uid`, `Bid`) USING BTREE,
  INDEX `Bid`(`Bid`) USING BTREE,
  INDEX `Uid`(`Uid`) USING BTREE,
  CONSTRAINT `Bid` FOREIGN KEY (`Bid`) REFERENCES `book` (`Bid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `Uid` FOREIGN KEY (`Uid`) REFERENCES `user` (`Uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sale
-- ----------------------------
INSERT INTO `sale` VALUES (1, 1, 1, 1);
INSERT INTO `sale` VALUES (2, 1, 1, 1);
INSERT INTO `sale` VALUES (3, 1, 1, 1);
INSERT INTO `sale` VALUES (4, 1, 1, 1);
INSERT INTO `sale` VALUES (5, 1, 1, 1);
INSERT INTO `sale` VALUES (6, 1, 1, 1);
INSERT INTO `sale` VALUES (7, 1, 3, 1);
INSERT INTO `sale` VALUES (8, 1, 2, 1);
INSERT INTO `sale` VALUES (9, 1, 2, 1);
INSERT INTO `sale` VALUES (10, 1, 2, 1);
INSERT INTO `sale` VALUES (11, 1, 1, 1);
INSERT INTO `sale` VALUES (12, 1, 2, 1);
INSERT INTO `sale` VALUES (13, 1, 3, 1);
INSERT INTO `sale` VALUES (14, 1, 4, 1);
INSERT INTO `sale` VALUES (15, 1, 1, 1);
INSERT INTO `sale` VALUES (16, 1, 2, 1);
INSERT INTO `sale` VALUES (17, 1, 3, 1);
INSERT INTO `sale` VALUES (18, 1, 3, 1);
INSERT INTO `sale` VALUES (19, 1, 3, 1);
INSERT INTO `sale` VALUES (20, 1, 3, 1);
INSERT INTO `sale` VALUES (21, 1, 3, 1);
INSERT INTO `sale` VALUES (22, 1, 3, 1);
INSERT INTO `sale` VALUES (23, 1, 4, 1);
INSERT INTO `sale` VALUES (24, 1, 1, 1);
INSERT INTO `sale` VALUES (25, 1, 1, 1);
INSERT INTO `sale` VALUES (26, 1, 1, 1);
INSERT INTO `sale` VALUES (27, 1, 1, 1);
INSERT INTO `sale` VALUES (28, 1, 1, 1);
INSERT INTO `sale` VALUES (29, 1, 1, 1);
INSERT INTO `sale` VALUES (30, 1, 2, 1);
INSERT INTO `sale` VALUES (31, 1, 2, 1);
INSERT INTO `sale` VALUES (32, 1, 1, 1);
INSERT INTO `sale` VALUES (33, 1, 2, 1);
INSERT INTO `sale` VALUES (34, 1, 4, 1);
INSERT INTO `sale` VALUES (35, 1, 2, 1);
INSERT INTO `sale` VALUES (36, 1, 4, 1);
INSERT INTO `sale` VALUES (37, 1, 4, 1);
INSERT INTO `sale` VALUES (38, 1, 5, 1);
INSERT INTO `sale` VALUES (39, 1, 1, 1);
INSERT INTO `sale` VALUES (40, 1, 2, 1);
INSERT INTO `sale` VALUES (41, 1, 1, 1);
INSERT INTO `sale` VALUES (42, 1, 1, 1);
INSERT INTO `sale` VALUES (43, 1, 1, 1);
INSERT INTO `sale` VALUES (44, 1, 5, 1);
INSERT INTO `sale` VALUES (45, 1, 5, 1);
INSERT INTO `sale` VALUES (46, 1, 4, 1);
INSERT INTO `sale` VALUES (47, 1, 4, 1);
INSERT INTO `sale` VALUES (48, 1, 4, 1);
INSERT INTO `sale` VALUES (49, 1, 4, 1);
INSERT INTO `sale` VALUES (50, 1, 6, 1);
INSERT INTO `sale` VALUES (51, 1, 6, 1);
INSERT INTO `sale` VALUES (52, 1, 1, 1);
INSERT INTO `sale` VALUES (53, 1, 5, 1);
INSERT INTO `sale` VALUES (54, 1, 6, 1);
INSERT INTO `sale` VALUES (55, 1, 3, 1);
INSERT INTO `sale` VALUES (56, 1, 3, 1);
INSERT INTO `sale` VALUES (57, 1, 6, 1);
INSERT INTO `sale` VALUES (58, 1, 4, 1);
INSERT INTO `sale` VALUES (59, 1, 5, 1);
INSERT INTO `sale` VALUES (60, 1, 6, 1);
INSERT INTO `sale` VALUES (61, 1, 1, 1);
INSERT INTO `sale` VALUES (62, 2, 3, 1);
INSERT INTO `sale` VALUES (63, 2, 5, 21);
INSERT INTO `sale` VALUES (64, 1, 2, 1);
INSERT INTO `sale` VALUES (65, 1, 2, 1);
INSERT INTO `sale` VALUES (66, 1, 3, 1);
INSERT INTO `sale` VALUES (67, 1, 7, 1);
INSERT INTO `sale` VALUES (68, 2, 5, 5);



-- ----------------------------
-- Procedure structure for sale
-- ----------------------------
DROP PROCEDURE IF EXISTS `sale`;
delimiter ;;
CREATE PROCEDURE `sale`(userid int,bookid int,salecount int)
BEGIN
	update book set Bstock=Bstock-salecount,Bsale=Bsale+salecount where Bid=bookid;
	insert into sale(Uid,Bid,Scount) values (userid,bookid,salecount);
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
