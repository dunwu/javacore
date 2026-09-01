package io.github.dunwu.javacore.container.list;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 示例：Collections.emptyList / emptySet 返回的是不可变空集合。
 * <p>注意：本例故意演示错误用法 —— 向空集合 add 会抛出 {@link UnsupportedOperationException}。
 */
public class CollectionsDemo01 {

    public static void main(String[] args) {
        List<String> allList = Collections.emptyList();
        Set<String> allSet = Collections.emptySet();
        allList.add("Hello");    // 加入数据
    }

}
