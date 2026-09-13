package io.github.dunwu.javacore.jvm.gc;

/**
 * 空间分配担保（Handle Promotion Failure）示例。
 * <p>
 * VM Args: -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure
 * <p>
 * 注意：上述参数只能在 JDK 8 及以前运行。在本仓库的编译目标 JDK 21 下，
 * -XX:-HandlePromotionFailure 已不存在，JVM 会直接启动失败（实测）：
 *
 * <pre>
 * Unrecognized VM option 'HandlePromotionFailure'
 * Did you mean '(+/-)PromotionFailureALot'?
 * Error: Could not create the Java Virtual Machine.
 * </pre>
 *
 * 另外 -XX:+PrintGCDetails 自 JDK 9 起已废弃，JDK 21 下仍可使用，但会提示改用 -Xlog:gc*。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018-04-13
 */
public class HandlePromotionFailureDemo {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6, allocation7;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        allocation1 = null;
        allocation4 = new byte[2 * _1MB];
        allocation5 = new byte[2 * _1MB];
        allocation6 = new byte[2 * _1MB];
        allocation4 = null;
        allocation5 = null;
        allocation6 = null;
        allocation7 = new byte[2 * _1MB];
    }

}
