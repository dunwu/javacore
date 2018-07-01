package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * Thread.sleep() 示例 sleep() 方法需要指定等待的时间，它可以让当前正在执行的线程在指定的时间内暂停执行，进入 Blocked 状态。
 * 该方法既可以让其他同优先级或者高优先级的线程得到执行的机会，也可以让低优先级的线程得到执行机会。
 * 但是，sleep() 方法不会释放“锁标志”，也就是说如果有 synchronized 同步块，其他线程仍然不能访问共享数据。
 * @author Zhang Peng
 * @date 2018/1/18
 * @see ThreadJoinDemo
 * @see ThreadYieldDemo
 */
public class ThreadSleepDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程A", 1000)).start();
        new Thread(new MyThread("线程A", 2000)).start();
        new Thread(new MyThread("线程A", 3000)).start();
    }

    static class MyThread implements Runnable {

        private String name;
        private int time;

        private MyThread(String name, int time) {
            this.name = name; // 设置线程名称
            this.time = time; // 设置休眠时间
        }

        @Override
        public void run() {
            try {
                Thread.sleep(this.time); // 休眠指定的时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.name + "线程，休眠" + this.time + "毫秒。");
        }
    }
}
