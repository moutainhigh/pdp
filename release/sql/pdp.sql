-- MySQL dump 10.13  Distrib 5.5.56, for Win64 (AMD64)
--
-- Host: localhost    Database: pdp_db
-- ------------------------------------------------------
-- Server version	5.5.56

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `au_auth`
--

DROP TABLE IF EXISTS `au_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sys_id` int(11) NOT NULL COMMENT '系统id',
  `name` varchar(30) NOT NULL COMMENT '权限名称',
  `status` tinyint(1) NOT NULL COMMENT '权限状态:0-不可用，1-可用，2-删除',
  `type` tinyint(1) NOT NULL COMMENT '权限类型:1-菜单，2-按钮，3-权限',
  `url` varchar(60) DEFAULT NULL COMMENT '权限url',
  `auth_key` varchar(30) NOT NULL COMMENT '权限标识',
  `sort_no` int(5) NOT NULL COMMENT '排列序号',
  `parent_id` int(11) unsigned DEFAULT '0' COMMENT '父权限id',
  `clazz` varchar(30) DEFAULT NULL COMMENT '权限样式',
  `target` varchar(100) DEFAULT NULL COMMENT '跳转目标页面名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_auth`
--

LOCK TABLES `au_auth` WRITE;
/*!40000 ALTER TABLE `au_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_dict`
--

DROP TABLE IF EXISTS `au_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_key` varchar(30) NOT NULL COMMENT '字典键',
  `dict_value` varchar(120) NOT NULL COMMENT '字典值',
  `sort_no` int(5) NOT NULL COMMENT '字典排列序号',
  `dict_type_id` int(11) DEFAULT NULL COMMENT '字典类型id',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '字典状态：0-可用，1-不可用，2-删除',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新信息的用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_dict`
--

LOCK TABLES `au_dict` WRITE;
/*!40000 ALTER TABLE `au_dict` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_dict_type`
--

DROP TABLE IF EXISTS `au_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_dict_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(20) NOT NULL COMMENT '类型编码',
  `name` varchar(20) NOT NULL COMMENT '类型名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '字典类型状态：0-可用，1-不可用，2-删除',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新信息的用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据字典类型信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_dict_type`
--

LOCK TABLES `au_dict_type` WRITE;
/*!40000 ALTER TABLE `au_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_login_log`
--

DROP TABLE IF EXISTS `au_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户主键id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `sys_id` int(11) DEFAULT NULL COMMENT '系统主键id',
  `ip` varchar(15) DEFAULT NULL COMMENT '登录地址',
  `platform` tinyint(1) DEFAULT NULL COMMENT '登录平台:1-PC,2-Android,3-iPhone,4-微信',
  `time` datetime DEFAULT NULL COMMENT '登录时间',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_login_log`
--

LOCK TABLES `au_login_log` WRITE;
/*!40000 ALTER TABLE `au_login_log` DISABLE KEYS */;
INSERT INTO `au_login_log` VALUES (1,25,'超级管理员',1,'0:0:0:0:0:0:0:1',1,'2021-09-30 15:02:04',NULL);
/*!40000 ALTER TABLE `au_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_oper_log`
--

DROP TABLE IF EXISTS `au_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_oper_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '用户主键id',
  `type` varchar(10) DEFAULT NULL COMMENT '操作类型',
  `time` datetime NOT NULL COMMENT '操作时间',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_oper_log`
--

LOCK TABLES `au_oper_log` WRITE;
/*!40000 ALTER TABLE `au_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_org`
--

DROP TABLE IF EXISTS `au_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL COMMENT '组织编码',
  `name` varchar(60) NOT NULL COMMENT '组织名称',
  `org_type` tinyint(1) DEFAULT '0' COMMENT '组织类别',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级组织id',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态：0-不可用，1-可用，2-删除',
  `is_parent` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是父节点 0 否 1 是',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新信息的用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织消息表\r\n放弃部门表，统一为组织';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_org`
--

LOCK TABLES `au_org` WRITE;
/*!40000 ALTER TABLE `au_org` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_role`
--

DROP TABLE IF EXISTS `au_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(90) DEFAULT NULL COMMENT '角色名',
  `org_id` int(11) DEFAULT NULL COMMENT '所属组织',
  `dict_value` int(2) DEFAULT NULL COMMENT '字典类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态：0-不可用，1-可用，2-删除',
  `description` varchar(600) DEFAULT NULL COMMENT '描述',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime DEFAULT NULL COMMENT '系统时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_role`
--

LOCK TABLES `au_role` WRITE;
/*!40000 ALTER TABLE `au_role` DISABLE KEYS */;
INSERT INTO `au_role` VALUES (4,'超级管理员',NULL,NULL,1,NULL,1,'2020-07-15 06:44:42',NULL,NULL);
/*!40000 ALTER TABLE `au_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_role_auth`
--

DROP TABLE IF EXISTS `au_role_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_role_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `auth_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_id_auth_id` (`role_id`,`auth_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色权限信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_role_auth`
--

LOCK TABLES `au_role_auth` WRITE;
/*!40000 ALTER TABLE `au_role_auth` DISABLE KEYS */;
INSERT INTO `au_role_auth` VALUES (56,6,25);
/*!40000 ALTER TABLE `au_role_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_sys`
--

DROP TABLE IF EXISTS `au_sys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_sys` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '系统名称',
  `code` varchar(10) DEFAULT NULL COMMENT '系统代号',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新信息的用户',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态：0-不可用，1-可用，2-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_sys`
--

LOCK TABLES `au_sys` WRITE;
/*!40000 ALTER TABLE `au_sys` DISABLE KEYS */;
/*!40000 ALTER TABLE `au_sys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_user`
--

DROP TABLE IF EXISTS `au_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(60) DEFAULT NULL COMMENT '用户真实姓名',
  `account` varchar(60) NOT NULL COMMENT '账号',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `status` tinyint(1) NOT NULL COMMENT '状态：0-不可用，1-可用，2-删除',
  `org_id` int(11) DEFAULT NULL COMMENT '组织id',
  `org_type` tinyint(1) DEFAULT '0' COMMENT '组织类别',
  `description` varchar(200) DEFAULT NULL COMMENT '描述信息',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息的用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建的时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '更新信息的用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户账号信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_user`
--

LOCK TABLES `au_user` WRITE;
/*!40000 ALTER TABLE `au_user` DISABLE KEYS */;
INSERT INTO `au_user` VALUES (25,'超级管理员','admin','18208985395','d39346b11421ee42c73bfea4c9083f4b',1,NULL,0,NULL,1,'2020-07-17 16:30:48',NULL,NULL);
/*!40000 ALTER TABLE `au_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `au_user_role`
--

DROP TABLE IF EXISTS `au_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `au_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL COMMENT '员工主键id',
  `role_id` int(11) NOT NULL COMMENT '角色主键id',
  `create_user_id` int(11) DEFAULT NULL COMMENT '分配人id',
  `create_time` datetime DEFAULT NULL COMMENT '分配时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id_role_id` (`user_id`,`role_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `au_user_role`
--

LOCK TABLES `au_user_role` WRITE;
/*!40000 ALTER TABLE `au_user_role` DISABLE KEYS */;
INSERT INTO `au_user_role` VALUES (27,25,4,1,'2020-07-17 16:30:48');
/*!40000 ALTER TABLE `au_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_info`
--

DROP TABLE IF EXISTS `data_sources_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_source_context` varchar(100) DEFAULT NULL COMMENT '数据源说明',
  `data_source_type` varchar(100) DEFAULT NULL COMMENT '数据源类型',
  `driver` varchar(100) DEFAULT NULL COMMENT '驱动连接串',
  `url` varchar(100) DEFAULT NULL COMMENT '连接url',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建信息用户id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改信息用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_info`
--

LOCK TABLES `data_sources_info` WRITE;
/*!40000 ALTER TABLE `data_sources_info` DISABLE KEYS */;
INSERT INTO `data_sources_info` VALUES (4,'test','JDBC','test','test','test','test',25,25,'2021-09-30 07:01:46','2021-09-30 15:01:46');
/*!40000 ALTER TABLE `data_sources_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_sources_type_info`
--

DROP TABLE IF EXISTS `data_sources_type_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_sources_type_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sources_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_sources_type_info`
--

LOCK TABLES `data_sources_type_info` WRITE;
/*!40000 ALTER TABLE `data_sources_type_info` DISABLE KEYS */;
INSERT INTO `data_sources_type_info` VALUES (1,'JDBC'),(2,'HIVE'),(3,'HDFS'),(4,'HBASE'),(5,'ES'),(6,'KAFKA'),(7,'HTTP'),(8,'REDIS');
/*!40000 ALTER TABLE `data_sources_type_info` ENABLE KEYS */;
UNLOCK TABLES;


