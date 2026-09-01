package io.github.dunwu.javacore.reflect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-26
 */
public class InstanceofDemo {

    /**
     * 演示 instanceof：判断对象是否属于某个类型。
     */
    public static void demo() {
        ArrayList arrayList = new ArrayList();
        if (arrayList instanceof List) {
            System.out.println("ArrayList is List");
        }
        if (arrayList instanceof List) {
            System.out.println("ArrayList is List");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// ArrayList is List
// ArrayList is List
