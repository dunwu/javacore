package io.github.dunwu.javacore.jdk17.sealed;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link SealedClassDemo} 单元测试。
 */
@DisplayName("Java 17 密封类示例测试")
public class SealedClassDemoTest {

    @Test
    @DisplayName("示例 1：permits 限定子类并配合 instanceof 模式匹配分派")
    public void testSealedHierarchy() {
        String output = captureOutput(SealedClassDemo::sealedHierarchy);
        assertThat(output)
            .contains("张三 是全职员工，月薪 20000")
            .contains("李四 是兼职员工，时薪 100")
            .contains("王五 是实习生");
    }

    @Test
    @DisplayName("示例 2：getPermittedSubclasses 反射查看许可子类")
    public void testPermittedSubclasses() {
        String output = captureOutput(SealedClassDemo::permittedSubclasses);
        assertThat(output)
            .contains("Employee 的许可子类: ")
            .contains("FullTimeEmployee")
            .contains("PartTimeEmployee")
            .contains("Intern");
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
