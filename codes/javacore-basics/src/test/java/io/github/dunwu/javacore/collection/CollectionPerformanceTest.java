package io.github.dunwu.javacore.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class CollectionPerformanceTest {

    @Test
    @DisplayName("对比 ArrayList 与 Vector 的插入性能")
    public void testVectorAndArrayList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Vector<Integer> vector = new Vector<Integer>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++)
            list.add(i);
        long end = System.currentTimeMillis();
        System.out.println("ArrayList进行200000次插入操作耗时：" + (end - start) + " ms");
        start = System.currentTimeMillis();
        for (int i = 0; i < 200000; i++)
            vector.add(i);
        end = System.currentTimeMillis();
        System.out.println("Vector进行200000次插入操作耗时：" + (end - start) + " ms");
    }

}
