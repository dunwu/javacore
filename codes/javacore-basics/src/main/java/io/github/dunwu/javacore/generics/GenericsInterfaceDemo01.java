package io.github.dunwu.javacore.generics;

import io.github.dunwu.javacore.generics.entity.Content;

public class GenericsInterfaceDemo01 implements Content<Integer> {

    private int text;

    public GenericsInterfaceDemo01(int text) {
        this.text = text;
    }

    /**
     * 演示实现泛型接口时指定具体类型：Content<Integer>。
     */
    public static void demo() {
        GenericsInterfaceDemo01 demo = new GenericsInterfaceDemo01(10);
        System.out.print(demo.text());
    }

    public static void main(String[] args) {
        demo();
    }

    @Override
    public Integer text() {
        return text;
    }

}
// Output:
// 10
