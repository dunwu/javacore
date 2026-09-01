package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;

/**
 * 泛型中无法向上转型
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo04 {

    /**
     * 演示泛型不支持向上转型：Info<String> 不能赋值给 Info<Object>（放开注释即编译报错）。
     */
    public static void demo() {
        Info<String> info1 = new Info<>();
        // 放开下面注释会报错
        // Info<Object> info2 = info1; // 试图将 Info<String> 转为 Info<Object> 会报错
    }

    public static void main(String[] args) {
        demo();
    }

}
