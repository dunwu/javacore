# JavaCore :: IO — Java IO/NIO 与网络示例

> 本模块展示 Java 输入输出体系（BIO 字节流/字符流、文件操作）以及 NIO（缓冲区、通道、选择器）和网络编程（TCP/UDP/URL）的用法。
>
> 每个示例类的 `main` 逻辑抽取为独立的 `demo()` 方法，涉及文件的示例改用相对路径临时文件（测试后自动清理），并在 `src/test` 下配套 JUnit 5 单元测试验证输出。

示例源码路径：`src/main/java/io/github/dunwu/javacore/<特性包>/`

---

## BIO 字节流（bio/bytes）

展示以字节为单位进行读写的各类输入输出流。

- `bio/bytes/FileStreamDemo` — `FileInputStream` / `FileOutputStream` 读写文件字节。
- `bio/bytes/ByteArrayStreamDemo` — `ByteArrayInputStream` / `ByteArrayOutputStream` 内存字节流。
- `bio/bytes/DataStreamDemo` — `DataInputStream` / `DataOutputStream` 按数据类型读写。
- `bio/bytes/ObjectStreamDemo` — `ObjectInputStream` / `ObjectOutputStream` 对象序列化读写。
- `bio/bytes/PrintStreamDemo` — `PrintStream` 格式化输出。
- `bio/bytes/PipedStreamDemo` — `PipedInputStream` / `PipedOutputStream` 管道流（线程间通信）。
- `bio/bytes/SequenceInputStreamDemo` — `SequenceInputStream` 合并多个输入流。
- `bio/bytes/ZipStreamDemo` — ZIP 压缩/解压流。

## BIO 字符流（bio/chars）

展示以字符为单位、处理文本与编码转换的读写流。

- `bio/chars/FileReadWriteDemo` — `FileReader` / `FileWriter` 读写文本文件。
- `bio/chars/BufferedReaderDemo` — `BufferedReader` 缓冲读取、按行读取。
- `bio/chars/InputStreamReaderDemo` — `InputStreamReader` 将字节流按指定编码转为字符流（读取控制台输入）。
- `bio/chars/OutputStreamWriterDemo` — `OutputStreamWriter` 将字符按编码写出（含 UTF-8 输出）。

## 文件与常用 IO（io）

展示 `File` 文件操作、`Properties` 配置读写、随机访问文件与标准流。

- `io/FileDemo` — `File` 的路径信息、创建、删除、目录遍历（list/listFiles/FileFilter）与递归。
- `io/PropertiesDemo01`~`PropertiesDemo05` — `Properties` 的读写、从 classpath/文件/URL 加载配置。
- `io/RandomAccessFileReadDemo`、`io/RandomAccessFileWriteDemo` — `RandomAccessFile` 随机读写文件任意位置。
- `io/ScannerDemo` — `Scanner` 扫描解析输入。
- `io/SystemInDemo` — 从标准输入 `System.in` 读取。
- `io/SystemOutDemo`、`io/SystemErrDemo` — 标准输出/标准错误流的输出方式与差异。

## NIO（nio）

展示新 IO 的缓冲区、通道、字符集、文件锁与多路复用。

- `nio/buffer/ByteBufferDemo01` — ByteBuffer 的 put/get/flip/clear 等核心操作。
- `nio/buffer/IntBufferDemo01`~`IntBufferDemo03` — IntBuffer 的存取、只读缓冲区（`IntBufferDemo03` 为反例：向只读缓冲区写入抛 `ReadOnlyBufferException`）、视图缓冲区。
- `nio/channel/FileChannelDemo01`~`FileChannelDemo03` — FileChannel 读写文件、通道间数据传输（transferTo/transferFrom）。
- `nio/charset/CharsetEnDeDemo` — 使用 `Charset` 进行编码/解码。
- `nio/charset/GetAllCharsetDemo` — 列出 JVM 支持的所有字符集。
- `nio/lock/FileLockDemo` — 文件锁（`FileLock`）的加锁与释放。
- `nio/selector/DateServer` — 基于 `Selector` 的多路复用非阻塞服务端。

## 网络编程（net）

展示 URL 访问、InetAddress、TCP 与 UDP 通信。

- `net/URLDemo` — 解析 URL 各组成部分。
- `net/URLConnectionDemo` — 通过 `URLConnection` 读取网络资源。
- `net/InetAddressDemo` — `InetAddress` 获取主机名与 IP 地址。
- `net/CodeDemo` — 网络编码相关演示。
- `net/tcp/HelloServer`、`net/tcp/HelloClient` — 最简单的 TCP 服务端/客户端通信。
- `net/tcp/EchoServer`、`net/tcp/EchoClient`、`net/tcp/EchoThread`、`net/tcp/EchoThreadServer` — 回显服务及多线程处理多客户端。
- `net/udp/UDPServer`、`net/udp/UDPClient` — 基于 `DatagramSocket` 的 UDP 通信。

---

## 单元测试

测试位于 `src/test/java/io/github/dunwu/javacore/`，分为 `bio`、`io`、`nio`、`net` 等，通过捕获标准输出对示例结果做精确断言。运行：

```bash
mvn test -pl codes/javacore-io
```

> 涉及文件读写的示例使用相对路径临时文件，测试后清理；反例（如向只读缓冲区写入）用 `assertThatThrownBy` 验证异常；依赖真实网络端口/交互输入的示例不纳入自动化测试。所有 `@Test` 方法均带有中文 `@DisplayName` 说明测试意图。
