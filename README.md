### Dubbo是什么?
dubbo是一个分布式的服务框架,提供了完整的rpc调用以及服务治理功能
### Dubbo框架设计
- <font color='blue'>Dubbo的各个角色</font> 
1.Registry:注册中心
2.Provider:生产者
3.Consumer:消费者
4.Monitor:监控者
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo_%E8%A7%92%E8%89%B2.png)
- <font color='blue'>Dubbo的模块</font> 
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo%E6%A8%A1%E5%9D%97.png)
- <font color='blue'>Dubbo的配置层</font> 
模块:
dubbo-config
作用:
1.负责所有dubbo相关的xml配置和注释配置转换为config对象(dubbo.xsd)
2.API配置的对象类,用于生成对应的register,protocol等对象
核心类:
ServiceBean,ReferenceBean(对应<dubbo:service /><dubbo:reference />)
ProtocolConfig,RegisterConfig(对应<dubbo:protocol /><dubbo:register />)
- <font color='blue'>Dubbo的服务代理层</font>
模块:
dubbo-rpc
作用:
负责生成消费者的代理对象(对消费者使用的接口生成代理对象),以及服务提供方的invoke
核心类:
ProxyFactory:接口的两种实现:JdkProxyFactory,JavassistProxyFactory
- <font color='blue'>Dubbo的注册中心层</font>
模块:
dubbo-registory
作用:
1.负责注册与查询服务,以及注册服务的本地缓存
2.支持多种协议注册发现服务,例如redis,zokeeper
核心类:
接口:RegisteryFactory,Registry
AbstractRegistry以及ZookeeperRegistry,ZookeeperRegistryFactory
- <font color='blue'>Dubbo的路由层</font>
模块:
dubbo-cluster
作用:
1.负责负载均衡策略以及rpc失败策略
2.支持轮询,随机,一致性Hash等负载均衡策略
- <font color='blue'>Dubbo的监控层</font>

模块:
dubbo-monitor
作用:
RPC调用的次数和调用时间监控
dubbo-simple下面的dubbo-monitor-simple提供了简单的控制台
核心类:
DubboMonitor,Statistics
- <font color='blue'>Dubbo远程调用层</font>

模块:
dubbo-rpc
作用:
1.支持RPC调用,支持多种RPC协议
2.支持RMI,http,Webservice等rpc调用方式
核心类:
接口:Protocol,Exporter,Invoker
- <font color='blue'>Dubbo信息交换层</font>
模块:
dubbo-remoting
作用:
1.封装请求的响应模式,同步或者异步
2.处理各种协议的通信请求,支持netty,mian,http等
3.默认采用netty进行通信
核心类:
接口Server,Channel,Client
NettyClient,NeetyServer
- <font color='blue'>Dubbo序列化层</font>
模块:
dubbo-common
作用:
1.数据序列化层可复用的一些工具,包括序列化线程池等
2.dubbo协议缺省为hessian2,rmi协议缺省为java,http协议缺省为json
核心类:
接口:ThreadPool,Serialization
FixedThreadPool,HessianSerialization
### Dubbo的使用入门
#### 环境配置
- <font color='blue'>生产者简介</font>

1.生产者又叫做服务提供者,通常是实现一个接口将服务发布到注册中心上,发布后等待其他的消费者来消费.
2.伴随着容器的启动,比如NIO(netty)等一些东西.占用一个端口
3.服务注册,一般是注册在zk上
- <font color='blue'>Dubbo加载过程</font>
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo%E5%AE%B9%E5%99%A8%E7%9A%84%E5%90%AF%E5%8A%A8.png)
- <font color='blue'>Main函数启动java进程</font>

