package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Java 容器 fail-fast 机制示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/6/29
 */
public class FailFastDemo {

    private static int MAX = 100;

    private static List<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < MAX; i++) {
            list.add(i);
        }
        new Thread(new MyThreadA()).start();
        new Thread(new MyThreadB()).start();
    }

    /**
     * 迭代遍历容器所有元素
     */
    static class MyThreadA implements Runnable {

        @Override
        public void run() {
            Iterator<Integer> iterator = list.iterator();
            while (iterator.hasNext()) {
                int i = iterator.next();
                System.out.println("MyThreadA 访问元素:" + i);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 遍历删除指定范围内的所有偶数
     */
    static class MyThreadB implements Runnable {

        @Override
        public void run() {
            int i = 0;
            while (i < MAX) {
                if (i % 2 == 0) {
                    System.out.println("MyThreadB 删除元素" + i);
                    list.remove(i);
                }
                i++;
            }
        }

    }

}
