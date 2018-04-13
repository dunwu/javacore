package io.github.dunwu.jvm.chapter03;

import java.util.ArrayList;
import java.util.List;

/**
 * parNew 收集器的使用
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
     * 固定JAVA虚拟机的大小
     * 参数：-Xms30m -Xmx30m -Xmn10m -XX:+UseParNewGC -XX:+PrintGCDetails
     */
    private static void outOfMemoryByFixSize() throws Exception {
        List<ParNewGCDemo> list = new ArrayList<ParNewGCDemo>();
        while (true) {
            ParNewGCDemo serial = new ParNewGCDemo();
            list.add(serial);
            //停顿10毫秒
            Thread.sleep(10);
        }
    }

    /**
     * JAVA虚拟机的大小适当可扩展，其中Xms30m，Xmx40m
     * 参数：-Xms30m -Xmx40m -XX:+UseParNewGC -XX:+PrintGCDetails
     */
    private static void outOfMemoryByExpansionSize() throws Exception {

        List<ParNewGCDemo> list = new ArrayList<ParNewGCDemo>();

        while (true) {
            ParNewGCDemo serial = new ParNewGCDemo();
            list.add(serial);
            //停顿10毫秒
            Thread.sleep(10);
        }
    }
}
