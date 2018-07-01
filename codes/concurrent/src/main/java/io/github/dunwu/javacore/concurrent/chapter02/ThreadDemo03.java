package io.github.dunwu.javacore.concurrent.chapter02;

/**
 * 重复调用 start() 会抛出 java.lang.IllegalThreadStateException 异常。
 * @author Zhang Peng
 */
public class ThreadDemo03 {

    public static void main(String[] args) {
        Thread03 mt1 = new Thread03("线程A "); // 实例化对象
        mt1.start(); // 调用线程主体
        mt1.start(); // 调用线程主体
    }

    static class Thread03 extends Thread {

        private int ticket = 5;

        Thread03(String name) {
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




