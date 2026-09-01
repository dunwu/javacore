package io.github.dunwu.javacore.jdk21.switchstmt;

/**
 * Java 21 Switch 模式匹配示例。
 * <p>
 * switch 模式匹配在 Java 17~20 多轮预览后，于 Java 21 正式转正（JEP 440）。
 * switch 的 case 不再局限于常量，还可以是：
 * <ul>
 * <li>类型模式：{@code case String s ->}，匹配类型并绑定变量</li>
 * <li>{@code case null ->}：显式处理 null（不再抛 NPE）</li>
 * <li>守卫条件：{@code case Integer i when i > 0 ->}</li>
 * <li>配合 sealed 类型实现穷举检查：覆盖所有子类型后无需 default</li>
 * </ul>
 */
public class SwitchPatternDemo {

    /**
     * 示例 1：类型模式按类型分派，case null 显式处理空值
     */
    public static void typePatternAndNull() {
        for (Object obj : new Object[] {"Java", 21, 3.14, null }) {
            System.out.println(describe(obj));
        }
    }

    /**
     * 示例 2：守卫条件 when——同一类型下进一步细分
     */
    public static void guardWhen() {
        System.out.println(classify(100));
        System.out.println(classify(-5));
        System.out.println(classify("0"));
    }

    /**
     * 示例 3：sealed 类型穷举——覆盖全部子类型后无需 default 分支
     */
    public static void sealedExhaustive() {
        System.out.println("圆面积: " + area(new Circle(2)));
        System.out.println("正方形面积: " + area(new Square(3)));
    }

    public static void main(String[] args) {
        typePatternAndNull();
        guardWhen();
        sealedExhaustive();
    }

    /**
     * 类型模式 + null 分支
     */
    private static String describe(Object obj) {
        return switch (obj) {
            case Integer i -> "整数: " + i;
            case String s -> "字符串: " + s;
            case Double d -> "浮点数: " + d;
            case null -> "空值";
            default -> "其他类型: " + obj.getClass().getSimpleName();
        };
    }

    /**
     * 守卫条件 when：分支 = 类型模式 + 附加条件
     */
    private static String classify(Object obj) {
        return switch (obj) {
            case Integer i when i > 0 -> "正整数: " + i;
            case Integer i -> "非正整数: " + i;
            case String s when s.isBlank() -> "空白字符串";
            case String s -> "非空字符串: " + s;
            default -> "其他";
        };
    }

    /**
     * sealed 类型穷举检查：编译器确认所有子类型都已覆盖，因此不需要 default。
     * 如果新增一个 Shape 子类型而忘记处理，这里会直接编译报错。
     */
    private static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Square s -> s.side() * s.side();
        };
    }

    sealed interface Shape permits Circle, Square {

    }

    record Circle(double radius) implements Shape {

    }

    record Square(double side) implements Shape {

    }

}
// Output:
// 字符串: Java
// 整数: 21
// 浮点数: 3.14
// 空值
// 正整数: 100
// 非正整数: -5
// 非空字符串: 0
// 圆面积: 12.566370614359172
// 正方形面积: 9.0
