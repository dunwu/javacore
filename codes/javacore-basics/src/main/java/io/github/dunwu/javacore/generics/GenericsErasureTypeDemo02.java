package io.github.dunwu.javacore.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型的类型擦除
 */
public class GenericsErasureTypeDemo02 {

    /**
     * 演示类型擦除的编译期约束：List<Integer> 不能赋值给 List<Object>（放开注释即编译报错）。
     */
    public static void demo() {
        List<Integer> list = new ArrayList<>();
        // List<Object> list2 = list; // Erorr
    }

    public static void main(String[] args) {
        demo();
    }

}
// 无运行时输出。
//
// 本示例演示的是编译期约束：类型擦除后 List<Integer> 与 List<Object> 的运行时类型相同，但编译器
// 仍禁止把 List<Integer> 直接赋给 List<Object>（放开 demo() 里的注释即编译报错）。demo() 内没有任何
// 打印语句，JDK 21 实测 stdout 与 stderr 均为空、退出码 0。
// （此前这里错放了兄弟类 GenericsErasureTypeDemo 的输出：那个类才真的调用 getClass() 并打印
// 两行 class java.util.ArrayList）
