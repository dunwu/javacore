package io.github.dunwu.javacore.collection;

import java.util.Vector;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class VectorDemo {

    static Vector<Integer> vector = new Vector<Integer>();

    public static void main(String[] args) {
        while (true) {
            for (int i = 0; i < 10; i++) {
                vector.add(i);
            }
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }
            };
            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.get(i);
                    }
                }
            };
            thread1.start();
            thread2.start();
            while (Thread.activeCount() > 10) {

            }
        }
    }

}
