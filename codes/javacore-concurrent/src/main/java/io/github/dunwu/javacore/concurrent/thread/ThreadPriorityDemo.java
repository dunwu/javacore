package io.github.dunwu.javacore.concurrent.thread;

/**
 * Java 的线程优先级控制示例
 * <p>
 * Java 中的线程优先级如何控制？
 * <p>
 * Java 中的线程优先级的范围是 [1,10]，一般来说，高优先级的线程在运行时会具有优先权。可以通过 thread.setPriority(Thread.MAX_PRIORITY) 的方式设置，默认优先级为 5。
 * <p>
 * 高优先级的Java线程一定先执行吗？
 * <p>
 * 即使设置了线程的优先级，也无法保证高优先级的线程一定先执行。 原因：这是因为线程优先级依赖于操作系统的支持，然而，不同的操作系统支持的线程优先级并不相同，不能很好的和Java中线程优先级一一对应。 结论：Java
 * 线程优先级控制并不可靠。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/10
 */
public class ThreadPriorityDemo {

    public static void main(String[] args) {
        System.out.println("主方法的优先级：" + Thread.currentThread().getPriority());
        System.out.println("MAX_PRIORITY = " + Thread.MAX_PRIORITY);
        System.out.println("NORM_PRIORITY = " + Thread.NORM_PRIORITY);
        System.out.println("MIN_PRIORITY = " + Thread.MIN_PRIORITY);

        Thread t1 = new Thread(new MyThread(), "线程A"); // 实例化线程对象
        Thread t2 = new Thread(new MyThread(), "线程B"); // 实例化线程对象
        Thread t3 = new Thread(new MyThread(), "线程C"); // 实例化线程对象
        t1.setPriority(Thread.MIN_PRIORITY); // 优先级最低
        t2.setPriority(Thread.MAX_PRIORITY); // 优先级最低
        t3.setPriority(Thread.NORM_PRIORITY); // 优先级最低
        t1.start(); // 启动线程
        t2.start(); // 启动线程
        t3.start(); // 启动线程
    }

    static class MyThread implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(500); // 线程休眠
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 取得当前线程的名字
                String out = Thread.currentThread().getName() + "，优先级：" + Thread.currentThread().getPriority()
                    + "，运行：i = " + i;
                System.out.println(out);
            }
        }

    }

}
