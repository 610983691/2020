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
		@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0",unless="#result == null")
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


####缓存穿透
缓存穿透是指查询一个一定不存在的数据，由于缓存是不命中时被动写的，并且出于容错考虑，如果从存储层查不到数据则不写入缓存，这将导致这个不存在的数据每次请求都要到存储层去查询，失去了缓存的意义。在流量大时，可能DB就挂掉了，要是有人利用不存在的key频繁攻击我们的应用，这就是漏洞。

例如当前代码中的：

	@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0",unless="#result == null")
	UserEntity selectById(Serializable id);

解决方案：

最常见的则是采用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被 这个bitmap拦截掉，从而避免了对底层存储系统的查询压力。

更为简单粗暴的方法，如果一个查询返回的数据为空（不管是数 据不存在，还是系统故障），我们仍然把这个空结果进行缓存，但它的过期时间会很短，最长不超过五分钟。

代码：
	
	@Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    	  RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig();
          // 设置缓存管理器管理的缓存的默认过期时间
          defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(300))//5分钟
                  // 设置 key为string序列化
                  .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                  // 设置value为json序列化
                  .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
                  // 允许缓存空值
	//              .disableCachingNullValues();

          RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                  .cacheDefaults(defaultCacheConfig)
                  .build();
          return cacheManager;
    }

优化：

对于我们系统来讲，id<6251947782832128L的主键都是非法的。因此可以做一个通用的拦截，对非法的主键直接过滤。


####缓存雪崩
缓存雪崩是指在我们设置缓存时采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到DB，DB瞬时压力过重雪崩。


解决方案：

缓存失效时的雪崩效应对底层系统的冲击非常可怕。大多数系统设计者考虑用加锁或者队列的方式保证缓存的单线 程（进程）写，从而避免失效时大量的并发请求落到底层存储系统上。这里分享一个简单方案就时讲缓存失效时间分散开，比如我们可以在原有的失效时间基础上增加一个随机值，比如1-5分钟随机，这样每一个缓存的过期时间的重复率就会降低，就很难引发集体失效的事件。


####缓存击穿

对于一些设置了过期时间的key，如果这些key可能会在某些时间点被超高并发地访问，是一种非常“热点”的数据。这个时候，需要考虑一个问题：缓存被“击穿”的问题，这个和缓存雪崩的区别在于这里针对某一key缓存，前者则是很多key。

缓存在某个时间点过期的时候，恰好在这个时间点对这个Key有大量的并发请求过来，这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮。。

解决方案：


1.采用分布式锁，只有拿到锁的第一个线程去请求数据库，然后插入缓存，当然每次拿到锁的时候都要去查询一下缓存有没有。

2.前端采用限流策略，限制并发量。

3.将热点数据设置为永远不过期；

以以下代码为例：

	@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0")
	UserEntity selectById(Serializable id);
这个方法在第一次查询时，会查询数据库，然后将结果缓存到redis.后续的（过期时间内）每次get(key)操作，由于都只查了redis,查询key并不会更新redis的key的失效时间，因此这个key才会过期，所以存在缓存击穿的问题！

这和想象中的LRU不一样呢？

>这是因为LRU淘汰策略是只在缓存已经“满”的时候才会触发。

解决方案代码：

方案1:

>

方案2：

>直接配置限流策略+降级规则。	


####redis过期键的删除策略
Redis会删除过期键以释放空间，过期键的删除策略有两种：

惰性删除：每次从键空间中获取键时，都检查取得的键是否过期，如果过期的话，就删除该键；如果没有过期，就返回该键。

定期删除：每隔一段时间，程序就对数据库进行一次检查，删除里面的过期键。



####redis 分布式锁
原理：

使用：
采用开源的redisson来使用redis的分布式锁。

配置：
由于使用了hash，需要配置hashkey的序列化方式为string.（key和hashkey的序列化不是继承关系，因此需要额外的指定）

	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		
		template.setDefaultSerializer(new GenericFastJsonRedisSerializer());//默认使用fastjson序列化
		template.setKeySerializer(new StringRedisSerializer());//单独设置keySerializer为string
		template.setHashKeySerializer(new StringRedisSerializer());//单独设置keySerializer
	//		template.setHashValueSerializer(new StringRedisSerializer());//单独设置keySerializer
	//		template.setValueSerializer(new GenericFastJsonRedisSerializer());//单独设置valueSerializer
		return template;
	}

分布式锁代码：

	@Autowired
	RedissonClient red;
	
	@Autowired
	RedisTemplate<String, Object>  redis;
	
	@Override
	@CachePut(value="user",key="'product_test:consumer:user:'+#p0.uid")
	public UserInfo add(UserInfo tobeAdd) {
		log.info("tobeadd {}:",tobeAdd);
		RLock rlock =red.getLock("mylock");
		boolean islock =false;
		try {
			islock = rlock.tryLock(1000, 10000, TimeUnit.MILLISECONDS);
			
			if(islock) {//如果获取到锁，可重入的
			
 				Map<Object, Object> map=redis.opsForHash().entries("mylock");
				for (Entry<Object, Object> item : map.entrySet()) {
					log.info("key ={},val={}",item.getKey(),item.getValue());
				}
				Thread.sleep(3000);
			}
		} catch (Exception e) {
			log.info("获取分布式锁失败：",e);
		}finally {
			rlock.unlock();//释放锁
		}
		return tobeAdd;
	}


通过RedisTemplate 获取到的key日志输出：

INFO 13452 --- [o-auto-1-exec-1] c.c.comsumer.redis.RedisServiceImpl      : key =2bacedd8-0f82-45b5-836b-1d557532ccd6:96,val=1





####集群的redis