package io.github.dunwu.javacore.container.map;

import java.util.Iterator;
import java.util.TreeMap;

public class TreeMapDemo {

    private static final String[] chars = "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z".split(" ");

    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (int i = 0; i < chars.length; i++) {
            treeMap.put(i, chars[i]);
        }
        System.out.println(treeMap);
        Integer low = treeMap.firstKey();
        Integer high = treeMap.lastKey();
        System.out.println(low);
        System.out.println(high);
        Iterator<Integer> it = treeMap.keySet().iterator();
        for (int i = 0; i <= 6; i++) {
            if (i == 3) {
                low = it.next();
            }
            if (i == 6) {
                high = it.next();
            } else {
                it.next();
            }
        }
        System.out.println(low);
        System.out.println(high);
        System.out.println(treeMap.subMap(low, high));
        System.out.println(treeMap.headMap(high));
        System.out.println(treeMap.tailMap(low));
    }

}
