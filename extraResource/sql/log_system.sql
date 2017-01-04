/*
Navicat MySQL Data Transfer

Source Server         : 120.77.59.164
Source Server Version : 50619
Source Host           : 120.77.59.164:3306
Source Database       : log_system

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2017-01-04 11:08:14
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collector
-- ----------------------------
DROP TABLE IF EXISTS `collector`;
CREATE TABLE `collector` (
  `id`           INT(11)      NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(255) NOT NULL
  COMMENT '采集器名:如深三,北大',
  `group_id`     INT(11)               DEFAULT NULL,
  `service_name` VARCHAR(255)          DEFAULT NULL
  COMMENT '采集器对应的服务名',
  `create_time`  DATETIME              DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `name`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id`          INT(11) NOT NULL,
  `key`         VARCHAR(255) DEFAULT NULL,
  `alias`       VARCHAR(255) DEFAULT NULL,
  `value`       VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id`          INT(11) NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255)     DEFAULT NULL,
  `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for keyword_index
-- ----------------------------
DROP TABLE IF EXISTS `keyword_index`;
CREATE TABLE `keyword_index` (
  `id`          INT(11) NOT NULL AUTO_INCREMENT,
  `keyword`     VARCHAR(255)     DEFAULT NULL,
  `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for kv_index
-- ----------------------------
DROP TABLE IF EXISTS `kv_index`;
CREATE TABLE `kv_index` (
  `id`          INT(11) NOT NULL AUTO_INCREMENT,
  `key`         VARCHAR(255)     DEFAULT NULL,
  `value`       VARCHAR(255)     DEFAULT NULL,
  `create_time` DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for mid_collector_keyword
-- ----------------------------
DROP TABLE IF EXISTS `mid_collector_keyword`;
CREATE TABLE `mid_collector_keyword` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `collector_id` INT(11) NOT NULL,
  `keyword_id`   INT(11)          DEFAULT NULL,
  `create_time`  DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for mid_collector_kv
-- ----------------------------
DROP TABLE IF EXISTS `mid_collector_kv`;
CREATE TABLE `mid_collector_kv` (
  `id`           INT(11) NOT NULL,
  `collector_id` INT(11)  DEFAULT NULL,
  `kv_id`        INT(11)  DEFAULT NULL,
  `create_time`  DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for store_record
-- ----------------------------
DROP TABLE IF EXISTS `store_record`;
CREATE TABLE `store_record` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `collector_id` INT(11)          DEFAULT NULL
  COMMENT '采集器id',
  `name`         VARCHAR(255)     DEFAULT NULL
  COMMENT '容器名,容器tag',
  `image`        VARCHAR(255)     DEFAULT NULL
  COMMENT '镜像名',
  `outer_ip`     VARCHAR(255)     DEFAULT NULL
  COMMENT '外网ip:docker容器宿主机外网ip',
  `outer_port`   INT(11)          DEFAULT NULL
  COMMENT '容器expose的宿主机端口',
  `inner_ip`     VARCHAR(255)     DEFAULT NULL
  COMMENT '内网ip:docker容易一般会使用172.17开头的内网ip',
  `inner_port`   INT(11)          DEFAULT NULL
  COMMENT '容器内部expose的端口',
  `create_time`  DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
