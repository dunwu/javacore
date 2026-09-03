package io.github.dunwu.javacore.concurrent.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 示例：基于 AQS 独占模式实现的互斥锁。
 * <p>
 * 这个类是 {@link AbstractQueuedSynchronizer} 类注释里给出的官方范例，也是理解 AQS 最好的入口：
 * 一个功能完整的 {@link Lock} 实现，<b>核心代码只有十几行</b>。
 * <p>
 * AQS 用的是<b>模板方法模式</b>，职责划分非常清晰：
 * <ul>
 *     <li><b>子类只需回答「此刻能不能拿到 / 能不能释放」</b>——即重写 {@code tryAcquire} 与 {@code tryRelease}，
 *     这两个方法必须是<b>非阻塞</b>的，只做一个 CAS 判断就立刻返回</li>
 *     <li><b>AQS 负责所有难做的部分</b>——线程入队、用 {@code LockSupport.park} 挂起、按 FIFO 唤醒、
 *     处理中断与超时、维护等待队列。这些逻辑一行都不用重写</li>
 * </ul>
 * 子类与 AQS 之间唯一的沟通渠道就是那个 {@code int state}，它的含义完全由子类定义。
 * 本类把它定义为「0 = 空闲，1 = 已被占用」。
 * <p>
 * <b>与 {@link java.util.concurrent.locks.ReentrantLock} 的关键区别</b>：本类<b>不支持重入</b>。
 * 因为 {@code tryAcquire} 只在 state 为 0 时才成功，同一线程第二次获取会返回 false 并把自己挂起，
 * 而它正是当前的持有者——于是永久死锁。ReentrantLock 则在 {@code tryAcquire} 里额外判断
 * 「持有者是不是当前线程」，是就把 state 加一，以此记录重入层数。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class Mutex implements Lock, java.io.Serializable {

    /**
     * 同步器：把「锁的持有状态」翻译成 AQS 的 state
     * <p>
     * 声明为 {@code private static}：它不需要访问 Mutex 的实例字段，
     * 加 static 可以避免隐含持有外部实例引用（详见 javacore-oop 的 {@code InnerClassDemo}）
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 是否处于独占持有状态。AQS 在实现条件队列时会用到它来校验「调用者必须持有锁」
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 尝试获取：只在 state 为 0 时用 CAS 抢到，抢不到立刻返回 false，绝不阻塞
         * <p>
         * 这里刻意没有判断「持有者是否为当前线程」，因此本锁不可重入
         */
        @Override
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 尝试释放：因为不可重入，一次释放就必然归零
         */
        @Override
        protected boolean tryRelease(int releases) {
            if (getState() == 0) {
                // 没人持有却来释放，属于编码错误，应当尽早暴露
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            // state 只由持有者线程写，且释放发生在 unlock 时，这里用 setState 即可，无需 CAS
            setState(0);
            return true;
        }

        /**
         * 提供条件变量。{@code ConditionObject} 是 AQS 的内部类，
         * 它维护的「条件队列」与 AQS 的「同步队列」是两个不同的队列
         */
        Condition newCondition() {
            return new ConditionObject();
        }

    }

    /**
     * 所有对外的锁操作都委托给同步器，Mutex 自己不含任何并发逻辑
     */
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        // acquire = tryAcquire 失败后入队并挂起，这一段全由 AQS 完成
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

    /**
     * 锁当前是否被某个线程独占持有
     */
    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    /**
     * 同步队列中正在等待获取锁的线程数（估计值，仅用于诊断与演示）
     */
    public int getQueueLength() {
        return sync.getQueueLength();
    }

    /**
     * 同步队列中是否有等待线程（估计值，仅用于诊断与演示）
     */
    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
    }

}
