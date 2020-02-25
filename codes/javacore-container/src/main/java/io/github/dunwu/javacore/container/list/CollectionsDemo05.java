package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsDemo05 {

    public static void main(String[] args) {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        if (Collections.replaceAll(all, "LXH", "李兴华")) {// 替换内容
            System.out.println("内容替换成功！");
        }
        System.out.print("替换之后的结果：");
        System.out.print(all);
    }

}
