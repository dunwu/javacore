package io.github.dunwu.javacore.jdk16.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 16 Stream.toList() 示例。
 * <p>
 * Java 16 为 {@link Stream} 新增了 {@code toList()} 终结方法，
 * 替代最常见的 {@code collect(Collectors.toList())} 写法。
 * <p>
 * 关键差异：
 * <ul>
 * <li>{@code Stream.toList()} 返回<b>不可变</b>列表（unmodifiable），修改会抛 {@link UnsupportedOperationException}</li>
 * <li>{@code Collectors.toList()} 当前实现返回 ArrayList（可变），但规范上不保证具体类型</li>
 * </ul>
 */
public class StreamToListDemo {

    /**
     * 示例 1：新写法 Stream.toList() 返回不可变列表
     */
    public static void streamToList() {
        List<Integer> unmodifiable = Stream.of(1, 2, 3).map(n -> n * 2).toList();
        System.out.println("toList 结果: " + unmodifiable);
        try {
            unmodifiable.add(4);
        } catch (UnsupportedOperationException e) {
            System.out.println("Stream.toList() 返回不可变列表，修改抛出 UnsupportedOperationException");
        }
    }

    /**
     * 示例 2：旧写法 Collectors.toList() 当前实现为可变 ArrayList
     */
    public static void collectorsToList() {
        List<Integer> mutable = Stream.of(1, 2, 3).map(n -> n * 2).collect(Collectors.toList());
        mutable.add(4);
        System.out.println("Collectors.toList() 可修改: " + mutable);
    }

    /**
     * 示例 3：需要可变列表时，可继续使用 Collectors.toCollection(ArrayList::new)
     */
    public static void toCollectionArrayList() {
        List<Integer> arrayList = Stream.of(1, 2).collect(Collectors.toCollection(ArrayList::new));
        arrayList.add(3);
        System.out.println("toCollection(ArrayList::new): " + arrayList);
    }

    public static void main(String[] args) {
        streamToList();
        collectorsToList();
        toCollectionArrayList();
    }

}
// Output:
// toList 结果: [2, 4, 6]
// Stream.toList() 返回不可变列表，修改抛出 UnsupportedOperationException
// Collectors.toList() 可修改: [2, 4, 6, 4]
// toCollection(ArrayList::new): [1, 2, 3]
