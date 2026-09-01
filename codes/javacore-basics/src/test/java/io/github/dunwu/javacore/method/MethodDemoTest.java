package io.github.dunwu.javacore.method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 方法示例测试：验证各方法示例的输出
 */
@DisplayName("方法示例测试")
public class MethodDemoTest {

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
    @DisplayName("AbstractMethodDemo：抽象方法")
    public void testAbstractMethodDemo() {
        String output = captureOutput(AbstractMethodDemo::demo);
        assertThat(output).isEqualTo("call print()\n");
    }

    @Test
    @DisplayName("ConstructorMethodDemo：构造方法")
    public void testConstructorMethodDemo() {
        String output = captureOutput(ConstructorMethodDemo::demo);
        assertThat(output).isEqualTo("person name is jack\n");
    }

    @Test
    @DisplayName("DefaultMethodDemo：接口方法实现")
    public void testDefaultMethodDemo() {
        String output = captureOutput(DefaultMethodDemo::demo);
        assertThat(output).isEqualTo("Hello World\n");
    }

    @Test
    @DisplayName("FinalMethodDemo：final 方法不可重写")
    public void testFinalMethodDemo() {
        String output = captureOutput(FinalMethodDemo::demo);
        assertThat(output).isEqualTo("call Father print()\n");
    }

    @Test
    @DisplayName("MainMethodDemo：main 方法命令行参数")
    public void testMainMethodDemo() {
        String output = captureOutput(() -> MainMethodDemo.demo(new String[] { "A", "B", "C" }));
        assertThat(output).isEqualTo("arg = [A]\narg = [B]\narg = [C]\n");
    }

    @Test
    @DisplayName("MethodDemo01：方法定义与调用")
    public void testMethodDemo01() {
        String output = captureOutput(MethodDemo01::demo);
        assertThat(output).isEqualTo("Hello,LXH\nHello,LXH\nHello,LXH\nHello World!!!\n");
    }

    @Test
    @DisplayName("MethodDemo02：带参有返回值方法")
    public void testMethodDemo02() {
        String output = captureOutput(MethodDemo02::demo);
        assertThat(output).contains("addOne的计算结果：30");
        assertThat(output).contains("addTwo的计算结果：");
    }

    @Test
    @DisplayName("MethodDemo03：方法重载")
    public void testMethodDemo03() {
        String output = captureOutput(MethodDemo03::demo);
        assertThat(output).contains("add(int x,int y)的计算结果：30");
        assertThat(output).contains("(int x,int y,int z)的计算结果：60");
    }

    @Test
    @DisplayName("MethodDemo05：return 提前返回")
    public void testMethodDemo05() {
        String output = captureOutput(MethodDemo05::demo);
        assertThat(output).isEqualTo("1、调用fun()方法之前。\n3、进入fun()方法。\n2、调用fun()方法之后。\n");
    }

    @Test
    @DisplayName("MethodDemo06：递归求和")
    public void testMethodDemo06() {
        String output = captureOutput(MethodDemo06::demo);
        assertThat(output).isEqualTo("计算结果：5050\n");
    }

    @Test
    @DisplayName("MethodOverloadDemo：方法重载")
    public void testMethodOverloadDemo() {
        String output = captureOutput(MethodOverloadDemo::demo);
        assertThat(output).isEqualTo("x + y = 30\nx + y = 3.0\n");
    }

    @Test
    @DisplayName("MethodOverrideDemo：方法重写")
    public void testMethodOverrideDemo() {
        String output = captureOutput(MethodOverrideDemo::demo);
        assertThat(output).isEqualTo("会动\n会跑\n");
    }

    @Test
    @DisplayName("MethodParamDemo：基本类型参数传值")
    public void testMethodParamDemo() {
        String output = captureOutput(MethodParamDemo::demo);
        assertThat(output).isEqualTo("num = [0]\nnum = [0]\n");
    }

    @Test
    @DisplayName("MethodParamDemo2：引用类型参数传值")
    public void testMethodParamDemo2() {
        String output = captureOutput(MethodParamDemo2::demo);
        assertThat(output).isEqualTo("sb = [A]\nsb = [A]\nsb = [C]\n");
    }

    @Test
    @DisplayName("RecursionMethodDemo：斐波那契数列")
    public void testRecursionMethodDemo() {
        String output = captureOutput(RecursionMethodDemo::demo);
        assertThat(output).isEqualTo("1\t1\t2\t3\t5\t8\t13\t21\t34\t");
    }

    @Test
    @DisplayName("VarargsDemo：可变参数")
    public void testVarargsDemo() {
        String output = captureOutput(VarargsDemo::demo);
        assertThat(output).isEqualTo(
            "params.length = 1\nparams = [red]\nparams.length = 2\nparams = [red]\nparams = [yellow]\nparams.length = 3\nparams = [red]\nparams = [yellow]\nparams = [blue]\n");
    }

}
