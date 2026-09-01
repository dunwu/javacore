package io.github.dunwu.javacore.jdk10.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Java 10 不可变集合增强示例。
 * <p>
 * Java 10 在 Java 9 集合工厂方法的基础上进一步补充了不可变集合能力：
 * <ul>
 * <li>{@code List.copyOf / Set.copyOf / Map.copyOf}：从已有集合复制生成不可变集合</li>
 * <li>{@code Collectors.toUnmodifiableList / toUnmodifiableSet / toUnmodifiableMap}：
 * Stream 收集为不可变集合</li>
 * <li>{@code Optional.orElseThrow()}：无参版本，值不存在时抛出 {@link java.util.NoSuchElementException}，
 * 等价于 {@code get()}，但语义更清晰</li>
 * </ul>
 */
public class UnmodifiableCollectionDemo {

    /**
     * 示例 1：copyOf——从已有集合复制出不可变副本，Set.copyOf 自动去重
     */
    public static void copyOfMethods() {
        // List.copyOf：从可变集合复制出不可变副本
        List<String> mutableList = new ArrayList<>(List.of("a", "b", "c"));
        List<String> copiedList = List.copyOf(mutableList);
        mutableList.add("d"); // 修改原集合不影响副本
        System.out.println("原集合: " + mutableList);
        System.out.println("copyOf 副本: " + copiedList);

        // Set.copyOf / Map.copyOf
        Set<Integer> copiedSet = Set.copyOf(List.of(1, 2, 2, 3)); // 自动去重
        System.out.println("Set.copyOf 去重: " + copiedSet.stream().sorted().collect(Collectors.toList()));
        Map<String, Integer> copiedMap = Map.copyOf(Map.of("x", 1));
        System.out.println("Map.copyOf: " + copiedMap);
    }

    /**
     * 示例 2：toUnmodifiableList——Stream 收集为不可变集合，修改时抛异常
     */
    public static void toUnmodifiableCollectors() {
        List<Integer> unmodifiable = List.of(1, 2, 3).stream()
            .map(n -> n * 10)
            .collect(Collectors.toUnmodifiableList());
        System.out.println("toUnmodifiableList: " + unmodifiable);
        try {
            unmodifiable.add(40);
        } catch (UnsupportedOperationException e) {
            System.out.println("不可变集合不允许修改，抛出 UnsupportedOperationException");
        }
    }

    /**
     * 示例 3：orElseThrow——无参版本，语义上优于 get()
     */
    public static void optionalOrElseThrow() {
        String value = Optional.of("存在").orElseThrow();
        System.out.println("orElseThrow 取值: " + value);
        try {
            Optional.empty().orElseThrow();
        } catch (java.util.NoSuchElementException e) {
            System.out.println("空 Optional 调用 orElseThrow 抛出 NoSuchElementException");
        }
    }

    public static void main(String[] args) {
        copyOfMethods();
        toUnmodifiableCollectors();
        optionalOrElseThrow();
    }

}
// Output:
// 原集合: [a, b, c, d]
// copyOf 副本: [a, b, c]
// Set.copyOf 去重: [1, 2, 3]
// Map.copyOf: {x=1}
// toUnmodifiableList: [10, 20, 30]
// 不可变集合不允许修改，抛出 UnsupportedOperationException
// orElseThrow 取值: 存在
// 空 Optional 调用 orElseThrow 抛出 NoSuchElementException
