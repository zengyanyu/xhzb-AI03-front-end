-- --------------------------------------------------------
-- 主机:                           192.168.100.168
-- 服务器版本:                        8.0.29 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 xhzb 的数据库结构
CREATE DATABASE IF NOT EXISTS `xhzb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `xhzb`;

-- 导出  表 xhzb.alert_data 结构
CREATE TABLE IF NOT EXISTS `alert_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `iot_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '物联网设备id',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
  `product_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属产品key',
  `product_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
  `function_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '功能标识符',
  `access_location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接入位置',
  `location_type` int DEFAULT NULL COMMENT '位置类型 0：随身设备 1：固定设备',
  `physical_location_type` int DEFAULT NULL COMMENT '物理位置类型 0楼层 1房间 2床位',
  `device_description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '位置备注',
  `data_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据值',
  `alert_rule_id` bigint NOT NULL COMMENT '报警规则id',
  `alert_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '报警原因，格式：功能名称+运算符+阈值+持续周期+聚合周期',
  `processing_result` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理结果',
  `processor_id` bigint DEFAULT NULL COMMENT '处理人id',
  `processor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理人名称',
  `processing_time` datetime DEFAULT NULL COMMENT '处理时间',
  `type` int NOT NULL COMMENT '报警数据类型，0：老人异常数据，1：设备异常数据',
  `status` int NOT NULL COMMENT '状态，0：待处理，1：已处理',
  `user_id` bigint DEFAULT '0' COMMENT '接收人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='报警数据';

-- 正在导出表  xhzb.alert_data 的数据：~0 rows (大约)
DELETE FROM `alert_data`;
/*!40000 ALTER TABLE `alert_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_data` ENABLE KEYS */;

-- 导出  表 xhzb.alert_rule 结构
CREATE TABLE IF NOT EXISTS `alert_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属产品的key',
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `module_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模块的key',
  `module_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模块名称',
  `function_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能名称',
  `function_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能标识',
  `iot_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物联网设备id',
  `device_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备名称',
  `alert_data_type` int DEFAULT NULL COMMENT '报警数据类型，0：老人异常数据，1：设备异常数据',
  `alert_rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '告警规则名称',
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运算符',
  `value` float DEFAULT NULL COMMENT '阈值',
  `duration` int DEFAULT NULL COMMENT '持续周期',
  `alert_effective_period` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报警生效时段',
  `alert_silent_period` int DEFAULT NULL COMMENT '报警沉默周期',
  `status` int DEFAULT NULL COMMENT '0 禁用 1启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  xhzb.alert_rule 的数据：~0 rows (大约)
DELETE FROM `alert_rule`;
/*!40000 ALTER TABLE `alert_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_rule` ENABLE KEYS */;

-- 导出  表 xhzb.bed 结构
CREATE TABLE IF NOT EXISTS `bed` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '床位ID',
  `bed_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '床位编号',
  `bed_status` int DEFAULT NULL COMMENT '床位状态: 未入住0, 已入住1 ',
  `sort` int DEFAULT NULL COMMENT '床位号',
  `room_id` bigint DEFAULT NULL COMMENT '房间ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `bed_number` (`bed_number`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='床位表';

