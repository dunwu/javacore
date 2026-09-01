package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.List;

/**
 * 示例：ArrayList 遍历 —— 通过下标由前向后、由后向前输出。
 */
public class ArrayListDemo03 {

    /** 演示通过下标双向遍历 ArrayList。 */
    public static void demo() {
        List<String> allList = null;
        allList = new ArrayList<String>();    // 指定操作的泛型为String
        allList.add("Hello");    // 此方法由Collection接口而来
        allList.add("Hello");    // 此方法由Collection接口而来
        allList.add(0, "World");    // 在第一个位置上添加新的内容
        allList.add("MLDN");    // 向Collection中加入内容
        allList.add("www.mldn.cn");
        System.out.print("由前向后输出：");
        for (int i = 0; i < allList.size(); i++) {
            System.out.print(allList.get(i) + "、");
        }
        System.out.print("\n由后向前输出：");
        for (int i = allList.size() - 1; i >= 0; i--) {
            System.out.print(allList.get(i) + "、");
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
