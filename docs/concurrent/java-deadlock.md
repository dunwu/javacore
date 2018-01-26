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
    - [饥饿和公平](#%E9%A5%A5%E9%A5%BF%E5%92%8C%E5%85%AC%E5%B9%B3)
        - [Java中导致饥饿的原因](#java%E4%B8%AD%E5%AF%BC%E8%87%B4%E9%A5%A5%E9%A5%BF%E7%9A%84%E5%8E%9F%E5%9B%A0)
        - [在Java中实现公平性](#%E5%9C%A8java%E4%B8%AD%E5%AE%9E%E7%8E%B0%E5%85%AC%E5%B9%B3%E6%80%A7)
            - [使用锁方式替代同步块](#%E4%BD%BF%E7%94%A8%E9%94%81%E6%96%B9%E5%BC%8F%E6%9B%BF%E4%BB%A3%E5%90%8C%E6%AD%A5%E5%9D%97)
            - [公平锁](#%E5%85%AC%E5%B9%B3%E9%94%81)
    - [嵌套管程锁死](#%E5%B5%8C%E5%A5%97%E7%AE%A1%E7%A8%8B%E9%94%81%E6%AD%BB)
        - [一个更现实的例子](#%E4%B8%80%E4%B8%AA%E6%9B%B4%E7%8E%B0%E5%AE%9E%E7%9A%84%E4%BE%8B%E5%AD%90)

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

## 饥饿和公平

如果一个线程因为CPU时间全部被其他线程抢走而得不到CPU运行时间，这种状态被称之为“饥饿”。而该线程被“饥饿致死”正是因为它得不到CPU运行时间的机会。解决饥饿的方案被称之为“公平性” – 即所有线程均能公平地获得运行机会。

### Java中导致饥饿的原因

在Java中，下面三个常见的原因会导致线程饥饿：

* 高优先级线程吞噬所有的低优先级线程的CPU时间

    你能为每个线程设置独自的线程优先级，优先级越高的线程获得的CPU时间越多，线程优先级值设置在1到10之间，而这些优先级值所表示行为的准确解释则依赖于你的应用运行平台。对大多数应用来说，你最好是不要改变其优先级值。

* 线程被永久堵塞在一个等待进入同步块的状态

    Java的同步代码区也是一个导致饥饿的因素。Java的同步代码区对哪个线程允许进入的次序没有任何保障。这就意味着理论上存在一个试图进入该同步区的线程处于被永久堵塞的风险，因为其他线程总是能持续地先于它获得访问，这即是“饥饿”问题，而一个线程被“饥饿致死”正是因为它得不到CPU运行时间的机会。

* 线程在等待一个本身(在其上调用wait())也处于永久等待完成的对象

    如果多个线程处在wait()方法执行上，而对其调用notify()不会保证哪一个线程会获得唤醒，任何线程都有可能处于继续等待的状态。因此存在这样一个风险：一个等待线程从来得不到唤醒，因为其他等待线程总是能被获得唤醒。

### 在Java中实现公平性

虽Java不可能实现100%的公平性，我们依然可以通过同步结构在线程间实现公平性的提高。

首先来学习一段简单的同步态代码：

```java
public class Synchronizer{

  public synchronized void doSynchronized(){
    //do a lot of work which takes a long time
  }

}
```

如果有一个以上的线程调用doSynchronized()方法，在第一个获得访问的线程未完成前，其他线程将一直处于阻塞状态，而且在这种多线程被阻塞的场景下，接下来将是哪个线程获得访问是没有保障的。

#### 使用锁方式替代同步块

为了提高等待线程的公平性，我们使用锁方式来替代同步块。

```java
public class Synchronizer{
  Lock lock = new Lock();

  public void doSynchronized() throws InterruptedException{
    this.lock.lock();
      //critical section, do a lot of work which takes a long time
    this.lock.unlock();
  }

}
```

注意到doSynchronized()不再声明为synchronized，而是用lock.lock()和lock.unlock()来替代。

下面是用Lock类做的一个实现：

```java
public class Lock{
  private boolean isLocked      = false;
  private Thread  lockingThread = null;

  public synchronized void lock() throws InterruptedException{
    while(isLocked){
      wait();
    }
    isLocked      = true;
    lockingThread = Thread.currentThread();
  }

  public synchronized void unlock(){
    if(this.lockingThread != Thread.currentThread()){
      throw new IllegalMonitorStateException(
        "Calling thread has not locked this lock");
    }
    isLocked      = false;
    lockingThread = null;
    notify();
  }
}
```

注意到上面对Lock的实现，如果存在多线程并发访问lock()，这些线程将阻塞在对lock()方法的访问上。另外，如果锁已经锁上（校对注：这里指的是isLocked等于true时），这些线程将阻塞在while(isLocked)循环的wait()调用里面。要记住的是，当线程正在等待进入lock() 时，可以调用wait()释放其锁实例对应的同步锁，使得其他多个线程可以进入lock()方法，并调用wait()方法。

这回看下doSynchronized()，你会注意到在lock()和unlock()之间的注释：在这两个调用之间的代码将运行很长一段时间。进一步设想，这段代码将长时间运行，和进入lock()并调用wait()来比较的话。这意味着大部分时间用在等待进入锁和进入临界区的过程是用在wait()的等待中，而不是被阻塞在试图进入lock()方法中。

在早些时候提到过，同步块不会对等待进入的多个线程谁能获得访问做任何保障，同样当调用notify()时，wait()也不会做保障一定能唤醒线程（至于为什么，请看线程通信）。因此这个版本的Lock类和doSynchronized()那个版本就保障公平性而言，没有任何区别。

但我们能改变这种情况。当前的Lock类版本调用自己的wait()方法，如果每个线程在不同的对象上调用wait()，那么只有一个线程会在该对象上调用wait()，Lock类可以决定哪个对象能对其调用notify()，因此能做到有效的选择唤醒哪个线程。

#### 公平锁

下面来讲述将上面Lock类转变为公平锁FairLock。你会注意到新的实现和之前的Lock类中的同步和wait()/notify()稍有不同。

准确地说如何从之前的Lock类做到公平锁的设计是一个渐进设计的过程，每一步都是在解决上一步的问题而前进的：Nested Monitor Lockout, Slipped Conditions和Missed Signals。这些本身的讨论虽已超出本文的范围，但其中每一步的内容都将会专题进行讨论。重要的是，每一个调用lock()的线程都会进入一个队列，当解锁后，只有队列里的第一个线程被允许锁住Farlock实例，所有其它的线程都将处于等待状态，直到他们处于队列头部。

```java
public class FairLock {
    private boolean           isLocked       = false;
    private Thread            lockingThread  = null;
    private List<QueueObject> waitingThreads =
            new ArrayList<QueueObject>();

  public void lock() throws InterruptedException{
    QueueObject queueObject           = new QueueObject();
    boolean     isLockedForThisThread = true;
    synchronized(this){
        waitingThreads.add(queueObject);
    }

    while(isLockedForThisThread){
      synchronized(this){
        isLockedForThisThread =
            isLocked || waitingThreads.get(0) != queueObject;
        if(!isLockedForThisThread){
          isLocked = true;
           waitingThreads.remove(queueObject);
           lockingThread = Thread.currentThread();
           return;
         }
      }
      try{
        queueObject.doWait();
      }catch(InterruptedException e){
        synchronized(this) { waitingThreads.remove(queueObject); }
        throw e;
      }
    }
  }

  public synchronized void unlock(){
    if(this.lockingThread != Thread.currentThread()){
      throw new IllegalMonitorStateException(
        "Calling thread has not locked this lock");
    }
    isLocked      = false;
    lockingThread = null;
    if(waitingThreads.size() > 0){
      waitingThreads.get(0).doNotify();
    }
  }
}
public class QueueObject {

  private boolean isNotified = false;

  public synchronized void doWait() throws InterruptedException {
    while(!isNotified){
        this.wait();
    }
    this.isNotified = false;
  }

  public synchronized void doNotify() {
    this.isNotified = true;
    this.notify();
  }

  public boolean equals(Object o) {
    return this == o;
  }
}
```

首先注意到lock()方法不在声明为synchronized，取而代之的是对必需同步的代码，在synchronized中进行嵌套。

FairLock新创建了一个QueueObject的实例，并对每个调用lock()的线程进行入队列。调用unlock()的线程将从队列头部获取QueueObject，并对其调用doNotify()，以唤醒在该对象上等待的线程。通过这种方式，在同一时间仅有一个等待线程获得唤醒，而不是所有的等待线程。这也是实现FairLock公平性的核心所在。

请注意，在同一个同步块中，锁状态依然被检查和设置，以避免出现滑漏条件。

还需注意到，QueueObject实际是一个semaphore。doWait()和doNotify()方法在QueueObject中保存着信号。这样做以避免一个线程在调用queueObject.doWait()之前被另一个调用unlock()并随之调用queueObject.doNotify()的线程重入，从而导致信号丢失。queueObject.doWait()调用放置在synchronized(this)块之外，以避免被monitor嵌套锁死，所以另外的线程可以解锁，只要当没有线程在lock方法的synchronized(this)块中执行即可。

最后，注意到queueObject.doWait()在try – catch块中是怎样调用的。在InterruptedException抛出的情况下，线程得以离开lock()，并需让它从队列中移除。

性能考虑

如果比较Lock和FairLock类，你会注意到在FairLock类中lock()和unlock()还有更多需要深入的地方。这些额外的代码会导致FairLock的同步机制实现比Lock要稍微慢些。究竟存在多少影响，还依赖于应用在FairLock临界区执行的时长。执行时长越大，FairLock带来的负担影响就越小，当然这也和代码执行的频繁度相关。

## 嵌套管程锁死

嵌套管程锁死类似于死锁， 下面是一个嵌套管程锁死的场景：

```
线程1获得A对象的锁。
线程1获得对象B的锁（同时持有对象A的锁）。
线程1决定等待另一个线程的信号再继续。
线程1调用B.wait()，从而释放了B对象上的锁，但仍然持有对象A的锁。

线程2需要同时持有对象A和对象B的锁，才能向线程1发信号。
线程2无法获得对象A上的锁，因为对象A上的锁当前正被线程1持有。
线程2一直被阻塞，等待线程1释放对象A上的锁。

线程1一直阻塞，等待线程2的信号，因此，不会释放对象A上的锁，
	而线程2需要对象A上的锁才能给线程1发信号……
```

你可以能会说，这是个空想的场景，好吧，让我们来看看下面这个比较挫的Lock实现：

```java
//lock implementation with nested monitor lockout problem

public class Lock{
  protected MonitorObject monitorObject = new MonitorObject();
  protected boolean isLocked = false;

  public void lock() throws InterruptedException{
    synchronized(this){
      while(isLocked){
        synchronized(this.monitorObject){
            this.monitorObject.wait();
        }
      }
      isLocked = true;
    }
  }

  public void unlock(){
    synchronized(this){
      this.isLocked = false;
      synchronized(this.monitorObject){
        this.monitorObject.notify();
      }
    }
  }
}
```

可以看到，lock()方法首先在”this”上同步，然后在monitorObject上同步。如果isLocked等于false，因为线程不会继续调用monitorObject.wait()，那么一切都没有问题 。但是如果isLocked等于true，调用lock()方法的线程会在monitorObject.wait()上阻塞。

这里的问题在于，调用monitorObject.wait()方法只释放了monitorObject上的管程对象，而与”this“关联的管程对象并没有释放。换句话说，这个刚被阻塞的线程仍然持有”this”上的锁。

（校对注：如果一个线程持有这种Lock的时候另一个线程执行了lock操作）当一个已经持有这种Lock的线程想调用unlock(),就会在unlock()方法进入synchronized(this)块时阻塞。这会一直阻塞到在lock()方法中等待的线程离开synchronized(this)块。但是，在unlock中isLocked变为false，monitorObject.notify()被执行之后，lock()中等待的线程才会离开synchronized(this)块。

简而言之，在lock方法中等待的线程需要其它线程成功调用unlock方法来退出lock方法，但是，在lock()方法离开外层同步块之前，没有线程能成功执行unlock()。

结果就是，任何调用lock方法或unlock方法的线程都会一直阻塞。这就是嵌套管程锁死。

### 一个更现实的例子

你可能会说，这么挫的实现方式我怎么可能会做呢？你或许不会在里层的管程对象上调用wait或notify方法，但完全有可能会在外层的this上调。
有很多类似上面例子的情况。例如，如果你准备实现一个公平锁。你可能希望每个线程在它们各自的QueueObject上调用wait()，这样就可以每次唤醒一个线程。

下面是一个比较挫的公平锁实现方式：

```java
//Fair Lock implementation with nested monitor lockout problem

public class FairLock {
  private boolean           isLocked       = false;
  private Thread            lockingThread  = null;
  private List<QueueObject> waitingThreads =
            new ArrayList<QueueObject>();

  public void lock() throws InterruptedException{
    QueueObject queueObject = new QueueObject();

    synchronized(this){
      waitingThreads.add(queueObject);

      while(isLocked || waitingThreads.get(0) != queueObject){

        synchronized(queueObject){
          try{
            queueObject.wait();
          }catch(InterruptedException e){
            waitingThreads.remove(queueObject);
            throw e;
          }
        }
      }
      waitingThreads.remove(queueObject);
      isLocked = true;
      lockingThread = Thread.currentThread();
    }
  }

  public synchronized void unlock(){
    if(this.lockingThread != Thread.currentThread()){
      throw new IllegalMonitorStateException(
        "Calling thread has not locked this lock");
    }
    isLocked      = false;
    lockingThread = null;
    if(waitingThreads.size() > 0){
      QueueObject queueObject = waitingThread.get(0);
      synchronized(queueObject){
        queueObject.notify();
      }
    }
  }
}
public class QueueObject {}
```

乍看之下，嗯，很好，但是请注意lock方法是怎么调用queueObject.wait()的，在方法内部有两个synchronized块，一个锁定this，一个嵌在上一个synchronized块内部，它锁定的是局部变量queueObject。
当一个线程调用queueObject.wait()方法的时候，它仅仅释放的是在queueObject对象实例的锁，并没有释放”this”上面的锁。

现在我们还有一个地方需要特别注意， unlock方法被声明成了synchronized，这就相当于一个synchronized（this）块。这就意味着，如果一个线程在lock()中等待，该线程将持有与this关联的管程对象。所有调用unlock()的线程将会一直保持阻塞，等待着前面那个已经获得this锁的线程释放this锁，但这永远也发生不了，因为只有某个线程成功地给lock()中等待的线程发送了信号，this上的锁才会释放，但只有执行unlock()方法才会发送这个信号。

因此，上面的公平锁的实现会导致嵌套管程锁死。更好的公平锁实现方式可以参考Starvation and Fairness。

嵌套管程锁死 VS 死锁

嵌套管程锁死与死锁很像：都是线程最后被一直阻塞着互相等待。

但是两者又不完全相同。在死锁中我们已经对死锁有了个大概的解释，死锁通常是因为两个线程获取锁的顺序不一致造成的，线程1锁住A，等待获取B，线程2已经获取了B，再等待获取A。如死锁避免中所说的，死锁可以通过总是以相同的顺序获取锁来避免。
但是发生嵌套管程锁死时锁获取的顺序是一致的。线程1获得A和B，然后释放B，等待线程2的信号。线程2需要同时获得A和B，才能向线程1发送信号。所以，一个线程在等待唤醒，另一个线程在等待想要的锁被释放。

不同点归纳如下：

死锁中，二个线程都在等待对方释放锁。

嵌套管程锁死中，线程1持有锁A，同时等待从线程2发来的信号，线程2需要锁A来发信号给线程1。