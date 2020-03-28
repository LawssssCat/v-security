官方：[https://spring.io/projects](https://spring.io/projects)

视频：[https://www.bilibili.com/video/BV1sE411w7NM?p=3](https://www.bilibili.com/video/BV1sE411w7NM?p=3)

上一篇 ： [https://blog.csdn.net/LawssssCat/article/details/105067494](https://blog.csdn.net/LawssssCat/article/details/105067494)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324212328334.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

# 项目构建

**模块关系：**
artifactId | groupId | 类型 | 备注
-- | --| -- | --
v-security | cn.vshop.security | ==pom== | 主模块
v-security-core | cn.vshop.security | jar | 核心业务逻辑
v-security-browser | cn.vshop.security | jar | 浏览器安全特定模块
v-security-app | cn.vshop.security | jar | app 相关特定模块
v-security-demo| cn.vshop.security | jar | 样例程序

[idea 创建 Maven 多模块项目](https://www.cnblogs.com/bingshu/p/7755182.html)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324204438982.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =300x)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324225437334.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =300x)

## # v-security

父模块，为其他模块提供环境

**pom.xml**

[platform-bom](https://docs.spring.io/platform/docs/Brussels-SR17/reference/htmlsingle/#getting-started-using-spring-io-platform-maven)
[spring-cloud-dependencies](https://blog.csdn.net/forezp/article/details/69696915)

> 选 GA 版本的依赖：
>
> - `GA`:General Availability,正式发布的版本，==官方推荐使用此版本==。在国外都是用 GA 来说明 `release` 版本的。
> - `PRE`: 预览版,内部测试版. 主要是给开发人员和测试人员测试和找 BUG 用的，不建议使用；
> - `SNAPSHOT`: 快照版，可以稳定使用，且仍在继续改进版本。

```xml
    <dependencyManagement>
	    <!--替我们管理maven依赖版本,避免版本不兼容-->
        <dependencies>
            <dependency>
                <groupId>io.spring.platform</groupId>
                <artifactId>platform-bom</artifactId>
                <version>Brussels-SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
  			<dependency>
				<groupId>org.springframework.cloud</groupId>
				<artifactId>spring-cloud-dependencies</artifactId>
				<version>Dalston.SR2</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
        </dependencies>
    </dependencyManagement>
```

[maven 编译插件](https://www.cnblogs.com/april-chen/p/10414857.html) 指定 JDK 版本为 1.8

```xml
<build>
    <plugins>
        <!--指定编译版本，指定为JDK1.8-->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>2.3.2</version>
            <configuration>
                <!-- 源代码使用的JDK版本 -->
                <source>1.8</source>
                <!-- 需要生成的目标class文件的编译版本 -->
                <target>1.8</target>
                <!-- 字符集编码 -->
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
    </plugins>
</build>
```

检查是否有自动构建父子模块关系，若没有就需要手动构建

```xml
<!-- v-security 的 pom 文件中需要有 -->
<modules>
    <module>v-security-core</module>
    <module>v-security-browser</module>
    <module>v-security-app</module>
    <module>v-security-demo</module>
</modules>
```

```xml
<!-- v-security-app、v-security-core、v-security-browser、v-security-demo 的 pom 文件中需要有 -->
    <parent>
        <artifactId>v-security</artifactId>
        <groupId>cn.vshop.security</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
```

## # v-security-core

**pom.xml**

[Spring cloud oauth2 研究--第一个 DEMO](https://www.jianshu.com/p/1405fc42c428)

```xml
 <!--作为验证服务包含了security 和 oauth2-->
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
 </dependency>
```

（上面）这个依赖会为我们引入大量的 jar 包

主要包括：spring-security 相关依赖、和 `spring-security-oauth2`（重要）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324231818190.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

然后引入两个和存储相关的依赖： `jdbc` 和 `redis`

```xml
<!--和存储相关的依赖:jdbc/redis,会帮我们做redis和数据库的相关配置-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

==注意，这里都是不需要写版本的，原因是父项目中的：== `spring-plaform` 和 `spring-cloud` ==在起作用==。

**引入 spring-social 依赖**

做第三方登录，如 qq、weixin、github。
（后面用到详细讲）

[ Spring Social Reference - Getting Spring Social](https://docs.spring.io/spring-social/docs/2.0.0.M4/reference/htmlsingle/#section_how-to-get)

```xml
<dependency>
    <groupId>org.springframework.social</groupId>
    <artifactId>spring-social-core</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.social</groupId>
    <artifactId>spring-social-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.social</groupId>
    <artifactId>spring-social-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.social</groupId>
    <artifactId>spring-social-security</artifactId>
</dependency>
```

**最后，添加三个 apache 提供的 commons 工具包**

[Apache commons（Java 常用工具包）简介](https://www.cnblogs.com/eer123/p/9120120.html)

```xml
<!--apache commons 工具包-->
<!--提供了许多许多通用的工具类集，提供了一些java.lang中类的扩展功能-->
<dependency>
    <groupId>commons-lang</groupId>
    <artifactId>commons-lang</artifactId>
</dependency>
<!--提供一个类包来扩展和增加标准的 Java Collection框架-->
<dependency>
    <groupId>commons-collections</groupId>
    <artifactId>commons-collections</artifactId>
</dependency>
<!--提供对 Java 反射和自省API的包装-->
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
</dependency>
```

## # v-security-app

**引用 core 模块作为依赖**

==注意，这里要指定版本==

```xml
<!--其他块：v-security-app、v-security-browser中添加-->
<dependencies>
    <dependency>
        <groupId>cn.vshop.security</groupId>
        <artifactId>v-security-core</artifactId>
        <version>${v.security.version}</version>
    </dependency>
</dependencies>
```

==版本在 父模块中指定==

```xml
<!--父模块：v-security中添加-->
<properties>
     <v.security.version>1.0-SNAPSHOT</v.security.version>
 </properties>
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324235152957.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

## # v-security-browser

```xml
<!--其他块：v-security-app、v-security-browser中添加-->
<dependencies>
    <dependency>
        <groupId>cn.vshop.security</groupId>
        <artifactId>v-security-core</artifactId>
        <version>${v.security.version}</version>
    </dependency>
</dependencies>
```

**引入 spring-session**

着这里，浏览器安全建立在 session 的基础上。
因此 ==需要 spring-session ，帮我们在 `集群` 环境下，做 session 管理==

```xml
<!-- core 模块中配置，所以不需要
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
</dependency>-->
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
</dependency>
```

## # v-security-demo

回顾这幅图， demo 分别依赖 browser 和 app
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324204438982.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =300x)

先讲 browser，因此，==先添加 browser 的依赖==

```xml
    <dependencies>
        <dependency>
            <groupId>cn.vshop.security</groupId>
            <artifactId>v-security-browser</artifactId>
            <version>${v.security.version}</version>
        </dependency>
    </dependencies>
```

# 测试：Hello World

| 域名                | ip                      | 端口 | 备注   |
| ------------------- | ----------------------- | ---- | ------ |
| vdb.cn              | 192.168.64.33           | 3306 | 数据库 |
| vshop.cn、localhost | 192.168.64.1、127.0.0.1 | 8080 | 客户端 |

**编写启动类：**

```java
package cn.vshop.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author alan smith
 * @version 1.0
 * @date 2020/3/25 0:27
 */
@SpringBootApplication
@RestController
public class DemoApplication {

    /**
     * 启动类
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security";
    }

}
```

**配置 dataSource（数据源）：**

application.yml 中添加

```yml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://vdb.cn:3306/vsdb?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false
    username: root
    password: root
```

> 否则，报错：Cannot determine embedded database driver class for database type NONE
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200325010119896.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

**配置 Spring Session 的存储（store）**

我们先把 spring 的 session 管理给关闭（后面用到再打开）

```yml
spring:
  session:
    store-type: none
  # dataSource ...
```

> 否则，会报错（下图）：No Spring Session store is configured: set the 'spring.session.store-type' property
> 这个错误的来源是：v-security.browser 中的 spring-session 依赖。
> ==后面做集群的 session 管理的==
> （点击放大查看）
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200325010550284.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =300x) ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200325010800504.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =300x)

**配置 redis 连接**

我的 redis 位置在 192.168.64.33:3679，没设密码，并且在 redis.conf 配置里关了保护 `protected-mode no`（方便开发）

所以只需要在 application.yml 中添加 ip 和 端口

```yml
spring:
  #  ...

  redis:
    host: 192.168.64.33
    port: 6379
    # password:
```

> 如果出现报错：
> Cannot get Jedis connection; nested exception is redis.clients.jedis.exceptions.JedisConnectionException: Could not get a resource from the pool
>
> 是因为没和 redis 成功连上，看下是否要加密码，或者是否是防火墙阻挡了
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328200832663.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70 =500x)

> 如果出现报错：
> DENIED Redis is running in protected mode because protected mode is enabled, no bind address was specified, no authentication password is requested to clients. In this mode connections are only accepted from the loopback interface. If you want to connect from external computers to Redis you may adopt one of the following solutions: 1) Just disable protected mode sending the command 'CONFIG SET protected-mode no' from the loopback interface by connecting to Redis from the same host the server is running, however MAKE SURE Redis is not publicly accessible from internet if you do so. Use CONFIG REWRITE to make this change permanent. 2) Alternatively you can just disable the protected mode by editing the Redis configuration file, and setting the protected mode option to 'no', and then restarting the server. 3) If you started the server manually just for testing, restart it with the '--protected-mode no' option. 4) Setup a bind address or an authentication password. NOTE: You only need to do one of the above things in order for the server to start accepting connections from the outside.;
> .![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328203853764.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)
> 是因为你没 设置密码，并且没设置保护模式关闭 `protected-mode no`
> （redis 默认对远程连接进行保护，要成功连接，要么设密码，要么关了保护）

