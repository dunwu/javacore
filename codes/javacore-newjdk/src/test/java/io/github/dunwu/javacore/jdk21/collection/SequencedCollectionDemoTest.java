package io.github.dunwu.javacore.jdk21.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link SequencedCollectionDemo} 单元测试。
 */
@DisplayName("Java 21 顺序集合示例测试")
public class SequencedCollectionDemoTest {

    @Test
    @DisplayName("示例 1：SequencedCollection 的 addFirst/addLast/getFirst/getLast/reversed")
    public void testSequencedCollectionApi() {
        String output = captureOutput(SequencedCollectionDemo::sequencedCollectionApi);
        assertThat(output)
            .contains("list: [1, 2, 3, 4]")
            .contains("getFirst: 1, getLast: 4")
            .contains("removeFirst: 1, removeLast: 4")
            .contains("reversed 逆序视图: [3, 2]");
    }

    @Test
    @DisplayName("示例 2：SequencedSet 按插入顺序保序")
    public void testSequencedSetApi() {
        String output = captureOutput(SequencedCollectionDemo::sequencedSetApi);
        assertThat(output)
            .contains("set: [a, b, c, d]")
            .contains("set.getFirst: a, set.getLast: d")
            .contains("set.reversed: [d, c, b, a]");
    }

    @Test
    @DisplayName("示例 3：SequencedMap 的 firstEntry/lastEntry/putFirst/putLast/reversed")
    public void testSequencedMapApi() {
        String output = captureOutput(SequencedCollectionDemo::sequencedMapApi);
        assertThat(output)
            .contains("map: {a=1, b=2, c=3}")
            .contains("firstEntry: a=1, lastEntry: c=3")
            .contains("map.reversed: {c=3, b=2, a=1}");
    }

    @Test
    @DisplayName("示例 4：reversed 逆序视图与原集合修改互相影响")
    public void testReversedViewIsLive() {
        String output = captureOutput(SequencedCollectionDemo::reversedViewIsLive);
        assertThat(output).contains("修改逆序视图后，原集合: [2, 99]");
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
