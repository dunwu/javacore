package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.MyMap;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo02 {

    /**
     * 演示多参数泛型类：MyMap 同时指定 key 和 value 的类型。
     */
    public static void demo() {
        MyMap<Integer, String> map = new MyMap<>(1, "one");
        System.out.println(map);
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// MyMap{key=1, value=one}
