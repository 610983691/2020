###安装下载
参考 [https://github.com/alibaba/nacos](https://github.com/alibaba/nacos "nacos github")

先使用本地包方式用于开发模式运行。
控制台默认密码：nacos/nacos

说一下为啥要选择nacos:
> 以前项目采用eureka做服务注册与发现，zookeeper集群做配置中心，zuul做微服务网关	。
> 
> 以前的问题在于配置中心的页面配置是全自己写的，不可避免有些bug.同时，由于更新zookeeper节点的策略不太好，导致每次更新配置很慢。
> 
> 以前eureka默认的服务注册中心管理页面也比较简陋，也没法做一些限流处理。
> 
> 查看nacos手册，发现一个nacos就能解决服务注册/发现，限流，配置中心等。也减少了运维人员的工作量和学习使用成本，因此考虑切换到nacos来做注册中心和配置中心。
> 
> 唯一缺陷就是，目前nacos集群部署只支持mysql数据库。

###运行

参考官方手册，先开始用起来。
[https://nacos.io/zh-cn/docs/console-guide.html](https://nacos.io/zh-cn/docs/console-guide.html "nacos使用手册")	

###注册
需要注意的是：[springhttps://start.spring.io/		](https://start.spring.io/ "spring官方")	，官方什么spring boot的版本要在2.2.0--2.3.0之间。

参考[https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme.md](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-examples/nacos-example/nacos-discovery-example/readme.md "服务注册")	文档，将gateway服务注册到nacos。查看控制台如下:

	
![gateway](doc_pic/gateway_register.png "gateway注册")						