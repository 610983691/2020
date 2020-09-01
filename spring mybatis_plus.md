#mybatis（Apache2 license）
官方手册：

mybatis:
[https://mybatis.org/mybatis-3/zh/index.html](https://mybatis.org/mybatis-3/zh/index.html "mybatis3")

mybatis plus:
[https://baomidou.com/guide/crud-interface.html#insert](https://baomidou.com/guide/crud-interface.html#insert "mybatis_plus官方手册")


##mybatis plus（Apache2 license）

为了简化开发，省略一些自定义的插件开发工作量，因此使用mybatis plus。

###单数据源

数据源采用hikariCP

	@Configuration
	@Slf4j
	public class StandaloneDataSourceConfig {
	
	
		
		@Bean
	    public DataSource dataSource() {
			log.info("配置数据源");
	        HikariConfig config = new HikariConfig();
	        Map<String,Object> pro = NacosPropertySourceRepository.getNacosPropertySource("product_test_shared_db.properties","REFRESH_GROUP_PRODUCT_SHARED").getSource();
	        String afterDecode= (String)pro.get("password");//TODO:获取密码，然后解密
	        log.info("解密数据库密码:{}",afterDecode);
	        pro.put("password",afterDecode);//解密后放回去
	        pro.entrySet().forEach(action->{
	        	if(!action.getKey().equals("password")) {
	        		log.info("datasource config put:{}--{}",action.getKey(),action.getValue());
	        	}
	        	config.addDataSourceProperty(action.getKey(), action.getValue());
	        });
	        PropertyElf.setTargetFromProperties(config, config.getDataSourceProperties());
	        return new HikariDataSource(config);
	    }
	}


事务管理：

	
	@Transactional(rollbackFor = Exception.class)//这里测试使用默认配置即可
	@RequestMapping("/user/update")
	public UserEntity update(@RequestBody UserEntity us){
		UserEntity oldus = userMapper.selectById(us.getId());
		us.setUsername(oldus.getUsername()+"modify");
		userMapper.updateById(us);//抛异常
		UserEntity newusr = userMapper.selectById(us.getId());
		log.info("new user info :{}",newusr);//打印新查询的user
		int a=1/0;//抛异常,
		return us;
	}

dao:

		/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值
	 * unless:除非result==null,否则都会存入缓存中
	 */
	@Cacheable(value = "pdt:product_test_consumer:t_user:id", key = "#p0")
	UserEntity selectById(Serializable id);

这里mapper层添加了缓存。

事务是生效了的，当抛出异常时，数据库并未提交。

但是这里有个问题：由于代码中对更新后的值进行了查询，当事务未提交时，由于查询成功（并且由于在同一个事务中，查询后的数据是最新的更新后的数据，这将导致缓存被更新），缓存更新，但是当异常抛出时，数据库回滚，但是缓存没有办法回滚，导致数据不一致的情况发生。


update方法：

可以看代码，这里调用一次update方法，只要update成功，那么缓存就会被情况。及时外部的service对事务回滚，那么这个缓存也将被清除掉。这个问题并不严重，最多只是导致缓存命中率降低。
	/**
	 * 缓存命名方式： 产品名：：数据库名：表名：字段名：字段值 更新的时候，会只剔除满足这个key:id的缓存记录
	 */
	@CacheEvict(value = "pdt:product_test_consumer:t_user:id", key = "#p0.id")
	int updateById(@Param(Constants.ENTITY) UserEntity entity);

select方法：

如果在service中，事务中同时find了update后的数据，就会导致缓存被更新，而当事务回滚时，缓存的值就会是错误的。如何避免这种情况发生呢？

参考这里的方案和解答：

[https://juejin.im/post/6844903849275162638](https://juejin.im/post/6844903849275162638 "最终一致性方案")

[https://segmentfault.com/q/1010000012344110](https://segmentfault.com/q/1010000012344110 "事务中不应该更新缓存")

1.在事务中，不使用MApper层加缓存的方法，改为使用普通方法？似乎代价比较高？（手动的管理缓存，需要添加额外的Mapper方法）

2.不要使用spring提供的cacheable,cacheput等默认实现，自己提供缓存实现。

3.在事务中不应该更新缓存，只删除缓存。（在上面代码中，selectByid的cacheable就是一个更新缓存的动作）或者说，事务里边就不应该去操作缓存才对？缓存都是查询接口使用时去更新才是更合理的，降低一些命中率来简化编码。
>约定大于配置，感觉第三种方式应该是更简单有效的方式 了。这里的查询方法不是一个合理的方法？

####分布式事务


