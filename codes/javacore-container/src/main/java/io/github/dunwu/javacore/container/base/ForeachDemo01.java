package io.github.dunwu.javacore.container.base;

import java.util.ArrayList;
import java.util.List;

public class ForeachDemo01 {

    public static void main(String[] args) {
        List<String> all = new ArrayList<String>();
        all.add("hello");
        all.add("_");
        all.add("world");
        for (String str : all) {
            System.out.print(str + "、");
        }
    }

}
