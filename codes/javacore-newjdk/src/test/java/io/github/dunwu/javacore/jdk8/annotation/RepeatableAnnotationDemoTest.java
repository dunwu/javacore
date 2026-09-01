package io.github.dunwu.javacore.jdk8.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link RepeatableAnnotationDemo} 单元测试。
 * <p>
 * 验证 Java 8 可重复注解（@Repeatable）的反射读取行为：
 * <ul>
 * <li>{@code getAnnotationsByType} 能直接拿到重复注解数组</li>
 * <li>编译器自动生成的容器注解 {@code Schedules} 与重复注解内容一致</li>
 * <li>只写一个注解时不生成容器，两个及以上才包装为容器</li>
 * <li>注解属性默认值生效</li>
 * </ul>
 */
@DisplayName("Java 8 可重复注解 @Repeatable 示例测试")
public class RepeatableAnnotationDemoTest {

    /**
     * 测试目标类：同一位置重复书写两个 @Schedule
     */
    private static final Class<?> TARGET = RepeatableAnnotationDemo.MonthlyReportTask.class;

    @Test
    @DisplayName("getAnnotationsByType 直接读取重复注解数组及属性值")
    public void testGetAnnotationsByType() {
        RepeatableAnnotationDemo.Schedule[] schedules =
            TARGET.getAnnotationsByType(RepeatableAnnotationDemo.Schedule.class);
        assertThat(schedules).hasSize(2);
        assertThat(schedules[0].dayOfMonth()).isEqualTo("1");
        assertThat(schedules[0].hour()).isEqualTo("08:00");
        assertThat(schedules[1].dayOfMonth()).isEqualTo("15");
        assertThat(schedules[1].hour()).isEqualTo("20:30");
    }

    @Test
    @DisplayName("重复书写时编译器自动生成容器注解，单次书写时无容器")
    public void testContainerAnnotation() {
        // 重复书写时编译器自动包装为容器注解
        RepeatableAnnotationDemo.Schedules container =
            TARGET.getAnnotation(RepeatableAnnotationDemo.Schedules.class);
        assertThat(container).isNotNull();
        assertThat(container.value()).hasSize(2);
        // 容器内容与 getAnnotationsByType 结果一致
        assertThat(container.value()).isEqualTo(TARGET.getAnnotationsByType(RepeatableAnnotationDemo.Schedule.class));
        // 只写一次时没有容器注解
        assertThat(SingleScheduleTask.class.getAnnotation(RepeatableAnnotationDemo.Schedules.class)).isNull();
    }

    @Test
    @DisplayName("只写一个 @Schedule 时也能正常读取")
    public void testSingleAnnotation() {
        RepeatableAnnotationDemo.Schedule[] schedules =
            SingleScheduleTask.class.getAnnotationsByType(RepeatableAnnotationDemo.Schedule.class);
        assertThat(schedules).hasSize(1);
        assertThat(schedules[0].dayOfMonth()).isEqualTo("1");
    }

    @Test
    @DisplayName("未显式指定的属性使用注解定义中的默认值")
    public void testDefaultValues() {
        // 未显式指定的属性使用注解定义中的默认值
        RepeatableAnnotationDemo.Schedule schedule =
            DefaultScheduleTask.class.getAnnotationsByType(RepeatableAnnotationDemo.Schedule.class)[0];
        assertThat(schedule.dayOfMonth()).isEqualTo("Mon");
        assertThat(schedule.hour()).isEqualTo("00:00");
    }

    @Test
    @DisplayName("示例 1：printRepeatedAnnotations 输出重复注解列表")
    public void testPrintRepeatedAnnotations() {
        // 示例 1：getAnnotationsByType 直接获取重复注解数组
        String output = captureOutput(RepeatableAnnotationDemo::printRepeatedAnnotations);
        assertThat(output)
            .contains("@Schedule 个数: 2")
            .contains("每月 1 日 08:00 执行")
            .contains("每月 15 日 20:30 执行");
    }

    @Test
    @DisplayName("示例 2：printContainerAnnotation 输出容器注解信息")
    public void testPrintContainerAnnotation() {
        // 示例 2：getAnnotation 拿到容器注解
        String output = captureOutput(RepeatableAnnotationDemo::printContainerAnnotation);
        assertThat(output)
            .contains("容器注解 value 长度: 2")
            .contains("容器内第一个注解: dayOfMonth=1, hour=08:00");
    }

    /**
     * 捕获被测代码的标准输出，测试结束后恢复原 System.out
     */
    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    @RepeatableAnnotationDemo.Schedule(dayOfMonth = "1")
    static class SingleScheduleTask {

    }

    @RepeatableAnnotationDemo.Schedule
    static class DefaultScheduleTask {

    }

}
