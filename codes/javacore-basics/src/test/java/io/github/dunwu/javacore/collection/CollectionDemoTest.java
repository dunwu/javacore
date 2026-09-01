package io.github.dunwu.javacore.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * collection 包示例测试：Collection 通用操作、Countries 数据、TreeMap。
 * <p>
 * 注意：VectorDemo 演示并发读写冲突，会无限循环运行，不在测试中执行。
 */
@DisplayName("集合示例测试")
public class CollectionDemoTest {

    @Test
    @DisplayName("CollectionDemo：Collection 通用操作")
    void testCollectionDemo() {
        String output = captureOutput(CollectionDemo::demo);
        assertThat(output).contains("[ALGERIA, ANGOLA, BENIN, BOTSWANA, BULGARIA, BURKINA FASO, ten, eleven]");
        assertThat(output).contains("Collections.max(c) = ten");
        assertThat(output).contains("Collections.min(c) = ALGERIA");
        assertThat(output).contains("c.contains(BOTSWANA) = true");
        assertThat(output).contains("c.containsAll(c2) = true");
        assertThat(output).contains("c2.isEmpty() = true");
        assertThat(output).contains("after c.clear():[]");
    }

    @Test
    @DisplayName("Countries：用国家数据构造各种集合")
    void testCountries() {
        String output = captureOutput(Countries::demo);
        assertThat(output).contains("ALGERIA=Algiers");
        assertThat(output).contains("{ALGERIA=Algiers, ANGOLA=Luanda, BENIN=Porto-Novo}");
        assertThat(output).contains("Brasilia");
    }

    @Test
    @DisplayName("Countries：names 与 capitals 取部分数据")
    void testCountriesApi() {
        assertThat(Countries.names(3)).containsExactly("ALGERIA", "ANGOLA", "BENIN");
        assertThat(Countries.capitals(2)).containsEntry("ALGERIA", "Algiers");
        assertThat(Countries.capitals().get("BRAZIL")).isEqualTo("Brasilia");
        assertThat(Countries.names()).isNotEmpty();
    }

    @Test
    @DisplayName("TreeMapDemo：TreeMap 有序特性与视图方法")
    void testTreeMapDemo() {
        String output = captureOutput(TreeMapDemo::demo);
        assertThat(output).contains("0=A");
        assertThat(output).contains("25=Z");
        assertThat(output).contains("{3=D, 4=E, 5=F, 6=G}");
    }

    /**
     * 捕获 System.out 输出。
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