**启动后，登录访问**

启动成功后，访问 [http://localhost:8080/hello](http://localhost:8080/hello)

输入账号：user，密码：fc62e05d-e5a8-49ac-96e2-5ca7162c11a1（看你的控制台）

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328195912200.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328200126131.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

> 如果想关闭 basichttp 登录，添加配置
>
> ```yml
> security:
>   basic:
>     enabled: false
> ```
>
> 访问就不用输入密码了

# 项目打包

v-security-demo 测试成功，但是要打包还需要添加一个插件
（帮我们把其他组件的 jar 包也进行打包，这样，我们的 jar 包能直接运行）

**在 v-security-demo 的 pom 里面添加**

```xml
    <!--指定build期间的插件-->
    <build>
        <plugins>
            <!--帮我们做打包的插件-->
            <plugin>
                <groupId>org.springframework.boot</groupId>
   <!--能够将Spring Boot应用打包为可执行的jar或war文件，然后以通常的方式运行Spring Boot应用-->
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.3.3.RELEASE</version>
                <executions>
                    <execution>
                        <goals>
   <!--在mvn package之后，再次打包可执行的jar/war，同时保留mvn package生成的jar/war为.origin-->
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <!--指定打包后的名字-->
        <finalName>demo</finalName>
    </build>
```

[Maven 插件系列之 spring-boot-maven-plugin](https://www.cnblogs.com/jpfss/p/11098740.html)

**maven 打包**

v-security-demo 组件的根路径 执行 maven 命令

```bash
mvn clean package
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328205658614.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)
。。。。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328205721174.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328210132805.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

**运行 jar 包**

```bash
uu@LAWSSSSCATWIN MINGW64 /f/environment/java/workspace/v-security/v-security-demo/target (master)
$ java -jar demo.jar
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200328210650535.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)

至此，环境构建完成，下面进行 rest 服务编写

done ~

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200324212353502.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xhd3Nzc3NDYXQ=,size_16,color_FFFFFF,t_70)
