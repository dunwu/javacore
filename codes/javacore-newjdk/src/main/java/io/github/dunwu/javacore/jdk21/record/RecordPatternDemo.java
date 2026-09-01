package io.github.dunwu.javacore.jdk21.record;

/**
 * Java 21 Record 模式（解构）示例。
 * <p>
 * Record 模式在 Java 19/20 预览、Java 21 正式转正（JEP 441）。
 * 它允许在 instanceof 和 switch 中直接"解构" record 的组件：
 * <ul>
 * <li>{@code obj instanceof Point(int x, int y)}：匹配类型同时取出组件值</li>
 * <li>支持嵌套解构：{@code case Line(Point(var x1, var y1), Point(var x2, var y2))}</li>
 * <li>组件位置可用 var 让编译器推断类型</li>
 * </ul>
 * 配合 switch 模式匹配，可以彻底替代手写的一连串 getter 取值代码。
 */
public class RecordPatternDemo {

    /**
     * 示例 1：instanceof + record 模式——一步完成类型判断与解构
     */
    public static void instanceofPattern() {
        Object obj = new Point(3, 4);
        if (obj instanceof Point(int x, int y)) {
            System.out.println("解构 Point: x=" + x + ", y=" + y);
        }
    }

    /**
     * 示例 2：switch + record 模式，配合守卫条件 when 细分分支
     */
    public static void switchPattern() {
        Object another = new Point(0, 0);
        System.out.println(describe(another));
    }

    /**
     * 示例 3：嵌套解构——Line 由两个 Point 组成，一次性取出全部四个分量
     */
    public static void nestedPattern() {
        Line line = new Line(new Point(0, 0), new Point(3, 4));
        System.out.println("线段长度: " + length(line));
    }

    /**
     * 示例 4：var 推断组件类型
     */
    public static void varPattern() {
        Object colored = new Point(6, 8);
        if (colored instanceof Point(var x, var y)) {
            System.out.println("var 解构: x=" + x + ", y=" + y);
        }
    }

    public static void main(String[] args) {
        instanceofPattern();
        switchPattern();
        nestedPattern();
        varPattern();
    }

    private static String describe(Object obj) {
        return switch (obj) {
            case Point(int x, int y) when x == y -> "对角线上的点: (" + x + ", " + y + ")";
            case Point(int x, int y) -> "普通点: (" + x + ", " + y + ")";
            default -> "不是点";
        };
    }

    /**
     * 嵌套 record 模式：外层 Line 与内层 Point 同时解构
     */
    private static double length(Object obj) {
        return switch (obj) {
            case Line(Point(int x1, int y1), Point(int x2, int y2)) ->
                Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            default -> Double.NaN;
        };
    }

    record Point(int x, int y) {

    }

    record Line(Point start, Point end) {

    }

}
// Output:
// 解构 Point: x=3, y=4
// 对角线上的点: (0, 0)
// 线段长度: 5.0
// var 解构: x=6, y=8
