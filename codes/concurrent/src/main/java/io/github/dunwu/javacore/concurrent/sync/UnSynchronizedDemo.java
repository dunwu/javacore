package io.github.dunwu.javacore.concurrent.sync;

/**
 * 未同步的多线程示例，因为存在竞态条件，所以线程不安全。
 * @author Zhang Peng
 * @date 2018/5/21
 */
@SuppressWarnings("all")
public class UnSynchronizedDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程A")).start();
        new Thread(new MyThread("线程B")).start();
        new Thread(new MyThread("线程C")).start();
    }

    static class MyThread extends Thread {

        private int ticket = 5; // 假设一共有5张票

        public MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (ticket > 0) { // 还有票
                    try {
                        Thread.sleep(300); // 加入延迟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 卖票：ticket = " + ticket--);
                }
            }
        }
    }
}
