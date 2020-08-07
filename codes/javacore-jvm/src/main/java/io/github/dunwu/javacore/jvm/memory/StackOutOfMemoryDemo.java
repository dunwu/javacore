package io.github.dunwu.javacore.jvm.memory;

/**
 * 创建线程导致内存溢出（执行要慎重）
 * <p>
 * VM Args: -Xss512k
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -Xss512k -cp target/javacore-jvm-1.0.1.jar io.github.dunwu.javacore.jvm.memory.StackOutOfMemoryDemo >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class StackOutOfMemoryDemo {

    private volatile int count;

    public static void main(String[] args) {
        StackOutOfMemoryDemo stackOutOfMemoryDemo = new StackOutOfMemoryDemo();
        stackOutOfMemoryDemo.stackLeakByThread();
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    StackOutOfMemoryDemo.this.dontStop();
                }
            });
            thread.start();
        }
    }

    private void dontStop() {
        while (true) {
            System.out.println(++count);
        }
    }

}
