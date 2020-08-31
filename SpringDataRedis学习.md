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

product_name:db:table:field:custom



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



####redis 基于注解的DAO缓存获取
参考代码：

	public interface UserMapper extends BaseMapper<UserEntity> {

		/**
		 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值
		 */
		@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0")
		UserEntity selectById(Serializable id);
	
		/**
		 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值 新增的时候不更新缓存，只有当系统真正查询的时候才添加到缓存。
		 */
		int insert(UserEntity user);
	
		/**
		 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值 更新的时候，会只剔除满足这个key:id的缓存记录
		 */
		@CacheEvict(value = "pdt:product_test_consumer:t_user:id", key = "#p0.id")
		int updateById(@Param(Constants.ENTITY) UserEntity entity);
	
		/**
		 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值
		 * 更新的时候，剔除pdt:product_test_consumer:t_user:id下面所有的缓存
		 */
		@CacheEvict(value = "pdt:product_test_consumer:t_user:id", allEntries = true)
		int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
	}

默认情况下，有该缓存就OK。更细粒度的缓存方案应当是专门的中间件来处理，不在这里单机版的代码中处理.



####update方法异常会剔除cache缓存吗？
肯定不会。

####集群的redis