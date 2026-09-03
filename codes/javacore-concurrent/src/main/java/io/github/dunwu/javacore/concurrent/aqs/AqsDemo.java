package io.github.dunwu.javacore.concurrent.aqs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 示例：AQS（{@link java.util.concurrent.locks.AbstractQueuedSynchronizer}）自定义同步器。
 * <p>
 * AQS 是 JUC 中绝大多数同步器的<b>共同骨架</b>：ReentrantLock、Semaphore、CountDownLatch、
 * ReentrantReadWriteLock、ThreadPoolExecutor.Worker、FutureTask 都建立在它之上。
 * 它把并发编程中最容易写错的部分——<b>CLH 变体的双向等待队列、挂起与唤醒、FIFO 公平性、
 * 中断响应、超时控制</b>——一次性实现好，只留下两个「能不能」的问题交给子类：
 * <ul>
 *     <li>独占模式：重写 {@code tryAcquire} / {@code tryRelease}</li>
 *     <li>共享模式：重写 {@code tryAcquireShared} / {@code tryReleaseShared}</li>
 * </ul>
 * 本包用两个官方范例类来演示这两条路径：{@link Mutex}（独占，实现完整的 {@code Lock}）
 * 与 {@link BooleanLatch}（共享，一次放行全部等待线程）。
 * <p>
 * <b>断言口径说明</b>：本类的示例都涉及多线程，输出的<b>行位置</b>由同步语义严格保证，
 * 但同一时刻被放行的多个线程之间的先后顺序取决于调度。因此示例刻意<b>不打印线程名</b>，
 * 让所有输出都成为可精确断言的确定内容。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class AqsDemo {

    /**
     * 持有者线程占住锁的时长，必须明显大于 {@link #WAIT_MILLIS}，才能让「超时获取」稳定失败
     */
    private static final long HOLD_MILLIS = 300;

    /**
     * {@code tryLock} 的等待超时
     */
    private static final long WAIT_MILLIS = 50;

    /**
     * 轮询等待的上限。轮询本身通常几毫秒就结束，这个值只是兜底，避免异常情况下测试永久挂住
     */
    private static final long POLL_TIMEOUT_MILLIS = 2000;

    /**
     * ① 独占模式：互斥保证「读-改-写」不被打断
     */
    public static void exclusiveMode() throws InterruptedException {
        Mutex mutex = new Mutex();
        System.out.println("新建的 Mutex 是否已被持有: " + mutex.isLocked());

        int threadCount = 3;
        int loopPerThread = 200;
        // 这里故意用「非原子」的 int 计数：counter[0]++ 是读、改、写三步，
        // 若没有互斥保护，多线程并发执行必然丢失更新，结果会小于 600
        int[] counter = new int[1];
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            Thread worker = new Thread(() -> {
                try {
                    startGate.await();   // 起跑门：让 3 个线程真正同时冲向临界区，把竞争拉到最大
                    for (int j = 0; j < loopPerThread; j++) {
                        mutex.lock();
                        try {
                            counter[0]++;
                        } finally {
                            mutex.unlock();   // 必须放在 finally：临界区一旦抛异常，锁就永远还不回去了
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endGate.countDown();
                }
            }, "worker-" + i);
            worker.start();
        }
        startGate.countDown();
        // 等全部线程跑完再读结果，同时也保证子线程的输出不会漏到 demo() 返回之后
        endGate.await();

        System.out.println(threadCount + " 个线程各累加 " + loopPerThread + " 次，最终计数: " + counter[0]);
    }

    /**
     * ② 同步队列：拿不到锁的线程会被挂起并排队，队列长度可以直接观测
     */
    public static void queuedThreads() throws InterruptedException {
        Mutex mutex = new Mutex();
        int blockerCount = 2;
        CountDownLatch started = new CountDownLatch(blockerCount);
        CountDownLatch release = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(blockerCount);

        // 主线程先占住锁，之后来的线程只能排队
        mutex.lock();
        try {
            for (int i = 0; i < blockerCount; i++) {
                Thread blocker = new Thread(() -> {
                    started.countDown();
                    mutex.lock();      // 抢不到锁，会被 AQS 挂起并挂到同步队列尾部
                    try {
                        release.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        mutex.unlock();
                        finished.countDown();
                    }
                }, "blocker-" + i);
                blocker.start();
            }
            started.await();
            // 注意：「线程已启动」并不等于「线程已入队」——只有真正阻塞在 acquire 上才会入队
            awaitQueueLength(mutex, blockerCount);
            System.out.println("主线程持锁期间，同步队列里的线程数: " + mutex.getQueueLength());
            System.out.println("同步队列里是否有等待线程: " + mutex.hasQueuedThreads());
        } finally {
            mutex.unlock();
        }

        // 独占模式下一次 unlock 只唤醒队首一个线程，两个线程只能依次通过；
        // 等它们都彻底跑完（各自 unlock 之后才会真正出队），队列才归零
        release.countDown();
        finished.await();
        System.out.println("两个线程都执行完之后，同步队列里的线程数: " + mutex.getQueueLength());
        System.out.println("此时锁是否仍被持有: " + mutex.isLocked());
    }

    /**
     * ③ 获取锁的几种方式，以及「不可重入」这个最容易踩的特性
     */
    public static void tryLockAndReentrancy() throws InterruptedException {
        Mutex mutex = new Mutex();

        // tryLock() 不排队也不阻塞，拿不到就立刻返回 false
        System.out.println("空闲时 tryLock(): " + mutex.tryLock());
        // 关键区别：Mutex 不可重入，已持锁的线程再次获取会失败。
        // 若这里调用的是 lock() 而不是 tryLock()，线程就会把自己挂起，造成无法自救的死锁。
        // 这也解释了为什么「已加锁的方法」不能直接调用另一个「要加同一把锁的方法」
        System.out.println("已持有时再次 tryLock(): " + mutex.tryLock());
        System.out.println("此时锁仍被持有: " + mutex.isLocked());
        mutex.unlock();

        // 对照 ReentrantLock：它允许重入，并用 state 记录重入层数
        ReentrantLock reentrant = new ReentrantLock();
        reentrant.lock();
        System.out.println("ReentrantLock 已持有时再次 tryLock(): " + reentrant.tryLock());
        System.out.println("ReentrantLock 的重入层数: " + reentrant.getHoldCount());
        // 重入几次就要释放几次，否则 state 不会归零，锁一直放不掉
        reentrant.unlock();
        reentrant.unlock();
        System.out.println("释放两次后的重入层数: " + reentrant.getHoldCount());

        // tryLock(timeout)：拿不到时最多等这么久，超时返回 false，而不是无限期阻塞
        Thread holder = new Thread(() -> {
            mutex.lock();
            try {
                Thread.sleep(HOLD_MILLIS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                mutex.unlock();
            }
        }, "holder");
        holder.start();
        // 先确认 holder 真的拿到了锁再开始计时，否则主线程可能反而抢成功，演示就失效了
        awaitLocked(mutex);
        long start = System.nanoTime();
        boolean acquired = mutex.tryLock(WAIT_MILLIS, TimeUnit.MILLISECONDS);
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("锁被占用时 tryLock(" + WAIT_MILLIS + "ms) 的结果: " + acquired);
        System.out.println("是否等满了超时时间才返回: " + (elapsed >= WAIT_MILLIS));
        holder.join();
        System.out.println("holder 释放之后再 tryLock(): " + mutex.tryLock());
        mutex.unlock();
    }

    /**
     * ④ 条件变量：await 释放锁并挂起，signal 只是把它移回同步队列，还要重新竞争到锁才能继续
     */
    public static void conditionUsage() throws InterruptedException {
        Mutex mutex = new Mutex();
        Condition condition = mutex.newCondition();
        // 这个门闩用来保证 waiter 一定先拿到锁。否则主线程可能先抢到，
        // 那么 signal 时条件队列还是空的，之后 waiter 再 await 就没人能唤醒它了
        CountDownLatch waiterHoldsLock = new CountDownLatch(1);

        Thread waiter = new Thread(() -> {
            mutex.lock();
            try {
                System.out.println("waiter 拿到锁，即将进入 await");
                waiterHoldsLock.countDown();
                // await 做两件事：释放锁、把自己挂到条件队列上，这两步对锁的释放是原子的
                condition.await();
                // 能走到这里，说明它已被 signal 移回同步队列，并且重新竞争到了锁
                System.out.println("waiter 被唤醒，重新竞争到锁后继续执行");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                mutex.unlock();
            }
        }, "waiter");

        waiter.start();
        waiterHoldsLock.await();
        // 此刻锁必定在 waiter 手上，主线程只能等它 await 时才释放——
        // 所以「主线程拿到了锁」这件事本身就证明了 waiter 已进入条件队列
        mutex.lock();
        try {
            System.out.println("main 拿到了锁，这说明 waiter 已经 await 并释放了锁");
            condition.signal();
            System.out.println("main 发出 signal，waiter 被移回同步队列，但要等 main 释放锁才能真正继续");
        } finally {
            mutex.unlock();
        }
        System.out.println("main 释放锁");
        waiter.join();
    }

    /**
     * ⑤ 共享模式：一次 signal 放行全部等待线程
     */
    public static void sharedMode() throws InterruptedException {
        BooleanLatch latch = new BooleanLatch();
        System.out.println("初始时闩是否已打开: " + latch.isSignalled());

        int waiterCount = 3;
        CountDownLatch started = new CountDownLatch(waiterCount);
        CountDownLatch finished = new CountDownLatch(waiterCount);
        for (int i = 0; i < waiterCount; i++) {
            Thread waiter = new Thread(() -> {
                started.countDown();
                try {
                    latch.await();     // 闩未打开，阻塞在共享模式的等待队列上
                    System.out.println("等待线程被放行");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finished.countDown();
                }
            }, "latch-waiter-" + i);
            waiter.start();
        }
        started.await();
        System.out.println("signal 之前闩是否已打开: " + latch.isSignalled());

        // 一次 signal 放行全部等待线程，这是共享模式与独占模式最直观的区别：
        // 换成 Mutex 的话，一次 unlock 只会唤醒队首的一个线程（见 queuedThreads）
        latch.signal();
        finished.await();
        System.out.println("signal 之后闩是否已打开: " + latch.isSignalled());

        // 闩打开后是永久开放的，此后调用 await 的线程不再阻塞
        latch.await();
        System.out.println("闩打开后再调用 await 会立即返回，不会阻塞");

        // state 已经是 1，重复 signal 只是把它再设成 1，没有副作用
        latch.signal();
        System.out.println("重复 signal 之后闩仍是打开的: " + latch.isSignalled());
    }

    /**
     * ⑥ state 的语义完全由子类定义：同一个 int 在各家同步器里表达的东西截然不同
     */
    public static void stateSemantics() {
        System.out.println("AQS 只维护一个 int state，它的含义完全由子类定义：");
        System.out.println("  ReentrantLock             —— 当前线程的重入次数，0 表示无人持有");
        System.out.println("  Semaphore                 —— 剩余可用许可数");
        System.out.println("  CountDownLatch            —— 剩余计数，减到 0 就永久放行");
        System.out.println("  ReentrantReadWriteLock    —— 高 16 位是读锁持有计数，低 16 位是写锁重入计数");
        System.out.println("  ThreadPoolExecutor.Worker —— 同样不可重入：-1 表示线程尚未启动（用于抑制中断），");
        System.out.println("                              0 表示空闲，1 表示正在执行任务");
        System.out.println("  本例的 Mutex               —— 只有 0 与 1 两种取值，且不允许重入");
        System.out.println("  本例的 BooleanLatch        —— 0 表示闩关闭，非 0 表示已打开");

        // 读写锁把两个计数打包进同一个 int，用公开 API 可以印证它们各自独立统计
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.writeLock().lock();
        readWriteLock.writeLock().lock();   // 写锁重入
        readWriteLock.readLock().lock();    // 持有写锁时仍可获取读锁，这是「锁降级」的前半段
        System.out.println("写锁重入 2 次、读锁持有 1 次，两个计数互不干扰：");
        System.out.println("  getWriteHoldCount(): " + readWriteLock.getWriteHoldCount());
        System.out.println("  getReadHoldCount(): " + readWriteLock.getReadHoldCount());
        // 释放顺序与获取顺序相反；写锁重入了两次，因此要释放两次
        readWriteLock.readLock().unlock();
        readWriteLock.writeLock().unlock();
        readWriteLock.writeLock().unlock();
    }

    /**
     * 依次演示独占模式、同步队列、获取方式、条件变量、共享模式与 state 语义
     */
    public static void demo() throws InterruptedException {
        exclusiveMode();
        queuedThreads();
        tryLockAndReentrancy();
        conditionUsage();
        sharedMode();
        stateSemantics();
    }

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 轮询等待同步队列达到指定长度。
     * <p>
     * 用带上限的轮询而不是固定 sleep：sleep 太短会偶发失败，太长会拖慢测试，
     * 而轮询通常几毫秒就能满足条件。
     */
    private static void awaitQueueLength(Mutex mutex, int expected) throws InterruptedException {
        long deadline = System.currentTimeMillis() + POLL_TIMEOUT_MILLIS;
        while (mutex.getQueueLength() < expected && System.currentTimeMillis() < deadline) {
            Thread.sleep(1);
        }
    }

    /**
     * 轮询等待锁被其他线程持有
     */
    private static void awaitLocked(Mutex mutex) throws InterruptedException {
        long deadline = System.currentTimeMillis() + POLL_TIMEOUT_MILLIS;
        while (!mutex.isLocked() && System.currentTimeMillis() < deadline) {
            Thread.sleep(1);
        }
    }

}

// Output:
// 新建的 Mutex 是否已被持有: false
// 3 个线程各累加 200 次，最终计数: 600
// 主线程持锁期间，同步队列里的线程数: 2
// 同步队列里是否有等待线程: true
// 两个线程都执行完之后，同步队列里的线程数: 0
// 此时锁是否仍被持有: false
// 空闲时 tryLock(): true
// 已持有时再次 tryLock(): false
// 此时锁仍被持有: true
// ReentrantLock 已持有时再次 tryLock(): true
// ReentrantLock 的重入层数: 2
// 释放两次后的重入层数: 0
// 锁被占用时 tryLock(50ms) 的结果: false
// 是否等满了超时时间才返回: true
// holder 释放之后再 tryLock(): true
// waiter 拿到锁，即将进入 await
// main 拿到了锁，这说明 waiter 已经 await 并释放了锁
// main 发出 signal，waiter 被移回同步队列，但要等 main 释放锁才能真正继续
// main 释放锁
// waiter 被唤醒，重新竞争到锁后继续执行
// 初始时闩是否已打开: false
// signal 之前闩是否已打开: false
// 等待线程被放行
// 等待线程被放行
// 等待线程被放行
// signal 之后闩是否已打开: true
// 闩打开后再调用 await 会立即返回，不会阻塞
// 重复 signal 之后闩仍是打开的: true
// AQS 只维护一个 int state，它的含义完全由子类定义：
//   ReentrantLock             —— 当前线程的重入次数，0 表示无人持有
//   Semaphore                 —— 剩余可用许可数
//   CountDownLatch            —— 剩余计数，减到 0 就永久放行
//   ReentrantReadWriteLock    —— 高 16 位是读锁持有计数，低 16 位是写锁重入计数
//   ThreadPoolExecutor.Worker —— 同样不可重入：-1 表示线程尚未启动（用于抑制中断），
//                               0 表示空闲，1 表示正在执行任务
//   本例的 Mutex               —— 只有 0 与 1 两种取值，且不允许重入
//   本例的 BooleanLatch        —— 0 表示闩关闭，非 0 表示已打开
// 写锁重入 2 次、读锁持有 1 次，两个计数互不干扰：
//   getWriteHoldCount(): 2
//   getReadHoldCount(): 1
