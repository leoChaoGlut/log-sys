/*
Navicat MySQL Data Transfer

Source Server         : vm_nat
Source Server Version : 50716
Source Host           : 192.168.75.129:3306
Source Database       : log_system

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2016-11-28 14:40:12
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
  `service_name` VARCHAR(255)          DEFAULT NULL
  COMMENT '采集器对应的服务名',
  `create_time`  DATETIME              DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `name`)
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
