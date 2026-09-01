package io.github.dunwu.javacore.jdk17.sealed;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link SealedShapeDemo} 单元测试。
 */
@DisplayName("Java 17 sealed 接口 + record 组合示例测试")
public class SealedShapeDemoTest {

    @Test
    @DisplayName("示例 1：封闭图形体系按类型分派计算面积与周长")
    public void testShapeAreaAndPerimeter() {
        String output = captureOutput(SealedShapeDemo::shapeAreaAndPerimeter);
        assertThat(output)
            .contains("Circle -> 面积: 12.57, 周长: 12.57")
            .contains("Square -> 面积: 9.00, 周长: 12.00")
            .contains("Triangle -> 面积: 6.00, 周长: 12.00");
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
