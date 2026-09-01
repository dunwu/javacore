package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;
import io.github.dunwu.javacore.generics.entity.MyMap;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo03 {

    /**
     * 演示泛型嵌套：泛型类的类型参数可以是另一个泛型类。
     */
    public static void demo() {
        Info<String> info = new Info("Hello");
        MyMap<Integer, Info<String>> map = new MyMap<>(1, info);
        System.out.println(map);
    }

    public static void main(String[] args) {
        demo();
    }

}
// Output:
// MyMap{key=1, value=Info{value=Hello}}
