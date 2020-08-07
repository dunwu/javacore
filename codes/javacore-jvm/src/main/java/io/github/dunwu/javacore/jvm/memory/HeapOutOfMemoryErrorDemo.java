package io.github.dunwu.javacore.jvm.memory;

/**
 * 堆溢出示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError: Java heap space
 * <p>
 * VM Args：-verbose:gc -Xms10M -Xmx10M
 * <p>
 * Linux Test Cli: java -verbose:gc -Xms10M -Xmx10M -cp target/javacore-jvm-1.0.1.jar io.github.dunwu.javacore.jvm.memory.HeapOutOfMemoryErrorDemo
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class HeapOutOfMemoryErrorDemo {

    public static void main(String[] args) {
        Double[] array = new Double[999999999];
        System.out.println("array length = [" + array.length + "]");
    }

}
