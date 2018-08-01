package io.github.dunwu.javacore.concurrent.chapter03;


public class ThreadDeadLockDemo implements Runnable {

    private static Zhangsan zs = new Zhangsan(); // 实例化static型对象
    private static Lisi ls = new Lisi(); // 实例化static型对象
    private boolean flag = false; // 声明标志位，判断那个先说话

    static class Zhangsan { // 定义张三类
        public void say() {
            System.out.println("张三对李四说：“你给我画，我就把书给你。”");
        }

        public void get() {
            System.out.println("张三得到画了。");
        }
    }

    static class Lisi { // 定义李四类
        public void say() {
            System.out.println("李四对张三说：“你给我书，我就把画给你”");
        }

        public void get() {
            System.out.println("李四得到书了。");
        }
    }

    @Override
    public void run() {
        if (flag) {
            synchronized (zs) { // 同步张三
                zs.say();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (ls) {
                    zs.get();
                }
            }
        } else {
            synchronized (ls) {
                ls.say();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (zs) {
                    ls.get();
                }
            }
        }
    }

    public static void main(String[] args) {
        ThreadDeadLockDemo t1 = new ThreadDeadLockDemo(); // 控制张三
        ThreadDeadLockDemo t2 = new ThreadDeadLockDemo(); // 控制李四
        t1.flag = true;
        t2.flag = false;
        Thread thA = new Thread(t1);
        Thread thB = new Thread(t2);
        thA.start();
        thB.start();
    }
};
