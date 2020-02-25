package io.github.dunwu.javacore.container.list;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Collection 示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-12-29
 */
public class CollectionDemo2 {

    public static void main(String[] args) {
        // 指定一个集合
        Collection<String> collection = new ArrayList<String>();
        // 添加元素
        collection.add("12345");
        collection.add("abced");
        System.out.println(collection);
    }

}
