package io.github.dunwu.javacore.concurrent.sync;

/**
 * 死锁示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/8/1
 */
public class ThreadDeadLockDemo implements Runnable {

    private final static PersonA PERSON_A = new PersonA();

    private final static PersonB PERSON_B = new PersonB();

    private boolean flag = false;

    public static void main(String[] args) {
        ThreadDeadLockDemo threadDeadLockDemo1 = new ThreadDeadLockDemo();
        ThreadDeadLockDemo threadDeadLockDemo2 = new ThreadDeadLockDemo();
        threadDeadLockDemo1.flag = true;
        threadDeadLockDemo2.flag = false;
        Thread thA = new Thread(threadDeadLockDemo1);
        Thread thB = new Thread(threadDeadLockDemo2);
        thA.start();
        thB.start();
    }

    @Override
    public void run() {
        if (flag) {
            synchronized (PERSON_A) { // 同步张三
                PERSON_A.say();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (PERSON_B) {
                    PERSON_A.get();
                }
            }
        } else {
            synchronized (PERSON_B) {
                PERSON_B.say();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (PERSON_A) {
                    PERSON_B.get();
                }
            }
        }
    }

    static class PersonA {

        public void say() {
            System.out.println("张三对李四说：“你先给我钱，我再给你货”");
        }

        public void get() {
            System.out.println("张三得到钱了。");
        }

    }

    static class PersonB {

        public void say() {
            System.out.println("李四对张三说：“你先给我货，我再给你钱”");
        }

        public void get() {
            System.out.println("李四得到货了。");
        }

    }

}
