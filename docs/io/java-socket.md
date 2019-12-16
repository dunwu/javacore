# Java 网络编程

> 📓 本文已归档到：「[javacore](https://github.com/dunwu/javacore)」
>
> 关键词：URL、InetAddress

<!-- TOC depthFrom:2 depthTo:3 -->

- [InetAddress](#inetaddress)
- [URL](#url)
- [Sockets](#sockets)
- [Datagram](#datagram)

<!-- /TOC -->

Java 中的网络支持：

- InetAddress：用于表示网络上的硬件资源，即 IP 地址；
- URL：统一资源定位符；
- Sockets：使用 TCP 协议实现网络通信；
- Datagram：使用 UDP 协议实现网络通信。

## InetAddress

没有公有的构造函数，只能通过静态方法来创建实例。

```java
InetAddress.getByName(String host);
InetAddress.getByAddress(byte[] address);
```

## URL

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

## Sockets

- ServerSocket：服务器端类
- Socket：客户端类
- 服务器和客户端通过 InputStream 和 OutputStream 进行输入输出。

<div align="center"> <img src="../pics//ClienteServidorSockets1521731145260.jpg"/> </div>

## Datagram

- DatagramPacket：数据包类
- DatagramSocket：通信类
