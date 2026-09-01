# JavaCore :: In Web — Java 核心特性在 Web 场景中的示例

> 本模块是一个可运行的 **Spring Boot** 应用，用于演示 Java 核心特性（尤其是并发）在真实 Web 环境中的表现与陷阱。与其他纯命令行示例模块不同，这里通过 HTTP 接口触发场景，更贴近实际生产问题。

示例源码路径：`src/main/java/io/github/dunwu/javacore/web/`

---

## 应用骨架

- `SpringBootHelloWorldApplication` — Spring Boot 启动类（`@SpringBootApplication`），并通过 `CommandLineRunner` 在启动后打印日志。
- `HelloController` — `@RestController`，映射 `/` 与 `/hello`，返回 `Hello World`，用于验证应用可正常启动与响应。
- `CustomConfig` — `@Configuration` 配置类，注册 `RestTemplate` Bean（通过 `RestTemplateBuilder` 构建）。
- `resources/application.properties` — 应用配置：**端口 18080**，且 `server.tomcat.max-threads = 1`（刻意设为单线程，以便复现线程复用场景）。
- `resources/banner.txt`、`resources/logback.xml` — 启动 banner 与日志配置。

## 并发：Web 场景下的 ThreadLocal 陷阱（web/concurrent）

演示 `ThreadLocal` 在 Web 容器线程复用下的经典错误与正确用法。

- `concurrent/ThreadLocalErrorDemo` — 一个 `@RestController`，通过 `ThreadLocal<Integer> currentUser` 存放当前用户：
  - `GET /threadlocal/wrong?id=xxx`（反例） — 设置 ThreadLocal 后**未清理**。由于 Tomcat 线程会被复用，下一个请求会读到上一个请求残留的用户数据，造成数据串号 / 内存泄漏。
  - `GET /threadlocal/right?id=xxx`（正确） — 在 `finally` 中调用 `currentUser.remove()`，确保每次请求结束后清理线程变量。
  - 两个接口都返回 `before` / `after` 两次查询结果（含线程名），便于对比线程复用带来的差异。

---

## 运行与验证

```bash
mvn spring-boot:run -pl codes/javacore-in-web
```

启动后访问：

- `http://localhost:18080/hello` → 返回 `Hello World`
- `http://localhost:18080/threadlocal/wrong?id=1`，再 `?id=2` → 观察 `before` 出现上一次请求残留值
- `http://localhost:18080/threadlocal/right?id=1`，再 `?id=2` → `before` 始终为 `null`（已正确清理）

> 说明：为使 ThreadLocal 反例易于复现，配置中将 Tomcat 最大线程数设为 1；实际生产中线程池同样会复用线程，问题本质一致。
