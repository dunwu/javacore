package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例：foreach（增强 for 循环）遍历 List。
 */
public class ForeachDemo01 {

    /** 演示 foreach 遍历 List。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();
        all.add("hello");
        all.add("_");
        all.add("world");
        for (String str : all) {
            System.out.print(str + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
