-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.21 - MySQL Community Server (GPL)
-- Server OS:                    Win32
-- HeidiSQL Version:             11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for charging-device
CREATE DATABASE IF NOT EXISTS `charging-device` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `charging-device`;

-- Dumping structure for table charging-device.tbl_alarm
CREATE TABLE IF NOT EXISTS `tbl_alarm` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `device_id` varchar(50) NOT NULL,
  `event_time` datetime DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `level` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Info, Warning, Error, Critical',
  `rule_id` int(11) DEFAULT '0' COMMENT 'null or 0: sent from device. Else: rule_id',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_id_event_time_type_severity` (`device_id`,`event_time`,`type`,`level`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Lưu các cảnh báo (do hệ thống generate tự đông theo rule hoặc do device gửi lên)\r\n';

-- Dumping data for table charging-device.tbl_alarm: ~56 rows (approximately)
/*!40000 ALTER TABLE `tbl_alarm` DISABLE KEYS */;
INSERT INTO `tbl_alarm` (`id`, `device_id`, `event_time`, `content`, `type`, `level`, `rule_id`, `created_user`, `created_date`, `modified_date`, `modified_user`) VALUES
	(1, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-23 16:38:10', 'Nhiệt độ quá cao', 'Temp', 2, 0, 1, '2021-10-23 16:38:43', '2021-10-23 16:38:43', NULL),
	(2, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-23 16:39:04', 'Độ ẩm nguy hiểm', 'Humid', 3, 0, 1, '2021-10-23 16:39:32', '2021-10-23 16:39:33', NULL),
	(3, 'X7vt2vs2OjixSVS29R95', '2021-10-30 07:58:08', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:58:08', '2021-10-30 07:58:08', -1),
	(4, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 07:58:22', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:58:22', '2021-10-30 07:58:22', -1),
	(5, '0y5SEpasGakoZCZbXb9L', '2021-10-30 07:58:22', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:58:22', '2021-10-30 07:58:22', -1),
	(6, 'X7vt2vs2OjixSVS29R95', '2021-10-30 07:58:32', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:58:32', '2021-10-30 07:58:32', -1),
	(7, '0y5SEpasGakoZCZbXb9L', '2021-10-30 07:59:27', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:59:27', '2021-10-30 07:59:27', -1),
	(8, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 07:59:32', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:59:32', '2021-10-30 07:59:32', -1),
	(9, '0y5SEpasGakoZCZbXb9L', '2021-10-30 07:59:52', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 07:59:52', '2021-10-30 07:59:52', -1),
	(10, '0y5SEpasGakoZCZbXb9L', '2021-10-30 07:59:57', '11111', '2342342', 0, 3, -1, '2021-10-30 07:59:57', '2021-10-30 07:59:57', -1),
	(11, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 07:59:58', '11111', '2342342', 0, 3, -1, '2021-10-30 07:59:58', '2021-10-30 07:59:58', -1),
	(12, 'X7vt2vs2OjixSVS29R95', '2021-10-30 07:59:58', '11111', '2342342', 0, 3, -1, '2021-10-30 07:59:58', '2021-10-30 07:59:58', -1),
	(13, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:00:27', '11111', '2342342', 0, 3, -1, '2021-10-30 08:00:27', '2021-10-30 08:00:27', -1),
	(14, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:00:32', '11111', '2342342', 0, 3, -1, '2021-10-30 08:00:32', '2021-10-30 08:00:32', -1),
	(15, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:00:32', '11111', '2342342', 0, 3, -1, '2021-10-30 08:00:32', '2021-10-30 08:00:32', -1),
	(16, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:00:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:00:57', '2021-10-30 08:00:57', -1),
	(17, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:01:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:02', '2021-10-30 08:01:02', -1),
	(18, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:01:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:02', '2021-10-30 08:01:02', -1),
	(19, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:01:02', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:01:02', '2021-10-30 08:01:02', -1),
	(20, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:01:27', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:27', '2021-10-30 08:01:27', -1),
	(21, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:01:32', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:32', '2021-10-30 08:01:32', -1),
	(22, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:01:32', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:32', '2021-10-30 08:01:32', -1),
	(23, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:01:47', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:47', '2021-10-30 08:01:47', -1),
	(24, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:01:47', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:47', '2021-10-30 08:01:47', -1),
	(25, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:01:47', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:47', '2021-10-30 08:01:47', -1),
	(26, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:01:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:57', '2021-10-30 08:01:57', -1),
	(27, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:01:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:57', '2021-10-30 08:01:57', -1),
	(28, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:01:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:01:57', '2021-10-30 08:01:57', -1),
	(29, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:02:07', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:02:07', '2021-10-30 08:02:07', -1),
	(30, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:03:18', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:03:18', '2021-10-30 08:03:18', -1),
	(31, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:03:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:03:57', '2021-10-30 08:03:57', -1),
	(32, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:03:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:03:57', '2021-10-30 08:03:57', -1),
	(33, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:03:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:03:57', '2021-10-30 08:03:57', -1),
	(34, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:04:32', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:04:32', '2021-10-30 08:04:32', -1),
	(35, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:05:37', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:05:37', '2021-10-30 08:05:37', -1),
	(36, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:06:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:06:02', '2021-10-30 08:06:02', -1),
	(37, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:06:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:06:02', '2021-10-30 08:06:02', -1),
	(38, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:06:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:06:02', '2021-10-30 08:06:02', -1),
	(39, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:07:08', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:07:08', '2021-10-30 08:07:08', -1),
	(40, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:08:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:08:02', '2021-10-30 08:08:02', -1),
	(41, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:08:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:08:02', '2021-10-30 08:08:02', -1),
	(42, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:08:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:08:02', '2021-10-30 08:08:02', -1),
	(43, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:08:12', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:08:12', '2021-10-30 08:08:12', -1),
	(44, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:09:12', '11111', '2342342', 0, 3, -1, '2021-10-30 08:09:12', '2021-10-30 08:09:12', -1),
	(45, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:09:12', '11111', '2342342', 0, 3, -1, '2021-10-30 08:09:12', '2021-10-30 08:09:12', -1),
	(46, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:09:12', '11111', '2342342', 0, 3, -1, '2021-10-30 08:09:12', '2021-10-30 08:09:12', -1),
	(47, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:09:32', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:09:32', '2021-10-30 08:09:32', -1),
	(48, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:10:42', '11111', '2342342', 0, 3, -1, '2021-10-30 08:10:42', '2021-10-30 08:10:42', -1),
	(49, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:10:42', '11111', '2342342', 0, 3, -1, '2021-10-30 08:10:42', '2021-10-30 08:10:42', -1),
	(50, 'X7vt2vs2OjixSVS29R95', '2021-10-30 08:10:42', '11111', '2342342', 0, 3, -1, '2021-10-30 08:10:42', '2021-10-30 08:10:42', -1),
	(51, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:10:42', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:10:42', '2021-10-30 08:10:42', -1),
	(52, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:10:57', '11111', '2342342', 0, 3, -1, '2021-10-30 08:10:57', '2021-10-30 08:10:57', -1),
	(53, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:11:47', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:11:47', '2021-10-30 08:11:47', -1),
	(54, '0y5SEpasGakoZCZbXb9L', '2021-10-30 08:12:47', 'Cửa mở', 'Door', 1, 1, -1, '2021-10-30 08:12:47', '2021-10-30 08:12:47', -1),
	(55, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:13:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:13:02', '2021-10-30 08:13:02', -1),
	(56, 'ZCncAUhQPAhmp1mMnnUN', '2021-10-30 08:15:02', '11111', '2342342', 0, 3, -1, '2021-10-30 08:15:02', '2021-10-30 08:15:02', -1);
/*!40000 ALTER TABLE `tbl_alarm` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_group
CREATE TABLE IF NOT EXISTS `tbl_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `status` int(10) NOT NULL COMMENT '0: inactive, 1: active',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='nhom nguoi dung';

