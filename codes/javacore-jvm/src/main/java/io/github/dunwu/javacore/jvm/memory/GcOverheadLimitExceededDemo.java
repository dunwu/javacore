package io.github.dunwu.javacore.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * GC overhead limit exceeded 示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: GC overhead limit exceeded
 * <p>
 * 发生在GC占用大量时间为释放很小空间的时候发生的，是一种保护机制。导致异常的原因：一般是因为堆太小，没有足够的内存。 
 * <p>
 * 官方对此的定义：超过98%的时间用来做GC并且回收了不到2%的堆内存时会抛出此异常。
 * <p>
 * VM Args: -Xms10M -Xmx10M
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError -classpath
 * "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.GcOverheadLimitExceededDemo >>
 * output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class GcOverheadLimitExceededDemo {

    public static void main(String[] args) {
        List<Double> list = new ArrayList<>();
        double d = 0.0;
        while (true) {
            list.add(d++);
        }
    }

}
