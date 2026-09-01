package io.github.dunwu.javacore.jdk21.collection;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.SequencedSet;

/**
 * Java 21 顺序集合（Sequenced Collections）示例。
 * <p>
 * Java 21 新增三个接口（JEP 431），统一了"有明确首尾顺序"的集合的操作 API：
 * <ul>
 * <li>{@link SequencedCollection}：List、LinkedHashSet 等实现，提供 getFirst/getLast/addFirst/addLast/reversed</li>
 * <li>{@link SequencedSet}：保序 Set（如 LinkedHashSet）；顺序由元素自身决定的集合（如 SortedSet）
 * 调用 addFirst/addLast 会抛出 {@link UnsupportedOperationException}</li>
 * <li>{@link SequencedMap}：保序 Map（如 LinkedHashMap），提供 firstEntry/lastEntry/putFirst/putLast/reversed</li>
 * </ul>
 * 过去取首尾元素要写 {@code list.get(list.size() - 1)} 之类的样板代码，现在一行搞定。
 */
public class SequencedCollectionDemo {

    /**
     * 示例 1：SequencedCollection——以 ArrayList 为例演示 addFirst/addLast/getFirst/getLast/reversed
     */
    public static void sequencedCollectionApi() {
        List<Integer> list = new ArrayList<>(List.of(2, 3));
        list.addFirst(1);
        list.addLast(4);
        System.out.println("list: " + list);
        System.out.println("getFirst: " + list.getFirst() + ", getLast: " + list.getLast());
        System.out.println("removeFirst: " + list.removeFirst() + ", removeLast: " + list.removeLast());
        System.out.println("reversed 逆序视图: " + list.reversed());
    }

    /**
     * 示例 2：SequencedSet——LinkedHashSet 按插入顺序保序
     */
    public static void sequencedSetApi() {
        SequencedSet<String> set = new LinkedHashSet<>(List.of("b", "c"));
        set.addFirst("a");
        set.addLast("d");
        System.out.println("set: " + set);
        System.out.println("set.getFirst: " + set.getFirst() + ", set.getLast: " + set.getLast());
        System.out.println("set.reversed: " + set.reversed());
    }

    /**
     * 示例 3：SequencedMap——LinkedHashMap 提供 firstEntry/lastEntry/putFirst/putLast/reversed
     */
    public static void sequencedMapApi() {
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("b", 2);
        map.putFirst("a", 1);
        map.putLast("c", 3);
        System.out.println("map: " + map);
        System.out.println("firstEntry: " + map.firstEntry() + ", lastEntry: " + map.lastEntry());
        System.out.println("map.reversed: " + map.reversed());
    }

    /**
     * 示例 4：reversed 返回的是原集合的逆序视图，修改会互相影响
     */
    public static void reversedViewIsLive() {
        List<Integer> list = new ArrayList<>(List.of(2, 3));
        List<Integer> reversedView = list.reversed();
        reversedView.set(0, 99);
        System.out.println("修改逆序视图后，原集合: " + list);
    }

    public static void main(String[] args) {
        sequencedCollectionApi();
        sequencedSetApi();
        sequencedMapApi();
        reversedViewIsLive();
    }

}
// Output:
// list: [1, 2, 3, 4]
// getFirst: 1, getLast: 4
// removeFirst: 1, removeLast: 4
// reversed 逆序视图: [3, 2]
// set: [a, b, c, d]
// set.getFirst: a, set.getLast: d
// set.reversed: [d, c, b, a]
// map: {a=1, b=2, c=3}
// firstEntry: a=1, lastEntry: c=3
// map.reversed: {c=3, b=2, a=1}
// 修改逆序视图后，原集合: [2, 99]
