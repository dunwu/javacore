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
        List<Double> list = new ArrayList<>();
        int i = 0;
        while (true) {
//            list.add(0.0);
            list.add(new Double(0.0));
//            list.add(String.valueOf(i++));
        }
    }
}
