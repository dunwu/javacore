package io.github.dunwu.javase.concurrent.tool;

import java.util.concurrent.Semaphore;

/**
 * Semaphore 示例
 * 字面意思为信号量，Semaphore 可以控同时访问的线程个数，通过 acquire() 获取一个许可，
 * 如果没有就等待，而 release() 释放一个许可。
 *
 * @author Zhang Peng
 * @date 2018/5/10
 * @see java.util.concurrent.Semaphore
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(5); //机器数目
        for (int i = 0; i < N; i++) { new Worker(i, semaphore).start(); }
    }

    static class Worker extends Thread {

        private int num;
        private Semaphore semaphore;

        Worker(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire(); // 获取许可
                System.out.println("工人" + this.num + "占用一个机器在生产...");
                Thread.sleep(2000);
                System.out.println("工人" + this.num + "释放出机器");
                semaphore.release(); // 释放许可
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
