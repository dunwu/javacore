package io.github.dunwu.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆溢出示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * <p>
 * VM Args：-verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class HeapOutOfMemoryDemo01 {
    static class OomObject {}

    public static void main(String[] args) {
        List<OomObject> list = new ArrayList<>();
        while (true) {
            list.add(new OomObject());
        }
    }
}
