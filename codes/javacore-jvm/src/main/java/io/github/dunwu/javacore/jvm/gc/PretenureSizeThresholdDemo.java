package io.github.dunwu.javacore.jvm.gc;

/**
 * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/13
 */
public class PretenureSizeThresholdDemo {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation;
        // 直接分配在老年代中
        allocation = new byte[4 * _1MB];
    }

}
