package io.github.dunwu.javacore.concurrent.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 示例：基于 AQS 共享模式实现的布尔闩。
 * <p>
 * 这个类同样来自 {@link AbstractQueuedSynchronizer} 的类注释范例，用于和 {@link Mutex} 的独占模式做对照。
 * <p>
 * <b>共享模式与独占模式的本质区别</b>在于「一次释放能放行几个线程」：
 * <ul>
 *     <li>独占模式（Mutex、ReentrantLock）—— 释放时只唤醒队首的<b>一个</b>线程，其余继续等</li>
 *     <li>共享模式（本类、Semaphore、CountDownLatch）—— 释放时会让<b>所有</b>满足条件的等待线程一起通过。
 *     AQS 的实现方式是：被唤醒的线程若发现 {@code tryAcquireShared} 返回值 &gt;= 0，
 *     除了让自己通过，还会<b>继续向后传播唤醒</b>（propagate）下一个节点，如此连锁放行</li>
 * </ul>
 * 对应到需要重写的方法上，独占模式是 {@code tryAcquire} / {@code tryRelease}（返回 boolean），
 * 共享模式则是 {@code tryAcquireShared} / {@code tryReleaseShared}（前者返回 int，
 * <b>负数表示失败、非负数表示成功且可能还有剩余资源</b>）。
 * <p>
 * 与 {@link java.util.concurrent.CountDownLatch} 的区别：CountDownLatch 的 state 是<b>剩余计数</b>，
 * 可以被 {@code countDown()} 逐步递减，且归零后不可逆；本类的 state 只有「关」与「开」两态，
 * 是一次性的开关，因此 {@code signal()} 天然幂等。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class BooleanLatch {

    /**
     * 同步器：state = 0 表示闩关闭，state = 1 表示闩已打开
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        boolean isSignalled() {
            return getState() != 0;
        }

        /**
         * 共享模式下的「尝试获取」：闩已打开就返回 1（放行，且表示资源充足），否则返回 -1（阻塞排队）
         * <p>
         * 注意这里<b>不修改 state</b>——闩一旦打开就对所有线程永久开放，
         * 这正是共享模式与「消耗型资源」（如 Semaphore 每次获取要扣减许可）的差别
         */
        @Override
        protected int tryAcquireShared(int ignore) {
            return isSignalled() ? 1 : -1;
        }

        /**
         * 共享模式下的「尝试释放」：把闩置为打开，并返回 true 通知 AQS 去唤醒等待队列
         */
        @Override
        protected boolean tryReleaseShared(int ignore) {
            setState(1);
            return true;
        }

    }

    private final Sync sync = new Sync();

    /**
     * 闩是否已打开
     */
    public boolean isSignalled() {
        return sync.isSignalled();
    }

    /**
     * 等待闩被打开。闩未打开时阻塞；一旦打开，所有等待线程会被一起放行，之后调用本方法也不再阻塞
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * 打开闩，放行全部等待线程。重复调用没有副作用
     */
    public void signal() {
        sync.releaseShared(1);
    }

}
