package io.github.dunwu.javacore.jdk16.record;

import java.lang.reflect.Modifier;

/**
 * Java 16 Record 基础示例。
 * <p>
 * Record 在 Java 14 预览、Java 16 正式转正（JEP 395）。
 * 它是一种"数据载体"类，编译器根据组件（components）自动生成：
 * <ul>
 * <li>私有 final 字段</li>
 * <li>全参构造器</li>
 * <li>与组件同名的访问器方法（注意不是 getXxx）</li>
 * <li>equals / hashCode / toString</li>
 * </ul>
 * Record 适合替代过去仅用于承载数据的 POJO / DTO，大幅消除样板代码。
 */
public class RecordDemo {

    /**
     * 示例 1：访问器方法与组件同名（不是 getX），equals / hashCode 基于所有组件的值
     */
    public static void accessorAndEquals() {
        // 通过全参构造器创建实例
        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(1, 2);

        // 访问器方法与组件同名（不是 getX）
        System.out.println("p1.x = " + p1.x() + ", p1.y = " + p1.y());

        // 自动生成的 equals / hashCode：基于所有组件的值
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1.equals(p3): " + p1.equals(p3));
        System.out.println("p1.hashCode() == p2.hashCode(): " + (p1.hashCode() == p2.hashCode()));
    }

    /**
     * 示例 2：自动生成的 toString，且 record 隐式 final、字段天然不可变
     */
    public static void toStringAndFinal() {
        Point p1 = new Point(3, 4);

        // 自动生成的 toString
        System.out.println("toString: " + p1);

        // record 是隐式 final 的，不能被继承；字段是 private final 的，天然不可变
        System.out.println("Point 是否 final: " + Modifier.isFinal(Point.class.getModifiers()));
    }

    public static void main(String[] args) {
        accessorAndEquals();
        toStringAndFinal();
    }

    /**
     * 一个组件为 x、y 的 record。等价于手写包含字段、构造器、访问器、equals、hashCode、toString 的完整类。
     */
    record Point(int x, int y) {

    }

}
// Output:
// p1.x = 3, p1.y = 4
// p1.equals(p2): true
// p1.equals(p3): false
// p1.hashCode() == p2.hashCode(): true
// toString: Point[x=3, y=4]
// Point 是否 final: true
