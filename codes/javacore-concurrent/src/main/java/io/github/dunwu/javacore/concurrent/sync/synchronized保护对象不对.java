package io.github.dunwu.javacore.concurrent.sync;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-07-31
 */
@Slf4j
public class synchronized保护对象不对 {

    public static void main(String[] args) {
        synchronized保护对象不对 demo = new synchronized保护对象不对();
        System.out.println(demo.wrong(1000000));
        System.out.println(demo.right(1000000));
    }

    public int wrong(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().wrong());
        return Data.getCounter();
    }

    public int right(int count) {
        Data.reset();
        IntStream.rangeClosed(1, count).parallel().forEach(i -> new Data().right());
        return Data.getCounter();
    }

    private static class Data {

        @Getter
        private static int counter = 0;
        private static Object locker = new Object();

        public static int reset() {
            counter = 0;
            return counter;
        }

        public synchronized void wrong() {
            counter++;
        }

        public void right() {
            synchronized (locker) {
                counter++;
            }
        }

    }

}
