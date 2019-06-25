package io.github.dunwu.jvm.memory;

/**
 * 栈
 *
 * VM Args: -Xss512k
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOOM {

    private volatile int count;

    private void dontStop() {
        while (true) {
            System.out.println(++count);
        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(() -> dontStop());
            thread.start();
        }
    }

    public static void main(String[] args) {
        StackOOM stackOOM = new StackOOM();
        stackOOM.stackLeakByThread();
    }
}
