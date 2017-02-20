/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : log_sys

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2017-02-17 15:12:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for collector
-- ----------------------------
DROP TABLE IF EXISTS `collector`;
CREATE TABLE `collector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '采集节点名称(如：shensan,beida)',
  `group_id` int(11) DEFAULT NULL COMMENT '分组id',
  `service_name` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '分组名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for keyword_index
-- ----------------------------
DROP TABLE IF EXISTS `keyword_index`;
CREATE TABLE `keyword_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for kv_index
-- ----------------------------
DROP TABLE IF EXISTS `kv_index`;
CREATE TABLE `kv_index` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mid_collector_keyword
-- ----------------------------
DROP TABLE IF EXISTS `mid_collector_keyword`;
CREATE TABLE `mid_collector_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collector_id` int(11) DEFAULT NULL,
  `keyword_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mid_collector_kv
-- ----------------------------
DROP TABLE IF EXISTS `mid_collector_kv`;
CREATE TABLE `mid_collector_kv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collector_id` int(11) DEFAULT NULL,
  `kv_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store_record
-- ----------------------------
DROP TABLE IF EXISTS `store_record`;
CREATE TABLE `store_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collector_name` varchar(255) DEFAULT NULL COMMENT '采集器名',
  `name` varchar(255) DEFAULT NULL COMMENT '容器名,容器tag',
  `image` varchar(255) DEFAULT NULL COMMENT '镜像名',
  `outer_ip` varchar(255) DEFAULT NULL COMMENT '外网ip:docker容器宿主机外网ip',
  `outer_port` int(11) DEFAULT NULL COMMENT '容器expose的宿主机端口',
  `inner_ip` varchar(255) DEFAULT NULL COMMENT '内网ip:docker容易一般会使用172.17开头的内网ip',
  `inner_port` int(11) DEFAULT NULL COMMENT '容器内部expose的端口',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
