# Java 死锁

<!-- TOC -->

- [Java 死锁](#java-%E6%AD%BB%E9%94%81)
    - [什么是死锁](#%E4%BB%80%E4%B9%88%E6%98%AF%E6%AD%BB%E9%94%81)
        - [发生死锁的情况](#%E5%8F%91%E7%94%9F%E6%AD%BB%E9%94%81%E7%9A%84%E6%83%85%E5%86%B5)
        - [更复杂的死锁](#%E6%9B%B4%E5%A4%8D%E6%9D%82%E7%9A%84%E6%AD%BB%E9%94%81)
        - [数据库的死锁](#%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E6%AD%BB%E9%94%81)
    - [如何避免死锁](#%E5%A6%82%E4%BD%95%E9%81%BF%E5%85%8D%E6%AD%BB%E9%94%81)
        - [加锁顺序](#%E5%8A%A0%E9%94%81%E9%A1%BA%E5%BA%8F)
        - [加锁时限](#%E5%8A%A0%E9%94%81%E6%97%B6%E9%99%90)
        - [死锁检测](#%E6%AD%BB%E9%94%81%E6%A3%80%E6%B5%8B)

<!-- /TOC -->

## 什么是死锁

### 发生死锁的情况

死锁是两个或更多线程阻塞着等待其它处于死锁状态的线程所持有的锁。死锁通常发生在多个线程同时但以不同的顺序请求同一组锁的时候。

例如，如果线程1锁住了A，然后尝试对B进行加锁，同时线程2已经锁住了B，接着尝试对A进行加锁，这时死锁就发生了。线程1永远得不到B，线程2也永远得不到A，并且它们永远也不会知道发生了这样的事情。为了得到彼此的对象（A和B），它们将永远阻塞下去。这种情况就是一个死锁。

该情况如下：

```
Thread 1  locks A, waits for B
Thread 2  locks B, waits for A
```

这里有一个TreeNode类的例子，它调用了不同实例的synchronized方法：

```java
public class TreeNode {
    TreeNode parent   = null;  
    List children = new ArrayList();

    public synchronized void addChild(TreeNode child){
        if(!this.children.contains(child)) {
            this.children.add(child);
            child.setParentOnly(this);
        }
    }
  
    public synchronized void addChildOnly(TreeNode child){
        if(!this.children.contains(child){
            this.children.add(child);
        }
    }
  
    public synchronized void setParent(TreeNode parent){
        this.parent = parent;
        parent.addChildOnly(this);
    }

    public synchronized void setParentOnly(TreeNode parent){
        this.parent = parent;
    }
}
```

如果线程1调用parent.addChild(child)方法的同时有另外一个线程2调用child.setParent(parent)方法，两个线程中的parent表示的是同一个对象，child亦然，此时就会发生死锁。下面的伪代码说明了这个过程：

```
Thread 1: parent.addChild(child); //locks parent
          --> child.setParentOnly(parent);

Thread 2: child.setParent(parent); //locks child
          --> parent.addChildOnly()
```

* 首先线程1调用parent.addChild(child)。因为addChild()是同步的，所以线程1会对parent对象加锁以不让其它线程访问该对象。
* 然后线程2调用child.setParent(parent)。因为setParent()是同步的，所以线程2会对child对象加锁以不让其它线程访问该对象。
* 现在child和parent对象被两个不同的线程锁住了。接下来线程1尝试调用child.setParentOnly()方法，但是由于child对象现在被线程2锁住的，所以该调用会被阻塞。线程2也尝试调用parent.addChildOnly()，但是由于parent对象现在被线程1锁住，导致线程2也阻塞在该方法处。现在两个线程都被阻塞并等待着获取另外一个线程所持有的锁。

注意：像上文描述的，这两个线程需要同时调用parent.addChild(child)和child.setParent(parent)方法，并且是同一个parent对象和同一个child对象，才有可能发生死锁。上面的代码可能运行一段时间才会出现死锁。

这些线程需要同时获得锁。举个例子，如果线程1稍微领先线程2，然后成功地锁住了A和B两个对象，那么线程2就会在尝试对B加锁的时候被阻塞，这样死锁就不会发生。因为线程调度通常是不可预测的，因此没有一个办法可以准确预测什么时候死锁会发生，仅仅是可能会发生。

### 更复杂的死锁

死锁可能不止包含2个线程，这让检测死锁变得更加困难。下面是4个线程发生死锁的例子：

```
Thread 1  locks A, waits for B
Thread 2  locks B, waits for C
Thread 3  locks C, waits for D
Thread 4  locks D, waits for A
```

线程1等待线程2，线程2等待线程3，线程3等待线程4，线程4等待线程1。

### 数据库的死锁

更加复杂的死锁场景发生在数据库事务中。一个数据库事务可能由多条SQL更新请求组成。当在一个事务中更新一条记录，这条记录就会被锁住避免其他事务的更新请求，直到第一个事务结束。同一个事务中每一个更新请求都可能会锁住一些记录。

当多个事务同时需要对一些相同的记录做更新操作时，就很有可能发生死锁，例如：

```
Transaction 1, request 1, locks record 1 for update
Transaction 2, request 1, locks record 2 for update
Transaction 1, request 2, tries to lock record 2 for update.
Transaction 2, request 2, tries to lock record 1 for update.
```

因为锁发生在不同的请求中，并且对于一个事务来说不可能提前知道所有它需要的锁，因此很难检测和避免数据库事务中的死锁。

## 如何避免死锁

### 加锁顺序

当多个线程需要相同的一些锁，但是按照不同的顺序加锁，死锁就很容易发生。

如果能确保所有的线程都是按照相同的顺序获得锁，那么死锁就不会发生。看下面这个例子：

```
Thread 1:
  lock A 
  lock B

Thread 2:
   wait for A
   lock C (when A locked)

Thread 3:
   wait for A
   wait for B
   wait for C
```

如果一个线程（比如线程3）需要一些锁，那么它必须按照确定的顺序获取锁。它只有获得了从顺序上排在前面的锁之后，才能获取后面的锁。

例如，线程2和线程3只有在获取了锁A之后才能尝试获取锁C(译者注：获取锁A是获取锁C的必要条件)。因为线程1已经拥有了锁A，所以线程2和3需要一直等到锁A被释放。然后在它们尝试对B或C加锁之前，必须成功地对A加了锁。

按照顺序加锁是一种有效的死锁预防机制。但是，这种方式需要你事先知道所有可能会用到的锁(译者注：并对这些锁做适当的排序)，但总有些时候是无法预知的。

### 加锁时限

另外一个可以避免死锁的方法是在尝试获取锁的时候加一个超时时间，这也就意味着在尝试获取锁的过程中若超过了这个时限该线程则放弃对该锁请求。若一个线程没有在给定的时限内成功获得所有需要的锁，则会进行回退并释放所有已经获得的锁，然后等待一段随机的时间再重试。这段随机的等待时间让其它线程有机会尝试获取相同的这些锁，并且让该应用在没有获得锁的时候可以继续运行(译者注：加锁超时后可以先继续运行干点其它事情，再回头来重复之前加锁的逻辑)。

以下是一个例子，展示了两个线程以不同的顺序尝试获取相同的两个锁，在发生超时后回退并重试的场景：

```
Thread 1 locks A
Thread 2 locks B

Thread 1 attempts to lock B but is blocked
Thread 2 attempts to lock A but is blocked

Thread 1's lock attempt on B times out
Thread 1 backs up and releases A as well
Thread 1 waits randomly (e.g. 257 millis) before retrying.

Thread 2's lock attempt on A times out
Thread 2 backs up and releases B as well
Thread 2 waits randomly (e.g. 43 millis) before retrying.
```

在上面的例子中，线程2比线程1早200毫秒进行重试加锁，因此它可以先成功地获取到两个锁。这时，线程1尝试获取锁A并且处于等待状态。当线程2结束时，线程1也可以顺利的获得这两个锁（除非线程2或者其它线程在线程1成功获得两个锁之前又获得其中的一些锁）。

需要注意的是，由于存在锁的超时，所以我们不能认为这种场景就一定是出现了死锁。也可能是因为获得了锁的线程（导致其它线程超时）需要很长的时间去完成它的任务。

此外，如果有非常多的线程同一时间去竞争同一批资源，就算有超时和回退机制，还是可能会导致这些线程重复地尝试但却始终得不到锁。如果只有两个线程，并且重试的超时时间设定为0到500毫秒之间，这种现象可能不会发生，但是如果是10个或20个线程情况就不同了。因为这些线程等待相等的重试时间的概率就高的多（或者非常接近以至于会出现问题）。
(译者注：超时和重试机制是为了避免在同一时间出现的竞争，但是当线程很多时，其中两个或多个线程的超时时间一样或者接近的可能性就会很大，因此就算出现竞争而导致超时后，由于超时时间一样，它们又会同时开始重试，导致新一轮的竞争，带来了新的问题。)

这种机制存在一个问题，在Java中不能对synchronized同步块设置超时时间。你需要创建一个自定义锁，或使用Java5中java.util.concurrent包下的工具。写一个自定义锁类不复杂，但超出了本文的内容。后续的Java并发系列会涵盖自定义锁的内容。

### 死锁检测

死锁检测是一个更好的死锁预防机制，它主要是针对那些不可能实现按序加锁并且锁超时也不可行的场景。

每当一个线程获得了锁，会在线程和锁相关的数据结构中（map、graph等等）将其记下。除此之外，每当有线程请求锁，也需要记录在这个数据结构中。

当一个线程请求锁失败时，这个线程可以遍历锁的关系图看看是否有死锁发生。例如，线程A请求锁7，但是锁7这个时候被线程B持有，这时线程A就可以检查一下线程B是否已经请求了线程A当前所持有的锁。如果线程B确实有这样的请求，那么就是发生了死锁（线程A拥有锁1，请求锁7；线程B拥有锁7，请求锁1）。

当然，死锁一般要比两个线程互相持有对方的锁这种情况要复杂的多。线程A等待线程B，线程B等待线程C，线程C等待线程D，线程D又在等待线程A。线程A为了检测死锁，它需要递进地检测所有被B请求的锁。从线程B所请求的锁开始，线程A找到了线程C，然后又找到了线程D，发现线程D请求的锁被线程A自己持有着。这是它就知道发生了死锁。

下面是一幅关于四个线程（A,B,C和D）之间锁占有和请求的关系图。像这样的数据结构就可以被用来检测死锁。

![deadlock-detection-graph.png](http://ifeve.com/wp-content/uploads/2013/03/deadlock-detection-graph.png)

那么当检测出死锁时，这些线程该做些什么呢？

一个可行的做法是释放所有锁，回退，并且等待一段随机的时间后重试。这个和简单的加锁超时类似，不一样的是只有死锁已经发生了才回退，而不会是因为加锁的请求超时了。虽然有回退和等待，但是如果有大量的线程竞争同一批锁，它们还是会重复地死锁（编者注：原因同超时类似，不能从根本上减轻竞争）。

一个更好的方案是给这些线程设置优先级，让一个（或几个）线程回退，剩下的线程就像没发生死锁一样继续保持着它们需要的锁。如果赋予这些线程的优先级是固定不变的，同一批线程总是会拥有更高的优先级。为避免这个问题，可以在死锁发生的时候设置随机的优先级。
