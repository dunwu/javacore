package io.github.dunwu.javacore.jdk16.record;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link RecordDemo} 单元测试。
 */
@DisplayName("Java 16 Record 基础示例测试")
public class RecordDemoTest {

    @Test
    @DisplayName("示例 1：访问器与组件同名，equals / hashCode 基于所有组件")
    public void testAccessorAndEquals() {
        String output = captureOutput(RecordDemo::accessorAndEquals);
        assertThat(output)
            .contains("p1.x = 3, p1.y = 4")
            .contains("p1.equals(p2): true")
            .contains("p1.equals(p3): false")
            .contains("p1.hashCode() == p2.hashCode(): true");
    }

    @Test
    @DisplayName("示例 2：自动生成 toString，record 隐式 final")
    public void testToStringAndFinal() {
        String output = captureOutput(RecordDemo::toStringAndFinal);
        assertThat(output)
            .contains("toString: Point[x=3, y=4]")
            .contains("Point 是否 final: true");
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
