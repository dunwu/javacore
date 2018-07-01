package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * 正确启动线程方式。
 * @author Zhang Peng
 */
public class ThreadDemo02 {

    public static void main(String[] args) {
        Thread02 mt1 = new Thread02("线程A "); // 实例化对象
        Thread02 mt2 = new Thread02("线程B "); // 实例化对象
        mt1.start(); // 调用线程主体
        mt2.start(); // 调用线程主体
    }

    static class Thread02 extends Thread {

        private int ticket = 5;

        Thread02(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (this.ticket > 0) {
                    System.out.println(this.getName() + " 卖票：ticket = " + ticket--);
                }
            }
        }
    }
}




