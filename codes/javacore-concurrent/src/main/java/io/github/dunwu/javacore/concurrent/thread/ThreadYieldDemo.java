package io.github.dunwu.javacore.concurrent.thread;

/**
 * Thread.yield() 示例 yield() 方法可以让当前正在执行的线程暂停，但它不会阻塞该线程，它只是将该线程从 Running 状态转入 Runnable 状态。 当某个线程调用了 yield()
 * 方法暂停之后，只有优先级与当前线程相同，或者优先级比当前线程更高的处于就绪状态的线程才会获得执行的机会。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ThreadJoinDemo
 * @see ThreadSleepDemo
 * @since 2018/1/18
 */
public class ThreadYieldDemo {

    public static void main(String[] args) {
        new Thread(new MyThread("线程A")).start();
        new Thread(new MyThread("线程B")).start();
    }

    static class MyThread implements Runnable {

        /**
         * 线程名称
         */
        private String name;

        private MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(this.name + "运行，i = " + i);
                if (i == 2) {
                    System.out.print("线程礼让：");
                    Thread.yield();
                }
            }
        }

    }

}
