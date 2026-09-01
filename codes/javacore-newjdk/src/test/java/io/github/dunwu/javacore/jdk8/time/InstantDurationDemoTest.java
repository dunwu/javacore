package io.github.dunwu.javacore.jdk8.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link InstantDurationDemo} 单元测试。
 */
@DisplayName("Java 8 Instant/Duration/Period 示例测试")
public class InstantDurationDemoTest {

    @Test
    @DisplayName("示例 1：Instant 时间戳与指定时区的 LocalDateTime 互转")
    public void testInstantDemo() {
        String output = captureOutput(InstantDurationDemo::instantDemo);
        assertThat(output)
            .contains("instant: 2024-05-20T15:30:45Z")
            .contains("纪元秒: 1716219045, 毫秒: 1716219045000")
            .contains("转上海本地时间: 2024-05-20T23:30:45")
            .contains("转回 instant: 2024-05-20T15:30:45Z");
    }

    @Test
    @DisplayName("示例 2：Duration 计算时分秒级别间隔")
    public void testDurationDemo() {
        String output = captureOutput(InstantDurationDemo::durationDemo);
        assertThat(output)
            .contains("Duration: PT30M45S, 秒数: 1845")
            .contains("加 2 小时: 2024-05-20T17:30:45Z")
            .contains("相差分钟: 30");
    }

    @Test
    @DisplayName("示例 3：Period 年月日语义间隔计算年龄")
    public void testPeriodDemo() {
        String output = captureOutput(InstantDurationDemo::periodDemo);
        assertThat(output)
            .contains("Period: P29Y2M5D")
            .contains("年龄: 29 岁 2 个月 5 天")
            .contains("1 年 2 个月 3 天后: 2025-07-23");
    }

    @Test
    @DisplayName("示例 4：Duration 精确时长与 Period 日历语义的区别")
    public void testDurationVsPeriod() {
        String output = captureOutput(InstantDurationDemo::durationVsPeriod);
        assertThat(output)
            .contains("加 30 天（ChronoUnit.DAYS）: 2024-03-01")
            .contains("Period.ofMonths(1) 加到 2024-01-31: 2024-02-29");
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
