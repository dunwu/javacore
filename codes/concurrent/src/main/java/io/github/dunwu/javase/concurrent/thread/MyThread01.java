package io.github.dunwu.javase.concurrent.thread;

/**
 * 继承 Thread 类方式实现线程
 * @author Zhang Peng
 */
public class MyThread01 extends Thread {
    private String name;

    /**
     * 通过构造方法配置name属性
     */
    public MyThread01(String name) {
        this.name = name;
    }

    /**
     * 覆写run()方法，作为线程 的操作主体
     */
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(name + "运行，i = " + i);
        }
    }
};
