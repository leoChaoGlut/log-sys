/*
Navicat MySQL Data Transfer

Source Server Version : 50619
Source Database       : log_sys

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2017-05-03 10:43:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for log_collected_item
-- ----------------------------
DROP TABLE IF EXISTS `log_collected_item`;
CREATE TABLE `log_collected_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '采集项名称',
  `collector_id` int(11) NOT NULL,
  `stdout_file_path` varchar(255) DEFAULT 'nohup.out' COMMENT '控制台输出文件名称,如console.out,nohup.out',
  `collected_log_dir` varchar(255) NOT NULL COMMENT '被采集的日志的根目录',
  `application_name` varchar(255) DEFAULT NULL COMMENT '被采集的应用名,需唯一.',
  PRIMARY KEY (`id`),
  KEY `log_collected_item_collector_id` (`collector_id`),
  CONSTRAINT `log_collected_item_collector_id` FOREIGN KEY (`collector_id`) REFERENCES `log_collector` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_collector
-- ----------------------------
DROP TABLE IF EXISTS `log_collector`;
CREATE TABLE `log_collector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  `port` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '采集节点名称(如：前置机-深三-测试)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `reachable` tinyint(4) DEFAULT '0' COMMENT '0: false, 1:true 该采集节点是否可以直接通过外网访问',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `application_name` varchar(255) NOT NULL COMMENT '采集器的spring.application.name，如: collector-shensan-test',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_keyword_tag
-- ----------------------------
DROP TABLE IF EXISTS `log_keyword_tag`;
CREATE TABLE `log_keyword_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_kv_tag
-- ----------------------------
DROP TABLE IF EXISTS `log_kv_tag`;
CREATE TABLE `log_kv_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL,
  `key_tag` varchar(255) DEFAULT NULL,
  `value_end_tag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_mid_collected_item_keyword
-- ----------------------------
DROP TABLE IF EXISTS `log_mid_collected_item_keyword`;
CREATE TABLE `log_mid_collected_item_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collected_item_id` int(11) DEFAULT NULL,
  `keyword_tag_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `log_mid_collected_item_keyword_collected_item_id` (`collected_item_id`),
  KEY `log_mid_collected_item_keyword_keyword_id` (`keyword_tag_id`),
  CONSTRAINT `log_mid_collected_item_keyword_keyword_id` FOREIGN KEY (`keyword_tag_id`) REFERENCES `log_keyword_tag` (`id`),
  CONSTRAINT `log_mid_collected_item_keyword_collected_item_id` FOREIGN KEY (`collected_item_id`) REFERENCES `log_collected_item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_mid_collected_item_kv
-- ----------------------------
DROP TABLE IF EXISTS `log_mid_collected_item_kv`;
CREATE TABLE `log_mid_collected_item_kv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collected_item_id` int(11) DEFAULT NULL,
  `kv_tag_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `log_mid_collected_item_kv_collected_item_id` (`collected_item_id`),
  KEY `log_mid_collected_item_kv_kv_tag_id` (`kv_tag_id`),
  CONSTRAINT `log_mid_collected_item_kv_kv_tag_id` FOREIGN KEY (`kv_tag_id`) REFERENCES `log_kv_tag` (`id`),
  CONSTRAINT `log_mid_collected_item_kv_collected_item_id` FOREIGN KEY (`collected_item_id`) REFERENCES `log_collected_item` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
