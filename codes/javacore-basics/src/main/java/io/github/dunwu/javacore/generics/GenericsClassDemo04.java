package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;

/**
 * 泛型中无法向上转型
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-03-20
 */
public class GenericsClassDemo04 {

    public static void main(String[] args) {
        Info<String> info1 = new Info<>();
        // 放开下面注释会报错
        // Info<Object> info2 = info1; // 试图将 Info<String> 转为 Info<Object> 会报错
    }

}
