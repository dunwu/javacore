---
title: JavaAgent
date: 2022-04-08 17:29:48
order: 08
categories:
  - Java
  - JavaCore
  - 基础
tags:
  - Java
  - JavaCore
  - JavaAgent
permalink: /pages/5a64d59b/
---

# JavaAgent

## 简介

Java Agent（Java 探针）是 Java 提供的一种强大的运行时增强能力。它利用 JVM 提供的 `Instrumentation` API，可以在不修改源代码的情况下，在类加载时或运行时动态修改类的字节码。Java Agent 广泛应用于 **APM（应用性能监控）**、**链路追踪**、**热部署**、**动态代理**、**代码分析** 等场景，是 SkyWalking、Arthas、JRebel 等工具的核心技术基础。

Javaagent 是 Java 命令的一个参数。参数 javaagent 可以用于指定一个 jar 包，它利用 JVM 提供的 Instrumentation API 来更改加载 JVM 中的现有字节码。

1. 这个 jar 包的 MANIFEST.MF 文件必须指定 Premain-Class 项。
2. Premain-Class 指定的那个类必须实现 premain() 方法。

premain 方法，从字面上理解，就是运行在 main 函数之前的的类。当 Java 虚拟机启动时，在执行 main 函数之前，JVM 会先运行`-javaagent`所指定 jar 包内 Premain-Class 这个类的 premain 方法 。

在命令行输入 `java`可以看到相应的参数，其中有和 Java Agent 相关的：

```shell
-agentlib:<libname>[=<选项>]
			  加载本机代理库 <libname>, 例如 -agentlib:hprof
			  另请参阅 -agentlib:jdwp=help 和 -agentlib:hprof=help
-agentpath:<pathname>[=<选项>]
			  按完整路径名加载本机代理库
-javaagent:<jarpath>[=<选项>]
			  加载 Java 编程语言代理, 请参阅 java.lang.instrument
```

## Java Agent 技术简介

Java Agent 直译为 Java 代理，也常常被称为 Java 探针技术。

Java Agent 是在 JDK1.5 引入的，是一种可以动态修改 Java 字节码的技术。Java 中的类编译后形成字节码被 JVM 执行，在 JVM 在执行这些字节码之前获取这些字节码的信息，并且通过字节码转换器对这些字节码进行修改，以此来完成一些额外的功能。

Java Agent 是一个不能独立运行 jar 包，它通过依附于目标程序的 JVM 进程，进行工作。启动时只需要在目标程序的启动参数中添加-javaagent 参数添加 ClassFileTransformer 字节码转换器，相当于在 main 方法前加了一个拦截器。

## Java Agent 功能介绍

Java Agent 主要有以下功能

- Java Agent 能够在加载 Java 字节码之前拦截并对字节码进行修改;
- Java Agent 能够在 Jvm 运行期间修改已经加载的字节码;

Java Agent 的应用场景

- IDE 的调试功能，例如 Eclipse、IntelliJ IDEA ；
- 热部署功能，例如 JRebel、XRebel、spring-loaded；
- 各种线上诊断工具，例如 Btrace、Greys，还有阿里的 Arthas；
- 各种性能分析工具，例如 Visual VM、JConsole 等；
- 全链路性能检测工具，例如 Skywalking、Pinpoint 等；

## Java Agent 实现原理

在了解 Java Agent 的实现原理之前，需要对 Java 类加载机制有一个较为清晰的认知。一种是在 man 方法执行之前，通过 premain 来执行，另一种是程序运行中修改，需通过 JVM 中的 Attach 实现，Attach 的实现原理是基于 JVMTI。

主要是在类加载之前，进行拦截，对字节码修改

下面我们分别介绍一下这些关键术语：

- **JVMTI** 就是 JVM Tool Interface，是 JVM 暴露出来给用户扩展使用的接口集合，JVMTI 是基于事件驱动的，JVM 每执行一定的逻辑就会触发一些事件的回调接口，通过这些回调接口，用户可以自行扩展

  JVMTI 是实现 Debugger、Profiler、Monitor、Thread Analyser 等工具的统一基础，在主流 Java 虚拟机中都有实现

