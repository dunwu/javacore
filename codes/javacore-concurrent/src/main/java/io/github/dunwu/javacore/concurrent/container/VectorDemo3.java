package io.github.dunwu.javacore.concurrent.container;

import java.util.Vector;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-27
 */
public class VectorDemo3 {

    static Vector<Integer> vector = new Vector<>();

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }

            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    synchronized (VectorDemo3.class) {   //进行额外的同步
                        for (int i = 0; i < vector.size(); i++) {
                            vector.remove(i);
                        }
                    }
                }
            };

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    synchronized (VectorDemo3.class) {
                        for (int i = 0; i < vector.size(); i++) {
                            vector.get(i);
                        }
                    }
                }
            };

            thread1.start();
            thread2.start();

            while (Thread.activeCount() > 10) {
                System.out.println("同时存在 10 个以上线程，退出");
                return;
            }
        }
    }

}
