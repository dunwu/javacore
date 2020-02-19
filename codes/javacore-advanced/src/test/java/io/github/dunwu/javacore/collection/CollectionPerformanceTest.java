package io.github.dunwu.javacore.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class CollectionPerformanceTest {

    @Test
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