- **JVMTIAgent**是一个动态库，利用 JVMTI 暴露出来的一些接口来干一些我们想做、但是正常情况下又做不到的事情，不过为了和普通的动态库进行区分，它一般会实现如下的一个或者多个函数：

  - Agent_OnLoad 函数，如果 agent 是在启动时加载的，通过 JVM 参数设置
  - Agent_OnAttach 函数，如果 agent 不是在启动时加载的，而是我们先 attach 到目标进程上，然后给对应的目标进程发送 load 命令来加载，则在加载过程中会调用 Agent_OnAttach 函数
  - Agent_OnUnload 函数，在 agent 卸载时调用

- **javaagent** 依赖于 instrument 的 JVMTIAgent（Linux 下对应的动态库是 libinstrument.so），还有个别名叫 JPLISAgent(Java Programming Language Instrumentation Services Agent)，专门为 Java 语言编写的插桩服务提供支持的

- **instrument** 实现了 Agent_OnLoad 和 Agent_OnAttach 两方法，也就是说在使用时，agent 既可以在启动时加载，也可以在运行时动态加载。其中启动时加载还可以通过类似-javaagent:jar 包路径的方式来间接加载 instrument agent，运行时动态加载依赖的是 JVM 的 attach 机制，通过发送 load 命令来加载 agent

- **JVM Attach** 是指 JVM 提供的一种进程间通信的功能，能让一个进程传命令给另一个进程，并进行一些内部的操作，比如进行线程 dump，那么就需要执行 jstack 进行，然后把 pid 等参数传递给需要 dump 的线程来执行

## Java Agent 案例

### 加载 Java 字节码之前拦截

#### App 项目

（1）创建一个名为 `javacore-javaagent-app` 的 maven 工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.github.dunwu.javacore</groupId>
  <artifactId>javacore-javaagent-app</artifactId>
  <version>1.0.1</version>
  <name>JavaCore :: JavaAgent :: App</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
  </properties>
</project>
```

（2）创建一个应用启动类

```java
public class AppMain {

    public static void main(String[] args) {
        System.out.println("APP 启动！！！");
        AppInit.init();
    }

}
```

（3）创建一个模拟应用初始化的类

```java
public class AppInit {

