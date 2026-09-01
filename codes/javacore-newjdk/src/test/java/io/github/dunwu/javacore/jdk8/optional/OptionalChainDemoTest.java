package io.github.dunwu.javacore.jdk8.optional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OptionalChainDemo} 单元测试。
 */
@DisplayName("Java 8 Optional 链式操作示例测试")
public class OptionalChainDemoTest {

    @Test
    @DisplayName("示例 1：map 链式调用替代层层判空，null 短路为 empty")
    public void testMapChain() {
        String output = captureOutput(OptionalChainDemo::mapChain);
        assertThat(output)
            .contains("map 链式取城市: 杭州")
            .contains("地址为 null 时: 未知城市");
    }

    @Test
    @DisplayName("示例 2：flatMap 避免 Optional 嵌套")
    public void testFlatMapChain() {
        String output = captureOutput(OptionalChainDemo::flatMapChain);
        assertThat(output).contains("flatMap: 杭州");
    }

    @Test
    @DisplayName("示例 3：filter 按条件保留值，不满足则变为 empty")
    public void testFilterDemo() {
        String output = captureOutput(OptionalChainDemo::filterDemo);
        assertThat(output)
            .contains("filter >= 18: 25")
            .contains("filter > 30: -1");
    }

    @Test
    @DisplayName("示例 4：Optional 作为方法返回值表达可能无结果")
    public void testReturnValueBestPractice() {
        String output = captureOutput(OptionalChainDemo::returnValueBestPractice);
        assertThat(output)
            .contains("findUser 存在: 张三")
            .contains("findUser 不存在: -");
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
