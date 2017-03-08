/*
Navicat MySQL Data Transfer

Source Server Version : 50619
Source Database       : log_sys

Target Server Type    : MYSQL
Target Server Version : 50619
File Encoding         : 65001

Date: 2017-03-01 14:40:06
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for log_collected_item
-- ----------------------------
DROP TABLE IF EXISTS `log_collected_item`;
CREATE TABLE `log_collected_item` (
  `id`                INT(11)      NOT NULL AUTO_INCREMENT,
  `collector_id`      INT(11)      NOT NULL,
  `name`              VARCHAR(255) NOT NULL
  COMMENT '采集项名称',
  `collected_log_dir` VARCHAR(255) NOT NULL
  COMMENT '被采集的日志的根目录',
  PRIMARY KEY (`id`),
  KEY `log_collected_item_collector_id` (`collector_id`),
  CONSTRAINT `log_collected_item_collector_id` FOREIGN KEY (`collector_id`) REFERENCES `log_collector` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_collector
-- ----------------------------
DROP TABLE IF EXISTS `log_collector`;
CREATE TABLE `log_collector` (
  `id`               INT(11)      NOT NULL AUTO_INCREMENT,
  `name`             VARCHAR(255) NOT NULL
  COMMENT '采集节点名称(如：前置机-深三-测试)',
  `application_name` VARCHAR(255) NOT NULL
  COMMENT 'spring.application.name，如: collector-shensan-test',
  `remark`           VARCHAR(255)          DEFAULT NULL
  COMMENT '备注',
  `create_time`      DATETIME              DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_keyword_tag
-- ----------------------------
DROP TABLE IF EXISTS `log_keyword_tag`;
CREATE TABLE `log_keyword_tag` (
  `id`      INT(11) NOT NULL AUTO_INCREMENT,
  `keyword` VARCHAR(255)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_kv_tag
-- ----------------------------
DROP TABLE IF EXISTS `log_kv_tag`;
CREATE TABLE `log_kv_tag` (
  `id`            INT(11) NOT NULL AUTO_INCREMENT,
  `key`           VARCHAR(255)     DEFAULT NULL,
  `key_tag`       VARCHAR(255)     DEFAULT NULL,
  `value_end_tag` VARCHAR(255)     DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_mid_collected_item_keyword
-- ----------------------------
DROP TABLE IF EXISTS `log_mid_collected_item_keyword`;
CREATE TABLE `log_mid_collected_item_keyword` (
  `id`                INT(11) NOT NULL AUTO_INCREMENT,
  `collected_item_id` INT(11)          DEFAULT NULL,
  `keyword_tag_id`    INT(11)          DEFAULT NULL,
  `create_time`       DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `log_mid_collected_item_keyword_collected_item_id` (`collected_item_id`),
  KEY `log_mid_collected_item_keyword_keyword_id` (`keyword_tag_id`),
  CONSTRAINT `log_mid_collected_item_keyword_keyword_id` FOREIGN KEY (`keyword_tag_id`) REFERENCES `log_keyword_tag` (`id`),
  CONSTRAINT `log_mid_collected_item_keyword_collected_item_id` FOREIGN KEY (`collected_item_id`) REFERENCES `log_collected_item` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_mid_collected_item_kv
-- ----------------------------
DROP TABLE IF EXISTS `log_mid_collected_item_kv`;
CREATE TABLE `log_mid_collected_item_kv` (
  `id`                INT(11) NOT NULL AUTO_INCREMENT,
  `collected_item_id` INT(11)          DEFAULT NULL,
  `kv_tag_id`         INT(11)          DEFAULT NULL,
  `create_time`       DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `log_mid_collected_item_kv_collected_item_id` (`collected_item_id`),
  KEY `log_mid_collected_item_kv_kv_tag_id` (`kv_tag_id`),
  CONSTRAINT `log_mid_collected_item_kv_kv_tag_id` FOREIGN KEY (`kv_tag_id`) REFERENCES `log_kv_tag` (`id`),
  CONSTRAINT `log_mid_collected_item_kv_collected_item_id` FOREIGN KEY (`collected_item_id`) REFERENCES `log_collected_item` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for log_store_record
-- ----------------------------
DROP TABLE IF EXISTS `log_store_record`;
CREATE TABLE `log_store_record` (
  `id`           INT(11) NOT NULL AUTO_INCREMENT,
  `collector_id` INT(11) NOT NULL
  COMMENT '采集器id',
  `outer_ip`     VARCHAR(255)     DEFAULT NULL
  COMMENT '外网ip:docker容器宿主机外网ip',
  `outer_port`   INT(11)          DEFAULT NULL
  COMMENT '容器expose的宿主机端口',
  `inner_ip`     VARCHAR(255)     DEFAULT NULL
  COMMENT '内网ip:docker容易一般会使用172.17开头的内网ip',
  `inner_port`   INT(11)          DEFAULT NULL
  COMMENT '容器内部expose的端口',
  `create_time`  DATETIME         DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_store_collected_item` (`collector_id`),
  CONSTRAINT `fk_store_collected_item` FOREIGN KEY (`collector_id`) REFERENCES `log_collected_item` (`id`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8;
