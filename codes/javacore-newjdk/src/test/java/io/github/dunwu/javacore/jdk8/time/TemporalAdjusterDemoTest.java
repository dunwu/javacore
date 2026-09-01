package io.github.dunwu.javacore.jdk8.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link TemporalAdjusterDemo} 单元测试。
 */
@DisplayName("Java 8 TemporalAdjusters 与时区 API 示例测试")
public class TemporalAdjusterDemoTest {

    @Test
    @DisplayName("示例 1：内置调整器计算月首/月末/下一个周五等")
    public void testBuiltinAdjusters() {
        String output = captureOutput(TemporalAdjusterDemo::builtinAdjusters);
        assertThat(output)
            .contains("本月第一天: 2024-05-01")
            .contains("本月最后一天: 2024-05-31")
            .contains("下个月第一天: 2024-06-01")
            .contains("下一个周五: 2024-05-24")
            .contains("上一个周五: 2024-05-17");
    }

    @Test
    @DisplayName("示例 2：lambda 自定义调整器求下一个工作日")
    public void testCustomAdjuster() {
        String output = captureOutput(TemporalAdjusterDemo::customAdjuster);
        assertThat(output).contains("下一个工作日: 2024-05-21");
    }

    @Test
    @DisplayName("示例 3：ZonedDateTime 跨时区转换自动处理时差")
    public void testZonedDateTime() {
        String output = captureOutput(TemporalAdjusterDemo::zonedDateTime);
        assertThat(output)
            .contains("上海时间: 2024-05-20T15:30+08:00[Asia/Shanghai]")
            .contains("同一时刻纽约时间: 2024-05-20T03:30-04:00[America/New_York]")
            .contains("时区偏移: +08:00");
    }

    @Test
    @DisplayName("示例 4：Date 与 Instant/LocalDateTime 新老 API 互转")
    public void testLegacyConversion() {
        String output = captureOutput(TemporalAdjusterDemo::legacyConversion);
        assertThat(output)
            .contains("Date -> Instant: 2024-05-20T15:30:45Z")
            .contains("Date -> LocalDateTime: 2024-05-20T23:30:45");
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
