package io.github.dunwu.javacore.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReentrantLock可重入示例 {

    public static void main(String[] args) {
        Task task = new Task();
        MyThread tA = new MyThread("Thread-A", task);
        MyThread tB = new MyThread("Thread-B", task);
        MyThread tC = new MyThread("Thread-C", task);
        tA.start();
        tB.start();
        tC.start();
    }

    static class MyThread extends Thread {

        private Task task;

        public MyThread(String name, Task task) {
            super(name);
            this.task = task;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                task.addOne();
                System.out.println("value: " + task.get());
            }
        }

    }

    static class Task {

        private int value;
        private final Lock lock = new ReentrantLock();

        public Task() {
            this.value = 0;
        }

        public int get() {
            // 获取锁
            lock.lock();
            try {
                return value;
            } finally {
                // 保证锁能释放
                lock.unlock();
            }
        }

        public void addOne() {
            // 获取锁
            lock.lock();
            try {
                // 注意：此处已经成功获取锁，进入 get 方法后，又尝试获取锁，
                // 如果锁不是可重入的，会导致死锁
                value = 1 + get();
            } finally {
                // 保证锁能释放
                lock.unlock();
            }
        }

    }

}
