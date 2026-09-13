package io.github.dunwu.javacore.concurrent.sync;

/**
 * 演示 synchronized 只覆盖了部分临界区导致的问题：{@code add()} 是同步的，但 {@code compare()} 不是，
 * 因此 compare 线程仍可能观察到 {@code a < b} 的中间状态。
 * <p>
 * {@code compareRight()} 同样加上 synchronized，与 {@code add()} 共用同一把对象锁，
 * 两者互斥后就不会再读到自增过程中的中间状态。
 * <p>
 * 注：当前 {@code main} 调用的 {@code testWrong()} 与 {@code testRight()} 方法体完全相同，两者都只调用了
 * {@code compare()}，因此 {@code compareRight()} 实际并未被执行（疑似复制粘贴遗漏）。
 * <p>
 * 注：{@code compare()} 里的 {@code a < b} 并非永不成立：虽然 {@code add()} 先自增 a 再自增 b，但
 * {@code compare()} 对 a、b 的两次 volatile 读并不是原子的——读完 a、尚未读 b 的窗口内若 add 线程插入
 * 一次完整自增，就会出现 {@code a < b}。但该窗口极窄，JDK 21 实测两次都只输出了 8 行 start/done，
 * 没有任何 {@code a:...,b:...} 行。
 * <p>
 * 注：两个线程的输出顺序依赖线程调度，每次运行结果都不同，不可作为断言依据。
 */
public class SynchronizedScopePitfallDemo {

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
            System.out.println("add start");
            for (int i = 0; i < 10000; i++) {
                a++;
                b++;
            }
            System.out.println("add done");
        }

        public void compare() {
            System.out.println("compare start");
            for (int i = 0; i < 10000; i++) {
                //a始终等于b吗？
                if (a < b) {
                    System.out.println("a:" + a + ",b:" + b + "," + (a > b));
                    //最后的a>b应该始终是false吗？
                }
            }
            System.out.println("compare done");
        }

        public synchronized void compareRight() {
            System.out.println("compare start");
            for (int i = 0; i < 10000; i++) {
                //a始终等于b吗？
                if (a < b) {
                    System.out.println("a:" + a + ",b:" + b + "," + (a > b));
                    //最后的a>b应该始终是false吗？
                }
            }
            System.out.println("compare done");
        }

    }

}
