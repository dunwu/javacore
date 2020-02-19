package io.github.dunwu.javacore.concurrent.thread;

/**
 * {@link Thread#sleep} 示例
 * <p>
 * {@link Thread#sleep} 方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入 Blocked 状态。
 * <p>
 * 该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。
 * <p>
 * 但是，{@link Thread#sleep} 方法不会释放“锁标志”，也就是说如果有 synchronized 同步块，其他线程仍然不能访问共享数据。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadJoinDemo
 * @see ThreadYieldDemo
 * @since 2018/1/18
 */
public class ThreadSleepDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程A", 500)).start();
        new Thread(new MyThread("线程B", 1000)).start();
        new Thread(new MyThread("线程C", 1500)).start();
    }

    static class MyThread implements Runnable {

        /**
         * 线程名称
         */
        private String name;

        /**
         * 休眠时间
         */
        private int time;

        private MyThread(String name, int time) {
            this.name = name;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                // 休眠指定的时间
                Thread.sleep(this.time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + "休眠" + this.time + "毫秒。");
        }

    }

}
