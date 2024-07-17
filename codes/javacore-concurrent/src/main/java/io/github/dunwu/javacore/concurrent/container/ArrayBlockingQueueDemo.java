package io.github.dunwu.javacore.concurrent.container;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @date 2024-07-15
 */
public class ArrayBlockingQueueDemo {

    public static final String EXIT_MSG = "Good bye!";

    public static void main(String[] args) {
        // 使用较小的队列，以更好地在输出中展示其影响
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
    }

    static class Producer implements Runnable {

        private BlockingQueue<String> queue;

        public Producer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20; i++) {
                try {
                    Thread.sleep(5L);
                    String msg = "Message" + i;
                    System.out.println("Produced new item: " + msg);
                    queue.put(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                System.out.println("Time to say good bye!");
                queue.put(EXIT_MSG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static class Consumer implements Runnable {

        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            try {
                String msg;
                while (!EXIT_MSG.equalsIgnoreCase((msg = queue.take()))) {
                    System.out.println("Consumed item: " + msg);
                    Thread.sleep(10L);
                }
                System.out.println("Got exit message, bye!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
// 输出示例：
//
// Produced new item: Message0
// Consumed item: Message0
// Produced new item: Message1
// Consumed item: Message1
// Produced new item: Message2
// Produced new item: Message3
// Consumed item: Message2
// Produced new item: Message4
// Produced new item: Message5
// Consumed item: Message3
// Produced new item: Message6
// Produced new item: Message7
// Consumed item: Message4
// Produced new item: Message8
// Consumed item: Message5
// Produced new item: Message9
// Consumed item: Message6
// Produced new item: Message10
// Consumed item: Message7
// Produced new item: Message11
// Consumed item: Message8
// Produced new item: Message12
// Consumed item: Message9
// Produced new item: Message13
// Consumed item: Message10
// Produced new item: Message14
// Consumed item: Message11
// Produced new item: Message15
// Consumed item: Message12
// Produced new item: Message16
// Consumed item: Message13
// Produced new item: Message17
// Consumed item: Message14
// Produced new item: Message18
// Consumed item: Message15
// Produced new item: Message19
// Consumed item: Message16
// Time to say good bye!
// Consumed item: Message17
// Consumed item: Message18
// Consumed item: Message1
