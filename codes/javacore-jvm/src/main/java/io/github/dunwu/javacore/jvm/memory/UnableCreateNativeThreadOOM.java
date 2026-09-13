package io.github.dunwu.javacore.jvm.memory;

import java.util.concurrent.TimeUnit;

/**
 * Unable to create new native thread 示例
 * <p>
 * 错误：java.lang.OutOfMemoryError: Unable to create new native thread
 * <p>
 * VM Args: 无需任何 VM 参数即可复现。能创建多少线程取决于操作系统对进程线程数的限制
 * （Linux 下受 kernel.threads-max、ulimit -u 等约束）与可用本地内存，因此触发阈值高度
 * 依赖具体环境，不要假定一个固定的线程数。
 * <p>
 * 注意：可能导致系统崩溃，执行请慎重。本示例不会自行终止，需在独立终端运行以便随时杀掉进程。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-08
 */
public class UnableCreateNativeThreadOOM {

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
