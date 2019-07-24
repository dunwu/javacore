package io.github.dunwu.jvm.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * 常量池内存溢出示例
 * <p>
 * VM Args:
 * <ul>
 * <li>(JDK8 以前)-XX:PermSize=10m -XX:MaxPermSize=10m</li>
 * <li>(JDK8 及以后)-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class ConstantPoolOOM {
    public static void main(String[] args) {
        // 使用List保持着常量池引用，避免Full GC回收常量池行为
        List<String> list = new ArrayList<String>();
        // 10MB的PermSize在integer范围内足够产生OOM了
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++)
                           .intern());
        }
    }
}
