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
