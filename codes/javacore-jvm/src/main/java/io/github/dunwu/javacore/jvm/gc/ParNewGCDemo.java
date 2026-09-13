package io.github.dunwu.javacore.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * parNew 收集器的使用
 * <p>
 * VM Args:
 * <ul>
 * <li>-Xms20m -Xmx40m -Xmn10m -XX:+UseParNewGC -XX:+PrintGCDetails</li>
 * <li>-Xms20m -Xmx40m -XX:+UseParNewGC -XX:+PrintGCDetails</li>
 * </ul>
 * <p>
 * 注意：上述参数只能在 JDK 8 及以前运行。ParNew 收集器已在 JDK 9 中被废弃、JDK 10 中被移除，
 * 在本仓库的编译目标 JDK 21 下，JVM 会直接启动失败（实测）：
 *
 * <pre>
 * Unrecognized VM option 'UseParNewGC'
 * Error: Could not create the Java Virtual Machine.
 * </pre>
 *
 * 另外 -XX:+PrintGCDetails 自 JDK 9 起已废弃，JDK 21 下仍可使用，但会提示改用 -Xlog:gc*。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-06-25
 */
public class ParNewGCDemo {

    /**
     * 占位符
     */
    public byte[] placeHolder = new byte[64 * 1024];

    public static void main(String[] args) throws Exception {
        outOfMemoryByFixSize();
    }

    /**
     * 持续分配对象并持有引用，直至堆内存溢出，用于观察 parNew 在不同新生代配置下的回收行为。
     */
    private static void outOfMemoryByFixSize() throws Exception {
        List<ParNewGCDemo> list = new ArrayList<ParNewGCDemo>();
        while (true) {
            ParNewGCDemo serial = new ParNewGCDemo();
            list.add(serial);
            Thread.sleep(10);
        }
    }

}
