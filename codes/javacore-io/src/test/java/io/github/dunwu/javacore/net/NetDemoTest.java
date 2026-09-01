package io.github.dunwu.javacore.net;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * net 包示例测试
 * <p>
 * 注：{@link InetAddressDemo}、{@link URLDemo}、{@link URLConnectionDemo} 依赖网络/DNS，
 * tcp、udp 包示例为长驻服务或需要成对运行，均不纳入自动化测试。
 */
public class NetDemoTest {

    @FunctionalInterface
    interface ThrowingRunnable {

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
    @DisplayName("编码转换：字符串编解码保证中文不乱码")
    void testCodeDemo() {
        String output = captureOutput(() -> CodeDemo.demo());
        assertThat(output)
            .contains("编码之后的内容：")
            .contains("解码之后的内容：乘风破浪会有时");
    }

}
