package io.github.dunwu.javacore.jdk10.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link UnmodifiableCollectionDemo} 单元测试。
 */
@DisplayName("Java 10 不可变集合增强示例测试")
public class UnmodifiableCollectionDemoTest {

    @Test
    @DisplayName("示例 1：copyOf 复制不可变副本且修改原集合不影响副本")
    public void testCopyOfMethods() {
        String output = captureOutput(UnmodifiableCollectionDemo::copyOfMethods);
        assertThat(output)
            .contains("原集合: [a, b, c, d]")
            .contains("copyOf 副本: [a, b, c]")
            .contains("Set.copyOf 去重: [1, 2, 3]")
            .contains("Map.copyOf: {x=1}");
    }

    @Test
    @DisplayName("示例 2：toUnmodifiableList 收集为不可变集合且修改抛异常")
    public void testToUnmodifiableCollectors() {
        String output = captureOutput(UnmodifiableCollectionDemo::toUnmodifiableCollectors);
        assertThat(output)
            .contains("toUnmodifiableList: [10, 20, 30]")
            .contains("不可变集合不允许修改，抛出 UnsupportedOperationException");
    }

    @Test
    @DisplayName("示例 3：orElseThrow 无参取值与空 Optional 抛 NoSuchElementException")
    public void testOptionalOrElseThrow() {
        String output = captureOutput(UnmodifiableCollectionDemo::optionalOrElseThrow);
        assertThat(output)
            .contains("orElseThrow 取值: 存在")
            .contains("空 Optional 调用 orElseThrow 抛出 NoSuchElementException");
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
