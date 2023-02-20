# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20035
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 5.7.18)
# 数据库: varyar
# 生成时间: 2023-01-17 02:21:13 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# 转储表 t_cloud_state
# ------------------------------------------------------------

DROP TABLE IF EXISTS `t_cloud_state`;

CREATE TABLE `t_cloud_state` (
  `device_id` varchar(30) NOT NULL,
  `timestamp` datetime DEFAULT NULL COMMENT '事件时间',
  `up_time` bigint(20) unsigned DEFAULT NULL COMMENT '毫秒',
  `wifi_rssi` int(11) DEFAULT NULL COMMENT 'wifi信号',
  `record_time` datetime DEFAULT NULL COMMENT '入库时间',
  `first_time` datetime DEFAULT NULL COMMENT '首次记录时间',
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='心跳事件';




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
