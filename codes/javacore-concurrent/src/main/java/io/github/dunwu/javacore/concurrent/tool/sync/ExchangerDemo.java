package io.github.dunwu.javacore.concurrent.tool.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Exchanger 示例：在两个线程之间双向交换数据
 * <p>
 * {@link Exchanger} 定义了一个两方同步点：两个线程各自调 {@code exchange(v)} 后会一直阻塞，
 * 直到对方也到达，然后各自拿到<b>对方传入的对象</b>并继续执行。
 * 它只能用于恰好两个线程（多方交换应用 {@link java.util.concurrent.CyclicBarrier} 或阻塞队列）。
 * <p>
 * 本例里生产者往 {@code buffer1} 装 3 条数据后交换，消费者拿到同一个 List 引用后逐条取出并清空，
 * 下一轮再交换回来——两边就这样反复复用<b>同一批</b> List 对象来传递数据，避免了反复创建容器。
 * <p>
 * 注意：{@code exchange} 交换的是引用而不是副本，因此双方拿到的是同一个可变对象，
 * 交换后不能再继续修改已交出去的 List，否则会与对方产生竞态。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see java.util.concurrent.Exchanger
 * @since 2018/5/10
 */
public class ExchangerDemo {

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    /**
     * 生产者与消费者各循环 4 轮，共输出 36 行（生产者 20 行 + 消费者 16 行）。
     * 两边的行会互相交织，交织位置取决于调度，但各自内部的顺序是固定的：
     * 消费者第 N 次提取到的必然是 {@code buffer：N--1}、{@code buffer：N--2}、{@code buffer：N--3}。
     * 末尾的 {@code join} 保证方法返回前所有输出已打印完毕
     */
    public static void demo() throws InterruptedException {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        Exchanger<List<String>> exchanger = new Exchanger<>();

        Thread producerThread = new Thread(new Producer(buffer1, exchanger));
        Thread consumerThread = new Thread(new Consumer(buffer2, exchanger));

        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.join();
    }

    static class Producer implements Runnable {

        // 生产者、消费者交换的数据结构
        private List<String> buffer;

        // 生产者和消费者的交换对象
        private Exchanger<List<String>> exchanger;

        Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
            this.buffer = buffer;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i < 5; i++) {
                System.out.println("生产者第" + i + "次提供");
                for (int j = 1; j <= 3; j++) {
                    System.out.println("生产者装入" + i + "--" + j);
                    buffer.add("buffer：" + i + "--" + j);
                }

                System.out.println("生产者装满，等待与消费者交换...");
                try {
                    exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class Consumer implements Runnable {

        private final Exchanger<List<String>> exchanger;

        private List<String> buffer;

        Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
            this.buffer = buffer;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 1; i < 5; i++) {
                // 调用exchange()与消费者进行数据交换
                try {
                    buffer = exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("消费者第" + i + "次提取");
                for (int j = 1; j <= 3; j++) {
                    System.out.println("消费者 : " + buffer.get(0));
                    buffer.remove(0);
                }
            }
        }

    }

}
