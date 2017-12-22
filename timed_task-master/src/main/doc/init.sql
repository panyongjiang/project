/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.6.27 : Database - assets
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`assets` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `assets`;

/*Table structure for table `authorities` */

DROP TABLE IF EXISTS `authorities`;

CREATE TABLE `authorities` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `function_name` varchar(255) DEFAULT NULL COMMENT '功能名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Table structure for table `log_operation` */

DROP TABLE IF EXISTS `log_operation`;

CREATE TABLE `log_operation` (
  `id` varchar(50) NOT NULL,
  `content` varchar(255) DEFAULT NULL COMMENT '日志内容',
  `time` datetime DEFAULT NULL COMMENT '记录时间',
  `operator` varchar(64) DEFAULT NULL COMMENT '操作人员',
  `type` varchar(32) DEFAULT NULL COMMENT '日志类型',
  `type_name` varchar(100) DEFAULT NULL COMMENT '操作名称',
  `ip` varchar(32) DEFAULT NULL COMMENT '操作人员ip',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `pack_upgrade` */

DROP TABLE IF EXISTS `pack_upgrade`;

CREATE TABLE `pack_upgrade` (
  `id` varchar(50) NOT NULL,
  `pack_name` varchar(200) NOT NULL COMMENT '升级包名称',
  `pack_url` varchar(100) NOT NULL COMMENT '上传存储地址',
  `create_user` bigint(11) DEFAULT NULL COMMENT '上传人员ID',
  `pack_aue_user` bigint(11) DEFAULT NULL COMMENT '升级包录入人员',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未删除，1-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `remark` text COMMENT '备注',
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `project` */

DROP TABLE IF EXISTS `project`;

CREATE TABLE `project` (
  `id` varchar(50) NOT NULL,
  `project_name` varchar(200) DEFAULT NULL COMMENT '项目名称',
  `project_details` varchar(500) DEFAULT NULL COMMENT '项目备注',
  `project_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '项目状态（0-开启，1-关闭）',
  `create_user` bigint(11) NOT NULL COMMENT '创建者ID',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未删除，1-已删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `project_user` */

DROP TABLE IF EXISTS `project_user`;

CREATE TABLE `project_user` (
  `project_id` varchar(50) NOT NULL COMMENT '项目ID',
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` char(2) NOT NULL DEFAULT '0' COMMENT '状态0,未完成,1已完成',
  PRIMARY KEY (`user_id`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `report` */

DROP TABLE IF EXISTS `report`;

CREATE TABLE `report` (
  `id` varchar(50) NOT NULL,
  `user_id` bigint(11) NOT NULL COMMENT '新增人ID',
  `project_id` varchar(50) NOT NULL COMMENT '项目ID',
  `report_name` varchar(500) NOT NULL COMMENT '报告名称',
  `report_url` varchar(200) NOT NULL COMMENT '报告存储位置',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `upgrade_message` */

DROP TABLE IF EXISTS `upgrade_message`;

CREATE TABLE `upgrade_message` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `msg_content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `is_readed` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未读，1-已读',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=314 DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `real_name` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `gender` tinyint(4) NOT NULL DEFAULT '1' COMMENT '性别，1-男，0-女',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `role` tinyint(4) DEFAULT NULL COMMENT '角色',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已删除（0-未删除，1-已删除）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `company` varchar(100) DEFAULT NULL COMMENT '公司/部门',
  `phone` varchar(100) DEFAULT NULL COMMENT '电话号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;

/*Table structure for table `user_authorities` */

DROP TABLE IF EXISTS `user_authorities`;

CREATE TABLE `user_authorities` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(11) NOT NULL COMMENT '用户ID',
  `authoritie_id` varchar(255) DEFAULT NULL COMMENT '功能id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

 ALTER TABLE report ADD file_name VARCHAR(100) NULL COMMENT '文件名称';
 
 
 ALTER TABLE pack_upgrade ADD file_name VARCHAR(100) NULL COMMENT '文件名称';
 
 		
 ALTER TABLE pack_upgrade ADD version_num  VARCHAR(100) NULL COMMENT '版本号'