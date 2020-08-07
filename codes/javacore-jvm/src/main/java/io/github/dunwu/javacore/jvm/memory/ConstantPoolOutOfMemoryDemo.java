package io.github.dunwu.javacore.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行时常量池内存溢出示例
 * <p>
 * VM Args:
 * <ul>
 * <li>(JDK8 以前)-XX:PermSize=10m -XX:MaxPermSize=10m</li>
 * <li>(JDK8 及以后)-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m</li>
 * </ul>
 * <p>
 * Linux Test Cli: java -verbose:gc -XX:PermSize=10m -XX:MaxPermSize=10m -cp target/javacore-jvm-1.0.1.jar io.github.dunwu.javacore.jvm.memory.ConstantPoolOutOfMemoryDemo
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class ConstantPoolOutOfMemoryDemo {

    public static void main(String[] args) {
        // 使用List保持着常量池引用，避免Full GC回收常量池行为
        List<String> list = new ArrayList<String>();
        // 10MB的PermSize在integer范围内足够产生OOM了
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }

}
