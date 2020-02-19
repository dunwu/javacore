package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;
import io.github.dunwu.javacore.generics.entity.MyMap;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo03 {

    public static void main(String[] args) {
        Info<String> info = new Info("Hello");
        MyMap<Integer, Info<String>> map = new MyMap<>(1, info);
        System.out.println(map);
    }

}
// Output:
// MyMap{key=1, value=Info{value=Hello}}