-- Dumping data for table charging-device.tbl_group: ~2 rows (approximately)
/*!40000 ALTER TABLE `tbl_group` DISABLE KEYS */;
INSERT INTO `tbl_group` (`id`, `name`, `status`, `created_user`, `created_date`, `modified_date`, `modified_user`) VALUES
	(1, 'sample group', 1, 1, NULL, '2020-03-21 07:34:08', 2),
	(2, 'nhóm 2', 0, 2, '2021-10-27 17:18:53', '2021-10-27 17:18:53', 2);
/*!40000 ALTER TABLE `tbl_group` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_group_role
CREATE TABLE IF NOT EXISTS `tbl_group_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `created_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `group_id` int(10) unsigned NOT NULL,
  `role_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_id_role_id` (`group_id`,`role_id`),
  KEY `FK_tbl_group_role_tbl_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Mapping between group and role. One group can has many roles';

-- Dumping data for table charging-device.tbl_group_role: ~4 rows (approximately)
/*!40000 ALTER TABLE `tbl_group_role` DISABLE KEYS */;
INSERT INTO `tbl_group_role` (`id`, `created_date`, `created_user`, `modified_date`, `modified_user`, `group_id`, `role_id`) VALUES
	(16, '2021-10-13 17:06:21', 2, '2021-10-13 17:06:21', 2, 3, 1),
	(17, '2021-10-13 17:07:57', 2, '2021-10-13 17:07:57', 2, 4, 1),
	(20, '2021-10-25 17:40:40', 2, '2021-10-25 17:40:40', 2, 1, 1),
	(21, '2021-10-27 17:18:53', 2, '2021-10-27 17:18:53', 2, 2, 2);