应用类型:
Java Application(Main函数启动的java进程)
启动类:
com.alibaba.dubbo.container.Main
com.alibaba.dubbo.container.spring.SpringContainer
springContainer默认加载Spring的目录:classpath:META-INF/spring/*.xml
加载所有的*.xml完成之后,如果有dubbo的配置就会启动dubbo的容器
- <font color='blue'>Dubbo服务提供者Xml</font>
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo%E6%8F%90%E4%BE%9B%E8%80%85xml%E9%85%8D%E7%BD%AE.png)
- <font color='blue'>Zookeeper的安装</font>

zookeeper的安装可以直接去官网下载:http://www.apache.org/dyn/closer.cgi/zookeeper/
下载完成之后我们进入到zookeeper文件夹下的conf文件夹下将zoo_sample.cfg文件命名为zoo.cfg即可,接下来我们进入到zookeeper的bin目录下运行zk
```sql
-- 进入目录
/usr/local/Cellar/zookeeper/zookeeper-3.3.6/bin

-- 运行zk
./zkServer.sh start
```
- <font color='blue'>dubbo-admin的安装</font>

安装dubbo-admin.war这一步是非必需的，不过使用dubbo-admin可以方便查看dubbo的运行状态和数据，便于管理.下载完成后放入到tomcat的webapps目录下，启动tomcat访问dubbo即可,但是网上现在的war包多一半是与jdk版本不匹配的,所以最好自己来fork源码进行编译:
1.fork dubbo的源码:https://github.com/alibaba/dubbo
2.下载下来之后进入到根目录进行编译:
```sql
-- 依据本地环境是否需要加上sudo
mvn package -Dmaven.skip.test=true
```
3.编译完成之后我们将dubbo-admin-2.5.4-SNAPSHOT.war 复制到我们的tomcat/webapp下面,修改tomcat的端口(因为zookeeper会用到8080的端口)
4.修改完成之后启动tomcat
```sql
-- 进入目录
cd /usr/local/Cellar/tomcat_flowengine/8.5.12/libexec/bin

-- 启动服务
./startup.sh
```
5.进入到tomcat解压后的dubbo-admin中,进入到WEB-INF下查看dubbo.properties,如果是以下内容则不用修改:
```sql
dubbo.registry.address=zookeeper://127.0.0.1:2181  
dubbo.admin.root.password=root  
dubbo.admin.guest.password=guest
```
6.<font color="red">注意事项:</font>启动tomcat之前一定要先启动zk
7.访问dubbo-admin:http://localhost:8099/dubbo-admin-2.5.4-SNAPSHOT/governance/services 让进行登录:用户名和密码都是root,看到如下的界面证明配置成功
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo-admin-login.jpg)
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo-admin-home.jpg)
#### Dubbo- 接口工程
1.创建一个打成jar包的java工程(http-interface),专门产生java接口供provider和consumer使用
2.将工程打成jar包,首先配置打包插接件,再执行mvn install
```java
  <build>
    <plugins>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>2.4.1</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
            <configuration>
              <transformers>
                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                  <mainClass>com.xxg.Main</mainClass>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring.handlers</resource>
                </transformer>
                <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                  <resource>META-INF/spring.schemas</resource>
                </transformer>
              </transformers>
            </configuration>
          </execution>
        </executions>
      </plugin>

    </plugins>
  </build>
```
#### Dubbo-Provider
1.pom文件配置:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.jose.provider</groupId>
  <artifactId>dubbo-provider</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>dubbo-provider Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <!-- dubbo -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>dubbo</artifactId>
      <version>2.5.2</version>
    </dependency>
    <dependency>
      <groupId>org.apache.zookeeper</groupId>
      <artifactId>zookeeper</artifactId>
      <version>3.4.6</version>
    </dependency>
    <!--zkclient-->
    <dependency>
      <groupId>com.github.sgroschupf</groupId>
      <artifactId>zkclient</artifactId>
      <version>0.1</version>
    </dependency>

    <!-- log relation -->
    <dependency>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

    <!-- spring relation -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-expression</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
  </dependencies>
  <build>
    <finalName>dubbo-provider</finalName>
  </build>
</project>

```
2.创建一个生产者的Maven工程dubbo-provider,将接口jar包添加到webapp/WEB-INF/lib下,并加入libery,或者有私服的话传入到私服,引入gav即可.
3.创建一个类实现接口工程中的接口(SpeakingAble):
```java
package com.jose.provider;

import com.jose.domain.People;
import com.jose.httpinterface.SpeakingAble;

public class SpeakInterfaceImpl implements SpeakingAble {

    public String speak(People people) {
        return "Hello world!";
    }
}
```
4.配置生产者的xml文件spring-dubbo-provider.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置dubbo服务的项目 -->
  <dubbo:application name="jose-dubbo-provider" />

  <!-- 配置注册中心 -->
  <dubbo:registry id="zk1" address="127.0.0.1:2181" protocol="zookeeper" />

  <!-- 配置协议使用dubbo -->
  <dubbo:protocol id="dubboProtocol" name="dubbo" port="20886" />

  <!-- 配置提供者 -->
  <dubbo:provider registry="zk1" protocol="dubboProtocol" />

  <!-- 定义实现类 -->
  <bean name="speakInterface" class="com.jose.provider.SpeakInterfaceImpl" />

  <!-- 定义服务 -->
  <dubbo:service interface="com.jose.httpinterface.SpeakingAble" ref="speakInterface"/>
</beans>
```
5.编写测试类
```java
package com.jose.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-dubbo-provider.xml");
        System.out.println("start dubbo");
        while (true) {
            Thread.yield();
        }
    }
}
```
6.启动后观察dubbo-admin控制台,发现多了一个服务
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo-admin-provider.jpg)
#### Dubbo-Consumer
功能:
1.消费服务
2.服务的发现和接口代理
3.负载均衡和服务容错策略

- <font color='red'>注意:</font>

因为消费者也需要使用到接口(http-interface),所以现将上面打的http-interface的jar包打进来

- <font color='blue'>Consumer配置项解释</font>

![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo-consumer%E8%A7%92%E8%89%B2.png)
- <font color='blue'>Pom文件配置</font>

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.jose.consumer</groupId>
  <artifactId>dubbo-consumer</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>dubbo-consumer Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <!-- dubbo -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>dubbo</artifactId>
      <version>2.5.2</version>
    </dependency>
    <dependency>
      <groupId>org.apache.zookeeper</groupId>
      <artifactId>zookeeper</artifactId>
      <version>3.4.6</version>
    </dependency>
    <!--zkclient-->
    <dependency>
      <groupId>com.github.sgroschupf</groupId>
      <artifactId>zkclient</artifactId>
      <version>0.1</version>
    </dependency>

    <!-- log relation -->
    <dependency>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

    <!-- spring relation -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-expression</artifactId>
      <version>4.1.4.RELEASE</version>
    </dependency>
  </dependencies>
  <build>
    <finalName>dubbo-consumer</finalName>
  </build>
</project>
```
- <font color='blue'>消费者类编写</font>

```java
package com.jose.consumer.test;

import com.jose.domain.People;
import com.jose.httpinterface.SpeakingAble;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-dubbo-consumer.xml");
        SpeakingAble speakInterface = (SpeakingAble) context.getBean("speakInterface");
        String say = speakInterface.speak(new People());
        System.out.print(say);
    }
}
```
- <font color='blue'>spring-dubbo-consumer.xml文件配置</font>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置dubbo服务的项目 -->
  <dubbo:application name="jose-dubbo-consumer" />

  <!-- 配置注册中心 -->
  <dubbo:registry id="zk1" address="127.0.0.1:2181" protocol="zookeeper" />

  <!-- 配置提供者 -->
  <dubbo:consumer registry="zk1" />

  <!-- 配置reference -->
  <dubbo:reference id="speakInterface" interface="com.jose.httpinterface.SpeakingAble" />
</beans>
```
- <font color='blue'>运行结果</font>

打印出了Hello world!
### Dubbo混合应用配置(即是provider又是consumer)
- <font color='blue'>分布式服务架构</font>

在网上找了一张分布式服务的图片,有助于我们理解分布式dubbo项目架构:
![enter image description here](http://oayt7zau6.bkt.clouddn.com/dubbo%E5%88%86%E5%B8%83%E5%BC%8F%E6%9C%8D%E5%8A%A1.png)
- <font color='blue'>注意事项</font>
1.循环依赖:
A=>B ,B=>A 这样的项目调用就会使循环依赖
2.超时时间
3.多线程(不安全性)
4.网络问题(zk和其他环境)
- <font color='blue'>同时配置Consumer和provider</font>
1.在同一个项目的reource文件下创建spring-dubbo-provider.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置dubbo服务的项目 -->
  <dubbo:application name="jose-dubbo-provider" />

  <!-- 配置注册中心 -->
  <dubbo:registry id="zk1" address="127.0.0.1:2181" protocol="zookeeper" />

  <!-- 配置协议使用dubbo -->
  <dubbo:protocol id="dubboProtocol" name="dubbo" port="20886" />

  <!-- 配置提供者 -->
  <dubbo:provider registry="zk1" protocol="dubboProtocol" />

  <!-- 定义实现类 -->
  <bean name="speakInterface" class="com.jose.provider.SpeakInterfaceImpl" />

  <!-- 定义服务 -->
  <dubbo:service interface="com.jose.httpinterface.SpeakingAble" ref="speakInterface"/>
</beans>
```
2.在这个目录下(resource)创建spring-dubbo-consumer.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置提供者 -->
  <dubbo:consumer registry="zk1" />

  <!-- 配置reference -->
  <dubbo:reference id="groupUpInterface" interface="com.jose.httpinterface.GroupUpInterface" />
</beans>
```
3.说明:由上面的配置可以看到这个应用提供了:speakInterface接口,同时消费了groupUpInterface接口
4.再创建另外的一个项目互相调用,配置和上面基本类似
spring-dubbo-consumer.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置dubbo服务的项目 -->
  <dubbo:application name="jose-dubbo-consumer" />

  <!-- 配置注册中心 -->
  <dubbo:registry id="zk1" address="127.0.0.1:2181" protocol="zookeeper" />

  <!-- 配置提供者 -->
  <dubbo:consumer registry="zk1" />

  <!-- 配置reference -->
  <dubbo:reference id="speakInterface" interface="com.jose.httpinterface.SpeakingAble" />
</beans>
```
spring-dubbo-provider.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
  http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

  <!-- 配置协议使用dubbo -->
  <dubbo:protocol id="dubboProtocol" name="dubbo" port="20887" />

  <!-- 配置提供者 -->
  <dubbo:provider registry="zk1" protocol="dubboProtocol" />

  <!-- 定义实现类 -->
  <bean name="groupUpInterface" class="com.jose.provider.GroupUpImpl" />

  <!-- 定义服务 -->
  <dubbo:service interface="com.jose.httpinterface.GroupUpInterface" ref="groupUpInterface"/>
</beans>
```
5.在一个项目中可以进行测试
```java
package com.jose.test;

import com.jose.domain.People;
import com.jose.httpinterface.GroupUpInterface;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        System.out.println("start dubbo");

        System.out.println("*********作为消费者****************");
        GroupUpInterface groupUpInterface = (GroupUpInterface) applicationContext.getBean("groupUpInterface");
        People people = groupUpInterface.addAge(new People());
        System.out.print(people.getAge());

        while (true) {
            Thread.yield();
        }
    }
}
```
