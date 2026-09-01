package io.github.dunwu.javacore.jdk8.methodref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Java 8 方法引用（Method Reference）示例。
 * <p>
 * 方法引用是 Lambda 的进一步简写：当 lambda 体只是调用某个已有方法时，
 * 可以直接用 {@code ::} 引用该方法，可读性更好。共四种形式：
 * <ul>
 * <li>类名::静态方法，如 {@code Integer::parseInt}</li>
 * <li>实例::实例方法（绑定对象），如 {@code System.out::println}</li>
 * <li>类名::实例方法（未绑定，第一个参数作为调用者），如 {@code String::length}</li>
 * <li>类名::new（构造器引用），如 {@code ArrayList::new}；数组引用 {@code int[]::new}</li>
 * </ul>
 */
public class MethodReferenceDemo {

    /**
     * 示例 1：静态方法引用与绑定对象的实例方法引用
     */
    public static void staticAndBoundReference() {
        // 1. 静态方法引用：类名::静态方法
        Function<String, Integer> parseInt = Integer::parseInt;
        System.out.println("静态方法引用: " + parseInt.apply("123"));

        // 2. 绑定对象的实例方法引用：实例::方法
        java.util.function.Consumer<String> print = System.out::println;
        print.accept("绑定对象的实例方法引用");
    }

    /**
     * 示例 2：未绑定的实例方法引用（类名::实例方法，lambda 第一个参数成为调用者）
     */
    public static void unboundReference() {
        Function<String, Integer> length = String::length;
        System.out.println("类::实例方法: " + length.apply("method reference"));
    }

    /**
     * 示例 3：构造器引用（类名::new）与数组引用（int[]::new）
     */
    public static void constructorReference() {
        Supplier<ArrayList<String>> listFactory = ArrayList::new;
        IntFunction<int[]> arrayFactory = int[]::new;
        List<String> list = listFactory.get();
        list.add("构造器引用创建的对象");
        int[] array = arrayFactory.apply(5);
        System.out.println("构造器引用: list.size=" + list.size() + ", array.length=" + array.length);
    }

    /**
     * 示例 4：方法引用与 lambda 的等价关系
     */
    public static void lambdaEquivalence() {
        List<String> words = Arrays.asList("c", "a", "b");
        List<String> upper1 = new ArrayList<>();
        words.forEach(w -> upper1.add(w.toUpperCase())); // lambda
        List<String> upper2 = new ArrayList<>();
        words.stream().map(String::toUpperCase).forEach(upper2::add); // 方法引用
        System.out.println("两种写法结果一致: " + upper1.equals(upper2));
    }

    public static void main(String[] args) {
        staticAndBoundReference();
        unboundReference();
        constructorReference();
        lambdaEquivalence();
    }

}
// Output:
// 静态方法引用: 123
// 绑定对象的实例方法引用
// 类::实例方法: 16
// 构造器引用: list.size=1, array.length=5
// 两种写法结果一致: true
