package io.github.dunwu.javacore.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web 应用骨架与 {@link HelloController} 单元测试
 * <p>
 * 使用 {@code webEnvironment = MOCK}（默认值）+ MockMvc：不启动真实 Tomcat，因此不会占用
 * {@code application.properties} 中配置的 18080 端口，也不受 {@code server.tomcat.max-threads=1} 影响。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Web 应用骨架与 HelloController 测试")
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @DisplayName("应用上下文可正常加载，CustomConfig 基于 RestTemplateBuilder 提供 RestTemplate Bean")
    void testContextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    @Test
    @DisplayName("GET / 返回 Hello World")
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello World"));
    }

    @Test
    @DisplayName("GET /hello 返回 Hello World，与 / 映射到同一个处理方法")
    void testHello() throws Exception {
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello World"));
    }

    @Test
    @DisplayName("未映射的路径返回 404")
    void testUnknownPath() throws Exception {
        mockMvc.perform(get("/not-exist")).andExpect(status().isNotFound());
    }

}
