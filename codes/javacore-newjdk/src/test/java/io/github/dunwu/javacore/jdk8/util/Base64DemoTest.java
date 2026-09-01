package io.github.dunwu.javacore.jdk8.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link Base64Demo} 单元测试。
 */
@DisplayName("Java 8 Base64 编解码示例测试")
public class Base64DemoTest {

    @Test
    @DisplayName("示例 1：标准 Base64 编解码往返一致")
    public void testStandardCodec() {
        String output = captureOutput(Base64Demo::standardCodec);
        assertThat(output)
            .contains("标准编码: SmF2YSA4IEJhc2U2NCDnvJbnoIHnpLrkvos=")
            .contains("标准解码: Java 8 Base64 编码示例");
    }

    @Test
    @DisplayName("示例 2：URL 安全版将 +/ 替换为 -_")
    public void testUrlSafeCodec() {
        String output = captureOutput(Base64Demo::urlSafeCodec);
        assertThat(output)
            .contains("标准版含 +/: +//+, URL 安全版: -__-")
            .contains("URL 解码还原一致: true");
    }

    @Test
    @DisplayName("示例 3：MIME 编码按 76 字符换行且解码还原一致")
    public void testMimeCodec() {
        String output = captureOutput(Base64Demo::mimeCodec);
        assertThat(output)
            .contains("MIME 编码含换行: true")
            .contains("MIME 解码还原一致: true");
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
