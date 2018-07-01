---
title: JDK8 升级常见问题
date: 2017/11/08
categories:
- javacore
tags:
- javacore
- 升级
---

# JDK8 升级常见问题

> JDK8 发布很久了，它提供了许多吸引人的新特性，能够提高编程效率。
>
> 如果是新的项目，使用 JDK8 当然是最好的选择。但是，对于一些老的项目，升级到 JDK8 则存在一些兼容性问题，是否升级需要酌情考虑。
>
> 近期，我在工作中遇到一个任务，将部门所有项目的 JDK 版本升级到 1.8 （老版本大多是 1.6）。在这个过程中，遇到一些问题点，并结合在网上看到的坑，在这里总结一下。

## FAQ

### Intellij 中的 JDK 环境设置

#### Settings

点击 **File > Settings > Java Compiler**

Project bytecode version 选择 1.8

点击 **File > Settings > Build Tools > Maven > Importing**

选择 JDK for importer 为 1.8

#### Projcet Settings

**Project SDK** 选择 1.8

#### Application

如果 web 应用的启动方式为 Application ，需要修改 JRE

点击 **Run/Debug Configurations > Configuration**

选择 JRE 为 1.8

### Linux 环境修改

#### 修改环境变量

修改 `/etc/profile` 中的 **JAVA_HOME**，设置 为 jdk8 所在路径。

修改后，执行 `source /etc/profile` 生效。

编译、发布脚本中如果有 `export JAVA_HOME` ，需要注意，需要使用 jdk8 的路径。

#### 修改 maven

settings.xml 中 profile 的激活条件如果是 jdk，需要修改一下 jdk 版本

```xml
<activation>
  <jdk>1.8</jdk> <!-- 修改为 1.8 -->
</activation>
```

#### 修改 server

修改 server 中的 javac 版本，以 resin 为例：

修改 resin 配置文件中的 javac 参数。

```xml
<javac compiler="internal" args="-source 1.8"/>
```

### sun.\* 包缺失问题

JDK8 不再提供 `sun.*` 包供开发者使用，因为这些接口不是公共接口，不能保证在所有 Java 兼容的平台上工作。

使用了这些 API 的程序如果要升级到 JDK 1.8 需要寻求替代方案。

虽然，也可以自己导入包含 `sun.*` 接口 jar 包到 classpath 目录，但这不是一个好的做法。

需要详细了解为什么不要使用 `sun.*` ，可以参考官方文档：[Why Developers Should Not Write Programs That Call 'sun' Packages](http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html)

### 默认安全策略修改

升级后估计有些小伙伴在使用不安全算法时可能会发生错误，so，支持不安全算法还是有必要的

找到$JAVA_HOME下 `jre/lib/security/java.security` ，将禁用的算法设置为空：`jdk.certpath.disabledAlgorithms=` 。

### JVM参数调整

在jdk8中，PermSize相关的参数已经不被使用：

```
-XX:MaxPermSize=size

Sets the maximum permanent generation space size (in bytes). This option was deprecated in JDK 8, and superseded by the -XX:MaxMetaspaceSize option.

-XX:PermSize=size

Sets the space (in bytes) allocated to the permanent generation that triggers a garbage collection if it is exceeded. This option was deprecated un JDK 8, and superseded by the -XX:MetaspaceSize option.
```

JDK8 中再也没有 `PermGen` 了。其中的某些部分，如被 intern 的字符串，在 JDK7 中已经移到了普通堆里。**其余结构在 JDK8 中会被移到称作“Metaspace”的本机内存区中，该区域在默认情况下会自动生长，也会被垃圾回收。它有两个标记：MetaspaceSize 和 MaxMetaspaceSize。**

-XX:MetaspaceSize=size

> Sets the size of the allocated class metadata space that will trigger a garbage collection the first time it is exceeded. This threshold for a garbage collection is increased or decreased depending on the amount of metadata used. The default size depends on the platform.

-XX:MaxMetaspaceSize=size

>  Sets the maximum amount of native memory that can be allocated for class metadata. By default, the size is not limited.  The amount of metadata for an application depends on the application itself, other running applications, and the amount of memory available on the system.

以下示例显示如何将类类元数据的上限设置为 256 MB：

XX:MaxMetaspaceSize=256m

### 字节码问题

ASM 5.0 beta 开始支持 JDK8

**字节码错误**

```
Caused by: java.io.IOException: invalid constant type: 15
	at javassist.bytecode.ConstPool.readOne(ConstPool.java:1113)
```

- 查找组件用到了 mvel，mvel 为了提高效率进行了字节码优化，正好碰上 JDK8 死穴，所以需要升级。

```xml
<dependency>
  <groupId>org.mvel</groupId>
  <artifactId>mvel2</artifactId>
  <version>2.2.7.Final</version>
</dependency>
```

- javassist

```xml
<dependency>
  <groupId>org.javassist</groupId>
  <artifactId>javassist</artifactId>
  <version>3.18.1-GA</version>
</dependency>
```

> **注意**
>
> 有些部署工具不会删除旧版本 jar 包，所以可以尝试手动删除老版本 jar 包。

http://asm.ow2.org/history.html

### Java连接redis启动报错Error redis clients jedis HostAndPort cant resolve localhost address

错误环境:
本地window开发环境没有问题。上到Linux环境,启动出现问题。
错误信息:
Error redis clients jedis HostAndPort cant resolve localhost address

解决办法:

1. 查看Linux系统的主机名

```
# hostname
template
```

2. 查看/etc/hosts文件中是否有127.0.0.1对应主机名，如果没有则添加

### Resin 容器指定 JDK 1.8

如果 resin 容器原来版本低于 JDK1.8，运行 JDK 1.8 编译的 web app 时，可能会提示错误：

```
java.lang.UnsupportedClassVersionError: PR/Sort : Unsupported major.minor version 52.0
```

解决方法就是，使用 JDK 1.8 要重新编译一下。然后，我在部署时出现过编译后仍报错的情况，重启一下服务器后，问题解决，不知是什么原因。

```
./configure --prefix=/usr/local/resin  --with-java=/usr/local/jdk1.8.0_121
make & make install
```

## 资料

- [Compatibility Guide for JDK 8](http://www.oracle.com/technetwork/java/javase/8-compatibility-guide-2156366.html)
- [Compatibility Guide for JDK 8 中文翻译](https://yq.aliyun.com/articles/236)
- [Why Developers Should Not Write Programs That Call 'sun' Packages](http://www.oracle.com/technetwork/java/faq-sun-packages-142232.html)
