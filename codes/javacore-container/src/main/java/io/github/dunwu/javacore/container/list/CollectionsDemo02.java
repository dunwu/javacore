package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CollectionsDemo02 {

    public static void main(String[] args) {
        List<String> all = new ArrayList<String>();    // 返回空的 List集合
        Collections.addAll(all, "MLDN", "LXH", "mldnjava");
        Iterator<String> iter = all.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + "、");
        }
    }

}
