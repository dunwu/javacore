package io.github.dunwu.javacore.concurrent.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * add 方法使用 synchronized，但是比較 a 和 b 时，发现仍旧不相等
 */
@Slf4j
public class synchronized使用范围不当 {

    public static void main(String[] args) {
        Interesting demo = new Interesting();
        testWrong();
        testRight();
    }

    static void testWrong() {
        Interesting demo = new Interesting();
        new Thread(() -> demo.add()).start();
        new Thread(() -> demo.compare()).start();
    }

    static void testRight() {
        Interesting demo = new Interesting();
        new Thread(() -> demo.add()).start();
        new Thread(() -> demo.compare()).start();
    }

    private static class Interesting {

        volatile int a = 1;
        volatile int b = 1;

        public synchronized void add() {
            log.info("add start");
            for (int i = 0; i < 10000; i++) {
                a++;
                b++;
            }
            log.info("add done");
        }

        public void compare() {
            log.info("compare start");
            for (int i = 0; i < 10000; i++) {
                //a始终等于b吗？
                if (a < b) {
                    log.info("a:{},b:{},{}", a, b, a > b);
                    //最后的a>b应该始终是false吗？
                }
            }
            log.info("compare done");
        }

        public synchronized void compareRight() {
            log.info("compare start");
            for (int i = 0; i < 10000; i++) {
                //a始终等于b吗？
                if (a < b) {
                    log.info("a:{},b:{},{}", a, b, a > b);
                    //最后的a>b应该始终是false吗？
                }
            }
            log.info("compare done");
        }

    }

}