-- 正在导出表  xhzb.bed 的数据：~87 rows (大约)
DELETE FROM `bed`;
/*!40000 ALTER TABLE `bed` DISABLE KEYS */;
INSERT INTO `bed` (`id`, `bed_number`, `bed_status`, `sort`, `room_id`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(1, '101-1', 0, 1, 1, '2023-09-26 17:39:53', '2025-04-20 17:00:53', 1671403256519078138, 1, NULL),
	(2, '102-1', 0, 1, 2, '2023-09-26 17:40:01', '2023-10-05 16:00:05', 1671403256519078138, 1671403256519078164, NULL),
	(3, '102-2', 0, 2, 2, '2023-09-26 17:40:09', '2023-10-05 15:59:45', 1671403256519078138, 1671403256519078164, NULL),
	(4, '103-1', 0, 1, 3, '2023-09-26 17:40:42', '2023-10-05 16:00:21', 1671403256519078138, 1671403256519078164, NULL),
	(5, '104-1', 0, 1, 4, '2023-09-26 17:40:49', '2023-09-26 17:45:39', 1671403256519078138, 1671403256519078138, NULL),
	(6, '104-2', 0, 2, 4, '2023-09-26 17:40:54', '2023-10-20 23:22:12', 1671403256519078138, 1671403256519078164, NULL),
	(7, '105-1', 0, 1, 5, '2023-09-26 17:41:09', '2023-09-26 17:45:52', 1671403256519078138, 1671403256519078138, NULL),
	(8, '106-1', 0, 1, 6, '2023-09-26 17:41:16', '2023-09-26 17:45:58', 1671403256519078138, 1671403256519078138, NULL),
	(9, '106-2', 0, 2, 6, '2023-09-26 17:41:24', '2023-09-26 17:46:04', 1671403256519078138, 1671403256519078138, NULL),
	(10, '107-1', 0, 1, 7, '2023-09-26 17:41:32', '2023-12-21 09:37:49', 1671403256519078138, 1671403256519078138, NULL),
	(11, '201-1', 0, 1, 8, '2023-09-26 17:44:53', '2025-04-20 17:07:59', 1671403256519078138, 1, NULL),
	(12, '202-1', 0, 1, 9, '2023-09-26 17:46:33', '2023-09-26 17:46:33', 1671403256519078138, 1, NULL),
	(13, '202-2', 0, 2, 9, '2023-09-26 17:46:47', '2023-09-26 17:46:47', 1671403256519078138, 1, NULL),
	(14, '203-1', 0, 1, 10, '2023-09-26 18:43:58', '2023-09-26 18:43:58', 1671403256519078138, 1, NULL),
	(15, '204-1', 0, 1, 11, '2023-09-26 18:44:03', '2023-09-26 18:44:03', 1671403256519078138, 1, NULL),
	(16, '204-2', 0, 2, 11, '2023-09-26 18:44:12', '2023-09-26 18:44:12', 1671403256519078138, 1, NULL),
	(18, '205-1', 0, 1, 12, '2023-09-26 18:44:23', '2023-12-20 18:40:07', 1671403256519078138, 1671403256519078138, NULL),
	(19, '206-1', 0, 1, 13, '2023-09-26 18:44:36', '2023-12-20 21:43:10', 1671403256519078138, 1671403256519078138, NULL),
	(20, '206-2', 0, 2, 13, '2023-09-26 18:44:42', '2023-09-26 18:44:48', 1671403256519078138, 1671403256519078138, NULL),
	(21, '207-1', 0, 1, 14, '2023-09-26 18:45:01', '2023-09-26 18:45:01', 1671403256519078138, NULL, NULL),
	(22, '301-2', 0, 1, 15, '2023-09-26 18:45:26', '2023-12-26 19:35:06', 1671403256519078138, 1671403256519078138, NULL),
	(23, '302-1', 0, 1, 16, '2023-09-26 18:45:31', '2023-09-26 18:45:31', 1671403256519078138, NULL, NULL),
	(24, '302-3', 0, 2, 16, '2023-09-26 18:45:39', '2023-12-26 19:35:15', 1671403256519078138, 1671403256519078138, NULL),
	(25, '303-1', 0, 1, 17, '2023-09-26 18:45:44', '2023-09-26 18:45:44', 1671403256519078138, NULL, NULL),
	(27, '304-1', 0, 1, 18, '2023-09-26 18:45:55', '2023-09-26 18:45:55', 1671403256519078138, NULL, NULL),
	(28, '304-2', 0, 2, 18, '2023-09-26 18:46:04', '2023-09-26 18:46:04', 1671403256519078138, 1, NULL),
	(29, '305-1', 0, 1, 19, '2023-09-26 18:46:11', '2023-12-21 10:04:47', 1671403256519078138, 1671403256519078138, NULL),
	(30, '306-1', 0, 1, 20, '2023-09-26 18:46:16', '2023-09-26 18:46:16', 1671403256519078138, NULL, NULL),
	(31, '306-2', 0, 2, 20, '2023-09-26 18:46:22', '2023-09-26 19:08:50', 1671403256519078138, 1671403256519078138, NULL),
	(32, '307-1', 0, 1, 21, '2023-09-26 18:46:29', '2023-09-26 18:46:29', 1671403256519078138, NULL, NULL),
	(33, '401-1', 0, 1, 22, '2023-09-26 18:52:14', '2023-09-26 18:52:14', 1671403256519078138, NULL, NULL),
	(34, '402-1', 0, 1, 23, '2023-09-26 18:52:22', '2023-09-26 18:52:22', 1671403256519078138, NULL, NULL),
	(35, '402-2', 0, 2, 23, '2023-09-26 18:52:35', '2023-09-26 18:52:38', 1671403256519078138, 1671403256519078138, NULL),
	(36, '403-1', 0, 1, 24, '2023-09-26 18:52:47', '2023-09-26 18:52:47', 1671403256519078138, 1, NULL),
	(37, '404-1', 0, 1, 25, '2023-09-26 18:52:54', '2023-09-26 18:52:54', 1671403256519078138, NULL, NULL),
	(38, '404-2', 0, 2, 25, '2023-09-26 18:53:02', '2023-09-26 18:53:10', 1671403256519078138, 1671403256519078138, NULL),
	(39, '405-1', 0, 1, 26, '2023-09-26 18:53:18', '2023-09-26 18:53:18', 1671403256519078138, NULL, NULL),
	(40, '406-1', 0, 1, 27, '2023-09-26 18:53:27', '2023-09-26 18:53:27', 1671403256519078138, NULL, NULL),
	(41, '406-2', 0, 2, 27, '2023-09-26 18:53:36', '2023-09-26 18:53:36', 1671403256519078138, NULL, NULL),
	(42, '407-1', 0, 1, 28, '2023-09-26 18:53:44', '2023-09-26 18:53:44', 1671403256519078138, NULL, NULL),
	(43, '501-1', 0, 1, 29, '2023-09-26 18:55:47', '2023-09-26 18:55:47', 1671403256519078138, NULL, NULL),
	(44, '502-1', 0, 1, 31, '2023-09-26 18:55:52', '2023-09-26 18:55:52', 1671403256519078138, NULL, NULL),
	(45, '502-2', 0, 2, 31, '2023-09-26 18:56:02', '2023-09-26 18:56:02', 1671403256519078138, NULL, NULL),
	(46, '503-1', 0, 1, 32, '2023-09-26 18:56:10', '2023-09-26 18:56:10', 1671403256519078138, NULL, NULL),
	(48, '504-1', 0, 1, 33, '2023-09-26 18:56:26', '2023-09-26 18:56:26', 1671403256519078138, NULL, NULL),
	(49, '504-2', 0, 2, 33, '2023-09-26 18:56:32', '2023-09-26 18:56:32', 1671403256519078138, NULL, NULL),
	(50, '505-1', 0, 1, 34, '2023-09-26 18:56:37', '2023-09-26 18:56:37', 1671403256519078138, NULL, NULL),
	(52, '506-1', 0, 1, 35, '2023-09-26 18:56:49', '2023-09-26 18:56:49', 1671403256519078138, NULL, NULL),
	(53, '506-2', 0, 2, 35, '2023-09-26 18:56:54', '2023-09-26 18:56:54', 1671403256519078138, NULL, NULL),
	(54, '507-1', 0, 1, 36, '2023-09-26 18:57:00', '2023-09-26 18:57:00', 1671403256519078138, NULL, NULL),
	(55, '601-1', 0, 1, 37, '2023-09-26 19:05:11', '2023-09-28 22:53:28', 1671403256519078138, 1671403256519078164, NULL),
	(56, '602-1', 0, 1, 38, '2023-09-26 19:05:16', '2023-09-26 19:05:16', 1671403256519078138, NULL, NULL),
	(57, '602-2', 0, 2, 38, '2023-09-26 19:05:24', '2023-09-26 19:05:24', 1671403256519078138, NULL, NULL),
	(58, '603-1', 0, 1, 39, '2023-09-26 19:05:29', '2023-09-26 19:05:29', 1671403256519078138, NULL, NULL),
	(59, '604-1', 0, 1, 40, '2023-09-26 19:05:33', '2023-09-26 19:05:33', 1671403256519078138, NULL, NULL),
	(60, '604-2', 0, 2, 40, '2023-09-26 19:05:38', '2023-09-26 19:05:38', 1671403256519078138, NULL, NULL),
	(61, '605-1', 0, 1, 41, '2023-09-26 19:05:43', '2023-09-26 19:05:43', 1671403256519078138, NULL, NULL),
	(62, '606-1', 0, 1, 42, '2023-09-26 19:05:48', '2023-09-26 19:05:48', 1671403256519078138, NULL, NULL),
	(63, '606-2', 0, 2, 42, '2023-09-26 19:05:54', '2023-09-26 19:05:54', 1671403256519078138, NULL, NULL),
	(64, '607-1', 0, 1, 43, '2023-09-26 19:05:59', '2023-09-26 19:05:59', 1671403256519078138, NULL, NULL),
	(65, '701-1', 0, 1, 44, '2023-09-26 19:06:10', '2023-09-26 19:06:10', 1671403256519078138, NULL, NULL),
	(66, '702-1', 0, 1, 45, '2023-09-26 19:06:14', '2023-09-26 19:06:26', 1671403256519078138, 1671403256519078138, NULL),
	(68, '702-2', 0, 2, 45, '2023-09-26 19:06:35', '2023-09-26 19:06:35', 1671403256519078138, NULL, NULL),
	(69, '703-1', 0, 1, 46, '2023-09-26 19:06:41', '2023-09-26 19:06:41', 1671403256519078138, NULL, NULL),
	(70, '704-1', 0, 1, 47, '2023-09-26 19:06:46', '2023-12-20 14:28:22', 1671403256519078138, 1671403256519078138, NULL),
	(71, '704-2', 0, 2, 47, '2023-09-26 19:06:52', '2023-09-26 19:06:57', 1671403256519078138, 1671403256519078138, NULL),
	(72, '705-1', 0, 1, 48, '2023-09-26 19:07:04', '2023-09-26 19:07:04', 1671403256519078138, NULL, NULL),
	(73, '706-1', 0, 1, 49, '2023-09-26 19:07:10', '2023-09-26 19:07:10', 1671403256519078138, NULL, NULL),
	(74, '706-2', 0, 2, 49, '2023-09-26 19:07:14', '2023-09-26 19:07:19', 1671403256519078138, 1671403256519078138, NULL),
	(75, '707-1', 0, 1, 50, '2023-09-26 19:07:25', '2023-09-26 19:07:25', 1671403256519078138, NULL, NULL),
	(76, '801-1', 0, 1, 51, '2023-09-26 19:07:41', '2023-09-26 19:07:41', 1671403256519078138, NULL, NULL),
	(77, '803-1', 0, 1, 53, '2023-09-26 19:07:46', '2023-09-26 19:07:46', 1671403256519078138, NULL, NULL),
	(78, '805-1', 0, 1, 55, '2023-09-26 19:07:51', '2023-09-26 19:07:51', 1671403256519078138, NULL, NULL),
	(79, '807-1', 0, 1, 57, '2023-09-26 19:07:56', '2023-09-26 19:07:56', 1671403256519078138, 1, NULL),
	(80, '802-1', 0, 1, 52, '2023-09-26 19:08:04', '2023-09-26 19:08:04', 1671403256519078138, NULL, NULL),
	(81, '801-2', 0, 2, 52, '2023-09-26 19:08:09', '2023-09-26 19:08:09', 1671403256519078138, NULL, NULL),
	(82, '804-1', 0, 1, 54, '2023-09-26 19:08:15', '2023-09-26 19:08:15', 1671403256519078138, NULL, NULL),
	(83, '804-2', 0, 2, 54, '2023-09-26 19:08:22', '2023-09-26 19:08:22', 1671403256519078138, NULL, NULL),
	(84, '806-1', 0, 1, 56, '2023-09-26 19:08:28', '2023-09-26 19:08:28', 1671403256519078138, NULL, NULL),
	(85, '806-2', 0, 2, 56, '2023-09-26 19:08:35', '2023-09-26 19:08:35', 1671403256519078138, 1, NULL),
	(170, '101-2', 1, 2, 1, '2023-12-21 11:45:09', '2025-04-20 17:13:18', 1671403256519078138, 1, NULL),
	(171, '103-2', 0, 1, 3, '2023-12-23 16:12:34', '2023-12-23 16:22:03', 1671403256519078138, 1671403256519078138, NULL),
	(177, '1011', 0, 1, 74, '2023-12-26 19:32:07', '2023-12-26 19:32:07', 1671403256519078138, NULL, NULL),
	(178, '101', 0, 1, 74, '2023-12-26 19:32:15', '2023-12-26 19:32:15', 1671403256519078138, NULL, NULL),
	(200, '107-2', 0, 1, 7, '2026-03-09 15:34:44', '2026-03-17 10:30:44', 1, 1, NULL),
	(210, '901-1', NULL, 1, 143, '2026-04-02 08:53:01', NULL, 1, NULL, NULL),
	(211, '901-2', NULL, 1, 143, '2026-04-02 08:53:07', NULL, 1, NULL, NULL);
/*!40000 ALTER TABLE `bed` ENABLE KEYS */;

-- 导出  表 xhzb.check_in 结构
CREATE TABLE IF NOT EXISTS `check_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '老人姓名',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `id_card_no` char(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号',
  `start_date` date NOT NULL COMMENT '入住开始时间',
  `end_date` date DEFAULT NULL COMMENT '入住结束时间',
  `nursing_level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '护理等级名称',
  `bed_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '入住床位',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 (0: 已入住, 1: 已退住)',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序编号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  xhzb.check_in 的数据：~4 rows (大约)
DELETE FROM `check_in`;
/*!40000 ALTER TABLE `check_in` DISABLE KEYS */;
/*!40000 ALTER TABLE `check_in` ENABLE KEYS */;

-- 导出  表 xhzb.check_in_config 结构
CREATE TABLE IF NOT EXISTS `check_in_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `check_in_id` bigint NOT NULL COMMENT '入住表ID',
  `nursing_level_id` bigint NOT NULL COMMENT '护理等级ID',
  `nursing_level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '护理等级名称',
  `fee_start_date` date NOT NULL COMMENT '费用开始时间',
  `fee_end_date` date DEFAULT NULL COMMENT '费用结束时间',
  `deposit` decimal(10,2) NOT NULL COMMENT '押金（元）',
  `nursing_fee` decimal(10,2) NOT NULL COMMENT '护理费用（元/月）',
  `bed_fee` decimal(10,2) NOT NULL COMMENT '床位费用（元/月）',
  `insurance_payment` decimal(10,2) NOT NULL COMMENT '医保支付（元/月）',
  `government_subsidy` decimal(10,2) NOT NULL COMMENT '政府补贴（元/月）',
  `other_fees` decimal(10,2) NOT NULL COMMENT '其他费用（元/月）',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序编号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  xhzb.check_in_config 的数据：~4 rows (大约)
DELETE FROM `check_in_config`;
/*!40000 ALTER TABLE `check_in_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `check_in_config` ENABLE KEYS */;

-- 导出  表 xhzb.contract 结构
CREATE TABLE IF NOT EXISTS `contract` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `elder_id` int NOT NULL COMMENT '老人ID',
  `contract_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '合同名称',
  `contract_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '合同编号',
  `agreement_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '协议地址（文件路径或URL）',
  `third_party_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '丙方手机号',
  `third_party_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '丙方姓名',
  `elder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '老人姓名',
  `start_date` date NOT NULL COMMENT '开始时间',
  `end_date` date NOT NULL COMMENT '结束时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 (0: 未生效, 1: 已生效, 2: 已过期, 3: 已失效)',
  `sign_date` date NOT NULL COMMENT '签约日期',
  `termination_submitter` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '解除提交人',
  `termination_date` date DEFAULT NULL COMMENT '解除日期',
  `termination_agreement_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '解除协议地址（文件路径或URL）',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序编号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在导出表  xhzb.contract 的数据：~4 rows (大约)
DELETE FROM `contract`;
/*!40000 ALTER TABLE `contract` DISABLE KEYS */;
/*!40000 ALTER TABLE `contract` ENABLE KEYS */;

-- 导出  表 xhzb.device 结构
CREATE TABLE IF NOT EXISTS `device` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `iot_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '物联网设备ID',
  `secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备秘钥',
  `binding_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '绑定位置',
  `location_type` int DEFAULT NULL COMMENT '位置类型 0：随身设备 1：固定设备',
  `physical_location_type` int DEFAULT NULL COMMENT '物理位置类型 0楼层 1房间 2床位',
  `device_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备名称',
  `product_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品key',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `device_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '位置备注',
  `have_entrance_guard` int NOT NULL DEFAULT '0' COMMENT '产品是否包含门禁，0：否，1：是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `binding_location_location_type_physical_location_type_product_id` (`binding_location`,`location_type`,`physical_location_type`,`product_key`) USING BTREE,
  KEY `device_id` (`iot_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  xhzb.device 的数据：~0 rows (大约)
DELETE FROM `device`;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
/*!40000 ALTER TABLE `device` ENABLE KEYS */;

-- 导出  表 xhzb.device_data 结构
CREATE TABLE IF NOT EXISTS `device_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '告警规则ID，自增主键',
  `device_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备名称',
  `iot_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备ID',
  `product_key` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属产品的key',
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品名称',
  `function_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '功能名称',
  `access_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接入位置',
  `location_type` int DEFAULT NULL COMMENT '位置类型 0：随身设备 1：固定设备',
  `physical_location_type` int DEFAULT NULL COMMENT '物理位置类型 0楼层 1房间 2床位',
  `device_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '位置备注',
  `data_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '数据值',
  `alarm_time` datetime DEFAULT NULL COMMENT '数据上报时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_iot_id_function_id` (`iot_id`,`function_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 正在导出表  xhzb.device_data 的数据：~0 rows (大约)
DELETE FROM `device_data`;
/*!40000 ALTER TABLE `device_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `device_data` ENABLE KEYS */;

-- 导出  表 xhzb.elder 结构
CREATE TABLE IF NOT EXISTS `elder` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像',
  `id_card_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证号',
  `sex` int DEFAULT NULL COMMENT '性别（0:女  1:男）',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态（0:禁用，1:已入住 2:请假 3:已退住）',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `birthday` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '出生日期',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '家庭住址',
  `id_card_national_emblem_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证国徽面',
  `id_card_portrait_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '身份证人像面',
  `bed_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '床位编号',
  `bed_id` bigint DEFAULT NULL COMMENT '床位id',
  `nation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '民族',
  `education_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文化程度',
  `social_security_card` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '社保卡号',
  `living_situation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '居住情况',
  `religious_belief` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '宗教信仰',
  `economic_source` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经济来源',
  `marital_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '婚姻状况',
  `medical_payment_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '医疗费用支付方式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `core_suggestion` varchar(50) DEFAULT NULL COMMENT '核心建议',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_id_card_no` (`name`,`id_card_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='老人表';

-- 正在导出表  xhzb.elder 的数据：~4 rows (大约)
DELETE FROM `elder`;
/*!40000 ALTER TABLE `elder` DISABLE KEYS */;
/*!40000 ALTER TABLE `elder` ENABLE KEYS */;

-- 导出  表 xhzb.family_member 结构
CREATE TABLE IF NOT EXISTS `family_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'OpenID',
  `gender` int DEFAULT NULL COMMENT '性别(0:男，1:女)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='老人家属';

-- 正在导出表  xhzb.family_member 的数据：~0 rows (大约)
DELETE FROM `family_member`;
/*!40000 ALTER TABLE `family_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `family_member` ENABLE KEYS */;

-- 导出  表 xhzb.family_member_elder 结构
CREATE TABLE IF NOT EXISTS `family_member_elder` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `family_member_id` bigint DEFAULT NULL COMMENT '家属id',
  `elder_id` bigint DEFAULT NULL COMMENT '老人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `member_id_elder_id` (`family_member_id`,`elder_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='客户老人关联表';

-- 正在导出表  xhzb.family_member_elder 的数据：~0 rows (大约)
DELETE FROM `family_member_elder`;
/*!40000 ALTER TABLE `family_member_elder` DISABLE KEYS */;
/*!40000 ALTER TABLE `family_member_elder` ENABLE KEYS */;

-- 导出  表 xhzb.floor 结构
CREATE TABLE IF NOT EXISTS `floor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `code` bigint DEFAULT NULL COMMENT '编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=426 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='楼层表';

-- 正在导出表  xhzb.floor 的数据：~10 rows (大约)
DELETE FROM `floor`;
/*!40000 ALTER TABLE `floor` DISABLE KEYS */;
INSERT INTO `floor` (`id`, `name`, `code`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(1, '1楼', 1, '2023-09-26 16:10:27', '2026-03-21 13:11:36', 1671403256519078153, 1, NULL),
	(2, '2楼', 2, '2023-09-26 17:37:20', '2023-09-26 17:37:20', 1671403256519078138, NULL, NULL),
	(3, '3楼', 3, '2023-09-26 17:37:26', '2023-09-26 17:37:26', 1671403256519078138, NULL, NULL),
	(4, '4楼', 4, '2023-09-26 17:37:32', '2023-09-26 17:37:32', 1671403256519078138, NULL, NULL),
	(5, '5楼', 5, '2023-09-26 17:37:38', '2023-09-26 17:37:38', 1671403256519078138, NULL, NULL),
	(6, '6楼', 6, '2023-09-26 17:37:42', '2023-09-26 17:37:59', 1671403256519078138, 1671403256519078138, NULL),
	(7, '7楼', 7, '2023-09-26 17:37:47', '2023-09-26 17:37:52', 1671403256519078138, 1671403256519078138, NULL),
	(8, '8楼', 8, '2023-09-26 17:38:09', '2023-09-26 17:38:09', 1671403256519078138, NULL, NULL),
	(391, '9楼', 8, '2023-12-18 14:53:50', '2023-12-18 14:53:50', 1671403256519078138, NULL, NULL),
	(401, '10楼', 9, '2023-12-26 19:29:54', '2023-12-27 10:15:34', 1671403256519078138, 1671403256519078138, NULL);
/*!40000 ALTER TABLE `floor` ENABLE KEYS */;

-- 导出  表 xhzb.gen_table 结构
CREATE TABLE IF NOT EXISTS `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表';

-- 正在导出表  xhzb.gen_table 的数据：~0 rows (大约)
DELETE FROM `gen_table`;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;

-- 导出  表 xhzb.gen_table_column 结构
CREATE TABLE IF NOT EXISTS `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=949 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表字段';

-- 正在导出表  xhzb.gen_table_column 的数据：~25 rows (大约)
DELETE FROM `gen_table_column`;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;

-- 导出  表 xhzb.health_assessment 结构
CREATE TABLE IF NOT EXISTS `health_assessment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elder_name` varchar(50) NOT NULL COMMENT '老人姓名',
  `id_card` varchar(18) NOT NULL COMMENT '身份证号码',
  `elder_id` bigint DEFAULT NULL COMMENT '老人id',
  `core_suggestion` tinyint(1) DEFAULT NULL COMMENT '核心建议，0-不建议入住，1-建议入住',
  `check_in_status` tinyint(1) DEFAULT NULL COMMENT '入住状态：0-未入住，1-已入住',
  `evaluation_progress` tinyint(1) DEFAULT NULL COMMENT '评估进度：0-评估中，1-已完成，2-已取消',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康评估记录表';

-- 正在导出表  xhzb.health_assessment 的数据：~4 rows (大约)
DELETE FROM `health_assessment`;
/*!40000 ALTER TABLE `health_assessment` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_assessment` ENABLE KEYS */;

-- 导出  表 xhzb.health_assessment_data_collection 结构
CREATE TABLE IF NOT EXISTS `health_assessment_data_collection` (
  `id` bigint NOT NULL COMMENT '主键',
  `basic_info` text COMMENT '基本信息',
  `health_assessment` text COMMENT '健康评估',
  `daily_living_activities` text COMMENT '日常生活活动',
  `mental_state` text COMMENT '精神状态',
  `perception_communication` text COMMENT '感知与沟通',
  `social_participation` text COMMENT '社会参与',
  `assessment_details` text COMMENT '详细评估报告内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康评估数据采集';

-- 正在导出表  xhzb.health_assessment_data_collection 的数据：~4 rows (大约)
DELETE FROM `health_assessment_data_collection`;
/*!40000 ALTER TABLE `health_assessment_data_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_assessment_data_collection` ENABLE KEYS */;

-- 导出  表 xhzb.health_assessment_report 结构
CREATE TABLE IF NOT EXISTS `health_assessment_report` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `health_assessment_id` bigint unsigned DEFAULT NULL COMMENT '健康评估ID',
  `assessment_time` datetime DEFAULT NULL COMMENT '评估时间',
  `assessor_name` varchar(50) DEFAULT NULL COMMENT '评估员姓名',
  `recommended_room_type` varchar(50) DEFAULT NULL COMMENT '推荐入住房型（如：标准双人间）',
  `recommended_care_level` varchar(20) DEFAULT NULL COMMENT '推荐护理等级（如：一级护理等级）',
  `care_focus` text COMMENT '护理重点（多条用分号分隔，如：血压管控；肢体协助；认知干预）',
  `suggestion_description` text COMMENT '建议说明（如：老人基础健康状况稳定，日常生活能部分自理）',
  `daily_activity_level` varchar(10) DEFAULT NULL COMMENT '日常生活活动等级',
  `mental_status_level` varchar(10) DEFAULT NULL COMMENT '精神状态等级',
  `perception_communication_level` varchar(10) DEFAULT NULL COMMENT '感知觉与沟通等级',
  `social_participation_level` varchar(10) DEFAULT NULL COMMENT '社会参与等级',
  `initial_ability_level` varchar(50) DEFAULT NULL COMMENT '老年人能力初步等级',
  `final_ability_level` varchar(50) DEFAULT NULL COMMENT '老年人能力最终等级',
  `level_change_reason` text COMMENT '等级变更依据说明',
  `family_cooperation` text COMMENT '家属配合事项（多条用分号分隔）',
  `diet_suggestion` text COMMENT '饮食建议（如：低盐低脂饮食，控制主食摄入量，避免高糖食物）',
  `psychological_care` text COMMENT '心理关怀建议（如：多关注老人情绪变化，每日进行不少于10分钟的一对一沟通）',
  `institution_preparation` text COMMENT '机构准备事项（多条用分号分隔，如：提前准备轮椅；房间配置；照护人员适配）',
  `health_score` varchar(20) DEFAULT NULL COMMENT '健康评分',
  `risk_level` varchar(20) DEFAULT NULL COMMENT '严重危险(健康, 提示, 风险, 危险, 严重危险)',
  `report_summary` text COMMENT '报告总结',
  `abnormal_analysis` text COMMENT '异常分析',
  `system_score` varchar(255) DEFAULT NULL COMMENT '健康系统分值',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` text COMMENT '备注',
  `core_suggestion` int DEFAULT NULL COMMENT '核心建议，0-不建议入住，1-建议入住',
  `check_in_status` tinyint DEFAULT NULL COMMENT '入住状态：0-未入住，1-已入住',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='健康评估报告表';

-- 正在导出表  xhzb.health_assessment_report 的数据：~4 rows (大约)
DELETE FROM `health_assessment_report`;
/*!40000 ALTER TABLE `health_assessment_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_assessment_report` ENABLE KEYS */;

-- 导出  表 xhzb.knowledge_base 结构
CREATE TABLE IF NOT EXISTS `knowledge_base` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL COMMENT '知识标题',
  `category` int unsigned NOT NULL COMMENT '分类',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签数组',
  `status` tinyint NOT NULL COMMENT '状态 0-禁用  1-启用',
  `priority` tinyint unsigned NOT NULL DEFAULT '3' COMMENT '优先级(1-5)',
  `document_url` varchar(255) DEFAULT NULL COMMENT '文档访问URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='知识库主表';

-- 正在导出表  xhzb.knowledge_base 的数据：~0 rows (大约)
DELETE FROM `knowledge_base`;
/*!40000 ALTER TABLE `knowledge_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `knowledge_base` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_elder 结构
CREATE TABLE IF NOT EXISTS `nursing_elder` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nursing_id` bigint DEFAULT NULL COMMENT '护理员id',
  `elder_id` bigint DEFAULT NULL COMMENT '老人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `nursing_id` (`nursing_id`,`elder_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理员老人关联表';

-- 正在导出表  xhzb.nursing_elder 的数据：~3 rows (大约)
DELETE FROM `nursing_elder`;
/*!40000 ALTER TABLE `nursing_elder` DISABLE KEYS */;
INSERT INTO `nursing_elder` (`id`, `nursing_id`, `elder_id`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(10, 100, 93, '2025-05-04 12:18:37', NULL, 1, NULL, NULL),
	(12, 101, 93, '2025-05-04 12:18:42', NULL, 1, NULL, NULL),
	(15, 100, 94, '2026-04-11 00:23:46', NULL, 1, NULL, NULL);
/*!40000 ALTER TABLE `nursing_elder` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_level 结构
CREATE TABLE IF NOT EXISTS `nursing_level` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '等级名称',
  `lplan_id` int NOT NULL COMMENT '护理计划ID',
  `fee` decimal(10,2) NOT NULL COMMENT '护理费用',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '等级说明',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理等级表';

-- 正在导出表  xhzb.nursing_level 的数据：~4 rows (大约)
DELETE FROM `nursing_level`;
/*!40000 ALTER TABLE `nursing_level` DISABLE KEYS */;
INSERT INTO `nursing_level` (`id`, `name`, `lplan_id`, `fee`, `status`, `description`, `create_time`, `create_by`, `update_by`, `remark`, `update_time`) VALUES
	(80, '二级护理等级', 138, 1500.00, 1, NULL, '2026-03-03 16:30:04', NULL, NULL, NULL, '2026-03-03 16:30:04'),
	(81, '三级护理等级', 139, 1200.00, 1, NULL, '2026-03-03 16:30:17', NULL, NULL, NULL, '2026-03-03 16:30:17'),
	(82, '四级护理等级', 140, 1000.00, 1, '无', '2026-03-03 16:30:30', NULL, NULL, NULL, '2026-03-13 17:54:25'),
	(83, '顶级护理', 141, 3010.00, 1, '最顶级的护理等级', '2026-03-09 16:37:08', NULL, NULL, NULL, '2026-04-02 03:16:47');
/*!40000 ALTER TABLE `nursing_level` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_plan 结构
CREATE TABLE IF NOT EXISTS `nursing_plan` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sort_no` int DEFAULT NULL COMMENT '排序号',
  `plan_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '名称',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态 0禁用 1启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `plan_name` (`plan_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理计划表';

-- 正在导出表  xhzb.nursing_plan 的数据：~5 rows (大约)
DELETE FROM `nursing_plan`;
/*!40000 ALTER TABLE `nursing_plan` DISABLE KEYS */;
INSERT INTO `nursing_plan` (`id`, `sort_no`, `plan_name`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(138, 1, '二级护理计划', 1, '2026-03-03 16:27:23', NULL, NULL, NULL, NULL),
	(139, 1, '三级护理计划', 1, '2026-03-03 16:27:57', NULL, NULL, NULL, NULL),
	(140, 1, '四级护理计划', 1, '2026-03-03 16:29:13', NULL, NULL, NULL, NULL),
	(141, 1, '特级护理计划', 1, '2026-03-09 16:23:53', NULL, NULL, NULL, NULL),
	(150, 2, '紧急护理计划', 1, '2026-03-09 16:43:41', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `nursing_plan` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_project 结构
CREATE TABLE IF NOT EXISTS `nursing_project` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `order_no` int DEFAULT NULL COMMENT '排序号',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '单位',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片',
  `nursing_requirement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '护理要求',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理项目表';

-- 正在导出表  xhzb.nursing_project 的数据：~11 rows (大约)
DELETE FROM `nursing_project`;
/*!40000 ALTER TABLE `nursing_project` DISABLE KEYS */;
INSERT INTO `nursing_project` (`id`, `name`, `order_no`, `unit`, `price`, `image`, `nursing_requirement`, `status`, `create_by`, `update_by`, `remark`, `create_time`, `update_time`) VALUES
	(1, '修剪指甲', 1, '次', 10.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/b6631465-1684-41fe-8ccd-0b027cb91e90.png', '根据老人身体状况，定期修剪手指甲、脚趾甲，避免过长、开裂或划伤皮肤。修剪时动作轻柔，仔细打磨边缘，兼顾舒适度与安全性，预防倒刺、嵌甲及感染。去', 1, '1', '1', NULL, '2024-08-29 16:51:50', '2026-03-21 13:04:21'),
	(2, '衣物清洁', 1, '件', 5.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/41fc58d3-0627-4fa9-8459-906599aa1efa.png', '定时收集、分类清洗老人衣物、床单等织物，按材质选择合适洗涤方式。洗净后烘干、熨烫、整理归位，保持衣物干净、整洁、无异味，提升老人穿着舒适度。', 1, '1', '1', NULL, '2024-08-29 16:52:27', '2025-04-27 15:09:00'),
	(3, '整理床铺', 1, '次', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/e611fcc9-dc45-49ac-abeb-f2ea99c2cffc.png', '每日定时整理床铺，更换枕套、床单、被罩，保持床单位平整、干燥、无褶皱。及时清理床上杂物，为老人营造整洁、舒适、卫生的睡眠与休息环境。', 1, '1', '1', NULL, '2024-08-29 16:52:52', '2024-08-29 08:51:46'),
	(4, '助餐', 1, '餐', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/d91ba642-88e5-4c3d-8e50-a681ae3300e5.png', '根据老人饮食需求与身体状况，协助进食、饮水。对行动不便、吞咽困难者，提供喂食、喂水服务，注意温度、速度与营养搭配，确保老人安全、顺利进餐。', 1, '1', '1', NULL, '2024-08-29 16:53:29', '2024-08-29 08:52:24'),
	(5, '助浴', 1, '次', 40.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/125df948-7646-4fce-b322-1db0a84856e7.png', '协助老人完成全身清洁洗浴，做好防滑、保暖与安全防护。根据老人自理能力，全程陪护，控制水温与时间，避免滑倒、受凉，清洁后及时擦干穿衣。', 1, '1', '1', NULL, '2024-08-29 16:53:51', '2024-08-29 08:52:46'),
	(6, '洗头', 1, '次', 20.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/a38883fc-870b-40ff-a256-54ce2fc17af9.png', '定期为老人清洗头发，调节合适水温，做好头部与颈部保暖。冲洗干净后及时擦干、吹干，预防感冒，保持头发清洁清爽，提升老人舒适感。', 1, '1', '1', NULL, '2024-08-29 16:54:22', '2024-08-29 08:53:17'),
	(7, '洗脸', 1, '次', 15.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/95b0ad37-5d61-4ec2-a961-d6fb691a18f0.png', '每日早晚协助老人清洁面部，使用温和用品，轻柔擦拭眼、耳、鼻、面部及颈部。保持面部干净清爽，促进血液循环，提升日常舒适度。', 1, '1', '1', NULL, '2024-08-29 16:54:45', '2024-08-29 08:53:40'),
	(8, '洗脚', 1, '次', 20.00, 'https://itheim.oss-cn-beijing.aliyuncs.com/8437eb2d-3ea5-4eee-9d78-017bc8b3a66e.png', '每日为老人清洁双脚，调节适宜水温，浸泡、清洗、擦干双脚及趾缝。促进足部血液循环，缓解疲劳，预防脚气、干裂，提升睡眠与生活质量。', 1, '1', '1', NULL, '2024-08-29 16:55:08', '2024-08-29 08:54:03'),
	(20, '全身洗浴', 2, '元', 90.00, 'https://hm-xhzb.oss-cn-beijing.aliyuncs.com/476920d1-1dbd-4205-a1b6-95568881477e.png', '北京市海淀区西三旗街北京市海淀区西三旗街北京市海淀区西三旗街北京市海淀区西三旗街北京市', 1, '1', '1', NULL, '2026-03-09 15:59:52', '2026-03-28 16:52:37'),
	(31, '心理咨询', 1, '次', 25.00, 'https://hm-xhzb.oss-cn-beijing.aliyuncs.com/12b06336-24df-416b-a0ea-eff8f53e6fc1.png', '心理咨询心理咨询', 1, '1', NULL, NULL, '2026-03-28 17:07:01', '2026-03-28 09:07:01'),
	(32, '护理项目测试', 1, '次', 10.00, 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ae7cf766-fb7b-49ff-a73c-c86c25f280e1.png', '无特殊要求', 1, '1', NULL, NULL, '2026-04-06 10:02:14', '2026-04-06 02:02:12');
/*!40000 ALTER TABLE `nursing_project` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_project_plan 结构
CREATE TABLE IF NOT EXISTS `nursing_project_plan` (
  `id` int NOT NULL AUTO_INCREMENT,
  `plan_id` int NOT NULL COMMENT '计划id',
  `project_id` int NOT NULL COMMENT '项目id',
  `execute_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '计划执行时间',
  `execute_cycle` int NOT NULL COMMENT '执行周期 0 天 1 周 2月',
  `execute_frequency` int NOT NULL COMMENT '执行频次',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1859 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理计划和项目关联表';

-- 正在导出表  xhzb.nursing_project_plan 的数据：~53 rows (大约)
DELETE FROM `nursing_project_plan`;
/*!40000 ALTER TABLE `nursing_project_plan` DISABLE KEYS */;
INSERT INTO `nursing_project_plan` (`id`, `plan_id`, `project_id`, `execute_time`, `execute_cycle`, `execute_frequency`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(1736, 133, 87, '19:16:49', 1, 7, '2024-08-19 11:28:43', NULL, NULL, NULL, NULL),
	(1737, 133, 85, '19:28:34', 1, 1, '2024-08-19 11:28:43', NULL, NULL, NULL, NULL),
	(1738, 134, 85, '19:28:45', 0, 1, '2024-08-19 11:36:10', NULL, NULL, NULL, NULL),
	(1739, 135, 1, '16:55:58', 2, 1, '2024-08-29 08:55:34', NULL, NULL, NULL, NULL),
	(1740, 135, 5, '16:56:09', 0, 1, '2024-08-29 08:55:34', NULL, NULL, NULL, NULL),
	(1741, 135, 4, '08:00:00', 0, 1, '2024-08-29 08:55:34', NULL, NULL, NULL, NULL),
	(1742, 136, 5, '18:28:23', 1, 1, '2025-04-18 10:28:47', NULL, NULL, NULL, NULL),
	(1743, 136, 6, '18:28:35', 1, 2, '2025-04-18 10:28:47', NULL, NULL, NULL, NULL),
	(1744, 136, 7, '18:28:43', 0, 1, '2025-04-18 10:28:47', NULL, NULL, NULL, NULL),
	(1755, 138, 31, '16:26:36', 2, 2, '2026-03-03 16:28:33', NULL, NULL, NULL, NULL),
	(1756, 138, 8, '16:27:05', 1, 3, '2026-03-03 16:28:33', NULL, NULL, NULL, NULL),
	(1757, 138, 2, '16:27:14', 1, 2, '2026-03-03 16:28:33', NULL, NULL, NULL, NULL),
	(1758, 138, 6, '16:28:20', 1, 1, '2026-03-03 16:28:33', NULL, NULL, NULL, NULL),
	(1759, 139, 1, '16:27:23', 2, 2, '2026-03-03 16:28:57', NULL, NULL, NULL, NULL),
	(1760, 139, 3, '16:27:46', 1, 2, '2026-03-03 16:28:57', NULL, NULL, NULL, NULL),
	(1761, 139, 5, '16:28:49', 1, 1, '2026-03-03 16:28:57', NULL, NULL, NULL, NULL),
	(1763, 140, 1, '16:28:57', 2, 2, '2026-03-03 16:29:27', NULL, NULL, NULL, NULL),
	(1764, 140, 2, '16:29:23', 1, 1, '2026-03-03 16:29:27', NULL, NULL, NULL, NULL),
	(1767, 150, 1, '16:30:19', 1, 1, '2026-03-09 16:43:40', NULL, NULL, NULL, NULL),
	(1768, 150, 6, '16:39:59', 1, 2, '2026-03-09 16:43:40', NULL, NULL, NULL, NULL),
	(1769, 152, 3, '09:43:40', 0, 7, '2026-03-09 16:44:47', NULL, NULL, NULL, NULL),
	(1782, 137, 1, '16:24:45', 2, 2, '2026-03-11 18:09:03', NULL, NULL, NULL, NULL),
	(1783, 137, 2, '08:25:07', 1, 1, '2026-03-11 18:09:03', NULL, NULL, NULL, NULL),
	(1784, 137, 4, '08:00:00', 0, 3, '2026-03-11 18:09:03', NULL, NULL, NULL, NULL),
	(1785, 137, 5, '20:00:00', 1, 1, '2026-03-11 18:09:03', NULL, NULL, NULL, NULL),
	(1786, 137, 8, '20:26:05', 1, 3, '2026-03-11 18:09:03', NULL, NULL, NULL, NULL),
	(1788, 161, 2, '08:00', 1, 1, '2026-03-12 11:38:18', NULL, NULL, NULL, NULL),
	(1789, 162, 2, '08:00:00', 1, 2, '2026-03-12 11:39:42', NULL, NULL, NULL, NULL),
	(1817, 166, 3, '09:00', 1, 2, '2026-03-12 17:10:33', NULL, NULL, NULL, NULL),
	(1818, 166, 31, '11:02', 2, 7, '2026-03-12 17:10:33', NULL, NULL, NULL, NULL),
	(1819, 166, 20, '10:00', 0, 7, '2026-03-12 17:10:33', NULL, NULL, NULL, NULL),
	(1820, 166, 4, '08:00', 1, 7, '2026-03-12 17:10:33', NULL, NULL, NULL, NULL),
	(1834, 168, 2, '08:00', 1, 1, '2026-03-12 19:21:17', NULL, NULL, NULL, NULL),
	(1835, 169, 2, '08:00', 1, 1, '2026-03-13 09:16:58', NULL, NULL, NULL, NULL),
	(1836, 169, 3, '10:00', 0, 2, '2026-03-13 09:16:58', NULL, NULL, NULL, NULL),
	(1837, 169, 6, '08:00', 1, 3, '2026-03-13 09:16:58', NULL, NULL, NULL, NULL),
	(1838, 170, 2, '08:00', 1, 1, '2026-03-13 09:22:39', NULL, NULL, NULL, NULL),
	(1839, 170, 3, '08:00', 0, 1, '2026-03-13 09:22:39', NULL, NULL, NULL, NULL),
	(1840, 172, 20, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1841, 172, 3, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1842, 172, 4, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1843, 172, 5, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1844, 172, 6, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1845, 172, 31, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1846, 172, 7, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1847, 172, 8, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1848, 172, 2, '08:00', 1, 1, '2026-03-13 10:52:18', NULL, NULL, NULL, NULL),
	(1853, 158, 3, '18:06:25', 2, 1, '2026-03-13 17:48:01', NULL, NULL, NULL, NULL),
	(1854, 158, 2, '08:00:00', 1, 1, '2026-03-13 17:48:01', NULL, NULL, NULL, NULL),
	(1855, 174, 2, '08:00', 1, 6, '2026-03-13 21:21:36', NULL, NULL, NULL, NULL),
	(1856, 174, 3, '08:00', 1, 7, '2026-03-13 21:21:36', NULL, NULL, NULL, NULL),
	(1857, 141, 4, '16:22:54', 1, 3, '2026-03-21 13:04:54', NULL, NULL, NULL, NULL),
	(1858, 141, 5, '17:23:31', 1, 3, '2026-03-21 13:04:54', NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `nursing_project_plan` ENABLE KEYS */;

-- 导出  表 xhzb.nursing_task 结构
CREATE TABLE IF NOT EXISTS `nursing_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nursing_id` varchar(50) DEFAULT NULL COMMENT '护理员id',
  `project_id` int NOT NULL COMMENT '项目id',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '护理项目名称',
  `elder_id` bigint DEFAULT NULL COMMENT '老人id',
  `elder_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '老人姓名',
  `bed_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '床位编号',
  `estimated_server_time` datetime DEFAULT NULL COMMENT '预计服务时间',
  `real_server_time` datetime DEFAULT NULL COMMENT '实际服务时间',
  `mark` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行记录',
  `cancel_reason` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '取消原因',
  `status` int DEFAULT NULL COMMENT '状态  1待执行 2已执行 3已关闭 ',
  `task_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '执行图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=212 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='护理任务表';

-- 正在导出表  xhzb.nursing_task 的数据：~0 rows (大约)
DELETE FROM `nursing_task`;
/*!40000 ALTER TABLE `nursing_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `nursing_task` ENABLE KEYS */;

-- 导出  表 xhzb.reservation 结构
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预约人姓名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '预约人手机号',
  `time` datetime NOT NULL COMMENT '预约时间',
  `visitor` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '探访人',
  `type` int NOT NULL COMMENT '预约类型，0：参观预约，1：探访预约',
  `status` int NOT NULL COMMENT '预约状态，0：待报道，1：已完成，2：取消，3：过期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_mobile_time_visitor` (`mobile`,`time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='预约信息表';

-- 正在导出表  xhzb.reservation 的数据：~3 rows (大约)
DELETE FROM `reservation`;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` (`id`, `name`, `mobile`, `time`, `visitor`, `type`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(1, '张三三', '13211234564', '2026-05-25 18:30:00', '老张', 0, 0, '2026-02-22 22:39:51', '2025-10-15 22:39:51', 7, 7, NULL),
	(2, '李四四', '18876651234', '2026-05-25 18:30:00', '老李', 0, 0, '2026-01-22 22:40:16', '2025-10-15 22:40:16', 7, 7, NULL),
	(3, '王五五', '13800138000', '2026-05-26 18:30:00', '老王', 0, 0, '2026-01-23 21:52:04', '2025-10-29 21:52:06', 7, 7, NULL);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;

-- 导出  表 xhzb.room 结构
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '房间编号',
  `sort` int DEFAULT NULL COMMENT '排序号',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '房间类型名称',
  `floor_id` bigint DEFAULT NULL COMMENT '楼层id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='房间表';

-- 正在导出表  xhzb.room 的数据：~58 rows (大约)
DELETE FROM `room`;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` (`id`, `code`, `sort`, `type_name`, `floor_id`, `create_time`, `update_time`, `is_deleted`, `create_by`, `update_by`, `remark`) VALUES
	(1, '101', 1, '豪华双人间', 1, '2023-09-26 17:38:25', '2025-04-18 18:32:41', 0, 1671403256519078138, 1, NULL),
	(2, '102', 2, '豪华双人间', 1, '2023-09-26 17:38:32', '2025-04-18 18:33:09', 0, 1671403256519078138, 1, NULL),
	(3, '103', 3, '豪华双人间', 1, '2023-09-26 17:38:41', '2025-04-18 18:33:21', 0, 1671403256519078138, 1, NULL),
	(4, '104', 4, '双人套房', 1, '2023-09-26 17:38:48', '2025-04-18 18:33:25', 0, 1671403256519078138, 1, NULL),
	(5, '105', 5, '单人套房', 1, '2023-09-26 17:38:55', '2025-04-18 18:33:31', 0, 1671403256519078138, 1, NULL),
	(6, '106', 6, '双人套房', 1, '2023-09-26 17:39:05', '2025-04-18 18:33:38', 0, 1671403256519078138, 1, NULL),
	(7, '107', 7, '单人套房', 1, '2023-09-26 17:39:13', '2025-04-18 18:33:47', 0, 1671403256519078138, 1, NULL),
	(8, '201', 1, '单人套房', 2, '2023-09-26 17:42:02', '2025-04-18 18:33:56', 0, 1671403256519078138, 1, NULL),
	(9, '202', 2, '豪华单人间', 2, '2023-09-26 17:42:08', '2025-04-18 18:34:06', 0, 1671403256519078138, 1, NULL),
	(10, '203', 3, '普通单人间', 2, '2023-09-26 17:42:15', '2025-04-18 18:34:11', 0, 1671403256519078138, 1, NULL),
	(11, '204', 4, '豪华双人间', 2, '2023-09-26 17:42:22', '2025-04-18 18:34:16', 0, 1671403256519078138, 1, NULL),
	(12, '205', 5, '豪华单人间', 2, '2023-09-26 17:42:30', '2023-09-26 17:42:30', 0, 1671403256519078138, NULL, NULL),
	(13, '206', 6, '双人套房', 2, '2023-09-26 17:42:41', '2023-09-26 17:42:41', 0, 1671403256519078138, NULL, NULL),
	(14, '207', 7, '单人套房', 2, '2023-09-26 17:42:48', '2025-04-18 18:34:30', 0, 1671403256519078138, 1, NULL),
	(15, '301', 1, '单人套房', 3, '2023-09-26 17:43:54', '2025-04-18 18:34:38', 0, 1671403256519078138, 1, NULL),
	(16, '302', 2, '豪华双人间', 3, '2023-09-26 17:44:01', '2025-04-18 18:34:43', 0, 1671403256519078138, 1, NULL),
	(17, '303', 3, '普通单人间', 3, '2023-09-26 17:44:09', '2025-04-18 18:34:48', 0, 1671403256519078138, 1, NULL),
	(18, '304', 4, '豪华双人间', 3, '2023-09-26 17:44:17', '2023-09-26 17:44:17', 0, 1671403256519078138, NULL, NULL),
	(19, '305', 5, '豪华单人间', 3, '2023-09-26 17:44:24', '2025-04-18 18:34:56', 0, 1671403256519078138, 1, NULL),
	(20, '306', 6, '双人套房', 3, '2023-09-26 17:44:33', '2025-04-18 18:34:59', 0, 1671403256519078138, 1, NULL),
	(21, '307', 7, '单人套房', 3, '2023-09-26 17:44:42', '2025-04-18 18:35:03', 0, 1671403256519078138, 1, NULL),
	(22, '401', 1, '单人套房', 4, '2023-09-26 18:51:10', '2025-04-18 18:35:11', 0, 1671403256519078138, 1, NULL),
	(23, '402', 2, '豪华双人间', 4, '2023-09-26 18:51:17', '2025-04-18 18:35:36', 0, 1671403256519078138, 1, NULL),
	(24, '403', 3, '普通单人间', 4, '2023-09-26 18:51:23', '2023-09-26 18:51:23', 0, 1671403256519078138, NULL, NULL),
	(25, '404', 4, '豪华双人间', 4, '2023-09-26 18:51:32', '2023-09-26 18:51:32', 0, 1671403256519078138, NULL, NULL),
	(26, '405', 5, '豪华单人间', 4, '2023-09-26 18:51:42', '2023-09-26 18:51:42', 0, 1671403256519078138, NULL, NULL),
	(27, '406', 6, '双人套房', 4, '2023-09-26 18:51:54', '2023-09-26 18:51:54', 0, 1671403256519078138, NULL, NULL),
	(28, '407', 7, '单人套房', 4, '2023-09-26 18:52:03', '2023-09-26 18:52:03', 0, 1671403256519078138, NULL, NULL),
	(29, '501', 1, '特护房', 5, '2023-09-26 18:53:54', '2023-09-26 18:53:54', 0, 1671403256519078138, NULL, NULL),
	(31, '502', 2, '普通双人间', 5, '2023-09-26 18:54:05', '2023-09-26 18:54:05', 0, 1671403256519078138, NULL, NULL),
	(32, '503', 3, '普通单人间', 5, '2023-09-26 18:54:12', '2023-09-26 18:54:12', 0, 1671403256519078138, NULL, NULL),
	(33, '504', 4, '豪华双人间', 5, '2023-09-26 18:54:20', '2023-09-26 18:54:20', 0, 1671403256519078138, NULL, NULL),
	(34, '505', 5, '豪华单人间', 5, '2023-09-26 18:54:28', '2023-09-26 18:54:28', 0, 1671403256519078138, NULL, NULL),
	(35, '506', 6, '双人套房', 5, '2023-09-26 18:54:37', '2023-09-26 18:54:37', 0, 1671403256519078138, NULL, NULL),
	(36, '507', 7, '单人套房', 5, '2023-09-26 18:54:47', '2023-09-26 18:54:47', 0, 1671403256519078138, NULL, NULL),
	(37, '601', 1, '特护房', 6, '2023-09-26 18:57:14', '2023-09-26 18:57:14', 0, 1671403256519078138, NULL, NULL),
	(38, '602', 2, '普通双人间', 6, '2023-09-26 18:57:20', '2023-09-26 18:57:20', 0, 1671403256519078138, NULL, NULL),
	(39, '603', 3, '普通单人间', 6, '2023-09-26 18:57:28', '2023-09-26 18:57:28', 0, 1671403256519078138, NULL, NULL),
	(40, '604', 4, '豪华双人间', 6, '2023-09-26 18:57:36', '2023-09-26 18:57:36', 0, 1671403256519078138, NULL, NULL),
	(41, '605', 5, '豪华单人间', 6, '2023-09-26 19:01:36', '2023-09-26 19:01:36', 0, 1671403256519078138, NULL, NULL),
	(42, '606', 6, '双人套房', 6, '2023-09-26 19:01:45', '2023-09-26 19:01:45', 0, 1671403256519078138, NULL, NULL),
	(43, '607', 7, '单人套房', 6, '2023-09-26 19:01:54', '2023-09-26 19:01:54', 0, 1671403256519078138, NULL, NULL),
	(44, '701', 1, '特护房', 7, '2023-09-26 19:02:13', '2023-09-26 19:02:13', 0, 1671403256519078138, NULL, NULL),
	(45, '702', 2, '普通双人间', 7, '2023-09-26 19:02:20', '2023-09-26 19:02:20', 0, 1671403256519078138, NULL, NULL),
	(46, '703', 3, '普通单人间', 7, '2023-09-26 19:02:28', '2023-09-26 19:02:28', 0, 1671403256519078138, NULL, NULL),
	(47, '704', 4, '豪华双人间', 7, '2023-09-26 19:02:49', '2023-09-26 19:02:49', 0, 1671403256519078138, NULL, NULL),
	(48, '705', 5, '豪华单人间', 7, '2023-09-26 19:03:00', '2023-09-26 19:03:00', 0, 1671403256519078138, NULL, NULL),
	(49, '706', 6, '双人套房', 7, '2023-09-26 19:03:07', '2023-09-26 19:03:07', 0, 1671403256519078138, NULL, NULL),
	(50, '707', 7, '单人套房', 7, '2023-09-26 19:03:15', '2023-09-26 19:03:15', 0, 1671403256519078138, NULL, NULL),
	(51, '801', 1, '特护房', 8, '2023-09-26 19:03:49', '2023-09-26 19:03:49', 0, 1671403256519078138, NULL, NULL),
	(52, '802', 2, '普通双人间', 8, '2023-09-26 19:03:57', '2023-09-26 19:03:57', 0, 1671403256519078138, NULL, NULL),
	(53, '803', 3, '普通单人间', 8, '2023-09-26 19:04:04', '2023-09-26 19:04:04', 0, 1671403256519078138, NULL, NULL),
	(54, '804', 4, '豪华双人间', 8, '2023-09-26 19:04:13', '2023-09-26 19:04:13', 0, 1671403256519078138, NULL, NULL),
	(55, '805', 5, '豪华单人间', 8, '2023-09-26 19:04:45', '2023-09-26 19:04:45', 0, 1671403256519078138, NULL, NULL),
	(56, '806', 6, '双人套房', 8, '2023-09-26 19:04:52', '2023-09-26 19:04:52', 0, 1671403256519078138, NULL, NULL),
	(57, '807', 7, '单人套房', 8, '2023-09-26 19:05:00', '2023-09-26 19:05:00', 0, 1671403256519078138, NULL, NULL),
	(74, '1001', 1, '普通单人间', 401, '2023-12-26 19:31:43', '2026-03-21 13:10:36', 0, 1671403256519078138, 1, NULL),
	(143, '901', 1, '双人套房', 391, '2026-04-02 08:52:55', NULL, 0, 1, NULL, NULL);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;

-- 导出  表 xhzb.room_type 结构
CREATE TABLE IF NOT EXISTS `room_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房型名称',
  `bed_count` int NOT NULL DEFAULT '0' COMMENT '床位数量',
  `price` decimal(10,2) NOT NULL COMMENT '床位费用',
  `introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '介绍',
  `photo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '照片',
  `status` tinyint NOT NULL COMMENT '状态，0：禁用，1：启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人id',
  `update_by` bigint DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='房型表';

-- 正在导出表  xhzb.room_type 的数据：~10 rows (大约)
DELETE FROM `room_type`;
/*!40000 ALTER TABLE `room_type` DISABLE KEYS */;
INSERT INTO `room_type` (`id`, `name`, `bed_count`, `price`, `introduction`, `photo`, `status`, `create_time`, `update_time`, `create_by`, `update_by`, `remark`) VALUES
	(1, '单人套房', 0, 4000.00, '宽敞舒适的套房，配备独立卫生间和基本生活设施，满足独自居住的需求，提供私密性和舒适度', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/e2f1031b-e23e-4379-95d4-ce8fe382f58f.png', 1, '2023-09-26 15:57:50', '2024-05-20 11:00:19', 1671403256519078153, 1, NULL),
	(2, '双人套房', 0, 6000.00, '适合夫妻或朋友两人居住的套房，设有独立卫生间和基本生活设施，提供共享空间和私密性', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/ff84c185-2e28-431c-951d-d004cc2d5bdc.png', 1, '2023-09-26 15:58:51', '2023-09-26 15:58:51', 1671403256519078153, NULL, NULL),
	(3, '豪华单人间', 0, 3000.00, '豪华装修的单人房间，提供舒适的居住环境和高品质的服务，设计精美，配备独立卫生间和必需设施', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/d803832c-5b93-4cae-ba95-aeb52ab0c5e0.png', 1, '2023-09-26 15:59:33', '2026-03-10 10:56:03', 1671403256519078153, 1, NULL),
	(4, '豪华双人间', 0, 4500.00, '精心装修的双人房间，提供舒适和豪华的居住环境，配备独立卫生间和高品质的家具', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/c3522da7-4c5c-48d2-94f9-9f0b95a048d2.png', 1, '2023-09-26 16:00:03', '2026-03-10 10:56:05', 1671403256519078153, 1, NULL),
	(5, '普通单人间', 0, 2000.00, '简洁实用的单人房间，提供基本的居住设施和舒适度，适合独自居住的老年人，提供相对经济实惠的居住选择', 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/1a330b1c-b0a1-463d-8d9a-221ef17c314f.png', 1, '2023-09-26 16:00:27', '2026-03-12 14:23:50', 1671403256519078153, 1, NULL),
	(115, '测试新增', 10, 3500.00, '水电费方式', 'https://itheim.oss-cn-beijing.aliyuncs.com/8fe7b29d-fce0-4201-becb-0586e8284a9d.png', 1, '2024-08-22 19:06:33', '2024-08-22 19:06:41', 1, 1, '是否'),
	(116, '标准双人间', 10, 1500.00, '123', 'https://itheim.oss-cn-beijing.aliyuncs.com/91c4a814-efd5-4093-a5ac-963b41047019.png,https://hm-xhzb.oss-cn-beijing.aliyuncs.com/5535f726-5ae2-413a-8548-0825459963eb.png', 1, '2024-09-12 22:52:36', '2024-09-12 22:55:10', 1, 1, '1231234565432'),
	(117, '豪华总统房', 3, 100000.00, '给你一个优雅舒适的家', 'https://hm-xhzb.oss-cn-beijing.aliyuncs.com/0054a549-63d2-4624-9e40-f530bb631d1f.png', 1, '2026-03-09 15:20:42', '2026-03-21 13:11:57', 1, 1, '给你一个优雅舒适的家'),
	(118, '大车店', 100, 10.00, '群居房', 'https://hm-xhzb.oss-cn-beijing.aliyuncs.com/554fdf86-803a-42b6-b29d-3678eb85004a.jpeg', 1, '2026-03-09 15:57:43', '2026-03-09 15:57:55', 1, 1, NULL),
	(120, '单人套房3', 100, 1.00, '桑', NULL, 1, '2026-03-11 17:14:52', '2026-03-28 16:50:17', 1, 1, '阿斯钢');
/*!40000 ALTER TABLE `room_type` ENABLE KEYS */;

-- 导出  表 xhzb.sys_config 结构
CREATE TABLE IF NOT EXISTS `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='参数配置表';

-- 正在导出表  xhzb.sys_config 的数据：~6 rows (大约)
DELETE FROM `sys_config`;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),
	(2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '初始化密码 123456'),
	(3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '深色主题theme-dark，浅色主题theme-light'),
	(4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '是否开启验证码功能（true开启，false关闭）'),
	(5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '是否开启注册用户功能（true开启，false关闭）'),
	(6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-08-14 02:48:24', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;

-- 导出  表 xhzb.sys_dept 结构
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- 正在导出表  xhzb.sys_dept 的数据：~14 rows (大约)
DELETE FROM `sys_dept`;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES
	(100, 0, '0', '智慧养老院', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-08-14 02:48:23', '', NULL),
	(101, 100, '0,100', '高层办公室', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-08-14 02:48:23', 'admin', '2025-05-04 04:10:38'),
	(102, 100, '0,100', '财务部', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-08-14 02:48:23', 'admin', '2025-05-04 04:11:21'),
	(103, 101, '0,100,101', '院长办公室', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-08-14 02:48:23', 'admin', '2025-05-04 04:11:06'),
	(104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2024-08-14 02:48:23', '', NULL),
	(200, 100, '0,100', '行政部', 3, NULL, NULL, NULL, '0', '0', 'admin', '2025-05-04 04:11:40', '', NULL),
	(201, 100, '0,100', '护理部', 4, NULL, NULL, NULL, '0', '0', 'admin', '2025-05-04 04:11:59', '', NULL),
	(202, 100, '0,100', '后勤部', 5, NULL, NULL, NULL, '0', '0', 'admin', '2025-05-04 04:12:07', '', NULL),
	(203, 100, '0,100', '销售部', 6, NULL, NULL, NULL, '0', '0', 'admin', '2025-05-04 04:12:16', '', NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;

-- 导出  表 xhzb.sys_dict_data 结构
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典数据表';

-- 正在导出表  xhzb.sys_dict_data 的数据：~42 rows (大约)
DELETE FROM `sys_dict_data`;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '性别男'),
	(2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '性别女'),
	(3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '性别未知'),
	(4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '显示菜单'),
	(5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '隐藏菜单'),
	(6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '正常状态'),
	(7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '停用状态'),
	(8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '正常状态'),
	(9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '停用状态'),
	(10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '默认分组'),
	(11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '系统分组'),
	(12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '系统默认是'),
	(13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '系统默认否'),
	(14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '通知'),
	(15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '公告'),
	(16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '正常状态'),
	(17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '关闭状态'),
	(18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '其他操作'),
	(19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '新增操作'),
	(20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '修改操作'),
	(21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '删除操作'),
	(22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '授权操作'),
	(23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '导出操作'),
	(24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '导入操作'),
	(25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '强退操作'),
	(26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '生成操作'),
	(27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '清空操作'),
	(28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '正常状态'),
	(29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '停用状态'),
	(100, 0, '启用', '1', 'nursing_project_status', NULL, 'default', 'N', '0', 'admin', '2024-08-18 02:41:15', '', NULL, NULL),
	(101, 0, '禁用', '0', 'nursing_project_status', NULL, 'default', 'N', '0', 'admin', '2024-08-18 02:41:30', '', NULL, NULL),
	(103, 0, '启用', '1', 'nursing_plan_status', NULL, 'default', 'N', '0', 'admin', '2024-08-19 11:00:04', '', NULL, NULL),
	(104, 0, '禁用', '2', 'nursing_plan_status', NULL, 'default', 'N', '0', 'admin', '2024-08-19 11:00:10', '', NULL, NULL),
	(105, 0, '启用', '1', 'nursing_level_status', NULL, 'default', 'N', '0', 'admin', '2024-08-20 03:04:14', '', NULL, NULL),
	(106, 0, '禁用', '0', 'nursing_level_status', NULL, 'default', 'N', '0', 'admin', '2024-08-20 03:04:21', '', NULL, NULL),
	(114, 0, '已入住', '0', 'admission_status', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:58:22', '', NULL, NULL),
	(115, 0, '未入住', '1', 'admission_status', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:58:28', '', NULL, NULL),
	(116, 0, '随身设备', '0', 'device_location_type', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:58:44', '', NULL, NULL),
	(117, 0, '固定设备', '1', 'device_location_type', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:58:51', '', NULL, NULL),
	(118, 0, '评估中', '0', 'evaluation_progress', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:59:03', '', NULL, NULL),
	(119, 0, '已完成', '1', 'evaluation_progress', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:59:09', '', NULL, NULL),
	(120, 0, '已取消', '2', 'evaluation_progress', NULL, 'default', 'N', '0', 'admin', '2026-03-21 12:59:15', '', NULL, NULL);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;

-- 导出  表 xhzb.sys_dict_type 结构
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表';

-- 正在导出表  xhzb.sys_dict_type 的数据：~16 rows (大约)
DELETE FROM `sys_dict_type`;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '用户性别列表'),
	(2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '菜单状态列表'),
	(3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '系统开关列表'),
	(4, '任务状态', 'sys_job_status', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '任务状态列表'),
	(5, '任务分组', 'sys_job_group', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '任务分组列表'),
	(6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '系统是否列表'),
	(7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '通知类型列表'),
	(8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '通知状态列表'),
	(9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '操作类型列表'),
	(10, '系统状态', 'sys_common_status', '0', 'admin', '2024-08-14 02:48:24', '', NULL, '登录状态列表'),
	(100, '护理项目状态', 'nursing_project_status', '0', 'admin', '2024-08-18 02:40:48', '', NULL, NULL),
	(101, '护理计划状态', 'nursing_plan_status', '0', 'admin', '2024-08-19 10:59:40', '', NULL, NULL),
	(102, '护理等级状态', 'nursing_level_status', '0', 'admin', '2024-08-20 03:04:03', '', NULL, NULL),
	(106, '健康评估-入住状态', 'admission_status', '0', 'admin', '2026-03-21 12:57:21', '', NULL, NULL),
	(107, '设备位置', 'device_location_type', '0', 'admin', '2026-03-21 12:57:33', '', NULL, NULL),
	(108, '评估进度', 'evaluation_progress', '0', 'admin', '2026-03-21 12:57:41', '', NULL, NULL);
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;

-- 导出  表 xhzb.sys_job 结构
CREATE TABLE IF NOT EXISTS `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=113 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务调度表';

-- 正在导出表  xhzb.sys_job 的数据：~7 rows (大约)
DELETE FROM `sys_job`;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` (`job_id`, `job_name`, `job_group`, `invoke_target`, `cron_expression`, `misfire_policy`, `concurrent`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-08-14 02:48:24', '', NULL, ''),
	(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-08-14 02:48:24', '', NULL, ''),
	(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-08-14 02:48:24', '', NULL, ''),
	(108, '测试任务', 'DEFAULT', 'helloJob.hello', '0/5 * * * * ?', '1', '1', '1', 'admin', '2026-04-04 01:11:49', 'admin', '2026-04-04 01:42:56', '');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;

-- 导出  表 xhzb.sys_job_log 结构
CREATE TABLE IF NOT EXISTS `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务调度日志表';

-- 正在导出表  xhzb.sys_job_log 的数据：~0 rows (大约)
DELETE FROM `sys_job_log`;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;

-- 导出  表 xhzb.sys_logininfor 结构
CREATE TABLE IF NOT EXISTS `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  KEY `idx_sys_logininfor_s` (`status`) USING BTREE,
  KEY `idx_sys_logininfor_lt` (`login_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统访问记录';

-- 正在导出表  xhzb.sys_logininfor 的数据：~4 rows (大约)
DELETE FROM `sys_logininfor`;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` (`info_id`, `user_name`, `ipaddr`, `login_location`, `browser`, `os`, `status`, `msg`, `login_time`) VALUES
	(1, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-05-25 00:42:10'),
	(2, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-05-25 01:44:49'),
	(3, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-05-25 03:12:47'),
	(4, 'admin', '127.0.0.1', '内网IP', 'Chrome 14', 'Windows 10', '0', '登录成功', '2026-05-25 06:27:24');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;

-- 导出  表 xhzb.sys_menu 结构
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2071 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- 正在导出表  xhzb.sys_menu 的数据：~138 rows (大约)
DELETE FROM `sys_menu`;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '系统管理', 0, 6, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'icon_xtgl', 'admin', '2024-08-14 02:48:23', 'admin', '2026-03-10 11:02:46', '系统管理目录'),
	(2, '系统监控', 0, 7, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'icon_xtjk', 'admin', '2024-08-14 02:48:23', 'admin', '2026-03-10 11:03:03', '系统监控目录'),
	(3, '系统工具', 0, 8, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'icon_xtgj', 'admin', '2024-08-14 02:48:23', 'admin', '2026-03-10 11:03:21', '系统工具目录'),
	(4, '若依官网', 0, 4, 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '1', '0', '', 'guide', 'admin', '2024-08-14 02:48:23', 'admin', '2024-08-20 01:57:10', '若依官网地址'),
	(100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-08-14 02:48:23', '', NULL, '用户管理菜单'),
	(101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-08-14 02:48:23', '', NULL, '角色管理菜单'),
	(102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-08-14 02:48:23', '', NULL, '菜单管理菜单'),
	(103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-08-14 02:48:23', '', NULL, '部门管理菜单'),
	(104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-08-14 02:48:23', '', NULL, '岗位管理菜单'),
	(105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-08-14 02:48:23', '', NULL, '字典管理菜单'),
	(106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-08-14 02:48:23', '', NULL, '参数设置菜单'),
	(107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-08-14 02:48:23', '', NULL, '通知公告菜单'),
	(108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2024-08-14 02:48:23', '', NULL, '日志管理菜单'),
	(109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-08-14 02:48:23', '', NULL, '在线用户菜单'),
	(110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-08-14 02:48:23', '', NULL, '定时任务菜单'),
	(111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2024-08-14 02:48:23', '', NULL, '数据监控菜单'),
	(112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-08-14 02:48:23', '', NULL, '服务监控菜单'),
	(113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2024-08-14 02:48:23', '', NULL, '缓存监控菜单'),
	(114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2024-08-14 02:48:23', '', NULL, '缓存列表菜单'),
	(115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-08-14 02:48:23', '', NULL, '表单构建菜单'),
	(116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-08-14 02:48:23', '', NULL, '代码生成菜单'),
	(117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-08-14 02:48:23', '', NULL, '系统接口菜单'),
	(500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2024-08-14 02:48:23', '', NULL, '操作日志菜单'),
	(501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2024-08-14 02:48:23', '', NULL, '登录日志菜单'),
	(1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(2000, '服务管理', 0, 4, 'serve', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'icon_fwgl', 'admin', '2024-08-14 02:59:12', 'admin', '2026-03-09 11:33:57', ''),
	(2001, '护理项目', 2000, 1, 'project', 'nursing/project/index', NULL, '', 1, 0, 'C', '0', '0', 'nursing:project:list', 'color', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:00', '护理项目菜单'),
	(2002, '护理项目查询', 2001, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:project:query', '#', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:09', ''),
	(2003, '护理项目新增', 2001, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:project:add', '#', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:13', ''),
	(2004, '护理项目修改', 2001, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:project:edit', '#', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:18', ''),
	(2005, '护理项目删除', 2001, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:project:remove', '#', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:21', ''),
	(2006, '护理项目导出', 2001, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:project:export', '#', 'admin', '2024-08-14 03:00:15', 'admin', '2025-04-18 10:24:25', ''),
	(2007, '护理等级', 2000, 1, 'nursingLevel', 'nursing/nursingLevel/index', NULL, '', 1, 0, 'C', '0', '0', 'nursing:nursingLevel:list', 'guide', 'admin', '2024-08-14 08:29:05', 'admin', '2024-08-22 12:16:22', '护理等级菜单'),
	(2008, '护理等级查询', 2007, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingLevel:query', '#', 'admin', '2024-08-14 08:29:05', '', NULL, ''),
	(2009, '护理等级新增', 2007, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingLevel:add', '#', 'admin', '2024-08-14 08:29:05', '', NULL, ''),
	(2010, '护理等级修改', 2007, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingLevel:edit', '#', 'admin', '2024-08-14 08:29:05', '', NULL, ''),
	(2011, '护理等级删除', 2007, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingLevel:remove', '#', 'admin', '2024-08-14 08:29:05', '', NULL, ''),
	(2012, '护理等级导出', 2007, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingLevel:export', '#', 'admin', '2024-08-14 08:29:05', '', NULL, ''),
	(2013, '护理计划', 2000, 1, 'nursingPlan', 'nursing/nursingPlan/index', NULL, '', 1, 0, 'C', '0', '0', 'nursing:nursingPlan:list', 'druid', 'admin', '2024-08-14 08:29:10', 'admin', '2024-08-22 12:16:43', '护理计划菜单'),
	(2014, '护理计划查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingPlan:query', '#', 'admin', '2024-08-14 08:29:10', '', NULL, ''),
	(2015, '护理计划新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingPlan:add', '#', 'admin', '2024-08-14 08:29:10', '', NULL, ''),
	(2016, '护理计划修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingPlan:edit', '#', 'admin', '2024-08-14 08:29:10', '', NULL, ''),
	(2017, '护理计划删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingPlan:remove', '#', 'admin', '2024-08-14 08:29:10', '', NULL, ''),
	(2018, '护理计划导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:nursingPlan:export', '#', 'admin', '2024-08-14 08:29:10', '', NULL, ''),
	(2019, '在住管理', 0, 3, 'liveIn', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'icon_zzgl', 'admin', '2024-08-22 06:49:19', 'admin', '2026-03-09 11:32:42', ''),
	(2020, '房型设置', 2019, 0, 'houseSet', 'nursing/roomType/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'size', 'admin', '2024-08-22 06:50:50', '', NULL, ''),
	(2021, '床位预览', 2019, 2, 'floor', 'nursing/floor/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'tree-table', 'admin', '2024-08-22 08:15:05', '', NULL, ''),
	(2022, '入退管理', 0, 2, 'enterQuit', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'component', 'admin', '2024-08-23 12:52:34', 'admin', '2024-08-29 06:42:15', ''),
	(2023, '入住办理', 2022, 1, 'checkIn', 'nursing/checkIn/index', NULL, '', 1, 0, 'C', '0', '0', 'nursing:checkIn:list', 'edit', 'admin', '2024-08-23 13:13:48', 'admin', '2024-08-24 02:58:12', '入住菜单'),
	(2024, '入住查询', 2023, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:checkIn:query', '#', 'admin', '2024-08-23 13:13:48', '', NULL, ''),
	(2025, '入住申请', 2023, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:checkIn:add', '#', 'admin', '2024-08-23 13:13:48', 'admin', '2026-03-18 16:57:30', ''),
	(2026, '入住详情', 2023, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:checkIn:edit', '#', 'admin', '2024-08-23 13:13:48', 'admin', '2026-03-18 16:57:51', ''),
	(2027, '入住删除', 2023, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:checkIn:remove', '#', 'admin', '2024-08-23 13:13:48', '', NULL, ''),
	(2028, '入住导出', 2023, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:checkIn:export', '#', 'admin', '2024-08-23 13:13:48', '', NULL, ''),
	(2035, '入住详情', 2022, 0, 'checkInInfo', 'nursing/checkIn/details', NULL, '', 1, 0, 'C', '1', '0', NULL, 'checkbox', 'admin', '2024-08-24 03:44:48', '', NULL, ''),
	(2037, '智能监测', 0, 5, 'intelligence', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'icon_znjc', 'admin', '2024-08-29 06:43:14', 'admin', '2026-03-09 11:35:17', ''),
	(2039, '健康评估', 2022, 0, 'healthAssessment', 'nursing/healthAssessment/index', NULL, '', 1, 0, 'C', '0', '0', '', 'eye', 'admin', '2024-08-29 06:48:51', 'admin', '2026-03-06 16:51:20', ''),
	(2040, '评估详情', 2022, 2, 'healthDetails', 'nursing/healthAssessment/details', NULL, '', 1, 0, 'F', '1', '0', 'enterQuit:healthAssessment:details', '#', 'admin', '2024-08-29 06:49:53', 'admin', '2026-03-18 16:37:46', ''),
	(2041, '负责老人', 2000, 4, 'oldPeople', 'nursing/oldPeople/index', NULL, '', 1, 0, 'C', '0', '0', '', 'peoples', 'admin', '2024-08-29 06:52:12', 'admin', '2024-08-29 06:52:23', ''),
	(2042, '任务安排', 2000, 5, 'arrange', 'nursing/arrange/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'redis-list', 'admin', '2024-08-29 06:53:06', '', NULL, ''),
	(2043, '任务安排详情', 2000, 1, 'arrangeDetails', 'nursing/arrange/details', NULL, '', 1, 0, 'C', '1', '0', NULL, '#', 'admin', '2024-08-29 06:53:37', '', NULL, ''),
	(2044, '设备管理', 2037, 0, 'device', 'nursing/device/index', NULL, '', 1, 0, 'C', '0', '0', '', 'tool', 'admin', '2024-08-29 06:54:54', 'admin', '2024-08-29 07:13:42', ''),
	(2045, '设备详情', 2037, 0, 'details', 'nursing/device/details', NULL, '', 1, 0, 'C', '1', '0', NULL, '#', 'admin', '2024-08-29 06:55:25', '', NULL, ''),
	(2046, '新增报警规则', 2037, 0, 'ruleDetails', 'nursing/alertRule/details', NULL, '', 1, 0, 'C', '1', '0', NULL, '#', 'admin', '2024-08-29 06:56:01', '', NULL, ''),
	(2047, '报警规则', 2037, 1, 'alertRule', 'nursing/alertRule/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'nested', 'admin', '2024-08-29 06:56:48', '', NULL, ''),
	(2048, '报警数据', 2037, 2, 'alertData', 'nursing/alertData/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'skill', 'admin', '2024-08-29 06:57:25', '', NULL, ''),
	(2050, '智能床位', 2019, 3, 'smartBed', 'nursing/smartBed/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'example', 'admin', '2025-05-04 01:06:31', '', NULL, ''),
	(2052, '星海智询', 0, 0, 'zhixun', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'icon_xhzx', 'admin', '2025-07-16 22:26:29', 'admin', '2026-03-10 11:02:23', ''),
	(2053, '小智', 2052, 1, 'xiaozhi', 'nursing/zhixun/index', NULL, '', 1, 0, 'C', '0', '0', '', 'drag', 'admin', '2025-07-16 22:27:41', 'admin', '2025-07-16 23:33:43', ''),
	(2055, '知识库', 2052, 1, 'knowledgeBase', 'nursing/knowledgeBase/index', NULL, '', 1, 0, 'C', '0', '0', 'nursing:knowledgeBase:list', 'education', 'admin', '2025-07-21 23:06:50', 'admin', '2025-07-21 23:12:37', '知识库菜单'),
	(2056, '知识库查询', 2055, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:knowledgeBase:query', '#', 'admin', '2025-07-21 23:06:50', '', NULL, ''),
	(2057, '知识库新增', 2055, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:knowledgeBase:add', '#', 'admin', '2025-07-21 23:06:50', '', NULL, ''),
	(2058, '知识库修改', 2055, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:knowledgeBase:edit', '#', 'admin', '2025-07-21 23:06:50', '', NULL, ''),
	(2059, '知识库删除', 2055, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:knowledgeBase:remove', '#', 'admin', '2025-07-21 23:06:50', '', NULL, ''),
	(2060, '知识库导出', 2055, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'nursing:knowledgeBase:export', '#', 'admin', '2025-07-21 23:06:50', '', NULL, ''),
	(2061, '新增修改评估', 2022, 3, 'detailsSteps', 'nursing/healthAssessment/detailsSteps', NULL, '', 1, 0, 'F', '1', '0', 'enterQuit:healthAssessment:detailsSteps', 'drag', 'admin', '2026-02-05 11:51:18', 'admin', '2026-03-18 16:36:39', ''),
	(2062, '来访管理', 0, 1, 'appointment', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'icon_laifang', 'admin', '2026-03-10 17:00:58', 'admin', '2026-03-11 09:42:29', ''),
	(2063, '预约登记', 2062, 0, 'reservation', 'nursing/reservation/index', NULL, '', 1, 0, 'C', '0', '0', '', '#', 'admin', '2026-03-10 17:03:30', 'admin', '2026-03-10 17:07:15', '');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

-- 导出  表 xhzb.sys_notice 结构
CREATE TABLE IF NOT EXISTS `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='通知公告表';

-- 正在导出表  xhzb.sys_notice 的数据：~2 rows (大约)
DELETE FROM `sys_notice`;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` (`notice_id`, `notice_title`, `notice_type`, `notice_content`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', _binary 0xe696b0e78988e69cace58685e5aeb9, '0', 'admin', '2024-08-14 02:48:24', '', NULL, '管理员'),
	(2, '维护通知：2018-07-01 若依系统凌晨维护', '1', _binary 0xe7bbb4e68aa4e58685e5aeb9, '0', 'admin', '2024-08-14 02:48:24', '', NULL, '管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;

-- 导出  表 xhzb.sys_oper_log 结构
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  KEY `idx_sys_oper_log_bt` (`business_type`) USING BTREE,
  KEY `idx_sys_oper_log_s` (`status`) USING BTREE,
  KEY `idx_sys_oper_log_ot` (`oper_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='操作日志记录';

-- 正在导出表  xhzb.sys_oper_log 的数据：~3 rows (大约)
DELETE FROM `sys_oper_log`;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` (`oper_id`, `title`, `business_type`, `method`, `request_method`, `operator_type`, `oper_name`, `dept_name`, `oper_url`, `oper_ip`, `oper_location`, `oper_param`, `json_result`, `status`, `error_msg`, `oper_time`, `cost_time`) VALUES
	(1, '操作日志', 9, 'com.xhzb.web.controller.monitor.SysOperlogController.clean()', 'DELETE', 1, 'admin', '院长办公室', '/monitor/operlog/clean', '127.0.0.1', '内网IP', '', '{`msg`:`操作成功`,`code`:200}', 0, '', '2026-04-11 08:35:46', 75),
	(2, '登录日志', 9, 'com.xhzb.web.controller.monitor.SysLogininforController.clean()', 'DELETE', 1, 'admin', '院长办公室', '/monitor/logininfor/clean', '127.0.0.1', '内网IP', '', '{`msg`:`操作成功`,`code`:200}', 0, '', '2026-04-11 08:35:50', 54),
	(3, '调度日志', 9, 'com.xhzb.quartz.controller.SysJobLogController.clean()', 'DELETE', 1, 'admin', '院长办公室', '/monitor/jobLog/clean', '127.0.0.1', '内网IP', '', '{`msg`:`操作成功`,`code`:200}', 0, '', '2026-04-11 08:35:59', 47);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;

-- 导出  表 xhzb.sys_post 结构
CREATE TABLE IF NOT EXISTS `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='岗位信息表';

-- 正在导出表  xhzb.sys_post 的数据：~4 rows (大约)
DELETE FROM `sys_post`;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 'ceo', '董事长', 1, '0', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(2, 'se', '项目经理', 2, '0', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(3, 'hr', '人力资源', 3, '0', 'admin', '2024-08-14 02:48:23', '', NULL, ''),
	(4, 'user', '普通员工', 4, '0', 'admin', '2024-08-14 02:48:23', '', NULL, '');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;

-- 导出  表 xhzb.sys_role 结构
CREATE TABLE IF NOT EXISTS `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色信息表';

-- 正在导出表  xhzb.sys_role 的数据：~7 rows (大约)
DELETE FROM `sys_role`;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `menu_check_strictly`, `dept_check_strictly`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-08-14 02:48:23', '', NULL, '超级管理员'),
	(2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2024-08-14 02:48:23', '', NULL, '普通角色'),
	(100, '行政主管', 'sys_role', 0, '1', 1, 1, '0', '0', 'admin', '2025-04-25 15:21:26', 'admin', '2025-07-16 19:25:31', NULL),
	(101, '院长', 'yuanzhang', 0, '1', 1, 1, '0', '0', 'admin', '2025-04-26 01:33:42', 'admin', '2026-04-06 01:38:22', NULL),
	(102, '护理员', 'nursing_elder', 5, '1', 1, 1, '0', '0', 'admin', '2025-05-04 04:14:26', 'admin', '2026-03-30 02:24:18', NULL),
	(103, '行政', 'administrator', 77, '1', 1, 1, '0', '0', 'admin', '2025-05-04 04:15:38', 'admin', '2025-07-16 19:25:15', NULL),
	(104, '测试', '123', 0, '1', 1, 1, '0', '0', 'admin', '2026-03-10 15:14:27', 'admin', '2026-03-16 17:56:55', NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

-- 导出  表 xhzb.sys_role_dept 结构
CREATE TABLE IF NOT EXISTS `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色和部门关联表';

-- 正在导出表  xhzb.sys_role_dept 的数据：~3 rows (大约)
DELETE FROM `sys_role_dept`;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES
	(2, 100),
	(2, 101),
	(2, 105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;

-- 导出  表 xhzb.sys_role_menu 结构
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';

-- 正在导出表  xhzb.sys_role_menu 的数据：~442 rows (大约)
DELETE FROM `sys_role_menu`;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
	(2, 1),
	(2, 2),
	(2, 3),
	(2, 4),
	(2, 100),
	(2, 101),
	(2, 102),
	(2, 103),
	(2, 104),
	(2, 105),
	(2, 106),
	(2, 107),
	(2, 108),
	(2, 109),
	(2, 110),
	(2, 111),
	(2, 112),
	(2, 113),
	(2, 114),
	(2, 115),
	(2, 116),
	(2, 117),
	(2, 500),
	(2, 501),
	(2, 1000),
	(2, 1001),
	(2, 1002),
	(2, 1003),
	(2, 1004),
	(2, 1005),
	(2, 1006),
	(2, 1007),
	(2, 1008),
	(2, 1009),
	(2, 1010),
	(2, 1011),
	(2, 1012),
	(2, 1013),
	(2, 1014),
	(2, 1015),
	(2, 1016),
	(2, 1017),
	(2, 1018),
	(2, 1019),
	(2, 1020),
	(2, 1021),
	(2, 1022),
	(2, 1023),
	(2, 1024),
	(2, 1025),
	(2, 1026),
	(2, 1027),
	(2, 1028),
	(2, 1029),
	(2, 1030),
	(2, 1031),
	(2, 1032),
	(2, 1033),
	(2, 1034),
	(2, 1035),
	(2, 1036),
	(2, 1037),
	(2, 1038),
	(2, 1039),
	(2, 1040),
	(2, 1041),
	(2, 1042),
	(2, 1043),
	(2, 1044),
	(2, 1045),
	(2, 1046),
	(2, 1047),
	(2, 1048),
	(2, 1049),
	(2, 1050),
	(2, 1051),
	(2, 1052),
	(2, 1053),
	(2, 1054),
	(2, 1055),
	(2, 1056),
	(2, 1057),
	(2, 1058),
	(2, 1059),
	(2, 1060),
	(100, 1),
	(100, 2),
	(100, 3),
	(100, 100),
	(100, 101),
	(100, 102),
	(100, 103),
	(100, 104),
	(100, 105),
	(100, 106),
	(100, 107),
	(100, 108),
	(100, 109),
	(100, 110),
	(100, 111),
	(100, 112),
	(100, 113),
	(100, 114),
	(100, 115),
	(100, 116),
	(100, 117),
	(100, 500),
	(100, 501),
	(100, 1000),
	(100, 1001),
	(100, 1002),
	(100, 1003),
	(100, 1004),
	(100, 1005),
	(100, 1006),
	(100, 1007),
	(100, 1008),
	(100, 1009),
	(100, 1010),
	(100, 1011),
	(100, 1012),
	(100, 1013),
	(100, 1014),
	(100, 1015),
	(100, 1016),
	(100, 1017),
	(100, 1018),
	(100, 1019),
	(100, 1020),
	(100, 1021),
	(100, 1022),
	(100, 1023),
	(100, 1024),
	(100, 1025),
	(100, 1026),
	(100, 1027),
	(100, 1028),
	(100, 1029),
	(100, 1030),
	(100, 1031),
	(100, 1032),
	(100, 1033),
	(100, 1034),
	(100, 1035),
	(100, 1036),
	(100, 1037),
	(100, 1038),
	(100, 1039),
	(100, 1040),
	(100, 1041),
	(100, 1042),
	(100, 1043),
	(100, 1044),
	(100, 1045),
	(100, 1046),
	(100, 1047),
	(100, 1048),
	(100, 1049),
	(100, 1050),
	(100, 1051),
	(100, 1052),
	(100, 1053),
	(100, 1054),
	(100, 1055),
	(100, 1056),
	(100, 1057),
	(100, 1058),
	(100, 1059),
	(100, 1060),
	(100, 2000),
	(100, 2001),
	(100, 2002),
	(100, 2003),
	(100, 2004),
	(100, 2005),
	(100, 2006),
	(100, 2007),
	(100, 2008),
	(100, 2009),
	(100, 2010),
	(100, 2011),
	(100, 2012),
	(100, 2013),
	(100, 2014),
	(100, 2015),
	(100, 2016),
	(100, 2017),
	(100, 2018),
	(100, 2037),
	(100, 2041),
	(100, 2042),
	(100, 2043),
	(100, 2044),
	(100, 2045),
	(100, 2046),
	(100, 2047),
	(100, 2048),
	(101, 1),
	(101, 2),
	(101, 3),
	(101, 100),
	(101, 101),
	(101, 102),
	(101, 103),
	(101, 104),
	(101, 105),
	(101, 106),
	(101, 107),
	(101, 108),
	(101, 109),
	(101, 110),
	(101, 111),
	(101, 112),
	(101, 113),
	(101, 114),
	(101, 115),
	(101, 116),
	(101, 117),
	(101, 500),
	(101, 501),
	(101, 1000),
	(101, 1001),
	(101, 1002),
	(101, 1003),
	(101, 1004),
	(101, 1005),
	(101, 1006),
	(101, 1007),
	(101, 1008),
	(101, 1009),
	(101, 1010),
	(101, 1011),
	(101, 1012),
	(101, 1013),
	(101, 1014),
	(101, 1015),
	(101, 1016),
	(101, 1017),
	(101, 1018),
	(101, 1019),
	(101, 1020),
	(101, 1021),
	(101, 1022),
	(101, 1023),
	(101, 1024),
	(101, 1025),
	(101, 1026),
	(101, 1027),
	(101, 1028),
	(101, 1029),
	(101, 1030),
	(101, 1031),
	(101, 1032),
	(101, 1033),
	(101, 1034),
	(101, 1035),
	(101, 1036),
	(101, 1037),
	(101, 1038),
	(101, 1039),
	(101, 1040),
	(101, 1041),
	(101, 1042),
	(101, 1043),
	(101, 1044),
	(101, 1045),
	(101, 1046),
	(101, 1047),
	(101, 1048),
	(101, 1049),
	(101, 1050),
	(101, 1051),
	(101, 1052),
	(101, 1053),
	(101, 1054),
	(101, 1055),
	(101, 1056),
	(101, 1057),
	(101, 1058),
	(101, 1059),
	(101, 1060),
	(101, 2000),
	(101, 2001),
	(101, 2002),
	(101, 2003),
	(101, 2004),
	(101, 2005),
	(101, 2006),
	(101, 2007),
	(101, 2008),
	(101, 2009),
	(101, 2010),
	(101, 2011),
	(101, 2012),
	(101, 2013),
	(101, 2014),
	(101, 2015),
	(101, 2016),
	(101, 2017),
	(101, 2018),
	(101, 2019),
	(101, 2020),
	(101, 2021),
	(101, 2022),
	(101, 2023),
	(101, 2024),
	(101, 2025),
	(101, 2026),
	(101, 2027),
	(101, 2028),
	(101, 2035),
	(101, 2037),
	(101, 2039),
	(101, 2040),
	(101, 2041),
	(101, 2042),
	(101, 2043),
	(101, 2044),
	(101, 2045),
	(101, 2046),
	(101, 2047),
	(101, 2048),
	(102, 2000),
	(102, 2001),
	(102, 2002),
	(102, 2003),
	(102, 2004),
	(102, 2005),
	(102, 2006),
	(102, 2007),
	(102, 2008),
	(102, 2009),
	(102, 2010),
	(102, 2011),
	(102, 2012),
	(102, 2013),
	(102, 2014),
	(102, 2015),
	(102, 2016),
	(102, 2017),
	(102, 2018),
	(102, 2019),
	(102, 2020),
	(102, 2021),
	(102, 2037),
	(102, 2041),
	(102, 2042),
	(102, 2043),
	(102, 2048),
	(102, 2050),
	(102, 2052),
	(102, 2053),
	(103, 1),
	(103, 2),
	(103, 3),
	(103, 100),
	(103, 101),
	(103, 102),
	(103, 103),
	(103, 104),
	(103, 105),
	(103, 106),
	(103, 107),
	(103, 108),
	(103, 109),
	(103, 110),
	(103, 111),
	(103, 112),
	(103, 113),
	(103, 114),
	(103, 115),
	(103, 116),
	(103, 117),
	(103, 500),
	(103, 501),
	(103, 1000),
	(103, 1001),
	(103, 1002),
	(103, 1003),
	(103, 1004),
	(103, 1005),
	(103, 1006),
	(103, 1007),
	(103, 1008),
	(103, 1009),
	(103, 1010),
	(103, 1011),
	(103, 1012),
	(103, 1013),
	(103, 1014),
	(103, 1015),
	(103, 1016),
	(103, 1017),
	(103, 1018),
	(103, 1019),
	(103, 1020),
	(103, 1021),
	(103, 1022),
	(103, 1023),
	(103, 1024),
	(103, 1025),
	(103, 1026),
	(103, 1027),
	(103, 1028),
	(103, 1029),
	(103, 1030),
	(103, 1031),
	(103, 1032),
	(103, 1033),
	(103, 1034),
	(103, 1035),
	(103, 1036),
	(103, 1037),
	(103, 1038),
	(103, 1039),
	(103, 1040),
	(103, 1041),
	(103, 1042),
	(103, 1043),
	(103, 1044),
	(103, 1045),
	(103, 1046),
	(103, 1047),
	(103, 1048),
	(103, 1049),
	(103, 1050),
	(103, 1051),
	(103, 1052),
	(103, 1053),
	(103, 1054),
	(103, 1055),
	(103, 1056),
	(103, 1057),
	(103, 1058),
	(103, 1059),
	(103, 1060),
	(103, 2037),
	(103, 2044),
	(103, 2045),
	(103, 2046),
	(103, 2047),
	(103, 2048);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

-- 导出  表 xhzb.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_eo_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_eo_0900_ai_ci COMMENT='用户信息表';

-- 正在导出表  xhzb.sys_user 的数据：~5 rows (大约)
DELETE FROM `sys_user`;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`user_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `phonenumber`, `sex`, `avatar`, `password`, `status`, `del_flag`, `login_ip`, `login_date`, `pwd_update_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
	(1, 103, 'admin', 'admin', '00', 'ry@163.com', '15888888888', '0', '', '$2a$10$jUXh5TJxP0A04bhrmZCDOOGrAvj8oii7HvovUH7L8rT.jvQpgcQm2', '0', '0', '127.0.0.1', '2026-05-25 14:27:25', '2025-06-13 17:13:43', 'admin', '2025-06-13 17:13:43', '', '2026-05-25 06:27:24', '管理员'),
	(2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-06-13 17:13:43', '2025-06-13 17:13:43', 'admin', '2025-06-13 17:13:43', '', NULL, '测试员'),
	(100, 201, 'xiaobai', '小白', '00', '', '', '0', '', '$2a$10$2bkX7Yl06FUbYiKtqpzH7enQTBmgSXufSZSTqm4AFNeN96n6zfWNG', '0', '0', '127.0.0.1', '2026-04-06 09:00:26', NULL, 'admin', '2026-03-01 21:44:50', 'admin', '2026-04-06 01:00:24', NULL),
	(101, 201, 'xiaoqing', '小青', '00', '', '', '0', '', '$2a$10$AZhhxUbX.IWNdlKeCH.CjeLHR3uktv/kFPM8ycZDIT.wnfeKOgxyG', '0', '0', '', NULL, NULL, 'admin', '2026-03-01 21:45:07', 'admin', '2026-03-01 21:45:47', NULL),
	(108, 200, 'laozhang', '老张', '00', '', '', '0', '', '$2a$10$Wdd6TVwEFAjVFbFOFYNDa.akX14eZyfpQJM6k/G5xFcl4RcMIiC/.', '0', '0', '', NULL, NULL, 'admin', '2026-04-10 09:13:15', '', NULL, NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

-- 导出  表 xhzb.sys_user_post 结构
CREATE TABLE IF NOT EXISTS `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';

-- 正在导出表  xhzb.sys_user_post 的数据：~2 rows (大约)
DELETE FROM `sys_user_post`;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` (`user_id`, `post_id`) VALUES
	(1, 1),
	(108, 4);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;

-- 导出  表 xhzb.sys_user_role 结构
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联表';

-- 正在导出表  xhzb.sys_user_role 的数据：~4 rows (大约)
DELETE FROM `sys_user_role`;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
	(1, 1),
	(100, 102),
	(101, 102),
	(108, 103);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
