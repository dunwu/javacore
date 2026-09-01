package io.github.dunwu.javacore.util.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * task 包示例测试：Timer 与 TimerTask 定时任务。
 * <p>
 * 注意：示例中的 demo() 会无限期重复调度，测试中改为短周期调度并在执行后取消。
 */
@DisplayName("定时任务示例测试")
public class TimerTaskDemoTest {

    @Test
    @DisplayName("TimerTaskDemo：定时任务周期性执行并可取消")
    void testTimerTaskDemo() throws InterruptedException {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Timer timer = new Timer();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            // 立即开始，每 50 毫秒重复执行一次（比示例的 2 秒更快，便于测试）
            timer.schedule(new TimerTaskDemo(), 0, 50);
            Thread.sleep(300);
        } finally {
            timer.cancel(); // 取消调度，防止任务继续执行
            System.setOut(original);
        }
        String output = new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        assertThat(output).contains("当前系统时间为：");
        // 300 毫秒内至少执行了 2 次
        int count = output.split("当前系统时间为：").length - 1;
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

}
