package io.github.dunwu.javacore.jdk14.npe;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link NullPointerExceptionDemo} 单元测试。
 */
@DisplayName("Java 14 NullPointerException 精准提示示例测试")
public class NullPointerExceptionDemoTest {

    @Test
    @DisplayName("示例 1：简单空引用调用，NPE 信息精确指出为 null 的变量")
    public void testSimpleNullInvoke() {
        String output = captureOutput(NullPointerExceptionDemo::simpleNullInvoke);
        assertThat(output)
            .contains("场景一 NPE 信息: Cannot invoke \"String.length()\" because \"text\" is null");
    }

    @Test
    @DisplayName("示例 2：链式调用中某一环为 null，NPE 信息定位到具体表达式")
    public void testChainedNullReference() {
        String output = captureOutput(NullPointerExceptionDemo::chainedNullReference);
        assertThat(output)
            .contains("场景二 NPE 信息: Cannot read field \"city\" because \"order.user.address\" is null");
    }

    @Test
    @DisplayName("示例 3：数组元素为 null，NPE 信息包含数组下标")
    public void testNullArrayElement() {
        String output = captureOutput(NullPointerExceptionDemo::nullArrayElement);
        assertThat(output)
            .contains("场景三 NPE 信息: Cannot invoke \"String.toUpperCase()\" because \"names[0]\" is null");
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
