package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Info;

/**
 * @author Zhang Peng
 * @date 2019-03-20
 */
public class GenericsClassDemo03 {
    public static void main(String[] args) {
        Info info = new Info();
        info.setValue(10);
        System.out.println(info.getValue());
        info.setValue("abc");
        System.out.println(info.getValue());
    }
}
