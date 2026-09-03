package io.github.dunwu.javacore.serial;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link SerializeDemo} 单元测试
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class SerialDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("序列化/反序列化：transient 修饰的 SSN 字段反序列化后为默认值 0")
    void testSerializeDemo() {
        String output = captureOutput(SerializeDemo::demo);
        // 路径必须取自示例自身的常量：示例已统一把临时文件写到 target/ 下
        assertThat(output).contains("Serialized data is saved in " + SerializeDemo.FILE_PATH);
        assertThat(output).contains("Deserialized Employee...");
        assertThat(output).contains("Name: Reyan Ali");
        assertThat(output).contains("Address: Phokka Kuan, Ambehta Peer");
        assertThat(output).contains("SSN: 0");
        assertThat(output).contains("Number: 101");
        // 清理示例在 target/ 下生成的临时文件
        File file = new File(SerializeDemo.FILE_PATH);
        if (file.exists()) {
            assertThat(file.delete()).isTrue();
        }
    }

}
