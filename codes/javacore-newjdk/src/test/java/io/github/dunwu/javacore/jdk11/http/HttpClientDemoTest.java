package io.github.dunwu.javacore.jdk11.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link HttpClientDemo} 单元测试。
 * <p>
 * 网络环境不可控，请求成功或优雅降级均视为通过。
 */
@DisplayName("Java 11 标准 HttpClient 示例测试")
public class HttpClientDemoTest {

    @Test
    @DisplayName("示例 1：同步 GET 请求成功或无网络时优雅降级")
    public void testSyncGet() {
        String output = captureOutput(HttpClientDemo::syncGet);
        assertThat(output).containsAnyOf("同步 GET 状态码: ", "同步 GET 请求失败");
    }

    @Test
    @DisplayName("示例 2：异步 GET 请求成功或无网络时优雅降级")
    public void testAsyncGet() {
        String output = captureOutput(HttpClientDemo::asyncGet);
        assertThat(output).containsAnyOf("异步 GET 状态码: ", "异步 GET 请求失败");
    }

    @Test
    @DisplayName("示例 3：POST 请求成功或无网络时优雅降级")
    public void testPost() {
        String output = captureOutput(HttpClientDemo::post);
        assertThat(output).containsAnyOf("POST 状态码: ", "POST 请求失败");
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
