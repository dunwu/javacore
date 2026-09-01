package io.github.dunwu.javacore.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 枚举示例测试：验证各枚举示例的输出
 */
@DisplayName("枚举示例测试")
public class EnumerationDemoTest {

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
    @DisplayName("App：策略枚举薪资计算 + EnumSet + EnumMap")
    public void testApp() {
        String output = captureOutput(App::demo);
        assertThat(output).contains("时薪100的人在周五工作8小时的收入：800.0");
        assertThat(output).contains("时薪100的人在周六工作8小时的收入：1200.0");
        assertThat(output).contains("EnumSet展示");
        assertThat(output).contains("EnumMap展示");
        assertThat(output).contains("RED : 红灯");
    }

    @Test
    @DisplayName("AddMethod2EnumDemo：枚举中添加方法")
    public void testAddMethod2EnumDemo() {
        String output = captureOutput(AddMethod2EnumDemo::demo);
        assertThat(output).contains("code: 0, description: 成功");
        assertThat(output).contains("code: 100, description: 错误A");
        assertThat(output).contains("code: 200, description: 错误B");
    }

    @Test
    @DisplayName("ErrorCodeEn：错误码枚举")
    public void testErrorCodeEn() {
        String output = captureOutput(ErrorCodeEn::demo);
        assertThat(output).contains("code: 0, description: 成功");
        assertThat(output).contains("code: 200, description: 错误B");
    }

    @Test
    @DisplayName("EnumMethodDemo：枚举常用方法")
    public void testEnumMethodDemo() {
        String output = captureOutput(EnumMethodDemo::demo);
        assertThat(output).contains("RED ordinal: 0");
        assertThat(output).contains("green name(): GREEN");
        assertThat(output).contains("green valueOf(): BLUE");
        assertThat(output).contains("green compareTo Color.GREEN: 0");
        assertThat(output).contains("green equals Color.GREEN: true");
        assertThat(output).contains("green equals Size.MIDDLE: false");
    }

    @Test
    @DisplayName("EnumMapDemo：EnumMap 按枚举定义顺序遍历")
    public void testEnumMapDemo() {
        String output = captureOutput(EnumMapDemo::demo);
        assertThat(output).isEqualTo("EnumMap展示\nGREEN : 绿灯\nYELLOW : 黄灯\nRED : 红灯\n");
    }

    @Test
    @DisplayName("EnumSetDemo：EnumSet 取全部枚举常量")
    public void testEnumSetDemo() {
        String output = captureOutput(EnumSetDemo::demo);
        assertThat(output).isEqualTo("EnumSet展示\nOK : 0\nERROR_A : 1\nERROR_B : 2\n");
    }

    @Test
    @DisplayName("EnumInClassDemo：类中定义枚举")
    public void testEnumInClassDemo() {
        String output = captureOutput(EnumInClassDemo::demo);
        assertThat(output).isEqualTo("土豆\n西红柿\n");
    }

    @Test
    @DisplayName("EnumInInterfaceDemo：接口中定义枚举")
    public void testEnumInInterfaceDemo() {
        String output = captureOutput(EnumInInterfaceDemo::demo);
        assertThat(output).isEqualTo("苹果\n桔子\n香蕉\n");
    }

    @Test
    @DisplayName("ErrorCodeEnumDemo：错误码枚举 toString")
    public void testErrorCodeEnumDemo() {
        String output = captureOutput(ErrorCodeEnumDemo::demo);
        assertThat(output).contains("ErrorCodeEn All Elements: [0, 100, 200, ]");
        assertThat(output).contains("ErrorCodeEn{code=0, msg='成功'}");
        assertThat(output).contains("ErrorCodeEn{code=200, msg='错误B'}");
    }

    @Test
    @DisplayName("SingleEnumDemo：枚举单例")
    public void testSingleEnumDemo() {
        String output = captureOutput(SingleEnumDemo::demo);
        assertThat(output).isEqualTo("zp\n");
    }

    @Test
    @DisplayName("StateMachineDemo：枚举状态机")
    public void testStateMachineDemo() {
        String output = captureOutput(StateMachineDemo::demo);
        assertThat(output).isEqualTo("红灯停\n");
    }

}
