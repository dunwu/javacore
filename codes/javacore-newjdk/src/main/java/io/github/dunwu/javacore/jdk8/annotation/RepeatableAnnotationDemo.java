package io.github.dunwu.javacore.jdk8.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Java 8 可重复注解（@Repeatable）示例。
 * <p>
 * Java 8 之前，同一个注解在同一个位置只能使用一次；
 * 需要多次使用时只能定义一个"容器注解"手动包裹。
 * Java 8 引入 {@link Repeatable}：只要用 {@code @Repeatable(容器注解.class)}
 * 修饰注解，编译器会自动把多个注解包装进容器注解，语法上可直接重复书写。
 * <ul>
 * <li>容器注解必须有一个 value 属性，类型为被重复注解的数组</li>
 * <li>反射读取：{@code getAnnotationsByType} 直接拿到数组，
 * {@code getAnnotation(容器.class)} 拿到容器</li>
 * </ul>
 */
public class RepeatableAnnotationDemo {

    /**
     * 可重复的注解：@Repeatable 指向其容器注解 Schedules
     */
    @Repeatable(Schedules.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Schedule {

        String dayOfMonth() default "Mon";

        String hour() default "00:00";

    }

    /**
     * 容器注解：value 为被重复注解的数组
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface Schedules {

        Schedule[] value();

    }

    /**
     * 同一位置重复书写 @Schedule，编译器自动包装为 @Schedules
     */
    @Schedule(dayOfMonth = "1", hour = "08:00")
    @Schedule(dayOfMonth = "15", hour = "20:30")
    static class MonthlyReportTask {

    }

    /**
     * 示例 1：getAnnotationsByType 直接获取重复注解数组（推荐）
     */
    public static void printRepeatedAnnotations() {
        Schedule[] schedules = MonthlyReportTask.class.getAnnotationsByType(Schedule.class);
        System.out.println("@Schedule 个数: " + schedules.length);
        for (Schedule schedule : schedules) {
            System.out.println("  每月 " + schedule.dayOfMonth() + " 日 " + schedule.hour() + " 执行");
        }
    }

    /**
     * 示例 2：getAnnotation 拿到容器注解（等价于 Java 8 之前的手工包装写法）
     */
    public static void printContainerAnnotation() {
        Schedules container = MonthlyReportTask.class.getAnnotation(Schedules.class);
        System.out.println("容器注解 value 长度: " + container.value().length);
        System.out.println("容器内第一个注解: dayOfMonth=" + container.value()[0].dayOfMonth()
            + ", hour=" + container.value()[0].hour());
    }

    public static void main(String[] args) {
        printRepeatedAnnotations();
        printContainerAnnotation();
    }

}
// Output:
// @Schedule 个数: 2
//   每月 1 日 08:00 执行
//   每月 15 日 20:30 执行
// 容器注解 value 长度: 2
// 容器内第一个注解: dayOfMonth=1, hour=08:00
