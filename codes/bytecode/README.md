# JavaCore :: Bytecode — Java 字节码操作与 JavaAgent 示例

> 本模块展示 Java 字节码（bytecode）层面的操作能力：使用 ASM、Javassist 在运行期修改类字节码实现 AOP，以及通过 JavaAgent（`premain` / `agentmain`）在应用启动时或运行中织入探针。
>
> 本模块为聚合模块，包含两个子模块：`basics`（字节码基础）与 `javaagent`（探针示例，含 `example01`、`example02`）。

---

## 子模块 basics — 字节码基础操作

演示以 `Base` 为目标类，用不同框架在其 `process()` 方法前后织入代码，实现 AOP 效果。

目标类：

- `bytecode/Demo` — 最简单的 `hello world`，作为字节码观察对象。
- `bytecode/Base` — 被字节码增强的目标类，`process()` 方法为切点；`bytecode/BaseInterface` — 配套接口。

ASM 操作：

- `bytecode/asm/AsmDemo` — 使用 ASM 的 `ClassReader` / `ClassVisitor` / `ClassWriter` 读取并改写 `Base` 的字节码，在 `process()` 前后织入 `start` / `end` 输出，回写 class 文件后调用验证。
- `bytecode/asm/MyClassVisitor` — 自定义 `ClassVisitor` / `MethodVisitor`，是 ASM 织入逻辑的核心实现。

Javassist 操作：

- `bytecode/javassist/JavassistDemo` — 使用 Javassist 的 `CtMethod.insertBefore` / `insertAfter` 在 `process()` 前后插入代码，比 ASM 更接近源码级 API。
- `bytecode/javassist/JavassistErrorDemo`（反例） — 演示错误用法：当目标类**已存在运行时实例**后再修改其字节码会出错（JVM 不允许运行时动态重载已加载的类），用于与 `JavassistDemo` 对比。

## 子模块 javaagent — JavaAgent 探针

通过 `java.lang.instrument` 在类加载时拦截并增强目标类。每个示例都分为 `agent`（探针）与 `app`（被增强应用）两部分。`RunTimeTransformer` 用 Javassist 拦截 `AppInit` 的方法，织入「开始/结束执行」与耗时统计。

- `javaagent/example01` — **启动期加载**：`agent/RunTimeAgent` 提供 `premain` 方法，随 JVM 启动通过 `-javaagent` 参数加载；`app/AppMain`、`app/AppInit` 为被增强的目标应用。
- `javaagent/example02` — **运行期附加**：`agent/RunTimeAgent` 提供 `agentmain` 方法，基于 Attach 机制在 `main` 运行之后动态挂载探针；`app` 结构同上。

两者的 `RunTimeTransformer` 织入逻辑一致，区别仅在于探针的加载时机（`premain` 启动期 vs `agentmain` 运行期）。

---

## 运行说明

- **ASM / Javassist 基础示例**：直接运行 `AsmDemo`、`JavassistDemo` 的 `main`，观察 `process()` 前后被织入的输出。
- **example01（premain）**：先将 `agent` 打包为含 `Premain-Class` 清单的 jar，再以 `java -javaagent:agent.jar=参数 -cp app AppMain` 启动应用。
- **example02（agentmain）**：应用启动后，通过 Attach API（`VirtualMachine.loadAgent`）将 `agentmain` 探针动态挂载到目标进程。

> 说明：字节码示例会**改写 class 文件并依赖类加载/附加机制**，属于底层演示，未配套自动化单元测试；反例 `JavassistErrorDemo` 用于对比正确写法，请勿修改其触发行为。
