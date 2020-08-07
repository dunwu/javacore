package io.github.dunwu.javacore.jvm.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Linux Test Cli: nohup java -verbose:gc -Xms10M -Xmx10M -XX:+HeapDumpOnOutOfMemoryError -classpath
 * "target/javacore-jvm-1.0.1.jar:target/lib/*" io.github.dunwu.javacore.jvm.memory.HeapMemoryLeakMemoryErrorDemo2 >>
 * output.log 2>&1 &
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2020-03-09
 */
public class HeapMemoryLeakMemoryErrorDemo2 {

    public static void main(String[] args) {
        HeapMemoryLeakMemoryErrorDemo2 demo = new HeapMemoryLeakMemoryErrorDemo2();
        System.out.println("往ArrayList中加入30w内容");
        demo.javaHeapSpace(300000);
        demo.memoryTotal();
        System.out.println("往ArrayList中加入40w内容");
        demo.javaHeapSpace(400000);
        demo.memoryTotal();
    }

    public void javaHeapSpace(Integer sum) {
        Random random = new Random();
        List<String> list = new ArrayList();
        for (int i = 0; i < sum; i++) {
            String str = String.valueOf(random.nextInt(10));
            list.add(str);
        }
    }

    public void memoryTotal() {
        Runtime run = Runtime.getRuntime();
        long max = run.maxMemory();
        long total = run.totalMemory();
        long free = run.freeMemory();
        long usable = max - total + free;
        System.out.println("最大内存 = " + max);
        System.out.println("已分配内存 = " + total);
        System.out.println("已分配内存中的剩余空间 = " + free);
        System.out.println("最大可用内存 = " + usable);
    }

}
