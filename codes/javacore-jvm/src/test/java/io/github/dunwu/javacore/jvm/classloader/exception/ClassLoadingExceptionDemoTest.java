package io.github.dunwu.javacore.jvm.classloader.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * classloader.exception 包类加载相关异常示例单元测试
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("类加载异常示例测试")
public class ClassLoadingExceptionDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String capture(ThrowingRunnable action, boolean error) {
        PrintStream original = error ? System.err : System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            PrintStream redirected = new PrintStream(buffer, true, StandardCharsets.UTF_8);
            if (error) {
                System.setErr(redirected);
            } else {
                System.setOut(redirected);
            }
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (error) {
                System.setErr(original);
            } else {
                System.setOut(original);
            }
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("ClassCastException：无继承关系的两个类型之间向下转型，运行期失败")
    void testClassCastExceptionDemo() {
        // 编译器只检查语法上是否存在可能的转换路径，运行期才校验对象的真实类型
        String output = capture(() -> ClassCastExceptionDemo.main(new String[0]), false);
        assertThat(output).contains("捕获到 ClassCastException：");
        assertThat(output).contains("java.lang.Object cannot be cast to");
    }

    @Test
    @DisplayName("ClassNotFoundException：Class.forName 加载不存在的类时抛出，属受检异常必须捕获")
    void testClassNotFoundExceptionDemo() {
        // printStackTrace() 输出到标准错误流，因此捕获 System.err
        String err = capture(() -> ClassNotFoundExceptionDemo.main(new String[0]), true);
        assertThat(err).contains("java.lang.ClassNotFoundException: NotFound");
    }

    @Test
    @DisplayName("UnsatisfiedLinkError：静态块加载不存在的本地库，首次触发类初始化即失败")
    void testUnsatisfiedLinkErrorDemo() {
        // Class.forName 会触发类初始化，静态块中的 System.loadLibrary("NoLib") 找不到本地库而抛错。
        // 首次触发抛 UnsatisfiedLinkError；若该类已被 JVM 标记为初始化失败，再次触发则抛 NoClassDefFoundError。
        // 二者的共同父类都是 LinkageError，因此以 LinkageError 断言，避免依赖测试执行顺序。
        assertThatThrownBy(
            () -> Class.forName("io.github.dunwu.javacore.jvm.classloader.exception.UnsatisfiedLinkErrorDemo"))
                .isInstanceOf(LinkageError.class);
    }

}