    public static void init() {
        try {
            System.out.println("APP初始化中...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
```

（4）输出

```
APP 启动！！！
APP初始化中...
```

#### Agent 项目

（1）创建一个名为 `javacore-javaagent-agent` 的 maven 工程

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/POM/4.0.0"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.github.dunwu.javacore</groupId>
  <artifactId>javacore-javaagent-agent</artifactId>
  <version>1.0.1</version>
  <name>JavaCore :: JavaAgent :: Agent</name>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
    <maven.compiler.source>${java.version}</maven.compiler.source>
    <maven.compiler.target>${java.version}</maven.compiler.target>
  </properties>

  <dependencies>
    <!--javaagent 工具包-->
    <dependency>
      <groupId>org.javassist</groupId>
      <artifactId>javassist</artifactId>
      <version>3.26.0-GA</version>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.5.1</version>
        <!--指定 maven 编译的 jdk 版本。若不指定，maven3 默认用 jdk 1.5；maven2 默认用 jdk1.3-->
        <configuration>
          <source>8</source>
          <target>8</target>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.2.0</version>
        <configuration>
          <archive>
            <!--自动添加META-INF/MANIFEST.MF -->
            <manifest>
              <addClasspath>true</addClasspath>
            </manifest>
            <manifestEntries>
              <Menifest-Version>1.0</Menifest-Version>
              <Premain-Class>io.github.dunwu.javacore.javaagent.RunTimeAgent</Premain-Class>
              <Can-Redefine-Classes>true</Can-Redefine-Classes>
              <Can-Retransform-Classes>true</Can-Retransform-Classes>
            </manifestEntries>
          </archive>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

（2）创建一个 Agent 启动类

```java
public class RunTimeAgent {

    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("探针启动！！！");
        System.out.println("探针传入参数：" + arg);
        instrumentation.addTransformer(new RunTimeTransformer());
    }
}
```

这里每个类加载的时候都会走这个方法，我们可以通过 className 进行指定类的拦截，然后借助 javassist 这个工具，进行对 Class 的处理，这里的思想和反射类似，但是要比反射功能更加强大，可以动态修改字节码。

（3）使用 javassist 拦截指定类，并进行代码增强

```java
package io.github.dunwu.javacore.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class RunTimeTransformer implements ClassFileTransformer {

    private static final String INJECTED_CLASS = "io.github.dunwu.javacore.javaagent.AppInit";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        String realClassName = className.replace("/", ".");
        if (realClassName.equals(INJECTED_CLASS)) {
            System.out.println("拦截到的类名：" + realClassName);
            CtClass ctClass;
            try {
                // 使用javassist,获取字节码类
                ClassPool classPool = ClassPool.getDefault();
                ctClass = classPool.get(realClassName);

                // 得到该类所有的方法实例，也可选择方法，进行增强
                CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                for (CtMethod method : declaredMethods) {
                    System.out.println(method.getName() + "方法被拦截");
                    method.addLocalVariable("time", CtClass.longType);
                    method.insertBefore("System.out.println(\"---开始执行---\");");
                    method.insertBefore("time = System.currentTimeMillis();");
                    method.insertAfter("System.out.println(\"---结束执行---\");");
                    method.insertAfter("System.out.println(\"运行耗时: \" + (System.currentTimeMillis() - time));");
                }
                return ctClass.toBytecode();
            } catch (Throwable e) { //这里要用Throwable，不要用Exception
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

}
```

（4）输出

指定 VM 参数 -javaagent:F:\code\myCode\agent-test\runtime-agent\target\runtime-agent-1.0-SNAPSHOT.jar=hello，运行 AppMain

```
探针启动！！！
探针传入参数：hello
APP 启动！！！
拦截到的类名：io.github.dunwu.javacore.javaagent.AppInit
init方法被拦截
---开始执行---
APP初始化中...
---结束执行---
运行耗时: 1014
```

### 运行时拦截（JDK 1.6 及以上）

如何实现在程序运行时去完成动态修改字节码呢？

动态修改字节码需要依赖于 JDK 为我们提供的 JVM 工具，也就是上边我们提到的 Attach，通过它去加载我们的代理程序。

首先我们在代理程序中需要定义一个名字为 agentmain 的方法，它可以和上边我们提到的 premain 是一样的内容，也可根据 agentmain 的特性进行自己逻辑的开发。

```java
/**
 * agentmain 在 main 函数开始运行后才启动（依赖于Attach机制）
 */
public class RunTimeAgent {

    public static void agentmain(String arg, Instrumentation instrumentation) {
        System.out.println("agentmain探针启动！！！");
        System.out.println("agentmain探针传入参数：" + arg);
        instrumentation.addTransformer(new RunTimeTransformer());
    }
}
```

然后就是我们需要将配置中设置，让其知道我们的探针需要加载这个类，在 maven 中设置如下，如果是 META-INF/MANIFEST.MF 文件同理。

```xml
<!--<Premain-Class>com.zhj.agent.agentmain.RunTimeAgent</Premain-Class>-->
<Agent-Class>com.zhj.agent.agentmain.RunTimeAgent</Agent-Class>
```

这样其实我们的探针就已经改造好了，然后我们需要在目标程序的 main 方法中植入一些代码，使其可以读取到我们的代理程序，这样我们也无需去配置 JVM 的参数，就可以加载探针程序。

```java
public class APPMain {

    public static void main(String[] args) {
        System.out.println("APP 启动！！！");
        for (VirtualMachineDescriptor vmd : VirtualMachine.list()) {
            // 指定的VM才可以被代理
            if (true) {
                System.out.println("该VM为指定代理的VM");
                System.out.println(vmd.displayName());
                try {
                    VirtualMachine vm = VirtualMachine.attach(vmd.id());
                    vm.loadAgent("D:/Code/java/idea_project/agent-test/runtime-agent/target/runtime-agent-1.0-SNAPSHOT.jar=hello");
                    vm.detach();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        AppInit.init();
    }
}
```

其中 VirtualMachine 是 JDK 工具包下的类，如果系统环境变量没有配置，需要自己在 Maven 中引入本地文件。

```java
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>D:/Software/java_dev/java_jdk/lib/tools.jar</systemPath>
</dependency>
复制代码
```

这样我们在程序启动后再去动态修改字节码文件的简单案例就完成了。

## 典型应用场景

### 场景一：APM 应用性能监控（SkyWalking/Pinpoint）

通过 Java Agent 无侵入地拦截方法调用，采集响应时间、异常、调用链等数据：

```java
public class PerfAgent {
    public static void premain(String args, Instrumentation inst) {
        inst.addTransformer((loader, className, classBeingRedefined,
                             protectionDomain, classfileBuffer) -> {
            if (className.startsWith("com/myapp/service")) {
                // 用 ASM/ByteBuddy 注入计时逻辑
                return injectTimingBytecode(classfileBuffer);
            }
            return null; // 不修改
        });
    }
}
```

### 场景二：在线诊断工具（Arthas）

通过 Attach API 动态加载 Agent，实时查看方法调用参数、返回值、耗时：

```bash
# Arthas 连接到目标 JVM
java -jar arthas-boot.jar
# 监控方法调用
watch com.myapp.UserService getUser "{params, returnObj, throwExp}"
```

### 场景三：热部署/热修复（JRebel/DCEVM）

开发阶段通过 Agent 监听 class 文件变化，自动重新加载修改的类，无需重启 JVM：

```java
// 检测到 class 文件变更后重新定义类
inst.redefineClasses(new ClassDefinition(targetClass, newBytecode));
```

## 最佳实践

1. **优先使用 ByteBuddy/ASM 操作字节码** - 直接操作 class 字节码非常复杂且易出错，ByteBuddy 提供了高级抽象 API
2. **premain 和 agentmain 都要实现** - `premain` 用于启动时加载，`agentmain` 用于运行时 Attach 加载，实现两者可以覆盖更多场景
3. **避免在 Agent 中引入复杂逻辑** - Agent 代码运行在每个类的加载路径上，复杂逻辑会严重影响类加载性能
4. **MANIFEST.MF 配置不能遗漏** - `Premain-Class`、`Agent-Class`、`Can-Redefine-Classes`、`Can-Retransform-Classes` 都要正确配置
5. **注意类加载顺序** - Agent 中的类可能比应用类先加载，注意依赖关系和 ClassLoader 隔离

## 常见问题

**Q1：`premain` 和 `agentmain` 有什么区别？**

`premain` 在 JVM 启动时、main 方法执行前运行，通过 `-javaagent` 参数加载；`agentmain` 在 JVM 运行过程中动态加载，通过 Attach API（`VirtualMachine.attach(pid)`）加载。前者适合始终启用的 Agent，后者适合按需诊断工具。

**Q2：Java Agent 会影响应用性能吗？**

会有一定影响。字节码增强本身会增加类加载时间，注入的拦截逻辑会增加方法执行时间。但在实际使用中（如 SkyWalking），影响通常在 3%~5% 以内，可以通过采样策略降低开销。

**Q3：Attach API 在某些环境下无法使用怎么办？**

在容器化环境（如 Docker/K8s）中，Attach API 可能因为 PID namespace 隔离而失败。解决方案：开启 `--pid=host`，或使用基于 premain 的方式替代动态 attach。

## 参考资料

- [Java Agent 探针技术](https://juejin.cn/post/7086026013498408973)
