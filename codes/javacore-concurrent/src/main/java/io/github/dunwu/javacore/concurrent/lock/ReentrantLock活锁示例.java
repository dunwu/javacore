package io.github.dunwu.javacore.concurrent.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-02
 */
public class ReentrantLock活锁示例 {

    public static void main(String[] args) {
        Account a = new Account(100000);
        Account b = new Account(0);

        final CountDownLatch latch = new CountDownLatch(10000);
        Runnable r = () -> {
            a.transfer(b, 1);
            System.out.println("args = " + a.balance);
            latch.countDown();
        };

        for (int i = 0; i < 10000; i++) {
            new Thread(r).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Account A: " + a.balance);
        System.out.println("Account B: " + b.balance);
    }

    static class Account {

        private int balance;
        private final Lock lock = new ReentrantLock();

        public Account(int balance) {
            this.balance = balance;
        }

        // 转账
        void transfer(Account tar, int amt) {
            while (true) {
                if (this.lock.tryLock()) {
                    System.out.println("转账账户获取锁");
                    try {
                        if (tar.lock.tryLock()) {
                            System.out.println("被转账账户获取锁");
                            try {
                                if (this.balance > amt) {
                                    this.balance -= amt;
                                    tar.balance += amt;
                                }
                                System.out.println("转账成功，两个账户金额发生变化");
                                System.out.printf("a = %s, b = %s", this.balance, tar.balance);
                            } finally {
                                tar.lock.unlock();
                                System.out.println("转账账户释放锁");
                            }
                        }//if
                    } finally {
                        this.lock.unlock();
                        System.out.println("被转账账户释放锁");
                    }
                }//if
            }//while
        }//transfer

    }

}
