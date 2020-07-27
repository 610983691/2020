###《Spring 实战（第四版）》

####常见的注解：
1.@component，
>标识为组件。

2.@primary 
> 标识该bean为首选注入的选项。例如：InterfaceA的实现类，ClassA,ClassB,当某处需要依赖InterfaceA接口时，标识有primary的那个Class被注入。

3.@qualifier("") 
>如果在创建时，那么该bean的ID使用qualifier限定。
>如果在使用时，标识使用该beanID为qualifier限定的ID来注入。

4.@Scope 
>限定bean的作用域：常见的为singleton,proptype
>另外：web中生效的有request/session表示为每个会话或者每个请求创建一个bean.

	proxyMode=ScopedProxyMode.INTERFACES
	表明注入的方式是注入接口，如果标记的是类不是接口的话，那么就应该使用CGLIB代理而不是接口代理。

####面向切面
例如：日志记录，事务控制等。都需要的功能，但却和业务耦合在一起，需要剥离业务和这些功能。他们都需要一类类似的功能，所以（代理模式就在这里有很好的使用空间。）

通知（advice）：
>Spring 切面可以应用 5 种类型的通知：
before/AFTER/AFTER-RETURNING/AFTER-THROWING/AROUND

通知定义了切面是什么以及何时使用。

切点（pointcut）:

>切点的定义会匹配通知所要织入的一个或多个连接点。我们通常使用明确的类和方法名称，或是利用正则表达式定义所匹配的类和方法名称来指定这些切点。	

接入点（join point）：


织入：
>织入是把切面应用到目标对象并创建新的代理对象的过程。切面在指定的连接点被织入到目标对象中。在目标对象的生命周期里有多个点可以进行织入：
>>编译期：AspectJ

>>类加载期：AspectJ5

>>运行期：Spring AOP


Spring AOP 构建在动态代理基础之上，因此，Spring 对 AOP 的支持局限于方法拦截。


####Spring 事务
参考文献：

[https://zhuanlan.zhihu.com/p/54067384](https://zhuanlan.zhihu.com/p/54067384 "事务")

[https://juejin.im/post/5b00c52ef265da0b95276091](https://juejin.im/post/5b00c52ef265da0b95276091 "事务")

什么是事务： 一组操作要么全部成功要么全部失败。 以下均以数据库事务为准描述。

事务特性(ACID)：

>atomic/consistency(一致性)/isolation(ˌīsəˈlāSH(ə)n)隔离性/durability(持久性)

TransactionDefinition 定义了事务的基本属性：包含事务隔离级别/传播行为/回滚规则/是否只读/事务超时时间。

隔离级别：

MYSQL默认是REPEATABLE_READ，mysql是READ_COMMITTED。

Spring 的TransactionDefinition 定义的隔离级别会应用于当前事务，因此它会覆盖掉数据库的默认隔离级别。

编程式事务处理和声明式事务处理。一般常用声明式事务处理就够了，简单明了，对代码侵入小。


方法上的tranactional注解优先级高于类上的注解。

###Spring IOC
ioc容器:
1.让所有的对象都由容器IocContainer来管理，而不是交给开发者来不断的new创建。开发者使用时，只需要通过容器获取即可使用。

####三种注入方式
接口注入/setter注入/构造方法注入

####BeanFactory 容器
IOC容器

####ApplicationContext 
间接继承至BeanFactory,是更高级的ioc容器实现，有了更多的扩展

######FactoryBean：
本身也是一种bean，也受beanfactory容器管理，不过这种类型的Bean本身就是生产对象的工厂 （Factory）。 

beanfactory。getbean（）方法获取时，返回的是factorybean工厂生产的bean类型。如果一定要取得FactoryBean本身的话，可以通过在bean定义的id之前加前缀&来达到目的。代 码清单4-34展示了获取FactoryBean本身与获取FactoryBean“生产”的对象之间的差别

######
beanpostprocessor和beanfactoryPostProcessor:

 >beanfactoryPostProcessor是在bean实例化之前，是在beandefinetion后触发。
 >而beanPostProcessor提供的方法是在bean的实例化过程中被调用。

####spring 官方文档
ioc阅读

Spring Enviroment 用于抽象properties和profile

Resouce ：

相对于java.net.Url,没有办法合适的抽象classPath等资源。而Spring的Resource接口旨在成为一种功能更强的接口，用于抽象对资源的访问。

	
	public interface Resource extends InputStreamSource {

	    boolean exists();
	
	    boolean isOpen();
	
	    URL getURL() throws IOException;
	
	    File getFile() throws IOException;
	
	    Resource createRelative(String relativePath) throws IOException;
	
	    String getFilename();
	
	    String getDescription();
	
	}

核心就是关注Spring如何加载系统中的File,是通过classpath方式，file：//方式还是URL方式？

额外注意，AbstractApplicationContext extends DefaultResourceLoader ,DefaultResourceLoader 实现如下：


	public class DefaultResourceLoader implements ResourceLoader {

		/**看看这个实现*/
		public Resource getResource(String location) {
	        Assert.notNull(location, "Location must not be null");
	        Iterator var2 = this.getProtocolResolvers().iterator();
	
	        Resource resource;
	        do {
	            if (!var2.hasNext()) {
	                if (location.startsWith("/")) {//实际也是classpath
	                    return this.getResourceByPath(location);
	                }
	
	                if (location.startsWith("classpath:")) {
	                    return new ClassPathResource(location.substring("classpath:".length()), this.getClassLoader());
	                }
	
	                try {
	                    URL url = new URL(location);
						//这里根据location前缀是file://or http分别返回FileUrlResource or UrlResource
	                    return (Resource)(ResourceUtils.isFileURL(url) ? new FileUrlResource(url) : new UrlResource(url));
	                } catch (MalformedURLException var5) {
	                    return this.getResourceByPath(location);
	                }
	            }
	
	            ProtocolResolver protocolResolver = (ProtocolResolver)var2.next();
	            resource = protocolResolver.resolve(location, this);
	        } while(resource == null);
	
	        return resource;
	    }
	}

###AOP
AOP在Spring框架中用于：

提供声明式企业服务。此类服务中最重要的是 声明式事务管理。

让用户实现自定义方面，并通过AOP补充其对OOP的使用。

Spring AOP当前仅支持方法执行连接点（建议在Spring Bean上执行方法）。尽管可以在不破坏核心Spring AOP API的情况下添加对字段拦截的支持，但并未实现字段拦截。如果需要建议**字段**访问和更新连接点，请考虑使用诸如AspectJ之类的语言。

Spring AOP默认是基于JDK的动态代理，因此会拦截Public方法，而如果选择CGLIB代理实现类的话，除了private方法，几乎都可以代理实现。

看到5.4.5