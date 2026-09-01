package io.github.dunwu.javacore.jdk16.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link StreamToListDemo} 单元测试。
 */
@DisplayName("Java 16 Stream.toList() 示例测试")
public class StreamToListDemoTest {

    @Test
    @DisplayName("示例 1：Stream.toList() 返回不可变列表，修改抛异常")
    public void testStreamToList() {
        String output = captureOutput(StreamToListDemo::streamToList);
        assertThat(output)
            .contains("toList 结果: [2, 4, 6]")
            .contains("Stream.toList() 返回不可变列表，修改抛出 UnsupportedOperationException");
    }

    @Test
    @DisplayName("示例 2：Collectors.toList() 当前实现为可变列表")
    public void testCollectorsToList() {
        String output = captureOutput(StreamToListDemo::collectorsToList);
        assertThat(output).contains("Collectors.toList() 可修改: [2, 4, 6, 4]");
    }

    @Test
    @DisplayName("示例 3：toCollection(ArrayList::new) 显式收集为可变 ArrayList")
    public void testToCollectionArrayList() {
        String output = captureOutput(StreamToListDemo::toCollectionArrayList);
        assertThat(output).contains("toCollection(ArrayList::new): [1, 2, 3]");
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
