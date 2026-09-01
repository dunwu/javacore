package io.github.dunwu.javacore.container.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * javacore-container map 包示例的单元测试
 */
@DisplayName("Map 示例测试")
public class MapDemoTest {

    @Test
    @DisplayName("HashMap 存入与按键取值")
    public void testHashMapDemo01() {
        String output = captureOutput(HashMapDemo01::demo);
        assertThat(output).contains("取出的内容是：www.mldn.cn");
    }

    @Test
    @DisplayName("containsKey 与 containsValue 判存")
    public void testHashMapDemo02() {
        String output = captureOutput(HashMapDemo02::demo);
        assertThat(output).contains("搜索的key存在！");
        assertThat(output).contains("搜索的value存在！");
    }

    @Test
    @DisplayName("keySet 遍历全部 key")
    public void testHashMapDemo03() {
        String output = captureOutput(HashMapDemo03::demo);
        assertThat(output).contains("mldn、");
        assertThat(output).contains("zhinangtuan、");
        assertThat(output).contains("mldnjava、");
    }

    @Test
    @DisplayName("values 遍历全部 value")
    public void testHashMapDemo04() {
        String output = captureOutput(HashMapDemo04::demo);
        assertThat(output).contains("www.mldn.cn、");
        assertThat(output).contains("www.zhinangtuan.net.cn、");
        assertThat(output).contains("www.mldnjava.cn、");
    }

    @Test
    @DisplayName("String 作 key 取值必然命中")
    public void testHashMapDemo05() {
        String output = captureOutput(HashMapDemo05::demo);
        assertThat(output).contains("姓名：张三；年龄：30");
    }

    @Test
    @DisplayName("重写 equals/hashCode 的自定义对象作 key 可被新对象命中")
    public void testHashMapDemo06() {
        String output = captureOutput(HashMapDemo06::demo);
        assertThat(output).contains("zhangsan");
    }

    @Test
    @DisplayName("同一对象引用作 key 必然命中")
    public void testHashMapDemo07() {
        String output = captureOutput(HashMapDemo07::demo);
        assertThat(output).contains("zhangsan");
    }

    @Test
    @DisplayName("类内 Person 重写 hashCode/equals 后内容相同的新对象可命中")
    public void testHashMapDemo08() {
        String output = captureOutput(HashMapDemo08::demo);
        assertThat(output).contains("zhangsan");
    }

    @Test
    @DisplayName("遍历 Map 的全部 key 与 value")
    public void testHashtableDemo01() {
        String output = captureOutput(HashtableDemo01::demo);
        assertThat(output).contains("全部的key：");
        assertThat(output).contains("mldn");
        assertThat(output).contains("全部的value：");
        assertThat(output).contains("www.mldn.cn");
    }

    @Test
    @DisplayName("SortedMap 的 firstKey/lastKey 与范围查询")
    public void testSortedMapDemo() {
        String output = captureOutput(SortedMapDemo::demo);
        assertThat(output).contains("第一个元素的内容的key：A、mldn：对应的值：www.mldn.cn");
        assertThat(output).contains("最后一个元素的内容的key：D、jiangker：对应的值：http://www.jiangker.com/");
        assertThat(output).contains("返回小于指定范围的集合：");
        assertThat(output).contains("A、mldn --> www.mldn.cn");
        assertThat(output).contains("返回大于指定范围的集合：");
        assertThat(output).contains("B、mldnjava --> www.mldnjava.cn");
        assertThat(output).contains("部分集合：");
    }

    @Test
    @DisplayName("TreeMap 按 key 排序与范围查询")
    public void testTreeMapDemo() {
        String output = captureOutput(TreeMapDemo::demo);
        assertThat(output).contains("0=A");
        assertThat(output).contains("25=Z");
        assertThat(output).contains("{3=D, 4=E, 5=F, 6=G}");
    }

    @Test
    @DisplayName("TreeMap 按 key 自然序遍历")
    public void testTreeMapDemo01() {
        String output = captureOutput(TreeMapDemo01::demo);
        assertThat(output).isEqualTo(
            "A、mldn --> www.mldn.cn\n"
                + "B、mldnjava --> www.mldnjava.cn\n"
                + "C、zhinangtuan --> www.zhinangtuan.net.cn\n");
    }

    @Test
    @DisplayName("WeakHashMap 存入与输出")
    public void testWeakHashMapDemo01() {
        String output = captureOutput(WeakHashMapDemo01::demo);
        assertThat(output).contains("mldn=www.mldn.cn");
        assertThat(output).contains("lxh=lixinghua");
    }

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

}
