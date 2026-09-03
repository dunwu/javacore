package io.github.dunwu.javacore.object;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * {@link InnerClassDemo} 单元测试：成员内部类、静态内部类、局部内部类、匿名内部类。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
@DisplayName("四类内部类示例测试")
public class InnerClassDemoTest {

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

    @Test
    @DisplayName("成员内部类：依附外部实例，可直接访问外部私有字段")
    void testMemberInner() {
        String output = captureOutput(InnerClassDemo::memberInner);
        assertThat(output).isEqualTo("成员内部类读到外部私有字段: 外部实例 A 的私有字段\n"
            + "成员内部类自己的字段: 成员内部类自己的字段\n"
            // 关键结论：成员内部类隐含持有 Outer.this，因此与外部实例是绑定的
            + "成员内部类持有的外部实例是同一个: true\n"
            + "换一个外部实例后读到: 外部实例 B 的私有字段\n");
    }

    @Test
    @DisplayName("静态内部类：不依附外部实例，Builder 是典型用法")
    void testStaticNested() {
        String output = captureOutput(InnerClassDemo::staticNested);
        assertThat(output).isEqualTo("静态内部类读到静态字段: 外部类的静态字段\n"
            + "Builder 构造出的外部对象字段: 由 Builder 构造\n");
    }

    @Test
    @DisplayName("局部内部类：作用域限于方法内，可捕获事实 final 的局部变量")
    void testLocalInner() {
        String output = captureOutput(InnerClassDemo::localInner);
        assertThat(output).isEqualTo("局部内部类 读到了 counter = 10\n"
            + "实例方法里的局部内部类读到: 外部实例的私有字段\n");
    }

    @Test
    @DisplayName("匿名内部类：定义即实例化，this 指向自身而非外部类")
    void testAnonymous() {
        String output = captureOutput(InnerClassDemo::anonymous);
        assertThat(output).isEqualTo("匿名内部类实现的 Runnable\n"
            + "匿名内部类中 this 指向自身: true\n"
            + "匿名子类实现了抽象方法: 你好，我是匿名子类\n");
    }

    @Test
    @DisplayName("demo：完整演示可正常执行")
    void testDemo() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(InnerClassDemo::demo)).doesNotThrowAnyException();
    }

}
