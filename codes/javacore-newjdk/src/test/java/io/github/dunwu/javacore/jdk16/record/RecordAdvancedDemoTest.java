package io.github.dunwu.javacore.jdk16.record;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link RecordAdvancedDemo} 单元测试。
 */
@DisplayName("Java 16 Record 进阶示例测试")
public class RecordAdvancedDemoTest {

    @Test
    @DisplayName("示例 1：紧凑构造器校验与自定义实例方法、静态工厂")
    public void testCompactConstructorAndMethods() {
        String output = captureOutput(RecordAdvancedDemo::compactConstructorAndMethods);
        assertThat(output)
            .contains("校验失败: start 不能大于 end")
            .contains("range 长度: 10")
            .contains("静态工厂: Range[start=0, end=5]");
    }

    @Test
    @DisplayName("示例 2：record 实现接口")
    public void testRecordImplementsInterface() {
        String output = captureOutput(RecordAdvancedDemo::recordImplementsInterface);
        assertThat(output).contains("Message: Java 16");
    }

    @Test
    @DisplayName("示例 3：局部 record 定义在方法内部用于临时数据聚合")
    public void testLocalRecord() {
        String output = captureOutput(RecordAdvancedDemo::localRecord);
        assertThat(output).contains("局部 record: NameCount[name=record, count=3]");
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
