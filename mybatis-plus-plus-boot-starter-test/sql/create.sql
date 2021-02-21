/*
测试表
*/
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `project_name` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `type` int(1) DEFAULT NULL COMMENT '类型',
  `deleted` int(255) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_on` datetime NOT NULL COMMENT '创建日期',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_on` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL,
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `role_desc` varchar(255) DEFAULT NULL COMMENT '角色描述',
  `create_on` date NOT NULL COMMENT '创建日期',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_on` date NOT NULL COMMENT '更新日期',
  `update_by` varchar(255) NOT NULL COMMENT '更新人',
  `deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `user_pwd` varchar(255) DEFAULT NULL COMMENT '密码',
  `account` varchar(255) DEFAULT NULL COMMENT '账号',
  `create_on` datetime NOT NULL COMMENT '创建日期',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_on` datetime NOT NULL COMMENT '更新日期',
  `update_by` varchar(255) NOT NULL COMMENT '更新人',
  `deleted` int(1) NOT NULL DEFAULT '0' COMMENT '是否删除，0否，1是',
  `version` int(255) NOT NULL DEFAULT '0' COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sys_user_id` int(11) NOT NULL COMMENT '用户id',
  `sys_role_id` int(11) NOT NULL COMMENT '角色id',
  `create_on` datetime DEFAULT NULL COMMENT '创建日期',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_on` datetime DEFAULT NULL COMMENT '更新日期',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `task_name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `task_time` datetime DEFAULT NULL COMMENT '队伍名称',
  `project_id` int(11) DEFAULT NULL COMMENT '项目id',
  `create_on` datetime DEFAULT NULL COMMENT '创建日期',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_on` datetime DEFAULT NULL COMMENT '更新日期',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;