官方手册：

[http://seata.io/zh-cn/docs/user/quickstart.html](http://seata.io/zh-cn/docs/user/quickstart.html "seata手册")

##AT模式

####一.建库建表建用户
1.建用户

	create user 'seata'@'%' identified by 'seata';

2.创建数据库

	CREATE DATABASE `seata` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

3.赋权

	grant all privileges on `seata`.* to 'seata'@'%';

4.建表AT模式的回滚表（分布式事务日志）

	CREATE TABLE `undo_log` (
	  `id` bigint(20) NOT NULL AUTO_INCREMENT,
	  `branch_id` bigint(20) NOT NULL,
	  `xid` varchar(100) NOT NULL,
	  `context` varchar(128) NOT NULL,
	  `rollback_info` longblob NOT NULL,
	  `log_status` int(11) NOT NULL,
	  `log_created` datetime NOT NULL,
	  `log_modified` datetime NOT NULL,
	  `ext` varchar(100) DEFAULT NULL,
	  PRIMARY KEY (`id`),
	  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
	) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

####二、下载seata服务


####三、创建order/storage服务

