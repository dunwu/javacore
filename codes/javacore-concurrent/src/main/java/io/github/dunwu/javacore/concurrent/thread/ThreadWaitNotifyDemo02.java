package io.github.dunwu.javacore.concurrent.thread;

import java.util.PriorityQueue;

/**
 * 利用 wait() 、notify() 实现生产者-消费者模型
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see ConditionDemo
 * @since 2018/5/11
 */
public class ThreadWaitNotifyDemo02 {

    private static final int QUEUE_SIZE = 10;

    private static final PriorityQueue<Integer> queue = new PriorityQueue<>(QUEUE_SIZE);

    public static void main(String[] args) {
        new Producer("生产者A").start();
        new Producer("生产者B").start();
        new Consumer("消费者A").start();
        new Consumer("消费者B").start();
    }

    static class Consumer extends Thread {

        Consumer(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notifyAll();
                        }
                    }
                    queue.poll(); // 每次移走队首元素
                    queue.notifyAll();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 从队列取走一个元素，队列当前有：" + queue.size() + "个元素");
                }
            }
        }

    }

    static class Producer extends Thread {

        Producer(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == QUEUE_SIZE) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notifyAll();
                        }
                    }
                    queue.offer(1); // 每次插入一个元素
                    queue.notifyAll();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 向队列取中插入一个元素，队列当前有：" + queue.size() + "个元素");
                }
            }
        }

    }

}
