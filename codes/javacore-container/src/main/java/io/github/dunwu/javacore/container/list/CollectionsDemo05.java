package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 示例：Collections.replaceAll 将集合中指定元素全部替换为新值。
 */
public class CollectionsDemo05 {

    /** 演示 Collections.replaceAll 替换元素。 */
    public static void demo() {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        if (Collections.replaceAll(all, "LXH", "李兴华")) {// 替换内容
            System.out.println("内容替换成功！");
        }
        System.out.print("替换之后的结果：");
        System.out.print(all);
    }

    public static void main(String[] args) {
        demo();
    }

}
