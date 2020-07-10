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

编程式事务处理和声明式事务处理。一般常用声明式事务处理就够了，简单明了，对代码侵入小。