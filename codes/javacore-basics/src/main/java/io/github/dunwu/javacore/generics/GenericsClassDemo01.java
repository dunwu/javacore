package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo01 {

    public static void main(String[] args) {
        Info<Integer> info = new Info<>();
        info.setValue(10);
        System.out.println(info.getValue());

        Info<String> info2 = new Info<>();
        info2.setValue("xyz");
        System.out.println(info2.getValue());
    }

}
// Output:
// 10
// xyz
