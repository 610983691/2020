##步骤
参考文档：
[https://www.runoob.com/mysql/mysql-install.html](https://www.runoob.com/mysql/mysql-install.html "参考文档")

1.下载页面：
[https://dev.mysql.com/downloads/repo/yum/](https://dev.mysql.com/downloads/repo/yum/ "官方链接")


3.下载：
	
	 wget https://repo.mysql.com//mysql80-community-release-el7-3.noarch.rpm 

4.执行安装

	rpm -ivh mysql80-community-release-el7-3.noarch.rpm
	yum update

	yum install mysql-server

权限设置：

	chown mysql:mysql -R /var/lib/mysql
初始化 MySQL：

	mysqld --initialize
启动 MySQL：

	systemctl start mysqld


6.报错解决：

启动报错 ，根据日志发现是无权限：
	
	more /var/log/mysqld.log 
	2020-08-17T06:03:02.049881Z 0 [System] [MY-013169] [Server] /usr/sbin/mysqld (mysqld 8.0.21) initializing of server in progress as process 31707
	2020-08-17T06:03:02.071674Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
	2020-08-17T06:03:04.485657Z 1 [System] [MY-013577] [InnoDB] InnoDB initialization has ended.
	2020-08-17T06:03:06.624134Z 6 [Note] [MY-010454] [Server] A temporary password is generated for root@localhost: r%E<P&8>Iq<k
	2020-08-17T06:07:37.198369Z 0 [System] [MY-010116] [Server] /usr/sbin/mysqld (mysqld 8.0.21) starting as process 31821
	2020-08-17T06:07:37.213276Z 1 [System] [MY-013576] [InnoDB] InnoDB initialization has started.
	2020-08-17T06:07:37.213459Z 1 [ERROR] [MY-012271] [InnoDB] **The innodb_system data file 'ibdata1' must be writable**
	2020-08-17T06:07:37.213532Z 1 [ERROR] [MY-012278] [InnoDB] The innodb_system data file 'ibdata1' must be writable**
	2020-08-17T06:07:37.213611Z 1 [ERROR] [MY-010334] [Server] Failed to initialize DD Storage Engine
	2020-08-17T06:07:37.213874Z 0 [ERROR] [MY-010020] [Server] Data Dictionary initialization failed.
	2020-08-17T06:07:37.213960Z 0 [ERROR] [MY-010119] [Server] Aborting

在/var/lib/mysql/目录下执行
	
	chown mysql:mysql *

7.Mysql安装成功后，尝试远程连接：

	mysql -user root -p

提示无权限：

	ERROR 1045 (28000): Access denied for user 'root'@'localhost' (using password: NO)

解决：

	systemctl stop mysqld

然后修改/etc/my.cnf,添加一行：

	skip-grant-tables
重启：

	systemctl start mysqld

然后执行下面命令，可以进入mysql命令行：

	mysql -u root -p

尝试修改密码报错：
	
	mysql> ALTER USER 'root'@'localhost' IDENTIFIED BY 'rootmysql3306' PASSWORD EXPIRE NEVER;
	ERROR 1290 (HY000): The MySQL server is running with the --skip-grant-tables option so it cannot execute this statement

取消root的密码：
	
	修改/etc/my.cnf,添加一行：

	skip-grant-tables
重启：

	systemctl start mysqld

然后执行下面命令，可以进入mysql命令行：

	mysql -u root -p

然后修改

	1. stop mysql service
	2. Start it this way: /usr/local/mysql/bin/mysqld_safe --skip-grant-tables
	3. Login (mysql) and exec: update user set authentication_string='' where User='root'; 
	4. killall mysqld
	5. Start service again


8.创建用户用于远程访问：

	CREATE USER 'root'@'%' IDENTIFIED BY '123456';
	GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

9.然后执行命令：
	ALTER USER 'root'@'%' IDENTIFIED BY 'rootmysql3306' PASSWORD EXPIRE NEVER;

这样数据库差不多就算安好 了。


