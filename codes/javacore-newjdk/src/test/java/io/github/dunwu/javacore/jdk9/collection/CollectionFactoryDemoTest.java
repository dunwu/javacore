package io.github.dunwu.javacore.jdk9.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link CollectionFactoryDemo} 单元测试。
 */
@DisplayName("Java 9 集合工厂方法示例测试")
public class CollectionFactoryDemoTest {

    @Test
    @DisplayName("示例 1：of / ofEntries 一行创建不可变 List、Set、Map")
    public void testCreateImmutableCollections() {
        String output = captureOutput(CollectionFactoryDemo::createImmutableCollections);
        assertThat(output)
            .contains("List.of: [Java, Python, Go]")
            .contains("张三=90")
            .contains("语文=100");
    }

    @Test
    @DisplayName("示例 2：不可修改、不允许 null、Set 不允许重复三大限制")
    public void testImmutableRestrictions() {
        String output = captureOutput(CollectionFactoryDemo::immutableRestrictions);
        assertThat(output)
            .contains("List.of 创建的集合不可修改，抛出 UnsupportedOperationException")
            .contains("集合工厂方法不允许 null 元素，抛出 NullPointerException")
            .contains("Set.of 不允许重复元素，抛出 IllegalArgumentException");
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
