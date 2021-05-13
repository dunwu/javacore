# Java 应用故障诊断

<!-- TOC depthFrom:2 depthTo:3 -->

- [一、故障定位思路](#一故障定位思路)
- [二、CPU 问题](#二cpu-问题)
  - [查找 CPU 占用率较高的进程、线程](#查找-cpu-占用率较高的进程线程)
  - [是否存在频繁 GC](#是否存在频繁-gc)
  - [是否存在频繁上下文切换](#是否存在频繁上下文切换)
- [三、内存问题](#三内存问题)
- [四、磁盘问题](#四磁盘问题)
  - [查看磁盘空间使用率](#查看磁盘空间使用率)
  - [查看磁盘读写性能](#查看磁盘读写性能)
  - [查看具体的文件读写情况](#查看具体的文件读写情况)
- [五、网络问题](#五网络问题)
  - [无法连接](#无法连接)
  - [网络超时](#网络超时)
  - [TCP 队列溢出](#tcp-队列溢出)
  - [RST 异常](#rst-异常)
  - [TIME_WAIT 和 CLOSE_WAIT](#time_wait-和-close_wait)
- [六、GC 问题](#六gc-问题)
  - [OOM](#oom)
  - [Minor GC](#minor-gc)
  - [Full GC 过频](#full-gc-过频)
- [七、常用 Linux 命令](#七常用-linux-命令)
  - [top](#top)
  - [vmstat](#vmstat)
- [参考资料](#参考资料)

<!-- /TOC -->

## 一、故障定位思路

Java 应用出现线上故障，如何进行诊断？

我们在定位线上问题时要有一个整体的思路，顺藤摸瓜，才能较快的找到问题原因。

一般来说，服务器故障诊断的整体思路如下：

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200309181645.png)

应用故障诊断思路：

![img](https://raw.githubusercontent.com/dunwu/images/dev/snap/20200309181831.png)

## 二、CPU 问题

一、**CPU 使用率过高**：往往是由于程序逻辑问题导致的。常见导致 CPU 飙升的问题场景如：死循环，无限递归、频繁 GC、线程上下文切换过多。

二、**CPU 始终升不上去**：往往是由于程序中存在大量 IO 操作并且时间很长（数据库读写、日志等）。

### 查找 CPU 占用率较高的进程、线程

线上环境的 Java 应用可能有多个进程、线程，所以，要先找到 CPU 占用率较高的进程、线程。

（1）使用 `ps` 命令查看 xxx 应用的进程 ID（PID）

```shell
ps -ef | grep xxx
```

也可以使用 `jps` 命令来查看。

（2）如果应用有多个进程，可以用 `top` 命令查看哪个占用 CPU 较高。

（3）用 `top -Hp pid` 来找到 CPU 使用率比较高的一些线程。

（4）将占用 CPU 最高的 PID 转换为 16 进制，使用 `printf '%x\n' pid` 得到 `nid`

（5）使用 `jstack pic | grep 'nid' -C5` 命令，查看堆栈信息：

```shell
$ jstack 7129 | grep '0x1c23' -C5
        at java.lang.Object.wait(Object.java:502)
        at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
        - locked <0x00000000b5383ff0> (a java.lang.ref.Reference$Lock)
        at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"main" #1 prio=5 os_prio=0 tid=0x00007f4df400a800 nid=0x1c23 in Object.wait() [0x00007f4dfdec8000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000b5384018> (a org.apache.felix.framework.util.ThreadGate)
        at org.apache.felix.framework.util.ThreadGate.await(ThreadGate.java:79)
        - locked <0x00000000b5384018> (a org.apache.felix.framework.util.ThreadGate)
```

（6）更常见的操作是用 `jstack` 生成堆栈快照，然后基于快照文件进行分析。生成快照命令：

```shell
jstack -F -l pid >> threaddump.log
```

（7）分析堆栈信息

一般来说，状态为 `WAITING`、`TIMED_WAITING` 、`BLOCKED` 的线程更可能出现问题。可以执行以下命令查看线程状态统计：

```
cat threaddump.log | grep "java.lang.Thread.State" | sort -nr | uniq -c
```

如果存在大量 `WAITING`、`TIMED_WAITING` 、`BLOCKED` ，那么多半是有问题啦。

### 是否存在频繁 GC

如果应用频繁 GC，也可能导致 CPU 飙升。为何频繁 GC 可以使用 `jstack` 来分析问题（分析和解决频繁 GC 问题，在后续讲解）。

那么，如何判断 Java 进程 GC 是否频繁？

可以使用 `jstat -gc pid 1000` 命令来观察 GC 状态。

```shell
$ jstat -gc 29527 200 5
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
22528.0 22016.0  0.0   21388.2 4106752.0 921244.7 5592576.0  2086826.5  110716.0 103441.1 12416.0 11167.7   3189   90.057  10      2.140   92.197
22528.0 22016.0  0.0   21388.2 4106752.0 921244.7 5592576.0  2086826.5  110716.0 103441.1 12416.0 11167.7   3189   90.057  10      2.140   92.197
22528.0 22016.0  0.0   21388.2 4106752.0 921244.7 5592576.0  2086826.5  110716.0 103441.1 12416.0 11167.7   3189   90.057  10      2.140   92.197
22528.0 22016.0  0.0   21388.2 4106752.0 921244.7 5592576.0  2086826.5  110716.0 103441.1 12416.0 11167.7   3189   90.057  10      2.140   92.197
22528.0 22016.0  0.0   21388.2 4106752.0 921244.7 5592576.0  2086826.5  110716.0 103441.1 12416.0 11167.7   3189   90.057  10      2.140   92.197
```

### 是否存在频繁上下文切换

针对频繁上下文切换问题，可以使用 `vmstat pid` 命令来进行查看。

```shell
$ vmstat 7129
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 1  0   6836 737532   1588 3504956    0    0     1     4    5    3  0  0 100  0  0
```

其中，`cs` 一列代表了上下文切换的次数。

【解决方法】

如果，线程上下文切换很频繁，可以考虑在应用中针对线程进行优化，方法有：

- **无锁并发**：多线程竞争时，会引起上下文切换，所以多线程处理数据时，可以用一些办法来避免使用锁，如将数据的 ID 按照 Hash 取模分段，不同的线程处理不同段的数据；
- **CAS 算法**：Java 的 Atomic 包使用 CAS 算法来更新数据，而不需要加锁；
- **最少线程**：避免创建不需要的线程，比如任务很少，但是创建了很多线程来处理，这样会造成大量线程都处于等待状态；
- **使用协程**：在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换；

## 三、内存问题

内存问题诊断起来相对比 CPU 麻烦一些，场景也比较多。主要包括 OOM、GC 问题和堆外内存。一般来讲，我们会先用 `free` 命令先来检查一发内存的各种情况。

诊断内存问题，一般首先会用 `free` 命令查看一下机器的物理内存使用情况。

```shell
$ free
              total        used        free      shared  buff/cache   available
Mem:        8011164     3767900      735364        8804     3507900     3898568
Swap:       5242876        6836     5236040
```

## 四、磁盘问题

### 查看磁盘空间使用率

可以使用 `df -hl` 命令查看磁盘空间使用率。

```
$ df -hl
Filesystem      Size  Used Avail Use% Mounted on
devtmpfs        494M     0  494M   0% /dev
tmpfs           504M     0  504M   0% /dev/shm
tmpfs           504M   58M  447M  12% /run
tmpfs           504M     0  504M   0% /sys/fs/cgroup
/dev/sda2        20G  5.7G   13G  31% /
/dev/sda1       380M  142M  218M  40% /boot
tmpfs           101M     0  101M   0% /run/user/0
```

### 查看磁盘读写性能

可以使用 `iostat` 命令查看磁盘读写性能。

```
iostat -d -k -x
Linux 3.10.0-327.el7.x86_64 (elk-server)        03/07/2020      _x86_64_        (4 CPU)

Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
sda               0.00     0.14    0.01    1.63     0.42   157.56   193.02     0.00    2.52   11.43    2.48   0.60   0.10
scd0              0.00     0.00    0.00    0.00     0.00     0.00     8.00     0.00    0.27    0.27    0.00   0.27   0.00
dm-0              0.00     0.00    0.01    1.78     0.41   157.56   177.19     0.00    2.46   12.09    2.42   0.59   0.10
dm-1              0.00     0.00    0.00    0.00     0.00     0.00    16.95     0.00    1.04    1.04    0.00   1.02   0.00
```

### 查看具体的文件读写情况

可以使用 `lsof -p pid` 命令

## 五、网络问题

### 无法连接

可以通过 `ping` 命令，查看是否能连通。

通过 `netstat -nlp | grep <port>` 命令，查看服务端口是否在工作。

### 网络超时

网络超时问题大部分出在应用层面。超时大体可以分为连接超时和读写超时，某些使用连接池的客户端框架还会存在获取连接超时和空闲连接清理超时。

- 读写超时。readTimeout/writeTimeout，有些框架叫做 so_timeout 或者 socketTimeout，均指的是数据读写超时。注意这边的超时大部分是指逻辑上的超时。soa 的超时指的也是读超时。读写超时一般都只针对客户端设置。
- 连接超时。connectionTimeout，客户端通常指与服务端建立连接的最大时间。服务端这边 connectionTimeout 就有些五花八门了，jetty 中表示空闲连接清理时间，tomcat 则表示连接维持的最大时间。
- 其他。包括连接获取超时 connectionAcquireTimeout 和空闲连接清理超时 idleConnectionTimeout。多用于使用连接池或队列的客户端或服务端框架。

我们在设置各种超时时间中，需要确认的是尽量保持客户端的超时小于服务端的超时，以保证连接正常结束。

在实际开发中，我们关心最多的应该是接口的读写超时了。

如何设置合理的接口超时是一个问题。如果接口超时设置的过长，那么有可能会过多地占用服务端的 tcp 连接。而如果接口设置的过短，那么接口超时就会非常频繁。

服务端接口明明 rt 降低，但客户端仍然一直超时又是另一个问题。这个问题其实很简单，客户端到服务端的链路包括网络传输、排队以及服务处理等，每一个环节都可能是耗时的原因。

### TCP 队列溢出

tcp 队列溢出是个相对底层的错误，它可能会造成超时、rst 等更表层的错误。因此错误也更隐蔽，所以我们单独说一说。
![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-083827.jpg)

如上图所示，这里有两个队列：syns queue(半连接队列）、accept queue（全连接队列）。三次握手，在 server 收到 client 的 syn 后，把消息放到 syns queue，回复 syn+ack 给 client，server 收到 client 的 ack，如果这时 accept queue 没满，那就从 syns queue 拿出暂存的信息放入 accept queue 中，否则按 tcp_abort_on_overflow 指示的执行。

tcp_abort_on_overflow 0 表示如果三次握手第三步的时候 accept queue 满了那么 server 扔掉 client 发过来的 ack。tcp_abort_on_overflow 1 则表示第三步的时候如果全连接队列满了，server 发送一个 rst 包给 client，表示废掉这个握手过程和这个连接，意味着日志里可能会有很多`connection reset / connection reset by peer`。

那么在实际开发中，我们怎么能快速定位到 tcp 队列溢出呢？

**netstat 命令，执行 netstat -s | egrep "listen|LISTEN"**
![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-83828.jpg)
如上图所示，overflowed 表示全连接队列溢出的次数，sockets dropped 表示半连接队列溢出的次数。

**ss 命令，执行 ss -lnt**
![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-083828.jpg)
上面看到 Send-Q 表示第三列的 listen 端口上的全连接队列最大为 5，第一列 Recv-Q 为全连接队列当前使用了多少。

接着我们看看怎么设置全连接、半连接队列大小吧：

全连接队列的大小取决于 min(backlog, somaxconn)。backlog 是在 socket 创建的时候传入的，somaxconn 是一个 os 级别的系统参数。而半连接队列的大小取决于 max(64, /proc/sys/net/ipv4/tcp_max_syn_backlog)。

在日常开发中，我们往往使用 servlet 容器作为服务端，所以我们有时候也需要关注容器的连接队列大小。在 tomcat 中 backlog 叫做`acceptCount`，在 jetty 里面则是`acceptQueueSize`。

### RST 异常

RST 包表示连接重置，用于关闭一些无用的连接，通常表示异常关闭，区别于四次挥手。

在实际开发中，我们往往会看到`connection reset / connection reset by peer`错误，这种情况就是 RST 包导致的。

**端口不存在**

如果像不存在的端口发出建立连接 SYN 请求，那么服务端发现自己并没有这个端口则会直接返回一个 RST 报文，用于中断连接。

**主动代替 FIN 终止连接**

一般来说，正常的连接关闭都是需要通过 FIN 报文实现，然而我们也可以用 RST 报文来代替 FIN，表示直接终止连接。实际开发中，可设置 SO_LINGER 数值来控制，这种往往是故意的，来跳过 TIMED_WAIT，提供交互效率，不闲就慎用。

**客户端或服务端有一边发生了异常，该方向对端发送 RST 以告知关闭连接**

我们上面讲的 tcp 队列溢出发送 RST 包其实也是属于这一种。这种往往是由于某些原因，一方无法再能正常处理请求连接了(比如程序崩了，队列满了)，从而告知另一方关闭连接。

**接收到的 TCP 报文不在已知的 TCP 连接内**

比如，一方机器由于网络实在太差 TCP 报文失踪了，另一方关闭了该连接，然后过了许久收到了之前失踪的 TCP 报文，但由于对应的 TCP 连接已不存在，那么会直接发一个 RST 包以便开启新的连接。

**一方长期未收到另一方的确认报文，在一定时间或重传次数后发出 RST 报文**

这种大多也和网络环境相关了，网络环境差可能会导致更多的 RST 报文。

之前说过 RST 报文多会导致程序报错，在一个已关闭的连接上读操作会报`connection reset`，而在一个已关闭的连接上写操作则会报`connection reset by peer`。通常我们可能还会看到`broken pipe`错误，这是管道层面的错误，表示对已关闭的管道进行读写，往往是在收到 RST，报出`connection reset`错后继续读写数据报的错，这个在 glibc 源码注释中也有介绍。

我们在诊断故障时候怎么确定有 RST 包的存在呢？当然是使用 tcpdump 命令进行抓包，并使用 wireshark 进行简单分析了。`tcpdump -i en0 tcp -w xxx.cap`，en0 表示监听的网卡。
![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-083829.jpg)

接下来我们通过 wireshark 打开抓到的包，可能就能看到如下图所示，红色的就表示 RST 包了。
![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-083830.jpg)

### TIME_WAIT 和 CLOSE_WAIT

TIME_WAIT 和 CLOSE_WAIT 是啥意思相信大家都知道。
在线上时，我们可以直接用命令`netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'`来查看 time-wait 和 close_wait 的数量

用 ss 命令会更快`ss -ant | awk '{++S[$1]} END {for(a in S) print a, S[a]}'`

![img](https://fredal-blog.oss-cn-hangzhou.aliyuncs.com/2019-11-04-083830.png)

#### TIME_WAIT

time_wait 的存在一是为了丢失的数据包被后面连接复用，二是为了在 2MSL 的时间范围内正常关闭连接。它的存在其实会大大减少 RST 包的出现。

过多的 time_wait 在短连接频繁的场景比较容易出现。这种情况可以在服务端做一些内核参数调优:

```java
#表示开启重用。允许将TIME-WAIT sockets重新用于新的TCP连接，默认为0，表示关闭
net.ipv4.tcp_tw_reuse = 1
#表示开启TCP连接中TIME-WAIT sockets的快速回收，默认为0，表示关闭
net.ipv4.tcp_tw_recycle = 1
```

当然我们不要忘记在 NAT 环境下因为时间戳错乱导致数据包被拒绝的坑了，另外的办法就是改小`tcp_max_tw_buckets`，超过这个数的 time_wait 都会被干掉，不过这也会导致报`time wait bucket table overflow`的错。

#### CLOSE_WAIT

close_wait 往往都是因为应用程序写的有问题，没有在 ACK 后再次发起 FIN 报文。close_wait 出现的概率甚至比 time_wait 要更高，后果也更严重。往往是由于某个地方阻塞住了，没有正常关闭连接，从而渐渐地消耗完所有的线程。

想要定位这类问题，最好是通过 jstack 来分析线程堆栈来诊断问题，具体可参考上述章节。这里仅举一个例子。

开发同学说应用上线后 CLOSE_WAIT 就一直增多，直到挂掉为止，jstack 后找到比较可疑的堆栈是大部分线程都卡在了`countdownlatch.await`方法，找开发同学了解后得知使用了多线程但是确没有 catch 异常，修改后发现异常仅仅是最简单的升级 sdk 后常出现的`class not found`。

## 六、GC 问题

GC 问题除了影响 CPU 也会影响内存，诊断思路也是一致的。

（1）通常，先使用 `jstat` 来查看分代变化情况，比如 **minor gc** 或 **full gc** 次数是不是太频繁、耗时太久。

线程量太大，且不被及时 GC 也会引发 OOM，大部分就是之前说的 `unable to create new native thread`。除了 jstack 细细分析 dump 文件外，我们一般先会看下总体线程。

可以执行以下命令中任意一个，没来查看当前进程创建的总线程数。

```shell
pstreee -p pid | wc -l
ls -l /proc/pid/task | wc -l
```

堆内内存泄漏总是和 GC 异常相伴。不过 GC 问题不只是和内存问题相关，还有可能引起 CPU 负载、网络问题等系列并发症，只是相对来说和内存联系紧密些，所以我们在此单独总结一下 GC 相关问题。

我们在 cpu 章介绍了使用 jstat 来获取当前 GC 分代变化信息。而更多时候，我们是通过 GC 日志来诊断问题的，在启动参数中加上`-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps`来开启 GC 日志。
常见的 Minor GC、Full GC 日志含义在此就不做赘述了。

针对 gc 日志，我们就能大致推断出 Minor GC 与 fullGC 是否过于频繁或者耗时过长，从而对症下药。我们下面将对 G1 垃圾收集器来做分析，这边也建议大家使用 G1`-XX:+UseG1GC`。

### OOM

查看 GC 日志，如果有明显提示 OOM 问题，那就可以根据提示信息，较为快速的定位问题。

> OOM 定位可以参考：[JVM 内存管理 之 OutOfMemoryError](jvm-memory.md#三OutOfMemoryError)

### Minor GC

#### Minor GC 过频

**Minor GC 频繁一般是短周期的 Java 小对象较多**。

（1）先考虑是不是 Eden 区/新生代设置的太小了，看能否通过调整 `-Xmn、-XX:SurvivorRatio` 等参数设置来解决问题。

（2）如果参数正常，但是 Minor GC 频率还是太高，就需要使用 `jmap` 和 `MAT` 对 dump 文件进行进一步诊断了。

#### Minor GC 耗时过长

Minor GC 耗时过长问题就要看 GC 日志里耗时耗在哪一块了。

以 G1 GC 日志为例，可以关注 Root Scanning、Object Copy、Ref Proc 等阶段。Ref Proc 耗时长，就要注意引用相关的对象。Root Scanning 耗时长，就要注意线程数、跨代引用。Object Copy 则需要关注对象生存周期。而且耗时分析它需要横向比较，就是和其他项目或者正常时间段的耗时比较。

### Full GC 过频

G1 中更多的还是 mixedGC，但 mixedGC 可以和 Minor GC 思路一样去诊断。触发 fullGC 了一般都会有问题，G1 会退化使用 Serial 收集器来完成垃圾的清理工作，暂停时长达到秒级别，可以说是半跪了。

fullGC 的原因可能包括以下这些，以及参数调整方面的一些思路：

- 并发阶段失败：在并发标记阶段，MixGC 之前老年代就被填满了，那么这时候 G1 就会放弃标记周期。这种情况，可能就需要增加堆大小，或者调整并发标记线程数`-XX:ConcGCThreads`。
- 晋升失败：在 GC 的时候没有足够的内存供存活/晋升对象使用，所以触发了 Full GC。这时候可以通过`-XX:G1ReservePercent`来增加预留内存百分比，减少`-XX:InitiatingHeapOccupancyPercent`来提前启动标记，`-XX:ConcGCThreads`来增加标记线程数也是可以的。
- 大对象分配失败：大对象找不到合适的 region 空间进行分配，就会进行 fullGC，这种情况下可以增大内存或者增大`-XX:G1HeapRegionSize`。
- 程序主动执行 System.gc()：不要随便写就对了。

另外，我们可以在启动参数中配置`-XX:HeapDumpPath=/xxx/dump.hprof`来 dump fullGC 相关的文件，并通过 jinfo 来进行 gc 前后的 dump

```java
jinfo -flag +HeapDumpBeforeFullGC pid
jinfo -flag +HeapDumpAfterFullGC pid
```

这样得到 2 份 dump 文件，对比后主要关注被 gc 掉的问题对象来定位问题。

## 七、常用 Linux 命令

在故障排查时，有一些 Linux 命令十分有用，建议掌握。

### top

top 命令可以实时动态地查看系统的整体运行情况，是一个综合了多方信息监测系统性能和运行信息的实用工具。

通常，会使用 `top -Hp pid` 查看具体线程使用系统资源情况。

> 命令详情参考：http://man.linuxde.net/top

### vmstat

vmstat 是一款指定采样周期和次数的功能性监测工具，我们可以看到，它不仅可以统计内存的使用情况，还可以观测到 CPU 的使用率、swap 的使用情况。但 vmstat 一般很少用来查看内存的使用情况，而是经常被用来观察进程的上下文切换。

- r：等待运行的进程数；
- b：处于非中断睡眠状态的进程数；
- swpd：虚拟内存使用情况；
- free：空闲的内存；
- buff：用来作为缓冲的内存数；
- si：从磁盘交换到内存的交换页数量；
- so：从内存交换到磁盘的交换页数量；
- bi：发送到块设备的块数；
- bo：从块设备接收到的块数；
- in：每秒中断数；
- cs：每秒上下文切换次数；
- us：用户 CPU 使用时间；
- sy：内核 CPU 系统使用时间；
- id：空闲时间；
- wa：等待 I/O 时间；
- st：运行虚拟机窃取的时间。

## 参考资料

- [《Java 性能调优实战》](https://time.geekbang.org/column/intro/100028001)
- [JAVA 线上故障诊断全套路](https://fredal.xin/java-error-check)
- [从实际案例聊聊 Java 应用的 GC 优化](https://tech.meituan.com/2017/12/29/jvm-optimize.html)
