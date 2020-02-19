package io.github.dunwu.javacore.concurrent.thread;

import io.github.dunwu.javacore.concurrent.annotation.Error;

/**
 * {@link Thread} 使用错误示例
 * <p>
 * 直接调用 {@link Thread#run()} 而不是调用 {@link Thread#start()}，会导致不能正常启动线程。
 * <p>
 * 注意：本示例中 MyThread 的两个实例是串行执行的
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@Error
public class ThreadErrorDemo01 {

    public static void main(String[] args) {
        // 实例化对象
        MyThread tA = new MyThread("错误线程-A");
        MyThread tB = new MyThread("错误线程-B");
        // 调用线程主体
        tA.run();
        tB.run();
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
