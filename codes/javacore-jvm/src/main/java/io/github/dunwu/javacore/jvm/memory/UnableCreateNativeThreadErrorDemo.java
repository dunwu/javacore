package io.github.dunwu.javacore.jvm.memory;

import java.util.concurrent.TimeUnit;

/**
 * Unable to create new native thread 示例
 * <p>
 * 错误：java.lang.OutOfMemoryError: Unable to create new native thread
 * <p>
 * 注意：可能导致系统崩溃，执行请慎重
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-08
 */
public class UnableCreateNativeThreadErrorDemo {

    public static void main(String[] args) {
        while (true) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.MINUTES.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
