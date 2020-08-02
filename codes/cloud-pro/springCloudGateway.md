###微服务网关
参考：
[http://www.ityouknow.com/springcloud/2018/12/12/spring-cloud-gateway-start.html](http://www.ityouknow.com/springcloud/2018/12/12/spring-cloud-gateway-start.html "gateway")

[https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.BUILD-SNAPSHOT/reference/html/](https://docs.spring.io/spring-cloud-gateway/docs/2.2.4.BUILD-SNAPSHOT/reference/html/ "官方手册")


####路由规则

可以通过一系列默认提供的Predicate来进行路由，可以是 时间/header/cookie等。

####gateway filter
[http://www.ityouknow.com/springcloud/2019/01/19/spring-cloud-gateway-service.html](http://www.ityouknow.com/springcloud/2019/01/19/spring-cloud-gateway-service.html "网关过滤器参考")

spring cloud gateway 的生命周期只有两个:pre/post.

> PRE： 这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。
> 
POST：这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的 HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。


Spring Cloud Gateway 的 Filter 分为两种：GatewayFilter 与 GlobalFilter。GlobalFilter 会应用到所有的路由上，而 GatewayFilter 将应用到单个路由或者一个分组的路由上。


retelimiter,采用令牌桶算法来进行限速。


###使用
1.更好的使用实践是采用动态路由配置，因此把路由配置都写到配置中心去。这样可以动态的通过更改配置中心的配置来控制路由转发。


###实践
1.建立一个springboot 项目，用作service 服务提供者，对外提供服务。
参考代码中test工程。

2.在nacos中，增加gateway 网关相应的配置。

	#默认采用注册中心注册的服务名方式来进行路由转发
	spring.cloud.gateway.discovery.locator.enabled=true
	spring.cloud.gateway.discovery.locator.lower-case-service-id=true
	logging.level.org.springframework.cloud.gateway=debug



> 采用 http://localhost:8080/providor/echo?param=123 方式访问providor服务。

3.默认的路由转发方式是负载均衡的吗？（是）

> 通过copy两个test.jar服务，发现默认的路由转发，已经是负载均衡的，默认是轮询的方式切换访问

4.权重设置

> nacos 中设置权重目前还未生效。[https://nacos.io/zh-cn/docs/faq.html](https://nacos.io/zh-cn/docs/faq.html "权重未生效")
这是由于nacos的权重配置默认没有启用，因此需要手动的在gateway网关启动时，把NacosRule 注入到容器中。
在gateway中添加如下配置：

	//nacos的默认实现不是轮询，具体算法目前还没看明白。
	@Bean
    public IRule loadBalanceNacosRule(){
      return new NacosRule();
    }

这样即可在nacos中动态的修改权重，对应用进行上下线处理。