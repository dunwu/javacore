package io.github.dunwu.javacore.jvm.memory;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 本机直接内存溢出示例
 * <p>
 * 错误现象：java.lang.OutOfMemoryError
 * <p>
 * VM Args：-verbose:gc -Xmx20M -XX:MaxDirectMemorySize=10M
 * <p>
 * Linux Test Cli: nohup java -verbose:gc -Xmx20M -XX:MaxDirectMemorySize=10M -classpath "target/javacore-jvm-1.0.1.jar:target/lib/*"
 * io.github.dunwu.javacore.jvm.memory.DirectOutOfMemoryDemo >> output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class DirectOutOfMemoryDemo {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }

}
