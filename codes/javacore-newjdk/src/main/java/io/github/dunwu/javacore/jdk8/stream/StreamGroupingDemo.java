package io.github.dunwu.javacore.jdk8.stream;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java 8 Stream 分组与分区示例。
 * <p>
 * {@code Collectors.groupingBy} / {@code Collectors.partitioningBy}
 * 是 collect 最强大的用法，对应 SQL 中的 GROUP BY：
 * <ul>
 * <li>{@code groupingBy(分类函数)}：按任意条件分组，返回 {@code Map<K, List<T>>}</li>
 * <li>二级分组：groupingBy 的第二个参数再传一个收集器（下游收集器）</li>
 * <li>{@code partitioningBy(谓词)}：特殊的二分分组，返回 {@code Map<Boolean, List<T>>}</li>
 * </ul>
 */
public class StreamGroupingDemo {

    /**
     * 学生：姓名、班级、分数
     */
    static class Student {

        private final String name;

        private final String clazz;

        private final int score;

        Student(String name, String clazz, int score) {
            this.name = name;
            this.clazz = clazz;
            this.score = score;
        }

        String getName() {
            return name;
        }

        String getClazz() {
            return clazz;
        }

        int getScore() {
            return score;
        }

    }

    /**
     * 示例 1：单级分组，按班级分组（LinkedHashMap 保证班级输出顺序稳定）
     */
    public static void groupByClazz() {
        Map<String, List<Student>> byClazz = sampleStudents().stream()
            .collect(Collectors.groupingBy(Student::getClazz, LinkedHashMap::new, Collectors.toList()));
        byClazz.forEach((clazz, list) -> System.out.println(clazz + ": "
            + list.stream().map(Student::getName).collect(Collectors.joining(", "))));
    }

    /**
     * 示例 2：下游收集器，按班级分组后统计人数 / 平均分
     */
    public static void downstreamCollector() {
        List<Student> students = sampleStudents();
        Map<String, Long> countByClazz = students.stream()
            .collect(Collectors.groupingBy(Student::getClazz, LinkedHashMap::new, Collectors.counting()));
        System.out.println("各班人数: " + countByClazz);
        Map<String, Double> avgByClazz = students.stream()
            .collect(Collectors.groupingBy(Student::getClazz, LinkedHashMap::new,
                Collectors.averagingInt(Student::getScore)));
        System.out.println("各班平均分: " + avgByClazz);
    }

    /**
     * 示例 3：多级分组，班级 -> 及格/不及格
     */
    public static void nestedGrouping() {
        Map<String, Map<Boolean, List<String>>> nested = sampleStudents().stream()
            .collect(Collectors.groupingBy(Student::getClazz, LinkedHashMap::new,
                Collectors.partitioningBy(s -> s.getScore() >= 60,
                    Collectors.mapping(Student::getName, Collectors.toList()))));
        System.out.println("一班及格: " + nested.get("一班").get(true));
        System.out.println("一班不及格: " + nested.get("一班").get(false));
    }

    /**
     * 示例 4：partitioningBy 二分分区，true/false 两个 key 一定都存在
     */
    public static void partitioning() {
        Map<Boolean, List<Student>> partition = sampleStudents().stream()
            .collect(Collectors.partitioningBy(s -> s.getScore() >= 60));
        System.out.println("及格人数: " + partition.get(true).size()
            + ", 不及格人数: " + partition.get(false).size());
    }

    /**
     * 测试数据：五个学生，分属两个班级
     */
    private static List<Student> sampleStudents() {
        return Arrays.asList(
            new Student("张三", "一班", 80),
            new Student("李四", "一班", 90),
            new Student("王五", "二班", 70),
            new Student("赵六", "二班", 85),
            new Student("孙七", "一班", 55));
    }

    public static void main(String[] args) {
        groupByClazz();
        downstreamCollector();
        nestedGrouping();
        partitioning();
    }

}
// Output:
// 一班: 张三, 李四, 孙七
// 二班: 王五, 赵六
// 各班人数: {一班=3, 二班=2}
// 各班平均分: {一班=75.0, 二班=77.5}
// 一班及格: [张三, 李四]
// 一班不及格: [孙七]
// 及格人数: 4, 不及格人数: 1
