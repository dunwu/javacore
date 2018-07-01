package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Java 容器 fail-fast 机制示例
 * @author Zhang Peng
 * @date 2018/6/29
 */
public class FailFastDemo {
    private static List<Integer> list = new ArrayList<>();

    private static class ThreadOne extends Thread {
        public void run() {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                System.out.println("ThreadOne 遍历:" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ThreadTwo extends Thread {
        public void run() {
            int i = 0;
            while (i < 6) {
                System.out.println("ThreadTwo run：" + i);
                if (i == 3) {
                    list.remove(i);
                }
                i++;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        new ThreadOne().start();
        new ThreadTwo().start();
    }
}
