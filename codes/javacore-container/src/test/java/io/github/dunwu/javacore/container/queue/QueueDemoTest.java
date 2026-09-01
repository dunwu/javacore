package io.github.dunwu.javacore.container.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.EmptyStackException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * javacore-container queue 包示例的单元测试
 */
@DisplayName("队列与栈示例测试")
public class QueueDemoTest {

    @Test
    @DisplayName("LinkedList 实现队列：offer 入队、poll 出队、element/peek 查看队首")
    public void testLinkedListQueueDemo() {
        String output = captureOutput(LinkedListQueueDemo::demo);
        assertThat(output).contains("poll=a");
        assertThat(output).contains("element=b");
        assertThat(output).contains("peek=b");
    }

    @Test
    @DisplayName("栈只有 3 个元素却 pop 4 次，第 4 次抛 EmptyStackException（反例）")
    public void testStackDemo() {
        assertThatThrownBy(() -> StackDemo.main(new String[0]))
            .isInstanceOf(EmptyStackException.class);
    }

    private static String captureOutput(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

}
