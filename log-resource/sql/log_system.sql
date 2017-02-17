/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50710
Source Host           : 127.0.0.1:3306
Source Database       : log_system

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2017-01-08 00:12:06
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for collector
-- ----------------------------
DROP TABLE IF EXISTS `collector`;
CREATE TABLE `collector` (
  `name`             VARCHAR(255) NOT NULL
  COMMENT '采集器名:如深三,北大',
  `application_name` VARCHAR(255) NOT NULL
  COMMENT '采集器对应的服务名 spring.application.name',
  `group`            VARCHAR(255) DEFAULT NULL
  COMMENT '所属分组',
  `create_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`name`)
)
  ENGINE = InnoDB
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
-- Table structure for keyword_tag
-- ----------------------------
DROP TABLE IF EXISTS `keyword_tag`;
CREATE TABLE `keyword_tag` (
  `id`               INT(11) NOT NULL AUTO_INCREMENT,
  `collector_name`   VARCHAR(255)     DEFAULT NULL,
  `application_name` VARCHAR(255)     DEFAULT NULL,
  `keyword`          VARCHAR(255)     DEFAULT NULL,
  `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for key_value_tag
-- ----------------------------
DROP TABLE IF EXISTS `key_value_tag`;
CREATE TABLE `key_value_tag` (
  `id`               INT(11) NOT NULL AUTO_INCREMENT,
  `collector_name`   VARCHAR(255)     DEFAULT NULL
  COMMENT '所属采集器名',
  `application_name` VARCHAR(255)     DEFAULT NULL
  COMMENT '所属应用名 spring.application.name',
  `key`              VARCHAR(255)     DEFAULT NULL,
  `key_tag`          VARCHAR(255)     DEFAULT NULL,
  `value_end_tag`    VARCHAR(255)     DEFAULT NULL,
  `create_time`      DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for store_record
-- ----------------------------
DROP TABLE IF EXISTS `store_record`;
CREATE TABLE `store_record` (
  `id`             INT(11) NOT NULL AUTO_INCREMENT,
  `collector_name` VARCHAR(255)     DEFAULT NULL
  COMMENT '采集器名',
  `name`           VARCHAR(255)     DEFAULT NULL
  COMMENT '容器名,容器tag',
  `image`          VARCHAR(255)     DEFAULT NULL
  COMMENT '镜像名',
  `outer_ip`       VARCHAR(255)     DEFAULT NULL
  COMMENT '外网ip:docker容器宿主机外网ip',
  `outer_port`     INT(11)          DEFAULT NULL
  COMMENT '容器expose的宿主机端口',
  `inner_ip`       VARCHAR(255)     DEFAULT NULL
  COMMENT '内网ip:docker容易一般会使用172.17开头的内网ip',
  `inner_port`     INT(11)          DEFAULT NULL
  COMMENT '容器内部expose的端口',
  `create_time`    DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;
