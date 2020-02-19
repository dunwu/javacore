package io.github.dunwu.javacore.concurrent.container;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CopyOnWriteArrayList 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/5/16
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        new CopyOnWriteArrayListDemo().run();
    }

    public void run() {
        final int NUM = 10;
        // ArrayList 在并发迭代访问时会抛出 ConcurrentModificationException 异常
        // List<String> list = new ArrayList<>();
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < NUM; i++) {
            list.add("main_" + i);
        }
        ExecutorService executorService = Executors.newFixedThreadPool(NUM);
        for (int i = 0; i < NUM; i++) {
            executorService.execute(new ReadTask(list));
            executorService.execute(new WriteTask(list, i));
        }
        executorService.shutdown();
    }

    static class ReadTask implements Runnable {

        List<String> list;

        ReadTask(List<String> list) {
            this.list = list;
        }

        @Override
        public void run() {
            for (String str : list) {
                System.out.println(str);
            }
        }

    }

    static class WriteTask implements Runnable {

        List<String> list;

        int index;

        WriteTask(List<String> list, int index) {
            this.list = list;
            this.index = index;
        }

        @Override
        public void run() {
            list.remove(index);
            list.add(index, "write_" + index);
        }

    }

}