CREATE TABLE `gather_dolphin_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `gather_job_id` varchar(200) NOT NULL COMMENT '任务id',
  `gather_context` varchar(200) DEFAULT NULL COMMENT '任务说明',
  `datasource_type_input` varchar(100) NOT NULL COMMENT '输入数据源类型',
  `datasource_input` int(11) NOT NULL COMMENT '输入数据源ID',
  `database_name_input` varchar(255) NOT NULL  COMMENT '输入数据源数据库',
  `table_name` varchar(255) NOT NULL  COMMENT '输入数据源表名',
  `datasource_type_output` varchar(100) NOT NULL COMMENT '输出数据源类型',
  `datasource_output` int(11) NOT NULL COMMENT '输出数据源ID',
  `sync_type` varchar(255) NOT NULL COMMENT '数据同步方式',
  `datasource_dolphin` int(11) NOT NULL COMMENT 'dolphin源',
  `dolphin_project_name` varchar(255) NOT NULL COMMENT 'dolphin项目名',
  `create_hive_table` boolean DEFAULT false COMMENT '是否创建表',
  `is_online` boolean DEFAULT false COMMENT '上线dolphin任务',
  `crontab` varchar(255) NOT NULL COMMENT '定时',
  `gather_job_info` text DEFAULT NULL COMMENT 'dolphin任务配置',
  `create_user_id` int(11) NOT NULL COMMENT '创建信息用户id',
  `update_user_id` int(11) NOT NULL COMMENT '修改信息用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `gather_type_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gather_type` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;
insert into `gather_type_info` values (1,'全量'),(2,'增量'),(3,'每日快照')



CREATE TABLE `job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job_log_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (1, 'xxl-job-executor-sample', '示例执行器', 0, NULL, '2018-11-03 22:21:31' );
INSERT INTO `job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, 1, '测试任务1', '2018-11-03 22:21:31', '2018-11-03 22:21:31', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '');
INSERT INTO `job_lock` ( `lock_name`) VALUES ( 'schedule_lock');

commit;



--
-- Dumping routines for database 'pdp_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-09-30 15:03:32
