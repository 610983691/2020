###	Spring Data Redis

官方手册：

[https://docs.spring.io/spring-data/redis/docs/2.3.3.RELEASE/reference/html/#reference](https://docs.spring.io/spring-data/redis/docs/2.3.3.RELEASE/reference/html/#reference "springRedis官方手册")

redis架构的主从和高并发：

[https://cloud.tencent.com/developer/article/1543023](https://cloud.tencent.com/developer/article/1543023 "主从，高并发")

redis的哨兵机制与使用：
[https://www.cnblogs.com/yangyuanhu/p/13039750.html](https://www.cnblogs.com/yangyuanhu/p/13039750.html "redis哨兵机制")

####单机的redis
由于仅仅测试，
所有的key/value都以string方式存储？


参考：
[https://developer.aliyun.com/article/531067](https://developer.aliyun.com/article/531067 "redis")

redis的key前缀：
productname_modulename
完整命名：

产品名+模块名+自定义功能名+自定义后缀
productname_modulename_funcname_custom

@cacheable 
这里的注解会直接从redis中尝试获取key,因此多个集群服务间的缓存是共享的。

示例：

	http://localhost:57815/redis/getuser/uid:1598431148562

	http://localhost:57708/redis/getuser/uid:1598431148562

分别从两个服务上都能获取到数据。

####集群的redis