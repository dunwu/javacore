package io.github.dunwu.javacore.jdk8.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Java 8 Map 接口新增方法示例。
 * <p>
 * Java 8 借助接口的默认方法给 {@link Map} 补充了大量实用操作，
 * 替代了过去"判空 + get + put"的样板代码：
 * <ul>
 * <li>{@code getOrDefault}：取不到 key 时返回默认值</li>
 * <li>{@code putIfAbsent}：key 不存在（或值为 null）时才放入</li>
 * <li>{@code compute / computeIfAbsent / computeIfPresent}：按旧值计算新值</li>
 * <li>{@code merge}：合并新旧值（常用于计数、聚合）</li>
 * <li>{@code replace / replaceAll / remove(key, value) / forEach}</li>
 * </ul>
 */
public class MapNewApiDemo {

    /**
     * 示例 1：getOrDefault 与 putIfAbsent（已存在的 key 不会被覆盖）
     */
    public static void getOrDefaultAndPutIfAbsent() {
        Map<String, Integer> scores = sampleScores();
        // getOrDefault：key 不存在时返回默认值
        System.out.println("getOrDefault: " + scores.getOrDefault("王五", 0));
        scores.putIfAbsent("张三", 100);
        scores.putIfAbsent("王五", 60);
        System.out.println("putIfAbsent 后: " + scores);
    }

    /**
     * 示例 2：computeIfAbsent（常用于多级缓存）与 compute 根据旧值计算新值
     */
    public static void computeMethods() {
        Map<String, Integer> scores = sampleScores();
        scores.putIfAbsent("王五", 60);
        // computeIfAbsent：key 不存在时用函数计算并放入
        scores.computeIfAbsent("赵六", k -> 66);
        System.out.println("computeIfAbsent 后: " + scores);
        scores.compute("张三", (name, score) -> score + 5);
        System.out.println("compute 张三加分后: " + scores.get("张三"));
    }

    /**
     * 示例 3：merge 经典词频统计写法，key 不存在放初始值，存在则用函数合并；
     * 合并函数返回 null 时删除该 key
     */
    public static void mergeWordCount() {
        Map<String, Integer> wordCount = new LinkedHashMap<>();
        for (String word : new String[] {"java", "go", "java", "java", "go"}) {
            wordCount.merge(word, 1, Integer::sum);
        }
        System.out.println("merge 词频统计: " + wordCount);
        wordCount.merge("go", 1, (oldV, newV) -> null);
        System.out.println("merge 返回 null 删除 go: " + wordCount);
    }

    /**
     * 示例 4：replaceAll 批量修改所有值，remove(key, value) 条件删除
     */
    public static void replaceAllAndRemove() {
        Map<String, Integer> scores = sampleScores();
        scores.put("张三", 85); // 相当于经 compute 加分后的状态
        scores.put("王五", 60);
        scores.put("赵六", 66);
        scores.replaceAll((name, score) -> score + 10);
        System.out.println("replaceAll 全班加 10 分: " + scores);
        // remove(key, value)：只有 key-value 都匹配才删除
        System.out.println("remove 匹配删除: " + scores.remove("王五", 70)
            + ", 删除后还有王五吗: " + scores.containsKey("王五"));
    }

    /**
     * 示例 5：forEach 配合 lambda 遍历
     */
    public static void forEachTraversal() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("张三", 95);
        scores.put("李四", 100);
        scores.put("赵六", 76);
        scores.forEach((name, score) -> System.out.println("  " + name + " -> " + score));
    }

    /**
     * 初始数据：使用 LinkedHashMap 保持插入顺序，输出稳定
     */
    private static Map<String, Integer> sampleScores() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        scores.put("张三", 80);
        scores.put("李四", 90);
        return scores;
    }

    public static void main(String[] args) {
        getOrDefaultAndPutIfAbsent();
        computeMethods();
        mergeWordCount();
        replaceAllAndRemove();
        forEachTraversal();
    }

}
// Output:
// getOrDefault: 0
// putIfAbsent 后: {张三=80, 李四=90, 王五=60}
// computeIfAbsent 后: {张三=80, 李四=90, 王五=60, 赵六=66}
// compute 张三加分后: 85
// merge 词频统计: {java=3, go=2}
// merge 返回 null 删除 go: {java=3}
// replaceAll 全班加 10 分: {张三=95, 李四=100, 王五=70, 赵六=76}
// remove 匹配删除: true, 删除后还有王五吗: false
//   张三 -> 95
//   李四 -> 100
//   赵六 -> 76

