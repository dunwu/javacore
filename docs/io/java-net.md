# Java 网络编程

> **📦 本文以及示例源码已归档在 [javacore](https://github.com/dunwu/javacore/)**
>
> ***关键词：`Socket`、`ServerSocket`、`DatagramPacket`、`DatagramSocket`***
>
> 网络编程是指编写运行在多个设备（计算机）的程序，这些设备都通过网络连接起来。
>
> `java.net` 包中提供了低层次的网络通信细节。你可以直接使用这些类和接口，来专注于解决问题，而不用关注通信细节。
>
> java.net 包中提供了两种常见的网络协议的支持：
>
> - **TCP** - TCP 是传输控制协议的缩写，它保障了两个应用程序之间的可靠通信。通常用于互联网协议，被称 TCP/ IP。
> - **UDP** - UDP 是用户数据报协议的缩写，一个无连接的协议。提供了应用程序之间要发送的数据的数据包。

<!-- TOC depthFrom:2 depthTo:3 -->

- [Socket 和 ServerSocket](#socket-和-serversocket)
  - [ServerSocket](#serversocket)
  - [Socket](#socket)
  - [Socket 通信示例](#socket-通信示例)
- [DatagramSocket 和 DatagramPacket](#datagramsocket-和-datagrampacket)
  - [DatagramSocket 通信示例](#datagramsocket-通信示例)
- [InetAddress](#inetaddress)
- [URL](#url)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、Socket 和 ServerSocket

套接字（Socket）使用 TCP 提供了两台计算机之间的通信机制。 客户端程序创建一个套接字，并尝试连接服务器的套接字。

**Java 通过 Socket 和 ServerSocket 实现对 TCP 的支持**。Java 中的 Socket 通信可以简单理解为：**`java.net.Socket` 代表客户端，`java.net.ServerSocket` 代表服务端**，二者可以建立连接，然后通信。

以下为 Socket 通信中建立建立的基本流程：

- 服务器实例化一个 `ServerSocket` 对象，表示服务器绑定一个端口。
- 服务器调用 `ServerSocket` 的 `accept()` 方法，该方法将一直等待，直到客户端连接到服务器的绑定端口（即监听端口）。
- 服务器监听端口时，客户端实例化一个 `Socket` 对象，指定服务器名称和端口号来请求连接。
- `Socket` 类的构造函数试图将客户端连接到指定的服务器和端口号。如果通信被建立，则在客户端创建一个 Socket 对象能够与服务器进行通信。
- 在服务器端，`accept()` 方法返回服务器上一个新的 `Socket` 引用，该引用连接到客户端的 `Socket` 。

连接建立后，可以通过使用 IO 流进行通信。每一个 `Socket` 都有一个输出流和一个输入流。客户端的输出流连接到服务器端的输入流，而客户端的输入流连接到服务器端的输出流。

TCP 是一个双向的通信协议，因此数据可以通过两个数据流在同一时间发送，以下是一些类提供的一套完整的有用的方法来实现 sockets。

### ServerSocket

服务器程序通过使用 `java.net.ServerSocket` 类以获取一个端口，并且监听客户端请求连接此端口的请求。

#### ServerSocket 构造方法

`ServerSocket` 有多个构造方法：

| **方法**                                                   | **描述**                                                            |
| ---------------------------------------------------------- | ------------------------------------------------------------------- |
| `ServerSocket()`                                           | 创建非绑定服务器套接字。                                            |
| `ServerSocket(int port)`                                   | 创建绑定到特定端口的服务器套接字。                                  |
| `ServerSocket(int port, int backlog)`                      | 利用指定的 `backlog` 创建服务器套接字并将其绑定到指定的本地端口号。 |
| `ServerSocket(int port, int backlog, InetAddress address)` | 使用指定的端口、监听 `backlog` 和要绑定到的本地 IP 地址创建服务器。 |

#### ServerSocket 常用方法

创建非绑定服务器套接字。 如果 `ServerSocket` 构造方法没有抛出异常，就意味着你的应用程序已经成功绑定到指定的端口，并且侦听客户端请求。

这里有一些 `ServerSocket` 类的常用方法：

| **方法**                                     | **描述**                                              |
| -------------------------------------------- | ----------------------------------------------------- |
| `int getLocalPort()`                         | 返回此套接字在其上侦听的端口。                        |
| `Socket accept()`                            | 监听并接受到此套接字的连接。                          |
| `void setSoTimeout(int timeout)`             | 通过指定超时值启用/禁用 `SO_TIMEOUT`，以毫秒为单位。  |
| `void bind(SocketAddress host, int backlog)` | 将 `ServerSocket` 绑定到特定地址（IP 地址和端口号）。 |

### Socket

`java.net.Socket` 类代表客户端和服务器都用来互相沟通的套接字。客户端要获取一个 `Socket` 对象通过实例化 ，而 服务器获得一个 `Socket` 对象则通过 `accept()` 方法 a 的返回值。

#### Socket 构造方法

`Socket` 类有 5 个构造方法：

| **方法**                                                                      | **描述**                                                 |
| ----------------------------------------------------------------------------- | -------------------------------------------------------- |
| `Socket()`                                                                    | 通过系统默认类型的 `SocketImpl` 创建未连接套接字         |
| `Socket(String host, int port)`                                               | 创建一个流套接字并将其连接到指定主机上的指定端口号。     |
| `Socket(InetAddress host, int port)`                                          | 创建一个流套接字并将其连接到指定 IP 地址的指定端口号。   |
| `Socket(String host, int port, InetAddress localAddress, int localPort)`      | 创建一个套接字并将其连接到指定远程主机上的指定远程端口。 |
| `Socket(InetAddress host, int port, InetAddress localAddress, int localPort)` | 创建一个套接字并将其连接到指定远程地址上的指定远程端口。 |

当 Socket 构造方法返回，并没有简单的实例化了一个 Socket 对象，它实际上会尝试连接到指定的服务器和端口。

#### Socket 常用方法

下面列出了一些感兴趣的方法，注意客户端和服务器端都有一个 Socket 对象，所以无论客户端还是服务端都能够调用这些方法。

| **方法**                                        | **描述**                                              |
| ----------------------------------------------- | ----------------------------------------------------- |
| `void connect(SocketAddress host, int timeout)` | 将此套接字连接到服务器，并指定一个超时值。            |
| `InetAddress getInetAddress()`                  | 返回套接字连接的地址。                                |
| `int getPort()`                                 | 返回此套接字连接到的远程端口。                        |
| `int getLocalPort()`                            | 返回此套接字绑定到的本地端口。                        |
| `SocketAddress getRemoteSocketAddress()`        | 返回此套接字连接的端点的地址，如果未连接则返回 null。 |
| `InputStream getInputStream()`                  | 返回此套接字的输入流。                                |
| `OutputStream getOutputStream()`                | 返回此套接字的输出流。                                |
| `void close()`                                  | 关闭此套接字。                                        |

### Socket 通信示例

服务端示例：

```java
public class HelloServer {

    public static void main(String[] args) throws Exception {
        // Socket 服务端
        // 服务器在8888端口上监听
        ServerSocket server = new ServerSocket(8888);
        System.out.println("服务器运行中，等待客户端连接。");
        // 得到连接，程序进入到阻塞状态
        Socket client = server.accept();
        // 打印流输出最方便
        PrintStream out = new PrintStream(client.getOutputStream());
        // 向客户端输出信息
        out.println("hello world");
        client.close();
        server.close();
        System.out.println("服务器已向客户端发送消息，退出。");
    }

}
```

客户端示例：

```java
public class HelloClient {

    public static void main(String[] args) throws Exception {
        // Socket 客户端
        Socket client = new Socket("localhost", 8888);
        InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
        // 一次性接收完成
        BufferedReader buf = new BufferedReader(inputStreamReader);
        String str = buf.readLine();
        buf.close();
        client.close();
        System.out.println("客户端接收到服务器消息：" + str + "，退出");
    }

}
```

## 二、DatagramSocket 和 DatagramPacket

Java 通过 `DatagramSocket` 和 `DatagramPacket` 实现对 UDP 协议的支持。

- `DatagramPacket`：数据包类
- `DatagramSocket`：通信类

UDP 服务端示例：

```java
public class UDPServer {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        String str = "hello World!!!";
        DatagramSocket ds = new DatagramSocket(3000); // 服务端在3000端口上等待服务器发送信息
        DatagramPacket dp =
            new DatagramPacket(str.getBytes(), str.length(), InetAddress.getByName("localhost"), 9000); // 所有的信息使用buf保存
        System.out.println("发送信息。");
        ds.send(dp); // 发送信息出去
        ds.close();
    }

}
```

UDP 客户端示例：

```java
public class UDPClient {

    public static void main(String[] args) throws Exception { // 所有异常抛出
        byte[] buf = new byte[1024]; // 开辟空间，以接收数据
        DatagramSocket ds = new DatagramSocket(9000); // 客户端在9000端口上等待服务器发送信息
        DatagramPacket dp = new DatagramPacket(buf, 1024); // 所有的信息使用buf保存
        ds.receive(dp); // 接收数据
        String str = new String(dp.getData(), 0, dp.getLength()) + "from " + dp.getAddress().getHostAddress() + "："
            + dp.getPort();
        System.out.println(str); // 输出内容
    }

}
```

## 三、InetAddress

`InetAddress` 类表示互联网协议(IP)地址。

没有公有的构造函数，只能通过静态方法来创建实例。

```java
InetAddress.getByName(String host);
InetAddress.getByAddress(byte[] address);
```

## 四、URL

可以直接从 URL 中读取字节流数据。

```java
public static void main(String[] args) throws IOException {

    URL url = new URL("http://www.baidu.com");

    /* 字节流 */
    InputStream is = url.openStream();

    /* 字符流 */
    InputStreamReader isr = new InputStreamReader(is, "utf-8");

    /* 提供缓存功能 */
    BufferedReader br = new BufferedReader(isr);

    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }

    br.close();
}
```

## 参考资料

- [Java 网络编程](https://www.runoob.com/java/java-networking.html)
