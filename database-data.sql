-- MariaDB dump 10.19  Distrib 10.10.2-MariaDB, for osx10.18 (arm64)
--
-- Host: 113.190.243.86    Database: easylink
-- ------------------------------------------------------
-- Server version	11.0.3-MariaDB-1:11.0.3+maria~ubu2204

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tbl_permission`
--
CREATE DATABASE `easylink`;
USE EASYLINK;
DROP TABLE IF EXISTS `tbl_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_permission` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `resource` varchar(128) NOT NULL DEFAULT '*',
                                  `action` varchar(128) NOT NULL DEFAULT '*' COMMENT 'allowed actions on the resource. Can be multiple action separated by comma',
                                  `condition` varchar(255) DEFAULT NULL COMMENT 'Spring EL syntax to check for the permission. Action is allowed if condition is fulfilled',
                                  `allow` tinyint(1) NOT NULL DEFAULT 1 COMMENT '0=false: deny action; 1=true: allow action',
                                  `description` varchar(255) DEFAULT NULL,
                                  `created_user` int(10) DEFAULT 1,
                                  `created_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `resource_action_instance` (`resource`,`action`,`condition`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Detail each permission\r\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_permission`
--

LOCK TABLES `tbl_permission` WRITE;
/*!40000 ALTER TABLE `tbl_permission` DISABLE KEYS */;
INSERT INTO `tbl_permission` VALUES
                                 (1,'*','*',NULL,1,NULL,1,NULL,NULL,NULL),
                                 (13,'groups','*',NULL,1,NULL,NULL,'2018-08-18 00:22:02',NULL,'2018-08-18 00:22:02'),
                                 (14,'users','*',NULL,1,NULL,NULL,'2018-08-18 00:22:02',NULL,'2018-08-18 00:22:02'),
                                 (15,'users','update','#user.id==#resource.createdUser',1,'Cho phép update user do chính mình tạo',1,NULL,NULL,NULL),
                                 (16,'users','*','aaaa',1,NULL,3,'2020-01-10 22:36:28',3,'2020-01-10 22:36:28'),
                                 (25,'*','*','',1,'',2,'2020-03-21 17:08:05',2,'2020-03-21 17:08:05'),
                                 (26,'dashboard','createAssignment','',1,'',2,'2020-03-22 05:07:51',2,'2020-03-22 05:07:51'),
                                 (27,'dashboard','updateAssignment','',1,'',2,'2020-03-22 05:08:02',2,'2020-03-22 05:08:02'),
                                 (28,'dashboard','listAssignment','',1,'',2,'2020-03-22 05:08:23',2,'2020-03-22 05:08:23'),
                                 (29,'user','*','#resource.type==1',1,'CRUD customer user',2,'2020-03-22 05:10:40',2,'2020-03-22 05:10:40'),
                                 (30,'mydashboard','*','',1,'',2,'2020-03-22 05:19:19',2,'2020-03-22 05:19:19'),
                                 (31,'dashboard','list','',1,'',2,'2020-03-23 17:17:22',2,'2020-03-23 17:17:22'),
                                 (32,'station','list','',1,'',2,'2021-10-25 17:41:26',2,'2021-10-25 17:41:26'),
                                 (34,'a','sdf','a',1,'ádfasdf',2,'2023-05-05 09:17:07',2,'2023-05-05 09:17:07'),
                                 (37,'a','','',1,'',2,'2023-05-05 09:28:56',2,'2023-05-05 09:28:56'),
                                 (38,'123','234','111',1,'asdfa',2,'2023-05-05 09:34:16',2,'2023-05-05 09:34:16'),
                                 (39,'a','b','c',1,'',2,'2023-05-05 09:49:00',2,'2023-05-05 09:49:00'),
                                 (40,'a','a','a',1,'a',2,'2023-05-05 09:59:59',2,'2023-05-05 09:59:59'),
                                 (41,'d','','',1,'',2,'2023-05-05 10:00:50',2,'2023-05-05 10:00:50'),
                                 (42,'e','','',1,'',2,'2023-05-05 10:00:52',2,'2023-05-05 10:00:52'),
                                 (43,'user','*','#resource.id!=99',1,'',2,'2023-05-07 10:28:27',2,'2023-05-07 10:28:27'),
                                 (44,'group','*','#resource.id==99',1,'',2,'2023-05-07 10:29:08',2,'2023-05-07 10:29:08'),
                                 (45,'group','*','#resource.id==2',1,'',2,'2023-05-07 14:45:39',2,'2023-05-07 14:45:39'),
                                 (46,'hhh','aab','',1,'',2,'2023-05-09 15:52:51',2,'2023-05-09 15:52:51'),
                                 (47,'hhh','123','',1,'',2,'2023-05-09 15:52:58',2,'2023-05-09 15:52:58'),
                                 (48,'hhh','123','akkk',1,'',2,'2023-05-09 15:53:05',2,'2023-05-09 15:53:05'),
                                 (55,'group','list','',1,'',2,'2023-05-11 02:27:38',2,'2023-05-11 02:27:38'),
                                 (56,'group','read','',1,'',2,'2023-05-11 02:27:44',2,'2023-05-11 02:27:44'),
                                 (57,'group','read','#resource.id==2',1,'',2,'2023-05-11 02:27:54',2,'2023-05-11 02:27:54'),
                                 (58,'group','read','#resource.id!=99',1,'',2,'2023-05-16 17:08:31',2,'2023-05-16 17:08:31'),
                                 (59,'hhh','123','ccc',1,'',2,'2023-05-16 17:08:50',2,'2023-05-16 17:08:50'),
                                 (60,'group','*','#resource.id==1',1,'',2,'2023-05-16 17:32:50',2,'2023-05-16 17:32:50'),
                                 (61,'dashboard','*','',1,'',2,'2023-10-26 17:09:02',2,'2023-10-26 17:09:02'),
                                 (62,'map','*','',1,'',2,'2023-10-26 17:09:10',2,'2023-10-26 17:09:10'),
                                 (63,'alarm','*','',1,'',2,'2023-10-26 17:09:15',2,'2023-10-26 17:09:15'),
                                 (64,'rule','*','',1,'',2,'2023-10-26 17:11:30',2,'2023-10-26 17:11:30'),
                                 (65,'device','list,read','',1,'',2,'2023-10-26 17:12:16',2,'2023-10-26 17:12:16'),
                                 (66,'user','*','#resource.id==#user.id',1,'',2,'2023-10-26 17:14:27',2,'2023-10-26 17:14:27'),
                                 (67,'mainDashboard','*','',1,'',2,'2023-10-26 17:17:41',2,'2023-10-26 17:17:41'),
                                 (68,'deviceDashboard','*','',1,'',2,'2023-10-26 17:17:47',2,'2023-10-26 17:17:47'),
                                 (69,'device','list,view','',1,'Xem danh sách & thông tin thiết bị',2,'2023-10-26 17:20:04',2,'2023-10-26 17:20:04'),
                                 (70,'user','profile,resetPassword','',1,'',2,'2023-10-26 17:26:14',2,'2023-10-26 17:26:14'),
                                 (71,'user','profile,resetPassword','#resource.id==#user.id',1,'',2,'2023-10-26 17:27:23',2,'2023-10-26 17:27:23');
/*!40000 ALTER TABLE `tbl_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_rule_device`
--

DROP TABLE IF EXISTS `tbl_rule_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_rule_device` (
                                   `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `created_date` datetime DEFAULT NULL,
                                   `created_user` int(10) DEFAULT NULL,
                                   `modified_date` datetime DEFAULT NULL,
                                   `modified_user` int(10) DEFAULT NULL,
                                   `rule_id` int(10) unsigned NOT NULL,
                                   `device_id` int(10) unsigned NOT NULL,
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Showing which device/station will apply this rule';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_rule_device`
--

LOCK TABLES `tbl_rule_device` WRITE;
/*!40000 ALTER TABLE `tbl_rule_device` DISABLE KEYS */;
INSERT INTO `tbl_rule_device` VALUES
                                  (15,'2023-11-15 08:36:22',3,'2023-11-15 08:36:22',3,3,3),
                                  (16,'2023-11-15 08:36:22',3,'2023-11-15 08:36:22',3,3,8),
                                  (21,'2023-11-24 09:35:49',2,'2023-11-24 09:35:49',2,4,55),
                                  (29,'2023-11-27 14:06:03',2,'2023-11-27 14:06:03',2,1,54),
                                  (30,'2023-11-27 14:06:03',2,'2023-11-27 14:06:03',2,1,55),
                                  (31,'2023-11-27 14:06:08',2,'2023-11-27 14:06:08',2,9,54),
                                  (32,'2023-11-27 14:06:08',2,'2023-11-27 14:06:08',2,9,55),
                                  (33,'2023-11-27 14:06:14',2,'2023-11-27 14:06:14',2,11,54),
                                  (34,'2023-11-27 14:06:14',2,'2023-11-27 14:06:14',2,11,55),
                                  (35,'2023-11-27 14:06:18',2,'2023-11-27 14:06:18',2,12,54),
                                  (36,'2023-11-27 14:06:18',2,'2023-11-27 14:06:18',2,12,55),
                                  (37,'2023-11-27 14:06:23',2,'2023-11-27 14:06:23',2,13,54),
                                  (38,'2023-11-27 14:06:23',2,'2023-11-27 14:06:23',2,13,55),
                                  (39,'2023-11-27 14:06:31',2,'2023-11-27 14:06:31',2,15,54),
                                  (40,'2023-11-27 14:06:31',2,'2023-11-27 14:06:31',2,15,55),
                                  (41,'2023-11-27 14:06:35',2,'2023-11-27 14:06:35',2,14,54),
                                  (42,'2023-11-27 14:06:35',2,'2023-11-27 14:06:35',2,14,55),
                                  (43,'2023-11-27 14:06:43',2,'2023-11-27 14:06:43',2,8,54),
                                  (44,'2023-11-27 14:06:43',2,'2023-11-27 14:06:43',2,8,55),
                                  (45,'2023-11-27 14:06:53',2,'2023-11-27 14:06:53',2,10,54),
                                  (46,'2023-11-27 14:06:53',2,'2023-11-27 14:06:53',2,10,55),
                                  (47,'2023-11-29 22:36:31',2,'2023-11-29 22:36:31',2,16,53);
/*!40000 ALTER TABLE `tbl_rule_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user_group`
--

DROP TABLE IF EXISTS `tbl_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user_group` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `created_date` datetime DEFAULT NULL,
                                  `created_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `user_id` int(10) unsigned NOT NULL,
                                  `group_id` int(10) unsigned NOT NULL,
                                  `inherit` tinyint(1) DEFAULT 0,
                                  PRIMARY KEY (`id`),
                                  KEY `FK_tbl_user_group_tbl_group` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=392 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Mapping between user & group. One user can belong to many group';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_group`
--

LOCK TABLES `tbl_user_group` WRITE;
/*!40000 ALTER TABLE `tbl_user_group` DISABLE KEYS */;
INSERT INTO `tbl_user_group` VALUES
                                 (214,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,3,100,0),
                                 (215,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,3,109,1),
                                 (216,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,3,110,1),
                                 (217,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,3,114,1),
                                 (218,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,3,124,1),
                                 (219,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,17,100,0),
                                 (220,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,17,109,1),
                                 (221,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,17,110,1),
                                 (222,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,17,114,1),
                                 (223,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,17,124,1),
                                 (226,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,28,109,0),
                                 (227,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,27,100,0),
                                 (228,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,27,109,1),
                                 (229,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,27,110,1),
                                 (230,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,27,114,1),
                                 (231,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,27,124,1),
                                 (232,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,29,111,0),
                                 (233,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,29,112,1),
                                 (234,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,29,113,1),
                                 (235,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,29,123,1),
                                 (236,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,10,112,0),
                                 (237,'2023-11-15 10:34:31',2,'2023-11-15 10:34:31',2,10,123,1),
                                 (248,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,1,0),
                                 (249,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,100,1),
                                 (250,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,109,1),
                                 (251,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,110,1),
                                 (252,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,111,1),
                                 (253,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,112,1),
                                 (254,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,113,1),
                                 (255,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,114,1),
                                 (256,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,123,1),
                                 (257,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,124,1),
                                 (258,'2023-11-15 11:34:46',2,'2023-11-15 11:34:46',2,2,125,1),
                                 (265,'2023-11-15 11:35:01',2,'2023-11-15 11:35:01',2,26,125,0),
                                 (266,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,100,0),
                                 (267,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,109,1),
                                 (268,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,110,1),
                                 (269,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,114,1),
                                 (270,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,124,1),
                                 (271,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,125,1),
                                 (272,'2023-11-15 11:37:59',2,'2023-11-15 11:37:59',2,31,109,0),
                                 (273,'2023-11-15 11:37:59',2,'2023-11-15 11:37:59',2,31,124,1),
                                 (274,'2023-11-15 13:50:20',26,'2023-11-15 13:50:20',26,32,125,0),
                                 (366,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,100,0),
                                 (367,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,109,1),
                                 (368,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,110,1),
                                 (369,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,114,1),
                                 (370,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,124,1),
                                 (371,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,125,1),
                                 (379,'2023-11-15 21:45:05',2,'2023-11-15 21:45:05',2,35,123,0),
                                 (380,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,100,0),
                                 (381,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,109,1),
                                 (382,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,110,1),
                                 (383,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,114,1),
                                 (384,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,124,1),
                                 (385,'2023-11-15 23:21:53',2,'2023-11-15 23:21:53',2,34,125,1),
                                 (387,'2023-11-20 11:22:21',34,'2023-11-20 11:22:21',34,37,109,0),
                                 (388,'2023-11-20 11:22:21',34,'2023-11-20 11:22:21',34,37,124,1),
                                 (389,'2023-11-24 11:20:08',2,'2023-11-24 11:20:08',2,36,125,0),
                                 (390,'2023-12-03 21:49:35',2,'2023-12-03 21:49:35',2,38,109,0),
                                 (391,'2023-12-03 21:49:35',2,'2023-12-03 21:49:35',2,38,124,1);
/*!40000 ALTER TABLE `tbl_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_device_type`
--

DROP TABLE IF EXISTS `tbl_device_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_device_type` (
                                   `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                   `name` varchar(100) NOT NULL,
                                   `description` varchar(255) DEFAULT '',
                                   `created_user` int(10) DEFAULT 1,
                                   `created_date` datetime DEFAULT NULL,
                                   `modified_user` int(10) DEFAULT NULL,
                                   `modified_date` datetime DEFAULT NULL,
                                   `sensors` varchar(1024) DEFAULT NULL,
                                   `schema_id` int(10) DEFAULT 1 COMMENT 'Reserved fields',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `username` (`name`),
                                   KEY `USER_NAME` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Device Type. Mang tính chất thông tin để hỗ trợ lọc trên báo cáo. Không ảnh hưởng tới hoạt động device';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_device_type`
--

LOCK TABLES `tbl_device_type` WRITE;
/*!40000 ALTER TABLE `tbl_device_type` DISABLE KEYS */;
INSERT INTO `tbl_device_type` VALUES
                                  (1,'Trạm thường','Có đủ hết các trường dữ liệu',NULL,NULL,2,'2023-12-04 08:47:43','temp1,hum1,temp2,hum2,door1,door2,vol1,cur1,vol2,cur2,vol3,cur3,vol4,cur4',1),
                                  (2,'Trạm UPS','Không có thông tin điện 1 chiều',NULL,NULL,2,'2023-12-03 23:56:31','temp1,hum1,temp2,hum2,door1,door2,vol1,cur1,vol2,cur2,vol3,cur3',1);
/*!40000 ALTER TABLE `tbl_device_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_group`
--

DROP TABLE IF EXISTS `tbl_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_group` (
                             `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                             `name` varchar(50) NOT NULL,
                             `status` int(10) NOT NULL COMMENT '0: inactive, 1: active',
                             `created_user` int(10) DEFAULT 1,
                             `created_date` datetime DEFAULT NULL,
                             `modified_date` datetime DEFAULT NULL,
                             `modified_user` int(10) DEFAULT NULL,
                             `parent_id` int(10) unsigned DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `tbl_group_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='nhom nguoi dung';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_group`
--

LOCK TABLES `tbl_group` WRITE;
/*!40000 ALTER TABLE `tbl_group` DISABLE KEYS */;
INSERT INTO `tbl_group` VALUES
                            (1,'Tất cả',1,1,NULL,'2023-11-21 00:10:25',2,0),
                            (100,'Trụ sở Hà Nội',1,2,'2023-05-17 02:27:03','2023-10-31 11:22:52',2,1),
                            (109,'Đống Đa',1,2,'2023-09-06 02:46:34','2023-10-05 16:55:22',2,100),
                            (110,'Cầu Giấy',1,2,'2023-10-05 16:55:35','2023-10-05 16:55:35',2,100),
                            (111,'Chi nhánh Hồ Chí Minh',1,2,'2023-10-31 11:15:53','2023-10-31 11:15:53',2,1),
                            (112,'Quận 1',1,2,'2023-10-31 11:16:06','2023-10-31 11:16:06',2,111),
                            (113,'Quận 2',1,2,'2023-10-31 11:16:13','2023-10-31 11:16:13',2,111),
                            (114,'Hà Đông',1,2,'2023-11-06 10:48:42','2023-11-06 10:48:42',2,100),
                            (123,'Thủ Đức',1,2,'2023-11-15 08:48:47','2023-11-15 08:48:47',2,112),
                            (124,'Kim Liên',1,2,'2023-11-15 10:16:58','2023-11-15 10:34:32',2,109),
                            (125,'Khu vực Sao Bắc Đẩu',1,2,'2023-11-15 11:34:46','2023-11-15 11:34:46',2,100);
/*!40000 ALTER TABLE `tbl_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_rule`
--

DROP TABLE IF EXISTS `tbl_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_rule` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) DEFAULT NULL,
                            `condition` varchar(1024) DEFAULT NULL COMMENT 'Condition expression',
                            `action_type` varchar(50) DEFAULT NULL COMMENT 'Create alarm, Send chat, run command, call url....',
                            `action` varchar(1024) DEFAULT NULL COMMENT 'Metadata to describe action: could be json, etc...',
                            `metadata` varchar(1024) DEFAULT NULL COMMENT 'reserved:  JSON string of metadata',
                            `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
                            `scope` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0: apply recursively to groups;  1:  no recursive',
                            `min_interval` int(11) NOT NULL DEFAULT 0 COMMENT 'Min interval between 2 action trigger (in seconds)',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Alarm rule';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_rule`
--

LOCK TABLES `tbl_rule` WRITE;
/*!40000 ALTER TABLE `tbl_rule` DISABLE KEYS */;
INSERT INTO `tbl_rule` VALUES
                           (1,'Cửa 2 mở','#door2 == 0','CreateAlarm','{\"alarmType\":\"Cảnh báo cửa\",\"alarmContent\":\"Cửa 2 mở\",\"alarmLevel\":\"Warning\"}',NULL,1,0,60,2,'2021-10-27 17:06:11','2023-11-27 14:23:36',2),
                           (8,'Cửa 1 mở','#door1 == 0','CreateAlarm','{\"alarmType\":\"Cảnh báo cửa\",\"alarmContent\":\"Cửa 1 mở\",\"alarmLevel\":\"Info\"}',NULL,1,0,60,2,'2023-11-24 09:27:13','2023-11-27 14:23:29',2),
                           (9,'Nhiệt độ môi trường vượt ngưỡng','#temp1 >35 OR #temp2 >35','CreateAlarm','{\"alarmType\":\"Cảnh báo môi trường\",\"alarmContent\":\"Nhiệt độ môi trường vượt ngưỡng\",\"alarmLevel\":\"Warning\"}',NULL,1,0,30,2,'2023-11-24 09:50:39','2023-11-30 10:11:19',2),
                           (10,'Điện áp AC đầu vào cao trên ngưỡng 240V','#vol1 >240 OR #vol2 >240 ','CreateAlarm','{\"alarmType\":\"Cảnh báo điện áp\",\"alarmContent\":\"Điện áp AC đầu vào cao trên ngưỡng 240V\",\"alarmLevel\":\"Info\"}',NULL,1,0,60,2,'2023-11-24 10:07:30','2023-11-25 15:10:50',2),
                           (11,'Điện áp AC đầu vào thấp dưới ngưỡng 200V','#vol1 <200 OR #vol2 <200','CreateAlarm','{\"alarmType\":\"Cảnh báo điện áp\",\"alarmContent\":\"Điện áo AC đầu vào thấp dưới ngưỡng 200V\",\"alarmLevel\":\"Info\"}',NULL,1,0,60,2,'2023-11-24 10:25:10','2023-11-30 09:12:55',2),
                           (12,'Địên áp AC đầu ra cao trên ngưỡng 240V','#vol3>240','CreateAlarm','{\"alarmType\":\"Cảnh báo điện áp AC\",\"alarmContent\":\"Địên áp AC đầu ra cao trên ngưỡng 240V\",\"alarmLevel\":\"Info\"}',NULL,1,0,60,2,'2023-11-24 10:27:01','2023-11-24 10:27:01',2),
                           (13,'Điện áp AC đầu ra thấp dưới 200V','#vol3 <200','CreateAlarm','{\"alarmType\":\"Cảnh báo điện áp AC\",\"alarmContent\":\"Điện áp AC đầu ra thấp dưới 200V\",\"alarmLevel\":\"Info\"}',NULL,1,0,60,2,'2023-11-24 10:29:52','2023-11-24 10:29:52',2),
                           (14,'Điện áp DC cao trên ngưỡng 60V','#vol4 > 60','CreateAlarm','{\"alarmType\":\"Ca nhr báo DC\",\"alarmContent\":\"Điện áp DC cao vượt ngưỡng 60V\",\"alarmLevel\":\"Warning\"}',NULL,1,0,60,2,'2023-11-24 10:30:44','2023-12-04 00:33:47',2),
                           (15,'Điện áp DC thấp dưới ngưỡng 54V','#vol4 <54','CreateAlarm','{\"alarmType\":\"Cảnh báo điện áp DC\",\"alarmContent\":\"Điện áp DC thấp dưới ngưỡng 54V\",\"alarmLevel\":\"Warning\"}',NULL,1,0,60,2,'2023-11-24 10:31:29','2023-11-24 10:33:27',2);
/*!40000 ALTER TABLE `tbl_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_role_permission`
--

DROP TABLE IF EXISTS `tbl_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_role_permission` (
                                       `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                       `created_date` datetime DEFAULT NULL,
                                       `created_user` int(10) DEFAULT NULL,
                                       `modified_date` datetime DEFAULT NULL,
                                       `modified_user` int(10) DEFAULT NULL,
                                       `role_id` int(10) unsigned NOT NULL,
                                       `permission_id` int(10) unsigned NOT NULL,
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `role_id_permission_id` (`role_id`,`permission_id`),
                                       KEY `FK_tbl_role_permission_tbl_permission` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Mapping between user & group. One user can belong to many group';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_role_permission`
--

LOCK TABLES `tbl_role_permission` WRITE;
/*!40000 ALTER TABLE `tbl_role_permission` DISABLE KEYS */;
INSERT INTO `tbl_role_permission` VALUES
                                      (1,NULL,NULL,NULL,NULL,1,1),
                                      (10,'2020-03-22 05:19:19',2,'2020-03-22 05:19:19',2,2,30),
                                      (12,'2021-10-25 17:41:26',2,'2021-10-25 17:41:26',2,2,32),
                                      (49,'2023-10-26 17:09:02',2,'2023-10-26 17:09:02',2,2,61),
                                      (50,'2023-10-26 17:09:10',2,'2023-10-26 17:09:10',2,2,62),
                                      (51,'2023-10-26 17:09:15',2,'2023-10-26 17:09:15',2,2,63),
                                      (52,'2023-10-26 17:11:30',2,'2023-10-26 17:11:30',2,2,64),
                                      (55,'2023-10-26 17:17:41',2,'2023-10-26 17:17:41',2,2,67),
                                      (56,'2023-10-26 17:17:47',2,'2023-10-26 17:17:47',2,2,68),
                                      (57,'2023-10-26 17:20:04',2,'2023-10-26 17:20:04',2,2,69);
/*!40000 ALTER TABLE `tbl_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user`
--

DROP TABLE IF EXISTS `tbl_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `username` varchar(50) NOT NULL,
                            `password` varchar(255) NOT NULL DEFAULT '',
                            `full_name` varchar(255) DEFAULT NULL,
                            `email` varchar(255) DEFAULT NULL,
                            `phone` varchar(20) DEFAULT NULL,
                            `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                            `type` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: internal, 1: customer',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `username` (`username`),
                            KEY `USER_NAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user`
--

LOCK TABLES `tbl_user` WRITE;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` VALUES
                           (2,'admin','$2a$10$ejqae6O1OQQuaN04oBNyPOG9yOfsEjUL8Pz3tT8B3QE2Ooy3WGxIe','Super admin 5','anhpd@vivas.vn','',1,0,NULL,'2017-12-28 17:47:45',2,'2023-11-02 22:45:03'),
                           (26,'thaont','$2a$10$DgEK9O7FGEX9TAFn6Wn8F.hHWQXzfWMBpC5VHFweIVcn3G3jE7uL6','Nguyễn Thị Thảo','nguyen.thao966958@gmail.com','',1,0,2,'2023-10-27 14:06:18',2,'2023-11-21 09:43:06'),
                           (30,'Loill','$2a$10$WRtB6WjTXXtgar5r4ZiDnO8OTpnE1mUcUxNWuz2Z7m9Oz7hyagfJG','Lưu Lê Lợi','loill@gmail.com','07578443',1,0,2,'2023-11-15 11:37:08',2,'2023-11-24 11:14:24'),
                           (31,'Huyvs','$2a$10$f7EuNhORRKzuUoT1Fr1EM.IG8vPUquajsJ6KvHvG3Qqy9Z7HCFhqS','Vũ Sỹ Huy','huy@gmail.com','75684093',1,0,2,'2023-11-15 11:37:59',2,'2023-11-27 11:07:25'),
                           (33,'Test','$2a$10$KhRhkDqHKlo/BvAP6ejaL.1qe8keENRzi4ThDNdmupEhk/EabOdLq','test','test@gmail.com','',1,0,30,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27'),
                           (34,'anhpd','$2a$10$ieLMRjVlKRRGyEpbWgIYeuJ3jsl9Sj/avonFtVC0cqxE9V9bjZFDa','Test HN','','',1,0,2,'2023-11-15 21:36:37',2,'2023-11-27 14:07:32'),
                           (35,'td1','$2a$10$a1LXssjXhXSGukVKTAM0Z.iw9jJUF4BbP.l1ISewlKQjqjB14mBw2','','','',1,0,2,'2023-11-15 21:45:05',2,'2023-11-15 21:45:05'),
                           (36,'Uyen','$2a$10$yn4LbiV3.ycdKPEc/zuwWu5TVlWTRvzKKlAqnt0Ylh11zOzFRbNKu','Uyên','uyen@gmail.com','9764535454',1,0,2,'2023-11-16 09:49:28',2,'2023-11-24 11:18:02'),
                           (37,'cskh_a1','$2a$10$b4aFEZwY6F75ugwztKFdu.N9EGDnnCqQLjFgMN/Rnh52s9Y5uHk5G','','','',1,0,34,'2023-11-20 11:22:21',34,'2023-11-20 11:22:21'),
                           (38,'a234234','$2a$10$va1/HSQUR3A3ogkxHW5J9.fsFnHtHejiseZDu0V99JmuNMfXmQP9a','123123','','',1,0,2,'2023-12-03 21:49:34',2,'2023-12-03 21:49:34');
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_role_tree`
--

DROP TABLE IF EXISTS `tbl_role_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_role_tree` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `created_date` datetime DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `child_id` int(10) unsigned NOT NULL COMMENT 'ID of child role',
                                 `parent_id` int(10) unsigned NOT NULL COMMENT 'ID of parent role',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `parent_id_child_id` (`child_id`,`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Represent role hierachy.\r\nOne role can have many child roles. \r\nOne role can have many parent roles.\r\nChild role  will inherit all priviledges from parent roles';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_role_tree`
--

LOCK TABLES `tbl_role_tree` WRITE;
/*!40000 ALTER TABLE `tbl_role_tree` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_role_tree` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_device_status`
--

DROP TABLE IF EXISTS `tbl_device_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_device_status` (
                                     `device_token` varchar(255) NOT NULL,
                                     `telemetry` varchar(1023) NOT NULL DEFAULT '' COMMENT 'latest telemetry record',
                                     `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT 'Trạng thái hoạt động chung của trạm. 1 là OK. Khác là mã lỗi',
                                     `event_time` datetime DEFAULT NULL COMMENT 'latest event time',
                                     UNIQUE KEY `device_id` (`device_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Lưu trang thái cập nhật latest của device. Update mỗi khi nhận được message từ device.\r\nỞ pha sau sẽ chuyển sang redis để tăng hiệu năng write\r\n\r\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_device_status`
--

LOCK TABLES `tbl_device_status` WRITE;
/*!40000 ALTER TABLE `tbl_device_status` DISABLE KEYS */;
INSERT INTO `tbl_device_status` VALUES
                                    ('0y5SEpasGakoZCZbXb9L','{\r\n\"UPSBatCap\": 18,\r\n\"UPSBatTemp\": 335,\r\n\"UPSBatVol\": 122,\r\n\"UPSCurrentLoad\": 97,\r\n\"UPSInVol\": 1898,\r\n\"UPSMaxOut\": 2283,\r\n\"UPSMinOut\": 1765,\r\n\"UPSOutVol\": 1935,\r\n\"UPSBatTemp\": 318,\r\n\"UPSBatVol\": 123,\r\n\"UPSCurrentLoad\": 128,\r\n\"UPSInVol\": 1819,\r\n\"UPSMaxOut\": 2289,\r\n\"UPSMinOut\": 1581,\r\n\"UPSOutVol\": 2172,\r\n\"UPSStatus\": 1,\r\n\"UPSTemp\": 338,\r\n\"door1\": 1,\r\n\"door2\": 1,\r\n\"hum1\": 43,\r\n\"hum2\": 75,\r\n\"smoke1\": 0,\r\n\"temp1\": 15,\r\n\"temp2\": 18,\r\n\"water1\": 1,\r\n\"UPSTemp\": 325,\r\n\"door1\": 0,\r\n\"door2\": 0,\r\n\"hum1\": 48,\r\n\"hum2\": 66,\r\n\"smoke1\": 1,\r\n\"temp1\": 21,\r\n\"temp2\": 23,\r\n\"water1\": 0\r\n}',0,'2023-05-17 02:41:43'),
                                    ('fd','{\"temp1\":27.400000,\"hum1\":72.600000,\"temp2\":27.200000,\"hum2\":73.300000,\"door1\":0,\"door2\":0,\"vol1\":233,\"cur1\":0,\"vol2\":\"error\",\"cur2\":\"error\",\"vol3\":\"error\",\"cur3\":\"error\",\"vol4\":\"error\",\"cur4\":\"error\"}',0,'2023-11-20 17:28:23'),
                                    ('GW01','{\"deviceToken\":\"SBD_GW01\",\"temp1\":27.800000,\"hum1\":61,\"temp2\":27.200000,\"hum2\":62.500000,\"vol1\":\"error\",\"cur1\":\"error\",\"vol2\":\"error\",\"cur2\":\"error\",\"vol3\":\"error\",\"cur3\":\"error\",\"vol4\":\"error\",\"cur4\":\"error\"}',0,'2023-10-26 17:32:36'),
                                    ('SBD_GW01','{\"device_token\":\"SBD_GW01\",\"temp1\":29,\"hum1\":62,\"temp2\":38,\"hum2\":63,\"door1\":1,\"door2\":1,\"vol1\":201,\"cur1\":0,\"vol2\":200,\"cur2\":0,\"vol3\":239,\"cur3\":0,\"vol4\":60,\"cur4\":0}',0,'2023-11-30 10:36:26'),
                                    ('TB311','{\"temp1\":31.40030,\"hum1\":72.600000,\"temp2\":27.200000,\"hum2\":73.300000,\"door1\":0,\"door2\":0,\"vol1\":123,\"cur1\":0,\"vol2\":39,\"cur2\":10.9,\"vol3\":5.7,\"cur3\":4.1}',0,'2023-12-01 11:35:46'),
                                    ('TK123','{\"temp1\":31.40030,\"hum1\":22.5,\"temp2\":27.200000,\"hum2\":73.300000,\"door1\":0,\"door2\":0,\"vol1\":123,\"cur1\":0,\"vol2\":\"error\",\"cur2\":22,\"vol3\":5.7,\"cur3\":4.1}',0,'2023-12-04 09:58:15'),
                                    ('token1','{\n  \"deviceToken\": \"token1\",\n\"UPSBatCap\": 18,\n\"UPSBatTemp\": 335,\n\"UPSBatVol\": 122,\n\"UPSCurrentLoad\": 97,\n\"UPSInVol\": 1898,\n\"UPSMaxOut\": 2283,\n\"UPSMinOut\": 1765,\n\"UPSOutVol\": 1935,\n\"UPSBatTemp\": 318,\n\"UPSBatVol\": 123,\n\"UPSCurrentLoad\": 128,\n\"UPSInVol\": 1819,\n\"UPSMaxOut\": 2289,\n\"UPSMinOut\": 1581,\n\"UPSOutVol\": 2172,\n\"status\": 1\n}',1,'2023-09-17 19:46:36'),
                                    ('tram4-hanoi','{\r\n\"UPSBatCap\": 18,\r\n\"UPSBatTemp\": 335,\r\n\"UPSBatVol\": 122,\r\n\"UPSCurrentLoad\": 97,\r\n\"UPSInVol\": 1898,\r\n\"UPSMaxOut\": 2283,\r\n\"UPSMinOut\": 1765,\r\n\"UPSOutVol\": 1935,\r\n\"UPSBatTemp\": 318,\r\n\"UPSBatVol\": 123,\r\n\"UPSCurrentLoad\": 128,\r\n\"UPSInVol\": 1819,\r\n\"UPSMaxOut\": 2289,\r\n\"UPSMinOut\": 1581,\r\n\"UPSOutVol\": 2172,\r\n\"UPSStatus\": 1,\r\n\"UPSTemp\": 338,\r\n\"door1\": 1,\r\n\"door2\": 1,\r\n\"hum1\": 43,\r\n\"hum2\": 75,\r\n\"smoke1\": 0,\r\n\"temp1\": 15,\r\n\"temp2\": 18,\r\n\"water1\": 1,\r\n\"UPSTemp\": 325,\r\n\"door1\": 0,\r\n\"door2\": 0,\r\n\"hum1\": 48,\r\n\"hum2\": 66,\r\n\"smoke1\": 1,\r\n\"temp1\": 21,\r\n\"temp2\": 23,\r\n\"water1\": 0\r\n}',0,'2021-10-25 22:23:20'),
                                    ('VIN_DC_1','{\n	\"deviceId\" : \"VIN_DC_1\",\n	\"eventTime\" : 1683276740,\n	\"status\" : 28\n}',1,'2023-05-05 08:52:20'),
                                    ('VIN_MEGA_MALL_1','{\n	\"UPSBatCap\" : 1,\n	\"UPSBatTemp\" : 336,\n	\"UPSBatVol\" : 123,\n	\"UPSCurrentLoad\" : 50,\n	\"UPSInVol\" : 2126,\n	\"UPSMaxOut\" : 2591,\n	\"UPSMinOut\" : 1425,\n	\"UPSOutVol\" : 1928,\n	\"UPSStatus\" : 1,\n	\"UPSTemp\" : 335,\n	\"deviceId\" : \"VIN_MEGA_MALL_1\",\n	\"door1\" : 0.0,\n	\"door2\" : 0.0,\n	\"eventTime\" : 1683276738,\n	\"hum1\" : 60.5,\n	\"hum2\" : 0.0,\n	\"input1\" : 0.0,\n	\"input13\" : 0.0,\n	\"input14\" : 0.0,\n	\"input16\" : 0.0,\n	\"input2\" : 0.0,\n	\"input3\" : 0.0,\n	\"input4\" : 0.0,\n	\"input5\" : 0.0,\n	\"input6\" : 0.0,\n	\"input7\" : 0.0,\n	\"input8\" : 0.0,\n	\"input9\" : 0.0,\n	\"smoke1\" : 0.0,\n	\"status\" : 0,\n	\"temp1\" : 28.8,\n	\"temp2\" : 0.0,\n	\"water1\" : 0.0\n}',0,'2023-05-05 08:52:18'),
                                    ('X7vt2vs2OjixSVS29R95','{\n	\"UPSBatCap\" : 4,\n	\"UPSBatTemp\" : 315,\n	\"UPSBatVol\" : 123,\n	\"UPSCurrentLoad\" : 56,\n	\"UPSInVol\" : 1822,\n	\"UPSMaxOut\" : 2439,\n	\"UPSMinOut\" : 1544,\n	\"UPSOutVol\" : 1822,\n	\"UPSStatus\" : 1,\n	\"UPSTemp\" : 332,\n	\"deviceId\" : \"X7vt2vs2OjixSVS29R95\",\n	\"door1\" : 0,\n	\"door2\" : 1,\n	\"eventTime\" : 1635582243,\n	\"hum1\" : 68,\n	\"hum2\" : 46,\n	\"smoke1\" : 0,\n	\"temp1\" : 15,\n	\"temp2\" : 21,\n	\"water1\" : 1\n}',1,'2021-10-30 08:24:02'),
                                    ('ZCncAUhQPAhmp1mMnnUN','{\"temp1\":31.40030,\"hum1\":72.600000,\"temp2\":27.200000,\"hum2\":73.300000,\"door1\":0,\"door2\":0,\"vol1\":123,\"cur1\":0,\"vol2\":39,\"cur2\":10.9,\"vol3\":5.7,\"cur3\":4.1}',0,'2023-11-30 23:43:47');
/*!40000 ALTER TABLE `tbl_device_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_role`
--

DROP TABLE IF EXISTS `tbl_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_role` (
                            `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                            `name` varchar(50) NOT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                            `editable` tinyint(3) unsigned NOT NULL DEFAULT 1 COMMENT '0 (false): system role, can not edit.  1(true):  normal role, can edit',
                            `created_user` int(10) DEFAULT 1,
                            `created_date` datetime DEFAULT NULL,
                            `modified_user` int(10) DEFAULT NULL,
                            `modified_date` datetime DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Bảng các quyền';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_role`
--

LOCK TABLES `tbl_role` WRITE;
/*!40000 ALTER TABLE `tbl_role` DISABLE KEYS */;
INSERT INTO `tbl_role` VALUES
                           (1,'ADMIN','Quản trị viên. Có quyền tạo/sửa/xóa thiết bị trong nhóm',1,0,1,NULL,NULL,NULL),
                           (2,'OPERATOR','Vận hành, giám sát. Không có quyền chỉnh sửa.',1,1,2,'2018-01-02 15:50:32',2,'2023-10-26 17:04:54');
/*!40000 ALTER TABLE `tbl_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_device`
--

DROP TABLE IF EXISTS `tbl_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_device` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `name` varchar(50) NOT NULL,
                              `description` varchar(255) DEFAULT '',
                              `location` varchar(255) DEFAULT '',
                              `street` varchar(255) DEFAULT NULL,
                              `district` varchar(255) DEFAULT NULL,
                              `city` varchar(255) DEFAULT NULL,
                              `device_token` varchar(255) DEFAULT NULL COMMENT 'IoT Device ID',
                              `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: active',
                              `lon` float DEFAULT NULL COMMENT 'Kinh do',
                              `lat` float DEFAULT NULL COMMENT 'Vi do',
                              `created_user` int(10) DEFAULT 1,
                              `created_date` datetime DEFAULT NULL,
                              `modified_user` int(10) DEFAULT NULL,
                              `modified_date` datetime DEFAULT NULL,
                              `type_id` int(10) DEFAULT 1,
                              `group_id` int(10) DEFAULT 0,
                              `tags` varchar(255) DEFAULT NULL,
                              `schema_id` int(10) DEFAULT 1,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `username` (`name`),
                              UNIQUE KEY `device_id` (`device_token`),
                              KEY `USER_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=DYNAMIC COMMENT='Trạm sạc. Mỗi trạm sạc tương ứng 1 device ID (1 thiết bị gateway duy nhất)\r\n\r\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_device`
--

LOCK TABLES `tbl_device` WRITE;
/*!40000 ALTER TABLE `tbl_device` DISABLE KEYS */;
INSERT INTO `tbl_device` VALUES
                             (3,'Trạm trụ sở HN','','30 Láng Hạ','Láng Hạ','Hoàn Kiếm','Hà Nội','ZCncAUhQPAhmp1mMnnUN',1,105.812,21.0296,NULL,NULL,2,'2023-11-28 00:37:19',1,100,'',1),
                             (6,'Trạm trụ sở HCM','4','asdfas','','Quận 1','Hồ Chí Minh','HCM123',1,NULL,NULL,2,'2022-11-01 16:38:59',2,'2023-12-01 00:13:06',1,111,'',1),
                             (53,'Thiết bị 2111','Thiết bị test bởi Thảo','43 Láng','Láng Hạ','Hoàn Kiếm','Hà Nội','TK123',1,105.827,21.0228,26,'2023-11-21 10:27:54',2,'2023-12-01 00:11:58',1,125,'',1),
                             (54,'Thiết bị 3111','Tes thiết bị số 2','Xã Đàn , Đống Đa, Hà Nội','Láng Hạ','Hoàn Kiếm','Hà Nội','TB311',1,105.824,21.0147,26,'2023-11-21 10:35:59',2,'2023-11-27 09:33:55',2,125,'',1),
                             (55,'Trạm SBD Demo','','','','','','SBD_GW01',1,105.824,21.0233,2,'2023-11-21 10:48:22',2,'2023-11-21 10:48:22',1,100,'',1);
/*!40000 ALTER TABLE `tbl_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_rpc_log`
--

DROP TABLE IF EXISTS `tbl_rpc_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_rpc_log` (
                               `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                               `request` varchar(5000) DEFAULT NULL,
                               `response` varchar(5000) DEFAULT NULL,
                               `status` tinyint(4) DEFAULT NULL,
                               `created_user` int(11) DEFAULT NULL,
                               `created_date` datetime DEFAULT NULL,
                               `modified_date` datetime DEFAULT NULL,
                               `modified_user` int(11) DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Log of RPC command and response';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_rpc_log`
--

LOCK TABLES `tbl_rpc_log` WRITE;
/*!40000 ALTER TABLE `tbl_rpc_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_rpc_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_parameter`
--

DROP TABLE IF EXISTS `tbl_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_parameter` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `name` varchar(50) NOT NULL COMMENT 'Parameter name',
                                 `value` varchar(1024) NOT NULL COMMENT 'Parameter value',
                                 `type` varchar(255) DEFAULT '1',
                                 `created_date` datetime DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Luu cac tham so cau hinh he thong';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_parameter`
--

LOCK TABLES `tbl_parameter` WRITE;
/*!40000 ALTER TABLE `tbl_parameter` DISABLE KEYS */;
INSERT INTO `tbl_parameter` VALUES
                                (1,'LAST_SYNC_DATE','10/06/2018 17:52:09','java.util.Date','2018-01-25 23:31:38','2018-06-10 17:52:09',-1,-1),
                                (3,'STATION_DASHBOARD','http://113.190.243.86:21080/monitor/d/a34694e8-9392-4ca5-9d1a-8eeb2c624d45/dashboard-tram?orgId=1&refresh=1m\n','java.lang.String','2020-08-24 15:34:53',NULL,NULL,NULL),
                                (5,'HOME_DASHBOARD','http://113.190.243.86:21080/monitor/d/ba6dae61-6c52-4dd6-8b85-4391497b9a78/dashboard-tong-quan?orgId=1&kiosk','java.lang.String',NULL,NULL,NULL,NULL),
                                (8,'LICENSE','TcEzVFuXGEPGpt8CoFGFpVGp8q3vnPuy2Fm0EKwp7CS7dCtt2fwHQc8k2C53OUKntgP6UrbMSJnPDYo3QEArEZcceg51CUZjN+pxnIr4y/FYk6KEgt0Tj4rYp3m2lCIrfITyc5XNXuGKILjjISDwRsniRo0hHrvx','java.lang.String','2023-11-17 15:27:09','2023-11-17 15:38:04',2,2),
                                (9,'MAP_URL','http://103.252.72.194/map/vietnam/{z}/{x}/{y}.png','java.lang.String',NULL,NULL,NULL,NULL),
                                (10,'CONNECTION_TIMEOUT','60','java.lang.Long',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tbl_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_schema`
--

DROP TABLE IF EXISTS `tbl_schema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_schema` (
                              `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                              `name` varchar(100) NOT NULL,
                              `data_schema` varchar(3000) DEFAULT '' COMMENT 'Data model',
                              `table_name` varchar(255) NOT NULL COMMENT 'table in analytics database',
                              `topic` varchar(255) NOT NULL COMMENT 'MQTT topic',
                              `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: active',
                              `created_user` int(10) DEFAULT 1,
                              `created_date` datetime DEFAULT NULL,
                              `modified_user` int(10) DEFAULT NULL,
                              `modified_date` datetime DEFAULT NULL,
                              `description` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `tbl_device_type_UN` (`topic`),
                              UNIQUE KEY `tbl_table_name_UN` (`table_name`),
                              UNIQUE KEY `username` (`name`),
                              KEY `USER_NAME` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Configuration for data schema';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_schema`
--

LOCK TABLES `tbl_schema` WRITE;
/*!40000 ALTER TABLE `tbl_schema` DISABLE KEYS */;
INSERT INTO `tbl_schema` VALUES
    (1,'Loại 1','{\n  \"device_token\": \"String\",\n  \"event_time\": \"Date\",\n  \"temp1\": \"Double\",\n  \"hum1\": \"Double\",\n  \"temp2\": \"Double\",\n  \"hum2\": \"Double\",\n  \"door1\": \"Integer\",\n  \"door2\": \"Integer\",\n  \"vol1\": \"Float\",\n  \"cur1\": \"Float\",\n  \"vol2\": \"Float\",\n  \"cur2\": \"Float\",\n  \"vol3\": \"Float\",\n  \"cur3\": \"Float\",\n  \"vol4\": \"Float\",\n  \"cur4\": \"Float\"\n}','sensor_data','$share/g1/telemetry/+',1,1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `tbl_schema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_user_role`
--

DROP TABLE IF EXISTS `tbl_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_user_role` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `created_date` datetime DEFAULT NULL,
                                 `created_user` int(10) DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `user_id` int(10) unsigned NOT NULL,
                                 `role_id` int(10) unsigned NOT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `user_id_role_id` (`user_id`,`role_id`),
                                 KEY `FK_tbl_user_role_tbl_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=COMPACT COMMENT='Mapping between user and role. One user can has many roles. Many users may have the same role.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_user_role`
--

LOCK TABLES `tbl_user_role` WRITE;
/*!40000 ALTER TABLE `tbl_user_role` DISABLE KEYS */;
INSERT INTO `tbl_user_role` VALUES
                                (9,'2018-08-18 00:11:19',2,'2018-08-18 00:11:19',2,2,1),
                                (11,'2020-03-21 15:55:14',2,'2020-03-21 15:55:14',2,17,2),
                                (12,'2022-11-01 15:51:59',2,'2022-11-01 15:51:59',2,22,2),
                                (21,'2023-10-31 11:18:31',2,'2023-10-31 11:18:31',2,27,1),
                                (22,'2023-10-31 11:19:06',2,'2023-10-31 11:19:06',2,28,1),
                                (23,'2023-11-02 22:35:36',2,'2023-11-02 22:35:36',2,3,1),
                                (24,'2023-11-13 00:00:27',2,'2023-11-13 00:00:27',2,10,2),
                                (26,'2023-11-15 11:35:01',2,'2023-11-15 11:35:01',2,26,1),
                                (27,'2023-11-15 11:37:08',2,'2023-11-15 11:37:08',2,30,1),
                                (28,'2023-11-15 11:37:59',2,'2023-11-15 11:37:59',2,31,1),
                                (29,'2023-11-15 13:50:20',26,'2023-11-15 13:50:20',26,32,1),
                                (30,'2023-11-15 14:28:27',30,'2023-11-15 14:28:27',30,33,1),
                                (31,'2023-11-15 21:36:37',2,'2023-11-15 21:36:37',2,34,1),
                                (32,'2023-11-15 21:45:05',2,'2023-11-15 21:45:05',2,35,1),
                                (33,'2023-11-16 09:49:28',2,'2023-11-16 09:49:28',2,36,2),
                                (34,'2023-11-20 11:22:21',34,'2023-11-20 11:22:21',34,37,2),
                                (35,'2023-12-03 21:49:35',2,'2023-12-03 21:49:35',2,38,2);
/*!40000 ALTER TABLE `tbl_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_device_group`
--

DROP TABLE IF EXISTS `tbl_device_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_device_group` (
                                    `device_id` int(10) unsigned NOT NULL,
                                    `group_id` int(10) unsigned NOT NULL,
                                    `inherit` tinyint(1) DEFAULT 0 COMMENT 'False if direct relationship, True if not',
                                    KEY `FK_tbl_user_group_tbl_group` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Mapping between device & group. Include inherited relationship';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_device_group`
--

LOCK TABLES `tbl_device_group` WRITE;
/*!40000 ALTER TABLE `tbl_device_group` DISABLE KEYS */;
INSERT INTO `tbl_device_group` VALUES
                                   (24,109,0),
                                   (24,1,1),
                                   (24,100,1),
                                   (25,100,0),
                                   (25,1,1),
                                   (28,100,0),
                                   (28,1,1),
                                   (29,109,0),
                                   (29,1,1),
                                   (29,100,1),
                                   (1,109,0),
                                   (1,1,1),
                                   (1,100,1),
                                   (3,100,0),
                                   (3,1,1),
                                   (6,111,0),
                                   (6,1,1),
                                   (7,100,0),
                                   (7,1,1),
                                   (10,100,0),
                                   (10,1,1),
                                   (11,113,0),
                                   (11,1,1),
                                   (11,111,1),
                                   (15,110,0),
                                   (15,1,1),
                                   (15,100,1),
                                   (16,100,0),
                                   (16,1,1),
                                   (17,110,0),
                                   (17,1,1),
                                   (17,100,1),
                                   (21,111,0),
                                   (21,1,1),
                                   (34,1,0),
                                   (35,114,0),
                                   (35,1,1),
                                   (35,100,1),
                                   (45,123,0),
                                   (45,1,1),
                                   (45,111,1),
                                   (45,112,1),
                                   (46,123,0),
                                   (46,1,1),
                                   (46,111,1),
                                   (46,112,1),
                                   (8,112,0),
                                   (8,1,1),
                                   (8,111,1),
                                   (50,1,0),
                                   (51,1,0),
                                   (52,1,0),
                                   (53,125,0),
                                   (53,1,1),
                                   (53,100,1),
                                   (54,125,0),
                                   (54,1,1),
                                   (54,100,1),
                                   (55,100,0),
                                   (55,1,1),
                                   (56,125,0),
                                   (56,1,1),
                                   (56,100,1);
/*!40000 ALTER TABLE `tbl_device_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_dashboard`
--

DROP TABLE IF EXISTS `tbl_dashboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_dashboard` (
                                 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                 `name` varchar(50) NOT NULL,
                                 `status` int(10) NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
                                 `created_user` int(10) DEFAULT 1,
                                 `created_date` datetime DEFAULT NULL,
                                 `modified_date` datetime DEFAULT NULL,
                                 `modified_user` int(10) DEFAULT NULL,
                                 `description` varchar(255) DEFAULT NULL,
                                 `url` varchar(1023) DEFAULT NULL,
                                 `metadata` varchar(1023) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `tbl_group_UN` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Cac dashboard duoc add vao he thong';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_dashboard`
--

LOCK TABLES `tbl_dashboard` WRITE;
/*!40000 ALTER TABLE `tbl_dashboard` DISABLE KEYS */;
INSERT INTO `tbl_dashboard` VALUES
                                (1,'Tổng quan',1,2,'2023-11-24 09:47:33','2023-11-27 12:52:48',2,'Thông tin tổng quan tất cả các trạm người dùng được giám sát','http://113.190.243.86:21080/superset/dashboard/BTS_OVERVIEW/?permalink_key=NWRGv5lMO34&force=1&standalone=2',NULL),
                                (2,'Giám sát chi tiết từng trạm (Trạm có điện 1 chiều)',1,2,'2023-11-24 09:09:36','2023-11-27 14:12:14',2,'Xem thông tin giám sát và lọc theo từng trạm ','http://113.190.243.86:21080/superset/dashboard/BTS_DETAIL/?device_type_id=1&force=1&standalone=2',NULL),
                                (4,'Giám sát chi tiết từng trạm (Trạm UPS)',1,2,'2023-11-25 22:17:35','2023-11-27 14:12:38',2,'Thông tin chi tiết các Trạm UPS','http://113.190.243.86:21080/superset/dashboard/UPS_DETAIL/?device_type_id=2&force=1&standalone=2',NULL);
/*!40000 ALTER TABLE `tbl_dashboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tbl_rule_group`
--

DROP TABLE IF EXISTS `tbl_rule_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbl_rule_group` (
                                  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                  `created_date` datetime DEFAULT NULL,
                                  `created_user` int(10) DEFAULT NULL,
                                  `modified_date` datetime DEFAULT NULL,
                                  `modified_user` int(10) DEFAULT NULL,
                                  `rule_id` int(10) unsigned NOT NULL,
                                  `group_id` int(10) unsigned NOT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci COMMENT='Showing which group will apply this rule. All device in group will apply.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tbl_rule_group`
--

LOCK TABLES `tbl_rule_group` WRITE;
/*!40000 ALTER TABLE `tbl_rule_group` DISABLE KEYS */;
INSERT INTO `tbl_rule_group` VALUES
                                 (3,'2023-11-12 10:23:24',2,'2023-11-12 10:23:24',2,1,1),
                                 (5,'2023-11-15 08:36:28',3,'2023-11-15 08:36:28',3,3,109),
                                 (6,'2023-11-15 08:36:28',3,'2023-11-15 08:36:28',3,3,110),
                                 (7,'2023-11-15 13:49:02',26,'2023-11-15 13:49:02',26,6,125),
                                 (12,'2023-11-20 17:28:02',2,'2023-11-20 17:28:02',2,7,110),
                                 (14,'2023-11-24 09:33:09',2,'2023-11-24 09:33:09',2,8,1),
                                 (15,'2023-11-24 09:35:49',2,'2023-11-24 09:35:49',2,4,1),
                                 (16,'2023-11-24 09:50:39',2,'2023-11-24 09:50:39',2,9,1),
                                 (17,'2023-11-24 10:07:30',2,'2023-11-24 10:07:30',2,10,1),
                                 (18,'2023-11-24 10:25:10',2,'2023-11-24 10:25:10',2,11,1),
                                 (19,'2023-11-24 10:27:01',2,'2023-11-24 10:27:01',2,12,1),
                                 (20,'2023-11-24 10:29:52',2,'2023-11-24 10:29:52',2,13,1),
                                 (21,'2023-11-24 10:30:44',2,'2023-11-24 10:30:44',2,14,1),
                                 (22,'2023-11-24 10:31:29',2,'2023-11-24 10:31:29',2,15,1),
                                 (28,'2023-11-30 00:21:14',2,'2023-11-30 00:21:14',2,16,100),
                                 (29,'2023-11-30 00:21:14',2,'2023-11-30 00:21:14',2,16,125),
                                 (30,'2023-12-04 00:43:34',2,'2023-12-04 00:43:34',2,17,100);
/*!40000 ALTER TABLE `tbl_rule_group` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-04 15:47:29
