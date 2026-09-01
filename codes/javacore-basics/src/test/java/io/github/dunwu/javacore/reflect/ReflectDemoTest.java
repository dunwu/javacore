package io.github.dunwu.javacore.reflect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 反射示例测试：验证各反射示例的输出。
 * 不测试：MethodDemo01/MethodDemo02（输出到 stderr 的调用轨迹观察）、
 * MethodPerformDemo01~04（20 亿次循环的性能基准）。
 */
@DisplayName("反射示例测试")
public class ReflectDemoTest {

    @FunctionalInterface
    private interface ThrowingRunnable {

        void run() throws Exception;
    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("InstanceofDemo：instanceof 类型判断")
    public void testInstanceofDemo() {
        String output = captureOutput(InstanceofDemo::demo);
        assertThat(output).isEqualTo("ArrayList is List\nArrayList is List\n");
    }

    @Test
    @DisplayName("InvocationHandlerDemo：动态代理增强调用")
    public void testInvocationHandlerDemo() {
        String output = captureOutput(InvocationHandlerDemo::demo);
        assertThat(output).contains("Before method");
        assertThat(output).contains("Hello  World");
        assertThat(output).contains("Goodbye");
        assertThat(output).contains("Result is: Over");
    }

    @Test
    @DisplayName("NewInstanceDemo：反射创建实例")
    public void testNewInstanceDemo() {
        String output = captureOutput(NewInstanceDemo::demo);
        assertThat(output).isEqualTo("aaa\nbbb\n");
    }

    @Test
    @DisplayName("ReflectArrayDemo：反射操作数组")
    public void testReflectArrayDemo() {
        String output = captureOutput(ReflectArrayDemo::demo);
        assertThat(output).isEqualTo("Scala\n");
    }

    @Test
    @DisplayName("ReflectClassDemo01：Class.forName 获取 Class")
    public void testReflectClassDemo01() {
        String output = captureOutput(ReflectClassDemo01::demo);
        assertThat(output).isEqualTo(
            "io.github.dunwu.javacore.reflect.ReflectClassDemo01\ndouble[]\njava.lang.String[][]\n");
    }

    @Test
    @DisplayName("ReflectClassDemo02：类名.class 获取 Class")
    public void testReflectClassDemo02() {
        String output = captureOutput(ReflectClassDemo02::demo);
        assertThat(output).isEqualTo("boolean\njava.io.PrintStream\nint[][][]\n");
    }

    @Test
    @DisplayName("ReflectClassDemo03：getClass 获取 Class")
    public void testReflectClassDemo03() {
        String output = captureOutput(ReflectClassDemo03::demo);
        assertThat(output).isEqualTo(
            "java.lang.String\nio.github.dunwu.javacore.reflect.ReflectClassDemo03.E\nbyte[]\njava.util.HashSet\n");
    }

    @Test
    @DisplayName("ReflectClassDemo04：通过成员关系获取 Class")
    public void testReflectClassDemo04() {
        String output = captureOutput(ReflectClassDemo04::demo);
        assertThat(output).contains("java.util.AbstractList");
        assertThat(output).contains("java.lang.Character.UnicodeBlock");
        assertThat(output).contains("java.lang.System");
        assertThat(output).contains("java.lang.Thread");
    }

    @Test
    @DisplayName("ReflectDemo：反射综合示例")
    public void testReflectDemo() {
        String output = captureOutput(ReflectDemo::demo);
        assertThat(output).contains("clazz1 的类名：io.github.dunwu.javacore.reflect.ReflectDemo$Person");
        assertThat(output).contains("[Person]name = Tom, age = 20");
        assertThat(output).contains("============== 打印Person类声明信息 ==============");
    }

    @Test
    @DisplayName("ReflectFieldDemo：反射获取属性类型")
    public void testReflectFieldDemo() {
        String output = captureOutput(ReflectFieldDemo::demo);
        assertThat(output).contains("Type: class [[Z");
        assertThat(output).contains("Type: class java.lang.String");
        assertThat(output).contains("Type: interface java.util.List");
        assertThat(output).contains("Type: class java.lang.Object");
    }

    @Test
    @DisplayName("ReflectMethodConstructorDemo：反射获取构造方法")
    public void testReflectMethodConstructorDemo() {
        String output = captureOutput(ReflectMethodConstructorDemo::demo);
        assertThat(output).contains("String getDeclaredConstructors 清单");
        assertThat(output).contains("String getConstructors 清单");
        assertThat(output).contains("bbb");
    }

    @Test
    @DisplayName("ReflectMethodDemo：反射获取并调用方法")
    public void testReflectMethodDemo() {
        String output = captureOutput(ReflectMethodDemo::demo);
        assertThat(output).contains("System getDeclaredMethods 清单");
        assertThat(output).contains("currentTimeMillis");
    }

    @Test
    @DisplayName("ReflectTypeDemo：包装类 TYPE 属性")
    public void testReflectTypeDemo() {
        String output = captureOutput(ReflectTypeDemo::demo);
        assertThat(output).isEqualTo("double\nvoid\n");
    }

    @Test
    @DisplayName("proxy.App：动态代理购买示例")
    public void testProxyApp() {
        String output = captureOutput(io.github.dunwu.javacore.reflect.proxy.App::demo);
        assertThat(output).isEqualTo("亚马逊代购\n张三 购买 进口奶粉\n");
    }

}
