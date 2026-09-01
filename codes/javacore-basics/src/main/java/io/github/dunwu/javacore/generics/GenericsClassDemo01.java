package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo01 {

    /**
     * 演示泛型类：同一个 Info 类分别指定 Integer 和 String 类型。
     */
    public static void demo() {
        Info<Integer> info = new Info<>();
        info.setValue(10);
        System.out.println(info.getValue());

        Info<String> info2 = new Info<>();
        info2.setValue("xyz");
        System.out.println(info2.getValue());
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// 10
// xyz
