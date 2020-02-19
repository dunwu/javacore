package io.github.dunwu.javacore.concurrent.thread;

import io.github.dunwu.javacore.concurrent.annotation.Error;

/**
 * {@link Thread} 使用错误示例
 * <p>
 * 重复调用 {@link Thread#start()} 会抛出 {@link java.lang.IllegalThreadStateException} 异常。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@Error
public class ThreadErrorDemo02 {

    public static void main(String[] args) {
        // 实例化对象
        MyThread t = new MyThread("错误线程");
        // 调用线程主体
        t.start();
        // 重复调用 start() 会抛出 java.lang.IllegalThreadStateException 异常
        t.start();
    }

    static class MyThread extends Thread {

        private int ticket = 5;

        MyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (ticket > 0) {
                System.out.println(this.getName() + " 卖出了第 " + ticket + " 张票");
                ticket--;
            }
        }

    }

}
