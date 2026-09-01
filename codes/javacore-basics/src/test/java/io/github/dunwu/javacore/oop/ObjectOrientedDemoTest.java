package io.github.dunwu.javacore.oop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * 面向对象示例测试：验证各示例的输出与行为
 */
@DisplayName("面向对象示例测试")
public class ObjectOrientedDemoTest {

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
    @DisplayName("Programmer：静态常量与实例属性")
    public void testProgrammer() {
        String output = captureOutput(Programmer::demo);
        assertThat(output).isEqualTo("I am a programmer\nMy name is zp\nMy core skill is programming\n");
    }

    @Test
    @DisplayName("Test：多态与类型转换")
    public void testTest() {
        String output = captureOutput(io.github.dunwu.javacore.oop.Test::demo);
        assertThat(output).isEqualTo("吃鱼\n抓老鼠\n吃骨头\n看家\n吃鱼\n抓老鼠\n");
    }

    @Test
    @DisplayName("PackageDemo/PackageDemo2：包的使用（输出当前时间，不断言内容）")
    public void testPackageDemos() {
        assertThatCode(PackageDemo::demo).doesNotThrowAnyException();
        assertThatCode(PackageDemo2::demo).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Person：类的实例化")
    public void testPerson() {
        assertThatCode(Person::demo).doesNotThrowAnyException();
    }

}
