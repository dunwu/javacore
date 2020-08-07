package io.github.dunwu.javacore.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 内存泄漏示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * <p>
 * VM Args：-verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError -classpath
 * "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.HeapMemoryLeakMemoryErrorDemo >>
 * output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class HeapMemoryLeakMemoryErrorDemo {

    public static void main(String[] args) {
        List<OomObject> list = new ArrayList<>();
        while (true) {
            list.add(new OomObject());
        }
    }

    static class OomObject {}

}
