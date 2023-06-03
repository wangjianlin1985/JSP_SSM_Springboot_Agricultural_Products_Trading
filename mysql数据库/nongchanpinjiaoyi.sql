/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : nongchanpinjiaoyi

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2018-07-08 10:22:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL default '',
  `password` varchar(32) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_leaveword`
-- ----------------------------
DROP TABLE IF EXISTS `t_leaveword`;
CREATE TABLE `t_leaveword` (
  `leaveWordId` int(11) NOT NULL auto_increment COMMENT '留言id',
  `leaveTitle` varchar(80) NOT NULL COMMENT '留言标题',
  `leaveContent` varchar(2000) NOT NULL COMMENT '留言内容',
  `userObj` varchar(30) NOT NULL COMMENT '留言人',
  `leaveTime` varchar(20) default NULL COMMENT '留言时间',
  `replyContent` varchar(1000) default NULL COMMENT '管理回复',
  `replyTime` varchar(20) default NULL COMMENT '回复时间',
  PRIMARY KEY  (`leaveWordId`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_leaveword_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_leaveword
-- ----------------------------
INSERT INTO `t_leaveword` VALUES ('1', '太好了，买农产品无忧了', '有了这个平台，以后可以淘到好的农产品了', 'user1', '2018-03-14 23:50:45', '很好！', '2017-12-19 05:17:13');
INSERT INTO `t_leaveword` VALUES ('2', '111', '222', 'user1', '2018-03-14 05:16:45', 'test', '2017-12-19 05:17:01');

-- ----------------------------
-- Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `noticeId` int(11) NOT NULL auto_increment COMMENT '公告id',
  `title` varchar(80) NOT NULL COMMENT '标题',
  `content` varchar(5000) NOT NULL COMMENT '公告内容',
  `publishDate` varchar(20) default NULL COMMENT '发布时间',
  PRIMARY KEY  (`noticeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES ('1', '农产品交易平台开通了', '<h2>同学们以后来这里交易农产品信息吧！</h2>', '2018-03-14 23:38:35');

