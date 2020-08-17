##数据库用户创建

	create user 'nacos'@'%' identified by 'nacos';



###数据库创建

	CREATE DATABASE `nacos_config` CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';

###赋权限
	
	grant all privileges on `nacos_config`.* to 'nacos'@'%';

###导入初始化表

	execute nacosmysql.sql

###修改服务器配置

nacos配置：

	spring.datasource.platform=mysql

	### Count of DB:
	db.num=1
	
	### Connect URL of DB:
	db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
	db.user=nacos
	db.password=nacos


###启动nacos服务

	startup.sh -m standalone

启动报错：
	
	Caused by: java.lang.NullPointerException
	at com.mysql.jdbc.ConnectionImpl.getServerCharset(ConnectionImpl.java:2983)
	at com.mysql.jdbc.MysqlIO.sendConnectionAttributes(MysqlIO.java:1873)
	at com.mysql.jdbc.MysqlIO.proceedHandshakeWithPluggableAuthentication(MysqlIO.java:1802)
	at com.mysql.jdbc.MysqlIO.doHandshake(MysqlIO.java:1206)
	at com.mysql.jdbc.ConnectionImpl.coreConnect(ConnectionImpl.java:2234)
	at com.mysql.jdbc.ConnectionImpl.connectWithRetries(ConnectionImpl.java:2085)
	... 191 more
可以在mysqld的/etc/my.cnf下添加配置：
	
	character_set_server=utf8


后续还有报错。原因是mysql-connector.jar版本不对，使用1.3.2最新版的nacos.

nacos启动还是报错：Public Key Retrieval is not allowed

解决方法：

	jdbc:mysql://localhost:3306/db?allowPublicKeyRetrieval=true

	启动命令行添加?allowPublicKeyRetrieval=true参数
	