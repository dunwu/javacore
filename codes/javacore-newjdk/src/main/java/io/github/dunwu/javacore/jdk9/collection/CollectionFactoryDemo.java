package io.github.dunwu.javacore.jdk9.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java 9 集合工厂方法示例。
 * <p>
 * Java 9 为 {@link List}、{@link Set}、{@link Map} 新增了静态工厂方法 {@code of()} 和 {@code ofEntries()}，
 * 可以一行代码创建不可变集合，替代过去冗长的 {@code Collections.unmodifiableList(Arrays.asList(...))} 写法。
 * <p>
 * 要点：
 * <ul>
 * <li>工厂方法创建的集合是不可变的，任何修改操作都会抛出 {@link UnsupportedOperationException}</li>
 * <li>不允许包含 null 元素，否则抛出 {@link NullPointerException}</li>
 * <li>Set 和 Map 不允许重复元素/键，否则抛出 {@link IllegalArgumentException}</li>
 * </ul>
 */
public class CollectionFactoryDemo {

    /**
     * 示例 1：使用 of / ofEntries 一行创建不可变 List、Set、Map
     */
    public static void createImmutableCollections() {
        // 创建不可变 List
        List<String> list = List.of("Java", "Python", "Go");
        System.out.println("List.of: " + list);

        // 创建不可变 Set
        Set<Integer> set = Set.of(1, 2, 3);
        System.out.println("Set.of: " + set);

        // 创建不可变 Map（键值对不超过 10 个时可直接使用 of）
        Map<String, Integer> map = Map.of("张三", 90, "李四", 85);
        System.out.println("Map.of: " + map);

        // 键值对较多时，使用 Map.ofEntries 可读性更好
        Map<String, Integer> entriesMap = Map.ofEntries(
            Map.entry("语文", 100),
            Map.entry("数学", 99),
            Map.entry("英语", 98));
        System.out.println("Map.ofEntries: " + entriesMap);
    }

    /**
     * 示例 2：工厂方法集合的三大限制——不可修改、不允许 null、Set 不允许重复
     */
    public static void immutableRestrictions() {
        List<String> list = List.of("Java", "Python", "Go");

        // 不可变集合不允许修改
        try {
            list.add("Rust");
        } catch (UnsupportedOperationException e) {
            System.out.println("List.of 创建的集合不可修改，抛出 UnsupportedOperationException");
        }

        // 不允许 null 元素
        try {
            List.of("a", null);
        } catch (NullPointerException e) {
            System.out.println("集合工厂方法不允许 null 元素，抛出 NullPointerException");
        }

        // Set 不允许重复元素
        try {
            Set.of(1, 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Set.of 不允许重复元素，抛出 IllegalArgumentException");
        }
    }

    public static void main(String[] args) {
        createImmutableCollections();
        immutableRestrictions();
    }

}
// Output:
// List.of: [Java, Python, Go]
// Set.of: [2, 1, 3]（Set.of / Map.of 不保证遍历顺序）
// Map.of: {张三=90, 李四=85}
// Map.ofEntries: {英语=98, 语文=100, 数学=99}
// List.of 创建的集合不可修改，抛出 UnsupportedOperationException
// 集合工厂方法不允许 null 元素，抛出 NullPointerException
// Set.of 不允许重复元素，抛出 IllegalArgumentException
