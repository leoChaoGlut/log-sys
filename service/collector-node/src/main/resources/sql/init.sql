SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `keyword`
-- ----------------------------
DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEYWORD` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------
INSERT INTO `keyword` VALUES ('1', 'pat_card_no');

-- ----------------------------
-- Table structure for `kvtag`
-- ----------------------------
DROP TABLE IF EXISTS `kvtag`;
CREATE TABLE `kvtag` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KEY` varchar(255) DEFAULT NULL,
  `KEY_TAG` varchar(255) DEFAULT NULL,
  `VALUE_END_TAG` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kvtag
-- ----------------------------
INSERT INTO `kvtag` VALUES ('1', 'patName', '<patName>', '</patName>');
INSERT INTO `kvtag` VALUES ('2', 'pat_card_no', '\\\"pat_card_no\\\":\\\"', '\\\"');
INSERT INTO `kvtag` VALUES ('3', 'pat_id_no', '\\\"pat_id_no\\\":\\\"', '\\\"');