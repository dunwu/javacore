package io.github.dunwu.javacore.web.concurrent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * {@link ThreadLocalErrorDemo} 单元测试：复现并对比 ThreadLocal 在 Web 容器中的「用户串号」问题
 * <p>
 * Web 容器会复用工作线程，若请求结束后不清理 ThreadLocal，下一个复用同一线程的请求就会读到上一次请求遗留的数据。
 * 生产环境靠 {@code application.properties} 中的 {@code server.tomcat.max-threads=1} 把线程池压到单线程来放大该问题；
 * MockMvc 不启动真实容器、所有请求都在测试线程上同步执行，同样天然满足「线程被复用」这一前提，因此可以稳定复现。
 * <p>
 * 每个测试方法都先调用 {@code right} 接口清空 ThreadLocal，使断言不依赖测试方法的执行顺序。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ThreadLocal 线程复用串号示例测试")
public class ThreadLocalErrorDemoTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * MockMvc 在测试线程上同步处理请求，故控制器里取到的线程名就是当前测试线程名
     */
    private String currentThread() {
        return Thread.currentThread().getName();
    }

    /**
     * right 接口在 finally 中会 remove，因此调用它一次即可把当前线程的 ThreadLocal 清空
     */
    private void resetThreadLocal() throws Exception {
        mockMvc.perform(get("/threadlocal/right").param("id", "0")).andExpect(status().isOk());
    }

    private String callWrong(int id) throws Exception {
        return mockMvc.perform(get("/threadlocal/wrong").param("id", String.valueOf(id)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    private String callRight(int id) throws Exception {
        return mockMvc.perform(get("/threadlocal/right").param("id", String.valueOf(id)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    @DisplayName("wrong（反例）：从不 remove，线程被复用时第二次请求的 before 读到上一次遗留的 id，发生串号")
    void testWrongLeaksUserId() throws Exception {
        resetThreadLocal();

        String first = callWrong(1);
        // 第一次请求：ThreadLocal 已被清空，before 为 null，after 为本次传入的 id
        assertThat(first).contains("\"before\":\"" + currentThread() + ":null\"");
        assertThat(first).contains("\"after\":\"" + currentThread() + ":1\"");

        String second = callWrong(2);
        // 第二次请求：wrong 方法从不 remove，同一线程被复用后 before 读到上一次遗留的 id=1，
        // 这意味着「用户 2 看到了用户 1 的身份」，即串号
        assertThat(second).contains("\"before\":\"" + currentThread() + ":1\"");
        assertThat(second).contains("\"after\":\"" + currentThread() + ":2\"");
    }

    @Test
    @DisplayName("right（正例）：finally 中 remove，下一次请求 before 仍为 null，不会串号")
    void testRightRemovesUserId() throws Exception {
        resetThreadLocal();

        String first = callRight(3);
        assertThat(first).contains("\"before\":\"" + currentThread() + ":null\"");
        assertThat(first).contains("\"after\":\"" + currentThread() + ":3\"");

        String second = callRight(4);
        // 与 wrong 的关键差异：上一次请求结束时已在 finally 中 remove，
        // 因此即便复用同一线程，before 依然是 null
        assertThat(second).contains("\"before\":\"" + currentThread() + ":null\"");
        assertThat(second).contains("\"after\":\"" + currentThread() + ":4\"");
    }

    @Test
    @DisplayName("wrong 连续三次请求：串号会沿着线程一直传递下去")
    void testWrongLeaksAcrossMultipleRequests() throws Exception {
        resetThreadLocal();

        callWrong(10);
        String second = callWrong(20);
        String third = callWrong(30);

        // 每次请求的 before 都等于上一次请求设置的 id
        assertThat(second).contains("\"before\":\"" + currentThread() + ":10\"");
        assertThat(third).contains("\"before\":\"" + currentThread() + ":20\"");
    }

    @Test
    @DisplayName("使用限制：@RequestParam 默认必填，缺少 id 参数返回 400")
    void testMissingRequiredParam() throws Exception {
        mockMvc.perform(get("/threadlocal/wrong")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/threadlocal/right")).andExpect(status().isBadRequest());
    }

}
