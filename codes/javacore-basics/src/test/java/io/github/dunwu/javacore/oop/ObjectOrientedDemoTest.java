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

    @Test
    @DisplayName("Employee/Salary：抽象类继承与动态绑定，父类引用同样调用子类重写方法")
    public void testEmployeeSalary() {
        String output = captureOutput(() -> {
            Salary s = new Salary("Mohd Mohtashim", "Ambehta, UP", 3, 3600.00);
            // 向上转型：父类抽象类引用指向子类对象
            Employee e = new Salary("John Adams", "Boston, MA", 2, 2400.00);
            System.out.println("Call mailCheck using Salary reference --");
            s.mailCheck();
            System.out.println("Call mailCheck using Employee reference--");
            e.mailCheck();
        });
        assertThat(output).isEqualTo(
            // 构造 Salary 时通过 super(...) 先执行父类构造器
            "Constructing an Employee\n"
                + "Constructing an Employee\n"
                + "Call mailCheck using Salary reference --\n"
                // 关键：不论用子类引用还是父类引用，调用的都是 Salary 重写的 mailCheck
                + "Within mailCheck of Salary class \n"
                + "Mailing check to Mohd Mohtashim with salary 3600.0\n"
                + "Call mailCheck using Employee reference--\n"
                + "Within mailCheck of Salary class \n"
                + "Mailing check to John Adams with salary 2400.0\n");
    }

}
