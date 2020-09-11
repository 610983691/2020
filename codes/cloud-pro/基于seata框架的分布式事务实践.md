

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
1.下载1.3.0

2.连接mysql报错，更换connnector解决

3.server端建表

	-- the table to store GlobalSession data
	CREATE TABLE IF NOT EXISTS `global_table`
	(
	    `xid`                       VARCHAR(128) NOT NULL,
	    `transaction_id`            BIGINT,
	    `status`                    TINYINT      NOT NULL,
	    `application_id`            VARCHAR(32),
	    `transaction_service_group` VARCHAR(32),
	    `transaction_name`          VARCHAR(128),
	    `timeout`                   INT,
	    `begin_time`                BIGINT,
	    `application_data`          VARCHAR(2000),
	    `gmt_create`                DATETIME,
	    `gmt_modified`              DATETIME,
	    PRIMARY KEY (`xid`),
	    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
	    KEY `idx_transaction_id` (`transaction_id`)
	) ENGINE = InnoDB
	  DEFAULT CHARSET = utf8;
	
	-- the table to store BranchSession data
	CREATE TABLE IF NOT EXISTS `branch_table`
	(
	    `branch_id`         BIGINT       NOT NULL,
	    `xid`               VARCHAR(128) NOT NULL,
	    `transaction_id`    BIGINT,
	    `resource_group_id` VARCHAR(32),
	    `resource_id`       VARCHAR(256),
	    `branch_type`       VARCHAR(8),
	    `status`            TINYINT,
	    `client_id`         VARCHAR(64),
	    `application_data`  VARCHAR(2000),
	    `gmt_create`        DATETIME(6),
	    `gmt_modified`      DATETIME(6),
	    PRIMARY KEY (`branch_id`),
	    KEY `idx_xid` (`xid`)
	) ENGINE = InnoDB
	  DEFAULT CHARSET = utf8;
	
	-- the table to store lock data
	CREATE TABLE IF NOT EXISTS `lock_table`
	(
	    `row_key`        VARCHAR(128) NOT NULL,
	    `xid`            VARCHAR(96),
	    `transaction_id` BIGINT,
	    `branch_id`      BIGINT       NOT NULL,
	    `resource_id`    VARCHAR(256),
	    `table_name`     VARCHAR(32),
	    `pk`             VARCHAR(36),
	    `gmt_create`     DATETIME,
	    `gmt_modified`   DATETIME,
	    PRIMARY KEY (`row_key`),
	    KEY `idx_branch_id` (`branch_id`)
	) ENGINE = InnoDB
	  DEFAULT CHARSET = utf8;

####三、创建order/storage服务

参考该文档，
[https://github.com/seata/seata-samples/tree/master/springcloud-eureka-feign-mybatis-seata](https://github.com/seata/seata-samples/tree/master/springcloud-eureka-feign-mybatis-seata "可以参考")

配置项：

[https://seata.io/zh-cn/docs/user/configurations.html](https://seata.io/zh-cn/docs/user/configurations.html "配置文档")

视频：

[https://www.bilibili.com/video/BV12Q4y1A7Nt](https://www.bilibili.com/video/BV12Q4y1A7Nt "视频")

[https://www.bilibili.com/video/BV1Cf4y1X7vR](https://www.bilibili.com/video/BV1Cf4y1X7vR "1.3.0")


###
服务会重试问题：
是由于feign远程调用默认会重试。由于没有实现幂等接口，需要修改配置禁止重试.
参考：[https://www.liujiajia.me/2019/1/22/feign-retry](https://www.liujiajia.me/2019/1/22/feign-retry "feign重试")

	feign.sentinel.enabled=true
	#feign.client.config.default.connectTimeout=12000
	#feign.client.config.default.readTimeout=12000
	ribbon.OkToRetryOnAllOperations=false
	ribbon.MaxAutoRetriesNextServer=0
	ribbon.MaxAutoRetries=0

####undo_log存在数据问题
例如：

![undo_log](doc_pic/seata_undolog.PNG  "undo_log")

查看源码：

	// If undo_log exists, it means that the branch transaction has completed the first phase,
    // we can directly roll back and clean the undo_log
    // Otherwise, it indicates that there is an exception in the branch transaction,
    // causing undo_log not to be written to the database.
    // For example, the business processing timeout, the global transaction is the initiator rolls back.
    // To ensure data consistency, we can insert an undo_log with GlobalFinished state
    // to prevent the local transaction of the first phase of other programs from being correctly submitted.
    // See https://github.com/seata/seata/issues/489

    if (exists) {
        deleteUndoLog(xid, branchId, conn);
        conn.commit();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("xid {} branch {}, undo_log deleted with {}", xid, branchId,
                State.GlobalFinished.name());
        }
    } else {
        insertUndoLogWithGlobalFinished(xid, branchId, UndoLogParserFactory.getInstance(), conn);
        conn.commit();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("xid {} branch {}, undo_log added with {}", xid, branchId,
                State.GlobalFinished.name());
        }
    }	

上面的注释写的很清楚，由于一些别的原因，例如网络问题，当发起方回滚全局事务，为了确保一致性，这个时候需要插入state=1的undo_log来保证其他分支事务的一阶段事务不会被提交。

	insertUndoLogWithGlobalFinished 方法：
	
    @Override
    protected void insertUndoLogWithGlobalFinished(String xid, long branchId, UndoLogParser parser, Connection conn) throws SQLException {
        insertUndoLog(xid, branchId, buildContext(parser.getName()),
                parser.getDefaultContent(), State.GlobalFinished, conn);
    }

	protected enum State {
        /**
         * This state can be properly rolled back by services
         */
        Normal(0),
        /**
         * This state prevents the branch transaction from inserting undo_log after the global transaction is rolled
         * back.
         */
        GlobalFinished(1); //主动添加的undo_log的状态，这个是1.而自动回滚的是上面的0

        private int value;

        State(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
	

