package io.github.dunwu.javacore.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * parNew 收集器的使用
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
     * JAVA虚拟机的大小适当可扩展
     * <p>
     * 参数：
     * <ul>
     * <li>-Xms20m -Xmx40m -Xmn10m -XX:+UseParNewGC -XX:+PrintGCDetails</li>
     * <li>-Xms20m -Xmx40m -XX:+UseParNewGC -XX:+PrintGCDetails</li>
     * </ul>
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
