package io.github.dunwu.javacore.jvm.classloader;

/**
 * 类初始化死循环示例。
 * <p>
 * 两个线程同时去初始化同一个类：抢到初始化权的线程在 {@code <clinit>} 中死循环，
 * 另一个线程则被永久阻塞在类初始化上，于是整个进程挂起。
 * <p>
 * VM Args: -XX:+TraceClassLoading（JDK 8 及以前）或 -Xlog:class+load（JDK 9 及以后）
 * <p>
 * 运行结果（JDK 21 实测）：
 *
 * <pre>
 * Thread[#30,Thread-1,5,main]start
 * Thread[#29,Thread-0,5,main]start
 * Thread[#30,Thread-1,5,main]init DeadLoopClass
 * </pre>
 *
 * 此后进程不再有任何输出，也不会自行退出，需手动终止（Ctrl+C）。
 * 线程编号与名称随平台而异，且两个线程谁先抢到初始化权并不固定（本次是 Thread-1）。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018-04-16
 */
public class DeadLoopClassDemo {

    public static void main(String[] args) {
        Runnable script = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread() + " run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }

    static class DeadLoopClass {

        static {
            // 如果不加上这个if语句，编译器将提示“Initializer does not complete normally”并拒绝编译
            if (true) {
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {
                }
            }
        }
    }

}
