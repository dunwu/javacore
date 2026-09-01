package io.github.dunwu.javacore.jdk17.sealed;

import java.util.List;

/**
 * Java 17 sealed 接口 + record 组合示例。
 * <p>
 * sealed 接口与 record 是 Java 17 的"黄金搭档"，可以构建类似函数式语言
 * 代数数据类型（ADT）的封闭类型体系：
 * <ul>
 * <li>sealed 接口限定"有哪些类型"</li>
 * <li>record 提供简洁的不可变数据载体</li>
 * <li>instanceof 模式匹配做类型分派（Java 21 起可改用 switch 模式匹配）</li>
 * </ul>
 * 本示例用几何图形建模：Shape 封闭为 Circle、Square、Triangle 三种实现，
 * 分别计算面积与周长。
 */
public class SealedShapeDemo {

    /**
     * 示例 1：sealed 接口 + record 建模几何图形，基于 instanceof 模式匹配计算面积与周长
     */
    public static void shapeAreaAndPerimeter() {
        List<Shape> shapes = List.of(
            new Circle(2),
            new Square(3),
            new Triangle(3, 4, 5));

        for (Shape shape : shapes) {
            System.out.printf("%s -> 面积: %.2f, 周长: %.2f%n",
                shape.getClass().getSimpleName(), area(shape), perimeter(shape));
        }
    }

    public static void main(String[] args) {
        shapeAreaAndPerimeter();
    }

    /**
     * 封闭类型体系：Shape 只允许 Circle、Square、Triangle 三种实现
     */
    sealed interface Shape permits Circle, Square, Triangle {

    }

    record Circle(double radius) implements Shape {

    }

    record Square(double side) implements Shape {

    }

    record Triangle(double a, double b, double c) implements Shape {

    }

    /**
     * 基于 instanceof 模式匹配的类型分派
     */
    private static double area(Shape shape) {
        if (shape instanceof Circle c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof Square s) {
            return s.side() * s.side();
        } else if (shape instanceof Triangle t) {
            // 海伦公式
            double p = (t.a() + t.b() + t.c()) / 2;
            return Math.sqrt(p * (p - t.a()) * (p - t.b()) * (p - t.c()));
        }
        throw new IllegalStateException("不可达：Shape 体系已封闭");
    }

    private static double perimeter(Shape shape) {
        if (shape instanceof Circle c) {
            return 2 * Math.PI * c.radius();
        } else if (shape instanceof Square s) {
            return 4 * s.side();
        } else if (shape instanceof Triangle t) {
            return t.a() + t.b() + t.c();
        }
        throw new IllegalStateException("不可达：Shape 体系已封闭");
    }

}
// Output:
// Circle -> 面积: 12.57, 周长: 12.57
// Square -> 面积: 9.00, 周长: 12.00
// Triangle -> 面积: 6.00, 周长: 12.00
