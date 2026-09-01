package io.github.dunwu.javacore.jdk10.var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Java 10 局部变量类型推断（var）示例。
 * <p>
 * Java 10 引入 {@code var} 关键字，允许编译器根据初始化表达式自动推断局部变量类型，
 * 减少冗长的类型声明，提升可读性。
 * <p>
 * 适用场景：
 * <ul>
 * <li>局部变量声明并初始化</li>
 * <li>for 循环、增强 for 循环的循环变量</li>
 * <li>try-with-resources 的资源变量</li>
 * </ul>
 * 注意：var 不是关键字，而是保留类型名；变量仍然拥有静态类型，只是由编译器推断。
 */
public class VarDemo {

    /**
     * 示例 1：局部变量声明——编译器自动推断 String、集合等类型
     */
    public static void localVarInference() {
        // 局部变量声明：编译器推断为 String
        var message = "Hello, Java 10";
        System.out.println(message);

        // 推断为 ArrayList<String>
        var list = new ArrayList<String>();
        list.add("Java");
        list.add("Kotlin");
        System.out.println("list 类型: " + list.getClass().getSimpleName());

        // 推断为 Map<String, Integer>
        var map = new LinkedHashMap<String, Integer>();
        map.put("一", 1);
        map.put("二", 2);
        System.out.println("map: " + map);
    }

    /**
     * 示例 2：增强 for 循环与传统 for 循环中使用 var
     */
    public static void varInLoops() {
        var map = new LinkedHashMap<String, Integer>();
        map.put("一", 1);
        map.put("二", 2);

        // 增强 for 循环中使用 var
        for (var entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // 传统 for 循环中使用 var
        for (var i = 0; i < 3; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    /**
     * 示例 3：try-with-resources 与泛型方法中使用 var
     */
    public static void varInTryWithResources() throws IOException {
        // try-with-resources 中使用 var
        try (var reader = new BufferedReader(new StringReader("try-with-resources 中的 var"))) {
            System.out.println(reader.readLine());
        }

        // 配合泛型方法使用，推断出完整泛型类型
        var numbers = pick(1, 2, 3);
        System.out.println("numbers 类型: " + numbers.getClass().getSimpleName() + ", 内容: " + numbers);
    }

    private static <T> List<T> pick(T... items) {
        return new ArrayList<>(List.of(items));
    }

    public static void main(String[] args) throws IOException {
        localVarInference();
        varInLoops();
        varInTryWithResources();
    }

}
// Output:
// Hello, Java 10
// list 类型: ArrayList
// map: {一=1, 二=2}
// 一 -> 1
// 二 -> 2
// 0 1 2
// try-with-resources 中的 var
// numbers 类型: ArrayList, 内容: [1, 2, 3]
