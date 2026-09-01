package io.github.dunwu.javacore.access;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * access 包示例测试：包、import、静态导入、访问权限。
 */
@DisplayName("Java 包与访问权限示例测试")
public class AccessDemoTest {

    @Test
    @DisplayName("同包类直接访问：无需 import 即可使用 Hello")
    public void testPackageDemo01() {
        String output = captureOutput(PackageDemo01::demo);
        assertThat(output).contains("Hello World!!!");
    }

    @Test
    @DisplayName("import 单个类：可直接用短名称调用 Arrays")
    public void testImportDemo01() {
        String output = captureOutput(ImportDemo01::demo);
        assertThat(output).contains("Arrays.toString: [3, 1, 2]");
    }

    @Test
    @DisplayName("导入项目内其他包的自定义类")
    public void testImportDemo02() {
        String output = captureOutput(ImportDemo02::demo);
        assertThat(output).contains("姓名：张三，年龄：30");
    }

    @Test
    @DisplayName("jar 包中的类使用方式与本地类一致")
    public void testImportJarDemo() {
        String output = captureOutput(ImportJarDemo::demo);
        assertThat(output).contains("Hello World!!!");
    }

    @Test
    @DisplayName("静态导入：直接调用静态方法无需类名前缀")
    public void testStaticImportDemo() {
        String output = captureOutput(StaticImportDemo::demo);
        assertThat(output)
            .contains("3 + 3 = 6")
            .contains("3 - 2 = 1")
            .contains("3 * 3 = 9")
            .contains("3 / 3 = 1");
    }

    @Test
    @DisplayName("protected：子类可访问父类受保护属性")
    public void testProtectedDemo01() {
        String output = captureOutput(ProtectedDemo01::demo);
        assertThat(output).contains("访问受保护属性：Zhang Peng");
    }

    @Test
    @DisplayName("protected：不同包的非子类只能访问 public 方法")
    public void testProtectedDemo02() {
        String output = captureOutput(ProtectedDemo02::demo);
        assertThat(output).contains("只能调用 public 方法：Hello World!!!");
    }

    @Test
    @DisplayName("Hello：public 方法可被任意类调用")
    public void testHelloGetInfo() {
        Hello hello = new Hello();
        assertThat(hello.getInfo()).isEqualTo("Hello World!!!");
    }

    @Test
    @DisplayName("Operate：静态工具方法四则运算")
    public void testOperate() {
        assertThat(Operate.add(3, 3)).isEqualTo(6);
        assertThat(Operate.sub(3, 2)).isEqualTo(1);
        assertThat(Operate.mul(3, 3)).isEqualTo(9);
        assertThat(Operate.div(6, 3)).isEqualTo(2);
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
