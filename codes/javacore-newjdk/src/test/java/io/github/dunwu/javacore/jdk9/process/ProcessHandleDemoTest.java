package io.github.dunwu.javacore.jdk9.process;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link ProcessHandleDemo} 单元测试。
 * <p>
 * 进程信息与环境相关，此处仅断言关键输出格式。
 */
@DisplayName("Java 9 ProcessHandle 进程管理示例测试")
public class ProcessHandleDemoTest {

    @Test
    @DisplayName("示例 1：当前进程句柄可获取 PID、存活状态与元信息")
    public void testCurrentProcessInfo() {
        String output = captureOutput(ProcessHandleDemo::currentProcessInfo);
        assertThat(output)
            .contains("当前进程 PID: ")
            .contains("当前进程是否存活: true")
            .contains("进程命令: ")
            .contains("进程启动用户: ");
    }

    @Test
    @DisplayName("示例 2：allProcesses 可遍历进程快照并统计总数")
    public void testAllProcessesSnapshot() {
        String output = captureOutput(ProcessHandleDemo::allProcessesSnapshot);
        assertThat(output)
            .contains("系统中部分进程快照：")
            .contains("PID=")
            .contains("当前系统进程总数: ");
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
