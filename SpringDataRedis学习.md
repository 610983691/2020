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




#####缓存加在业务层还是dao?


>1.如果加在service ，service 需要区分uid,params.一般参数较复杂。结合我们自己的业务，一般只需要对热点数据加cache就行了。所以考虑加在DAO方法上。

>考虑两个用户同时查同一份数据的场景，加在dao上会更容易命中缓存。当然更容易命中缓存也会有更多的业务层代码需要执行，这是有额外代价的。

>我们的业务复杂度目前不需要加多级的缓存，因此只加一级缓存到DAO层即可。


#####redis的key命名
参考：
[https://developer.aliyun.com/article/531067](https://developer.aliyun.com/article/531067 "redis")

dao层方法的key命名：

product_name:db:table:func:custom



#####其他业务类的key命名：

产品名+模块名+自定义功能名+自定义后缀
productname_modulename_funcname_custom

例如
sesseionid:

	productname:common:sid:***

业务类：
	
	productname:modulea:busa:***




#####缓存是分布式共享的
@cacheable 
这里的注解会直接从redis中尝试获取key,因此多个集群服务间的缓存是共享的。

示例：

	http://localhost:57815/redis/getuser/uid:1598431148562

	http://localhost:57708/redis/getuser/uid:1598431148562

分别从两个服务上都能获取到数据。

####集群的redis