-- ----------------------------
-- Table structure for `t_productclass`
-- ----------------------------
DROP TABLE IF EXISTS `t_productclass`;
CREATE TABLE `t_productclass` (
  `productClassId` int(11) NOT NULL auto_increment COMMENT '类别编号',
  `productClassName` varchar(20) NOT NULL COMMENT '类别名称',
  PRIMARY KEY  (`productClassId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_productclass
-- ----------------------------
INSERT INTO `t_productclass` VALUES ('1', '蔬菜类');
INSERT INTO `t_productclass` VALUES ('2', '水果类');
INSERT INTO `t_productclass` VALUES ('3', '禽畜类');
INSERT INTO `t_productclass` VALUES ('4', '花卉苗木');

-- ----------------------------
-- Table structure for `t_productorder`
-- ----------------------------
DROP TABLE IF EXISTS `t_productorder`;
CREATE TABLE `t_productorder` (
  `orderId` int(11) NOT NULL auto_increment COMMENT '订单id',
  `productSellObj` int(11) NOT NULL COMMENT '图书信息',
  `userObj` varchar(30) NOT NULL COMMENT '意向用户',
  `price` float NOT NULL COMMENT '意向出价',
  `orderMemo` varchar(500) default NULL COMMENT '用户备注',
  `addTime` varchar(20) default NULL COMMENT '下单时间',
  PRIMARY KEY  (`orderId`),
  KEY `productSellObj` (`productSellObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_productorder_ibfk_1` FOREIGN KEY (`productSellObj`) REFERENCES `t_productsell` (`sellId`),
  CONSTRAINT `t_productorder_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_productorder
-- ----------------------------
INSERT INTO `t_productorder` VALUES ('1', '1', 'user1', '80', '我需要这个，给我吧！', '2018-03-14 14:15:16');
INSERT INTO `t_productorder` VALUES ('3', '1', 'user2', '75', '我也喜欢这种土鸡啊，给我吧，马上交易', '2018-03-14 06:20:48');

-- ----------------------------
-- Table structure for `t_productseek`
-- ----------------------------
DROP TABLE IF EXISTS `t_productseek`;
CREATE TABLE `t_productseek` (
  `seekId` int(11) NOT NULL auto_increment COMMENT '求购id',
  `productPhoto` varchar(60) NOT NULL COMMENT '图书主图',
  `productName` varchar(20) NOT NULL COMMENT '图书名称',
  `productClassObj` int(11) NOT NULL COMMENT '图书类别',
  `publish` varchar(50) NOT NULL COMMENT '出版社',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `price` float NOT NULL COMMENT '求购价格',
  `xjcd` varchar(20) NOT NULL COMMENT '新旧程度',
  `seekDesc` varchar(5000) NOT NULL COMMENT '求购说明',
  `userObj` varchar(30) NOT NULL COMMENT '发布用户',
  `addTime` varchar(20) default NULL COMMENT '用户发布时间',
  PRIMARY KEY  (`seekId`),
  KEY `productClassObj` (`productClassObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_productseek_ibfk_1` FOREIGN KEY (`productClassObj`) REFERENCES `t_productclass` (`productClassId`),
  CONSTRAINT `t_productseek_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_productseek
-- ----------------------------
INSERT INTO `t_productseek` VALUES ('1', 'upload/aeaf4e5b-f3c1-46da-a0f1-6c0842f21934.jpg', '水果酸角甜酸角360g', '2', '中国大陆', 'xxxx', '100', '全新', '<ul style=\"list-style-type: none;\" class=\" list-paddingleft-2\"><li><p>生产许可证编号：QS5301 1702 0010</p></li><li><p>产品标准号：LY/T 1741</p></li><li><p>厂名：云南连宸食品有限公司</p></li><li><p>厂址：云南省昆明市宜良县狗街镇工业园区食品片区</p></li><li><p>厂家联系方式：0871-64663064</p></li><li><p>配料表：甜角</p></li><li><p>储藏方法：阴凉干燥处</p></li><li><p>保质期：365 天</p></li><li><p>净含量:&nbsp;720g</p></li><li><p>包装方式:&nbsp;包装</p></li><li><p>品牌:&nbsp;傣乡园</p></li><li><p>系列:&nbsp;甜角360g*2</p></li><li><p>商品条形码:&nbsp;6938301821977</p></li><li><p>是否为有机食品:&nbsp;否</p></li><li><p>食品口味:&nbsp;甜酸角360gX2袋&nbsp;甜酸角360gX4袋</p></li><li><p>食品工艺:&nbsp;其他/other</p></li><li><p>是否含糖:&nbsp;含糖</p></li><li><p>产地:&nbsp;中国大陆</p></li><li><p>省份:&nbsp;云南省</p></li><li><p>城市:&nbsp;昆明市</p></li><li><p>特产品类:&nbsp;酸角糕</p></li></ul><p><br/></p>', 'user1', '2018-03-14 23:43:11');
INSERT INTO `t_productseek` VALUES ('2', 'upload/NoImage.jpg', '11', '1', '22', '333', '44', '55', '<p>6688sg</p>', 'user1', '2018-03-14 04:12:11');

-- ----------------------------
-- Table structure for `t_productsell`
-- ----------------------------
DROP TABLE IF EXISTS `t_productsell`;
CREATE TABLE `t_productsell` (
  `sellId` int(11) NOT NULL auto_increment COMMENT '出售id',
  `productPhoto` varchar(60) NOT NULL COMMENT '图书主图',
  `productName` varchar(20) NOT NULL COMMENT '图书名称',
  `productClassObj` int(11) NOT NULL COMMENT '图书类别',
  `publish` varchar(20) NOT NULL COMMENT '出版社',
  `author` varchar(20) NOT NULL COMMENT '作者',
  `sellPrice` float NOT NULL COMMENT '出售价格',
  `xjcd` varchar(20) NOT NULL COMMENT '新旧程度',
  `sellDesc` varchar(5000) NOT NULL COMMENT '出售说明',
  `userObj` varchar(30) NOT NULL COMMENT '发布用户',
  `addTime` varchar(20) default NULL COMMENT '用户发布时间',
  PRIMARY KEY  (`sellId`),
  KEY `productClassObj` (`productClassObj`),
  KEY `userObj` (`userObj`),
  CONSTRAINT `t_productsell_ibfk_1` FOREIGN KEY (`productClassObj`) REFERENCES `t_productclass` (`productClassId`),
  CONSTRAINT `t_productsell_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_productsell
-- ----------------------------
INSERT INTO `t_productsell` VALUES ('1', 'upload/90aed892-12bb-452e-9078-ad6d6cd4b733.jpg', '山林散养（260天）农家土鸡', '3', '中国大陆', '达达农产品公司', '86', '全新', '<p><span style=\"color: rgb(0, 0, 255);\"><strong><span style=\"font-size: 24px;\">此链接是1只装母鸡。净重（2-2.5斤之间</span></strong></span><strong style=\"color: rgb(0, 0, 255); line-height: 1.5;\"><span style=\"font-size: 24px;\">）</span></strong><span style=\"font-size: 36px;\"><strong><span style=\"line-height: 1.5;\">临安高山放养，吃杂粮，野草虫子长大。此链接是1只装母鸡。净重2-2.5斤，散养260天左右！</span></strong></span></p><p style=\"margin-top: 1.12em; margin-bottom: 1.12em; padding: 0px; font-family: tahoma, arial, 宋体, sans-serif; font-size: 14px; white-space: normal; background-color: rgb(255, 255, 255);\"><span style=\"color: rgb(0, 0, 255);\"><strong><span style=\"font-size: 24px;\"><span style=\"line-height: 1.5;\">顺丰包邮，除新疆，西藏，内蒙，青海，甘肃，之外 顺丰不到地区勿拍！</span></span></strong></span></p><p><br/></p>', 'user1', '2018-03-14 23:48:11');
INSERT INTO `t_productsell` VALUES ('2', 'upload/NoImage.jpg', '222', '1', '33', '55', '44', '66', '<p>fasfasf</p>', 'user1', '2018-03-14 04:27:019');

-- ----------------------------
-- Table structure for `t_userinfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo` (
  `user_name` varchar(30) NOT NULL COMMENT 'user_name',
  `password` varchar(100) NOT NULL COMMENT '登录密码',
  `name` varchar(20) NOT NULL COMMENT '姓名',
  `gender` varchar(4) NOT NULL COMMENT '性别',
  `birthDate` varchar(20) default NULL COMMENT '出生日期',
  `userPhoto` varchar(60) NOT NULL COMMENT '用户照片',
  `telephone` varchar(20) NOT NULL COMMENT '联系电话',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `address` varchar(80) default NULL COMMENT '家庭地址',
  `regTime` varchar(20) default NULL COMMENT '注册时间',
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('user1', '$2a$10$FxAcoHcbrexH6Lq61O3jAe41LhgGw7QF2jiemXmc451vRxngfQd7m', '双鱼林', '男', '2018-03-14', 'upload/fc4c7dc8-c2d5-4527-8ba7-bb83e2c090e4.jpg', '13589834234', 'syl@163.com', '四川成都红星路', '2018-03-14 23:39:38');
INSERT INTO `t_userinfo` VALUES ('user2', '$2a$10$Q3p5vhW0vZO3ihn8bFJThOnb6hiTaT32f8b/9RYP6MhH2HdSDizEy', '李倩', '女', '2017-12-14', 'upload/NoImage.jpg', '13573598343', 'liqian@163.com', '四川成都广元市', '2018-03-14 05:26:17');
