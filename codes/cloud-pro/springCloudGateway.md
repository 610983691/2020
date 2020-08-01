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
