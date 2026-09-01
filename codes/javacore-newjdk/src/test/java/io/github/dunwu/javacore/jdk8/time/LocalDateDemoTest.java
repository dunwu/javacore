package io.github.dunwu.javacore.jdk8.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link LocalDateDemo} 单元测试。
 */
@DisplayName("Java 8 LocalDate/LocalTime/LocalDateTime 示例测试")
public class LocalDateDemoTest {

    @Test
    @DisplayName("示例 1：of 工厂方法创建与字段取值")
    public void testCreateAndAccess() {
        String output = captureOutput(LocalDateDemo::createAndAccess);
        assertThat(output)
            .contains("date: 2024-05-20")
            .contains("time: 15:30:45")
            .contains("dateTime: 2024-05-20T15:30:45")
            .contains("年: 2024, 月: 5, 日: 20")
            .contains("星期: MONDAY, 一年中的第 141 天");
    }

    @Test
    @DisplayName("示例 2：不可变性，plus/minus 返回新对象")
    public void testImmutability() {
        String output = captureOutput(LocalDateDemo::immutability);
        assertThat(output)
            .contains("加一天: 2024-05-21, 加一月: 2024-06-20, 减一周: 2024-05-13")
            .contains("原对象不变: 2024-05-20");
    }

    @Test
    @DisplayName("示例 3：with 直接设置字段")
    public void testWithField() {
        String output = captureOutput(LocalDateDemo::withField);
        assertThat(output).contains("当年第一天: 2024-01-01");
    }

    @Test
    @DisplayName("示例 4：isBefore 比较与 ChronoUnit 距离计算")
    public void testCompareAndBetween() {
        String output = captureOutput(LocalDateDemo::compareAndBetween);
        assertThat(output)
            .contains("isBefore: true")
            .contains("相差天数: 225");
    }

    @Test
    @DisplayName("示例 5：atTime 组合与 toLocalDate/toLocalTime 拆分")
    public void testCombineAndSplit() {
        String output = captureOutput(LocalDateDemo::combineAndSplit);
        assertThat(output)
            .contains("atTime 组合: 2024-01-01T08:00")
            .contains("toLocalDate 拆分: 2024-05-20, toLocalTime: 15:30:45");
    }

    @Test
    @DisplayName("示例 6：闰年、当月天数与星期枚举比较")
    public void testCommonChecks() {
        String output = captureOutput(LocalDateDemo::commonChecks);
        assertThat(output)
            .contains("2024 是闰年: true, 当月天数: 29")
            .contains("星期几枚举比较: true");
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

}
