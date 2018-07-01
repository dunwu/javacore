package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * 错误启动线程方式。使用 run() 方法启动线程并不会交错执行。
 * @author Zhang Peng
 */
public class ThreadDemo01 {

    public static void main(String[] args) {
        Thread01 mt1 = new Thread01("线程A "); // 实例化对象
        Thread01 mt2 = new Thread01("线程B "); // 实例化对象
        mt1.run(); // 调用线程主体
        mt2.run(); // 调用线程主体
    }

    static class Thread01 extends Thread {

        private int ticket = 5;

        Thread01(String name) {
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



