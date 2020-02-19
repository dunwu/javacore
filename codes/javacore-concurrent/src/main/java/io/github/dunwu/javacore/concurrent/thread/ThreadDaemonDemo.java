package io.github.dunwu.javacore.concurrent.thread;

/**
 * 守护线程示例
 * <p>
 * 什么是守护线程 守护线程（Daemon Thread）是在后台执行并且不会阻止 JVM 终止的线程。 与守护线程（Daemon Thread）相反的，叫用户线程（User Thread），也就是非守护线程。
 * <p>
 * 为什么要用守护线程 守护线程的优先级比较低，用于为系统中的其它对象和线程提供服务。典型的应用就是垃圾回收器。
 * <p>
 * 如何创建守护线程 使用 thread.setDaemon(true) 可以设置 thread 线程为守护线程。
 * <p>
 * 注意点： 正在运行的用户线程无法设置为守护线程，所以 thread.setDaemon(true)必须在 thread.start()之前设置，否则会抛出 llegalThreadStateException 异常；
 * 一个守护线程创建的子线程依然是守护线程。 不要认为所有的应用都可以分配给 Daemon 来进行服务，比如读写操作或者计算逻辑。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/1/18
 */
public class ThreadDaemonDemo {

    public static void main(String[] args) {
        Thread t = new Thread(new MyThread(), "线程");
        t.setDaemon(true); // 此线程在后台运行
        System.out.println("线程 t 是否是守护进程：" + t.isDaemon());
        t.start(); // 启动线程
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "在运行。");
            }
        }

    }

}
