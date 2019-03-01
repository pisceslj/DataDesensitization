-- MySQL dump 10.15  Distrib 10.0.38-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: taxi
-- ------------------------------------------------------
-- Server version	10.0.38-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `privateinfo`
--

DROP TABLE IF EXISTS `privateinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `privateinfo` (
  `id` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT 'ID',
  `name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '姓名',
  `sex` varchar(4) COLLATE utf8mb4_bin NOT NULL COMMENT '性别',
  `birthTime` datetime NOT NULL COMMENT '出生日期',
  `idCard` varchar(18) COLLATE utf8mb4_bin NOT NULL COMMENT '身份证号',
  `phone` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `tel` varchar(16) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '固定电话',
  `email` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `addr` text COLLATE utf8mb4_bin COMMENT '住址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `privateinfo`
--

LOCK TABLES `privateinfo` WRITE;
/*!40000 ALTER TABLE `privateinfo` DISABLE KEYS */;
INSERT INTO `privateinfo` VALUES ('00000001','刘晓东','男','1989-05-01 00:00:00','370784198905019989','15600651018','010-62600288','liuxiaodong@ict.ac.cn','北京市海淀区科学院南路6号中科院计算所'),('00000002','习健','女','1993-11-04 00:00:00','370784199311041234','13500656456','010-62600266','xijian@163.com','北京市海淀区稻香湖路中关村环保科技园中科院计算所'),('00000003','卢杰','男','1996-06-01 00:00:00','370784199906013456','13700771987','010-62600258','lujie@ict.ac.cn','北京市石景山区玉泉路19号'),('00000004','王成瑞','男','1996-03-21 00:00:00','370784199603214455','13812776688','010-62600298','88664488@qq.com','北京市石景山区玉泉路19号');
/*!40000 ALTER TABLE `privateinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-21 10:16:44
