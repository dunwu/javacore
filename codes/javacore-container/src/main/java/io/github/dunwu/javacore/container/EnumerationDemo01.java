package io.github.dunwu.javacore.container;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 示例：Enumeration 枚举输出——Vector 的老式遍历方式（对应 Iterator 的 hasMoreElements/nextElement）。
 */
public class EnumerationDemo01 {

    /** 演示用 Enumeration 遍历 Vector。 */
    public static void demo() {
        Vector<String> all = new Vector<String>();
        all.add("hello");
        all.add("_");
        all.add("world");
        Enumeration<String> enu = all.elements();
        while (enu.hasMoreElements()) {    //判断是否有内容，hasNext()
            System.out.print(enu.nextElement() + "、");    // 输出元素：next()
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