/*!40000 ALTER TABLE `tbl_group_role` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_parameter
CREATE TABLE IF NOT EXISTS `tbl_parameter` (
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Luu cac tham so cau hinh he thong';

-- Dumping data for table charging-device.tbl_parameter: ~4 rows (approximately)
/*!40000 ALTER TABLE `tbl_parameter` DISABLE KEYS */;
INSERT INTO `tbl_parameter` (`id`, `name`, `value`, `type`, `created_date`, `modified_date`, `modified_user`, `created_user`) VALUES
	(1, 'LAST_SYNC_DATE', '10/06/2018 17:52:09', 'java.util.Date', '2018-01-25 23:31:38', '2018-06-10 17:52:09', -1, -1),
	(2, 'PRICE_PER_HOUR', '234', 'java.lang.Long', NULL, '2019-12-22 14:07:42', NULL, NULL),
	(3, 'STATION_DASHBOARD', 'http://113.190.243.86:48080/monitor/d/SqHpHPd7k/dashboard-tram?orgId=1&refresh=30s&from=1635482807802&to=1635569207802&theme=dark&kiosk=tv', 'java.lang.String', '2020-08-24 15:34:53', NULL, NULL, NULL),
	(5, 'HOME_DASHBOARD', 'http://113.190.243.86:48080/monitor/d/rRCgN4K7k/dashboard-tong-quan?orgId=1&refresh=30s&from=1635569347481&to=1635569647481&theme=dark&kiosk=tv', 'java.lang.String', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `tbl_parameter` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_permission
CREATE TABLE IF NOT EXISTS `tbl_permission` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `resource` varchar(128) NOT NULL DEFAULT '*',
  `action` varchar(128) NOT NULL DEFAULT '*' COMMENT 'allowed actions on the resource. Can be multiple action separated by comma',
  `condition` varchar(255) DEFAULT NULL COMMENT 'Spring EL syntax to check for the permission. Action is allowed if condition is fulfilled',
  `allow` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0=false: deny action; 1=true: allow action',
  `description` varchar(255) DEFAULT NULL,
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `resource_action_instance` (`resource`,`action`,`condition`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Detail each permission\r\n';

-- Dumping data for table charging-device.tbl_permission: ~14 rows (approximately)
/*!40000 ALTER TABLE `tbl_permission` DISABLE KEYS */;
INSERT INTO `tbl_permission` (`id`, `resource`, `action`, `condition`, `allow`, `description`, `created_user`, `created_date`, `modified_user`, `modified_date`) VALUES
	(1, '*', '*', NULL, 1, NULL, 1, NULL, NULL, NULL),
	(13, 'groups', '*', NULL, 1, NULL, NULL, '2018-08-18 00:22:02', NULL, '2018-08-18 00:22:02'),
	(14, 'users', '*', NULL, 1, NULL, NULL, '2018-08-18 00:22:02', NULL, '2018-08-18 00:22:02'),
	(15, 'users', 'update', '#user.id==#resource.createdUser', 1, 'Cho phép update user do chính mình tạo', 1, NULL, NULL, NULL),
	(16, 'users', '*', 'aaaa', 1, NULL, 3, '2020-01-10 22:36:28', 3, '2020-01-10 22:36:28'),
	(19, 'groups', 'update', '#user.id!=#resource.createdUser', 0, 'Không cho update group không phải do mình tạo', 1, NULL, NULL, NULL),
	(25, '*', '*', '', 1, '', 2, '2020-03-21 17:08:05', 2, '2020-03-21 17:08:05'),
	(26, 'dashboard', 'createAssignment', '', 1, '', 2, '2020-03-22 05:07:51', 2, '2020-03-22 05:07:51'),
	(27, 'dashboard', 'updateAssignment', '', 1, '', 2, '2020-03-22 05:08:02', 2, '2020-03-22 05:08:02'),
	(28, 'dashboard', 'listAssignment', '', 1, '', 2, '2020-03-22 05:08:23', 2, '2020-03-22 05:08:23'),
	(29, 'user', '*', '#resource.type==1', 1, 'CRUD customer user', 2, '2020-03-22 05:10:40', 2, '2020-03-22 05:10:40'),
	(30, 'mydashboard', '*', '', 1, '', 2, '2020-03-22 05:19:19', 2, '2020-03-22 05:19:19'),
	(31, 'dashboard', 'list', '', 1, '', 2, '2020-03-23 17:17:22', 2, '2020-03-23 17:17:22'),
	(32, 'device', 'list', '', 1, '', 2, '2021-10-25 17:41:26', 2, '2021-10-25 17:41:26');
/*!40000 ALTER TABLE `tbl_permission` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_role
CREATE TABLE IF NOT EXISTS `tbl_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0: inactive; 1: active',
  `editable` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '0 (false): system role, can not edit.  1(true):  normal role, can edit',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Bảng các quyền';

-- Dumping data for table charging-device.tbl_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `tbl_role` DISABLE KEYS */;
INSERT INTO `tbl_role` (`id`, `name`, `description`, `status`, `editable`, `created_user`, `created_date`, `modified_user`, `modified_date`) VALUES
	(1, 'ROLE_ADMIN', 'Quyền quản trị cao nhất toàn bộ hệ thống', 1, 0, 1, NULL, NULL, NULL),
	(2, 'ROLE_OPERATOR', 'Can create customer user, assign dashboard to user', 1, 1, 2, '2018-01-02 15:50:32', 2, '2020-03-21 07:31:12');
/*!40000 ALTER TABLE `tbl_role` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_role_permission
CREATE TABLE IF NOT EXISTS `tbl_role_permission` (
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Mapping between user & group. One user can belong to many group';

-- Dumping data for table charging-device.tbl_role_permission: ~8 rows (approximately)
/*!40000 ALTER TABLE `tbl_role_permission` DISABLE KEYS */;
INSERT INTO `tbl_role_permission` (`id`, `created_date`, `created_user`, `modified_date`, `modified_user`, `role_id`, `permission_id`) VALUES
	(1, NULL, NULL, NULL, NULL, 1, 1),
	(6, '2020-03-22 05:07:51', 2, '2020-03-22 05:07:51', 2, 2, 26),
	(7, '2020-03-22 05:08:02', 2, '2020-03-22 05:08:02', 2, 2, 27),
	(8, '2020-03-22 05:08:23', 2, '2020-03-22 05:08:23', 2, 2, 28),
	(9, '2020-03-22 05:10:40', 2, '2020-03-22 05:10:40', 2, 2, 29),
	(10, '2020-03-22 05:19:19', 2, '2020-03-22 05:19:19', 2, 2, 30),
	(11, '2020-03-23 17:17:22', 2, '2020-03-23 17:17:22', 2, 2, 31),
	(12, '2021-10-25 17:41:26', 2, '2021-10-25 17:41:26', 2, 2, 32);
/*!40000 ALTER TABLE `tbl_role_permission` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_role_tree
CREATE TABLE IF NOT EXISTS `tbl_role_tree` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `created_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `child_id` int(10) unsigned NOT NULL COMMENT 'ID of child role',
  `parent_id` int(10) unsigned NOT NULL COMMENT 'ID of parent role',
  PRIMARY KEY (`id`),
  UNIQUE KEY `parent_id_child_id` (`child_id`,`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Represent role hierachy.\r\nOne role can have many child roles. \r\nOne role can have many parent roles.\r\nChild role  will inherit all priviledges from parent roles';

-- Dumping data for table charging-device.tbl_role_tree: ~0 rows (approximately)
/*!40000 ALTER TABLE `tbl_role_tree` DISABLE KEYS */;
/*!40000 ALTER TABLE `tbl_role_tree` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_rule
CREATE TABLE IF NOT EXISTS `tbl_rule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `condition` varchar(1024) DEFAULT NULL COMMENT 'Condition expression',
  `action_type` varchar(50) DEFAULT NULL COMMENT 'Create alarm, Send chat, run command, call url....',
  `action` varchar(1024) DEFAULT NULL COMMENT 'Metadata to describe action: could be json, etc...',
  `active_time` varchar(1024) DEFAULT NULL COMMENT 'reserved:  array of time schedule to active rule',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `scope` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1: global (all devices), 0: per device, 2: per city?',
  `min_interval` int(11) NOT NULL DEFAULT '0' COMMENT 'Min interval between 2 action trigger (in seconds)',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Alarm rule';

-- Dumping data for table charging-device.tbl_rule: ~3 rows (approximately)
/*!40000 ALTER TABLE `tbl_rule` DISABLE KEYS */;
INSERT INTO `tbl_rule` (`id`, `name`, `condition`, `action_type`, `action`, `active_time`, `status`, `scope`, `min_interval`, `created_user`, `created_date`, `modified_date`, `modified_user`) VALUES
	(1, 'rule da', '#door2 != 1', 'CreateAlarm', '{"alarmType":"Door","alarmContent":"Cửa mở","alarmLevel":"Warning"}', NULL, 0, 0, 60, 2, '2021-10-27 17:06:11', '2021-10-30 08:13:35', 2),
	(2, 'nhiet do', '#temp1 > 0 and #temp2 > 2', 'CreateAlarm', '{"alarmType":"Temp","alarmContent":"Nhiệt độ quá cao","alarmLevel":"Warning"}', NULL, 0, 1, 0, 2, '2021-10-27 17:11:22', '2021-10-30 07:33:51', 2),
	(3, 'Sự kiên trong ngày', '#eventTime < (new java.util.Date().time - 86400)', 'CreateAlarm', '{"alarmType":"2342342","alarmContent":"11111","alarmLevel":"Info"}', NULL, 0, 0, 120, 2, '2021-10-27 17:23:44', '2021-10-30 08:15:37', 2);
/*!40000 ALTER TABLE `tbl_rule` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_rule_station
CREATE TABLE IF NOT EXISTS `tbl_rule_station` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `created_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `rule_id` int(10) unsigned NOT NULL,
  `station_id` int(10) unsigned NOT NULL,
  `device_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Showing which device/device will apply this rule';

-- Dumping data for table charging-device.tbl_rule_station: ~2 rows (approximately)
/*!40000 ALTER TABLE `tbl_rule_station` DISABLE KEYS */;
INSERT INTO `tbl_rule_station` (`id`, `created_date`, `created_user`, `modified_date`, `modified_user`, `rule_id`, `station_id`, `device_id`) VALUES
	(10, '2021-10-30 08:13:35', 2, '2021-10-30 08:13:35', 2, 1, 1, '0y5SEpasGakoZCZbXb9L'),
	(11, '2021-10-30 08:15:37', 2, '2021-10-30 08:15:37', 2, 3, 3, 'ZCncAUhQPAhmp1mMnnUN');
/*!40000 ALTER TABLE `tbl_rule_station` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_station
CREATE TABLE IF NOT EXISTS `tbl_station` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT '',
  `location` varchar(255) DEFAULT '',
  `street` varchar(255) DEFAULT NULL,
  `district` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `device_id` varchar(255) DEFAULT NULL COMMENT 'IoT Device ID',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0: inactive; 1: active',
  `group` varchar(255) DEFAULT NULL COMMENT 'Nhóm trạm. Để đơn giản mỗi trạm chỉ 1 nhóm',
  `lon` float DEFAULT NULL COMMENT 'Kinh do',
  `lat` float DEFAULT NULL COMMENT 'Vi do',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`name`),
  UNIQUE KEY `device_id` (`device_id`),
  KEY `USER_NAME` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='Trạm sạc. Mỗi trạm sạc tương ứng 1 device ID (1 thiết bị gateway duy nhất)\r\n\r\n';

-- Dumping data for table charging-device.tbl_station: ~5 rows (approximately)
/*!40000 ALTER TABLE `tbl_station` DISABLE KEYS */;
INSERT INTO `tbl_station` (`id`, `name`, `description`, `location`, `street`, `district`, `city`, `device_id`, `status`, `group`, `lon`, `lat`, `created_user`, `created_date`, `modified_user`, `modified_date`) VALUES
	(1, 'trạm 1', 'aaaa', 'Hồ Gươm', 'Xã Đàn', 'Đống Đa', 'Hà Nội', '0y5SEpasGakoZCZbXb9L', 1, 'Bosch', 105.833, 21.0219, NULL, NULL, 2, '2021-10-25 17:06:23'),
	(2, 'trạm 2', '', 'Ngã tư sở', 'Đại La', 'Hoàng Mai', 'Hà Nội', 'X7vt2vs2OjixSVS29R95', 0, '', 105.828, 21.0194, NULL, NULL, 2, '2021-10-26 14:28:51'),
	(3, 'Trạm 3', '', '30 Láng Hạ', 'Láng Hạ', 'Đống Đa', 'Hà Nội', 'ZCncAUhQPAhmp1mMnnUN', 0, '', 105.804, 21.0204, NULL, NULL, 2, '2021-10-25 17:52:12'),
	(4, 'trạm 4', '', '25 Xã Đàn', 'Xã Đàn', 'Đống Đa', 'Hà Nội', 'tram4-hanoi', 0, NULL, 1, 1, NULL, NULL, 2, '2021-10-23 05:00:49'),
	(5, 'trạm 5', '', 'Ngã tư Phạm Văn Đồng - Hoàng Quốc Việt', 'Hoàng Quốc Việt', 'Cầu Giấy', 'Hà Nội', '', 0, '', 1, 1, -1, '2020-06-09 07:04:03', 2, '2021-10-25 15:43:46');
/*!40000 ALTER TABLE `tbl_station` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_station_status
CREATE TABLE IF NOT EXISTS `tbl_station_status` (
  `device_id` varchar(255) NOT NULL,
  `telemetry` varchar(1023) NOT NULL DEFAULT '' COMMENT 'latest telemetry record',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT 'Trạng thái hoạt động chung của trạm. 1 là OK. Khác là mã lỗi',
  `event_time` datetime DEFAULT NULL COMMENT 'latest event time',
  UNIQUE KEY `device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Lưu trang thái cập nhật latest của device. Update mỗi khi nhận được message từ device.\r\nỞ pha sau sẽ chuyển sang redis để tăng hiệu năng write\r\n\r\n';

-- Dumping data for table charging-device.tbl_station_status: ~4 rows (approximately)
/*!40000 ALTER TABLE `tbl_station_status` DISABLE KEYS */;
INSERT INTO `tbl_station_status` (`device_id`, `telemetry`, `status`, `event_time`) VALUES
	('0y5SEpasGakoZCZbXb9L', '{\n	"UPSBatCap" : 6,\n	"UPSBatTemp" : 306,\n	"UPSBatVol" : 120,\n	"UPSCurrentLoad" : 101,\n	"UPSInVol" : 1966,\n	"UPSMaxOut" : 2250,\n	"UPSMinOut" : 1559,\n	"UPSOutVol" : 2099,\n	"UPSStatus" : 0,\n	"UPSTemp" : 334,\n	"deviceId" : "0y5SEpasGakoZCZbXb9L",\n	"door1" : 1,\n	"door2" : 0,\n	"eventTime" : 1635582243,\n	"hum1" : 86,\n	"hum2" : 78,\n	"smoke1" : 0,\n	"temp1" : 21,\n	"temp2" : 24,\n	"water1" : 0\n}', 0, '2021-10-30 08:24:02'),
	('tram4-hanoi', '{\r\n"UPSBatCap": 18,\r\n"UPSBatTemp": 335,\r\n"UPSBatVol": 122,\r\n"UPSCurrentLoad": 97,\r\n"UPSInVol": 1898,\r\n"UPSMaxOut": 2283,\r\n"UPSMinOut": 1765,\r\n"UPSOutVol": 1935,\r\n"UPSBatTemp": 318,\r\n"UPSBatVol": 123,\r\n"UPSCurrentLoad": 128,\r\n"UPSInVol": 1819,\r\n"UPSMaxOut": 2289,\r\n"UPSMinOut": 1581,\r\n"UPSOutVol": 2172,\r\n"UPSStatus": 1,\r\n"UPSTemp": 338,\r\n"door1": 1,\r\n"door2": 1,\r\n"hum1": 43,\r\n"hum2": 75,\r\n"smoke1": 0,\r\n"temp1": 15,\r\n"temp2": 18,\r\n"water1": 1,\r\n"UPSTemp": 325,\r\n"door1": 0,\r\n"door2": 0,\r\n"hum1": 48,\r\n"hum2": 66,\r\n"smoke1": 1,\r\n"temp1": 21,\r\n"temp2": 23,\r\n"water1": 0\r\n}', 1, '2021-10-25 22:23:20'),
	('X7vt2vs2OjixSVS29R95', '{\n	"UPSBatCap" : 4,\n	"UPSBatTemp" : 315,\n	"UPSBatVol" : 123,\n	"UPSCurrentLoad" : 56,\n	"UPSInVol" : 1822,\n	"UPSMaxOut" : 2439,\n	"UPSMinOut" : 1544,\n	"UPSOutVol" : 1822,\n	"UPSStatus" : 1,\n	"UPSTemp" : 332,\n	"deviceId" : "X7vt2vs2OjixSVS29R95",\n	"door1" : 0,\n	"door2" : 1,\n	"eventTime" : 1635582243,\n	"hum1" : 68,\n	"hum2" : 46,\n	"smoke1" : 0,\n	"temp1" : 15,\n	"temp2" : 21,\n	"water1" : 1\n}', 1, '2021-10-30 08:24:02'),
	('ZCncAUhQPAhmp1mMnnUN', '{\n	"UPSBatCap" : 15,\n	"UPSBatTemp" : 309,\n	"UPSBatVol" : 122,\n	"UPSCurrentLoad" : 138,\n	"UPSInVol" : 1856,\n	"UPSMaxOut" : 2499,\n	"UPSMinOut" : 1690,\n	"UPSOutVol" : 2044,\n	"UPSStatus" : 0,\n	"UPSTemp" : 325,\n	"deviceId" : "ZCncAUhQPAhmp1mMnnUN",\n	"door1" : 1,\n	"door2" : 1,\n	"eventTime" : 1635582243,\n	"hum1" : 58,\n	"hum2" : 63,\n	"smoke1" : 0,\n	"temp1" : 20,\n	"temp2" : 23,\n	"water1" : 0\n}', 0, '2021-10-30 08:24:02');
/*!40000 ALTER TABLE `tbl_station_status` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_user
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  `full_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0: inactive; 1: active',
  `type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0: internal, 1: customer',
  `created_user` int(10) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `USER_NAME` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- Dumping data for table charging-device.tbl_user: ~5 rows (approximately)
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `status`, `type`, `created_user`, `created_date`, `modified_user`, `modified_date`) VALUES
	(2, 'admin', '$2a$10$KPD8a1Xf2Ycxrejs0iQQZOSbqqh9db6uSLVr2G0l.76B.LgCGG2ja', '', 'anhpd@vivas.vn', NULL, 1, 0, NULL, '2017-12-28 17:47:45', 2, '2020-03-26 07:25:51'),
	(3, 'anhpd', '$2a$12$Jr/6.J3kcLHKlBLPkseBPey1c7/ONExhZbPD3VWpvXOkIUDfYPg72', 'Phạm Duy Anh  2', 'anhpd@vivas.vn', NULL, 1, 1, 2, '2017-12-28 23:41:46', 2, '2021-10-25 17:11:53'),
	(10, 'guest', '$2a$12$yudRJtuOMHTYZc4z9eWxIOdkyPL48dhRAU4LZprwrFvINsFF6xuTC', 'khách hàng xịn', 'guest@1234.vn', NULL, 1, 1, 10, '2018-01-05 16:26:23', 17, '2020-03-24 01:24:41'),
	(17, 'cskh', '$2a$10$HXLcWcdKWyHMJN9TWNnmWOFZ4PhcG5A2XyQnUxAHHQ3nJwY3G3yXm', '', '', NULL, 1, 0, 2, '2020-03-21 15:55:14', 2, '2020-03-21 15:55:14'),
	(21, 'aaaa', '$2a$10$ZWc/wOs.LMCKJHwO5d0EnOPPSEq9gQAIvO6ZSE3rgqDK5xAf7jWce', 'adsfa', '', NULL, 0, 0, 2, '2020-03-26 07:29:34', 2, '2021-10-25 17:40:14');
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_user_group
CREATE TABLE IF NOT EXISTS `tbl_user_group` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `created_user` int(10) DEFAULT NULL,
  `modified_date` datetime DEFAULT NULL,
  `modified_user` int(10) DEFAULT NULL,
  `user_id` int(10) unsigned NOT NULL,
  `group_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `USER_ID_GROUP_ID` (`user_id`,`group_id`),
  KEY `FK_tbl_user_group_tbl_group` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='Mapping between user & group. One user can belong to many group';

-- Dumping data for table charging-device.tbl_user_group: ~2 rows (approximately)
/*!40000 ALTER TABLE `tbl_user_group` DISABLE KEYS */;
INSERT INTO `tbl_user_group` (`id`, `created_date`, `created_user`, `modified_date`, `modified_user`, `user_id`, `group_id`) VALUES
	(14, '2021-10-13 17:06:21', 2, '2021-10-13 17:06:21', 2, 2, 3),
	(18, '2021-10-25 17:41:00', 2, '2021-10-25 17:41:00', 2, 2, 1);
/*!40000 ALTER TABLE `tbl_user_group` ENABLE KEYS */;

-- Dumping structure for table charging-device.tbl_user_role
CREATE TABLE IF NOT EXISTS `tbl_user_role` (
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Mapping between user and role. One user can has many roles. Many users may have the same role.';

-- Dumping data for table charging-device.tbl_user_role: ~2 rows (approximately)
/*!40000 ALTER TABLE `tbl_user_role` DISABLE KEYS */;
INSERT INTO `tbl_user_role` (`id`, `created_date`, `created_user`, `modified_date`, `modified_user`, `user_id`, `role_id`) VALUES
	(9, '2018-08-18 00:11:19', 2, '2018-08-18 00:11:19', 2, 2, 1),
	(11, '2020-03-21 15:55:14', 2, '2020-03-21 15:55:14', 2, 17, 2);
/*!40000 ALTER TABLE `tbl_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
