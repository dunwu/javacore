package io.github.dunwu.javacore.container.list;

import io.github.dunwu.javacore.container.bean.Countries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * javacore-container list 包示例的单元测试
 */
@DisplayName("List 示例测试")
public class ListDemoTest {

    @Test
    @DisplayName("ArrayList 在指定位置插入元素与集合")
    public void testArrayListDemo01() {
        String output = captureOutput(ArrayListDemo01::demo);
        assertThat(output).contains("[hello, world]");
        assertThat(output).contains("[ABC, abc, hello, world, ABC, abc]");
    }

    @Test
    @DisplayName("ArrayList 按位置删除与按内容删除")
    public void testArrayListDemo02() {
        String output = captureOutput(ArrayListDemo02::demo);
        assertThat(output).contains("[MLDN, www.mldn.cn]");
    }

    @Test
    @DisplayName("ArrayList 通过下标双向遍历")
    public void testArrayListDemo03() {
        String output = captureOutput(ArrayListDemo03::demo);
        assertThat(output).contains("由前向后输出：World、Hello、Hello、MLDN、www.mldn.cn、");
        assertThat(output).contains("由后向前输出：www.mldn.cn、MLDN、Hello、Hello、World、");
    }

    @Test
    @DisplayName("ArrayList 转换为数组")
    public void testArrayListDemo04() {
        String output = captureOutput(ArrayListDemo04::demo);
        assertThat(output).contains("指定数组类型：World、Hello、MLDN、www.mldn.cn、");
        assertThat(output).contains("返回对象数组：World、Hello、MLDN、www.mldn.cn、");
    }

    @Test
    @DisplayName("ArrayList 判空、包含、截取、定位")
    public void testArrayListDemo05() {
        String output = captureOutput(ArrayListDemo05::demo);
        assertThat(output).contains("集合操作前是否为空？true");
        assertThat(output).contains("\"Hello\"字符串存在！");
        assertThat(output).contains("集合截取：");
        assertThat(output).contains("MLDN、");
        assertThat(output).contains("MLDN字符串的位置：2");
        assertThat(output).contains("集合操作后是否为空？false");
    }

    @Test
    @DisplayName("ArrayList 存储自定义对象并批量加薪")
    public void testArrayListTest() {
        String output = captureOutput(ArrayListTest::demo);
        assertThat(output).contains("name=Carl Cracker,salary=78750.0");
        assertThat(output).contains("name=Harry Hacker,salary=52500.0");
        assertThat(output).contains("name=Tony Tester,salary=42000.0");
    }

    @Test
    @DisplayName("Vector 基本用法")
    public void testVectorDemo() {
        String output = captureOutput(VectorDemo::demo);
        assertThat(output).contains("A\nB\nC");
        assertThat(output).contains("X\nY\nZ");
    }

    @Test
    @DisplayName("LinkedList 三种遍历方式与删除")
    public void testLinkedListDemo01() {
        String output = captureOutput(LinkedListDemo01::demo);
        assertThat(output).contains("初始化链表：[A, B, C]");
        assertThat(output).contains("第一次遍历");
        assertThat(output).contains("第二次遍历");
        assertThat(output).contains("第三次遍历");
        assertThat(output).contains("A B C ");
        assertThat(output).contains("链表：[A, B]");
    }

    @Test
    @DisplayName("LinkedList 作双端队列：element/peek 不移除，poll 移除头部")
    public void testLinkedListDemo02() {
        String output = captureOutput(LinkedListDemo02::demo);
        assertThat(output).contains("初始化 LinkedList：[X, A, B, C, Y]");
        assertThat(output).contains("1-1、element()方法找到表头：X");
        assertThat(output).contains("2-1、peek()方法找到表头：X");
        assertThat(output).contains("3-1、poll()方法找到表头：X");
        assertThat(output).contains("3-2、找完之后的链表的内容：[A, B, C, Y]");
        assertThat(output).contains("以FIFO的方式输出：A、B、C、");
    }

    @Test
    @DisplayName("LinkedList FIFO 弹出直至取空")
    public void testLinkedListDemo03() {
        String output = captureOutput(LinkedListDemo03::demo);
        assertThat(output).isEqualTo("以FIFO的方式输出：A、B、C、");
    }

    @Test
    @DisplayName("Collections.emptyList 不可变，add 抛 UnsupportedOperationException")
    public void testCollectionsDemo01() {
        assertThatThrownBy(() -> CollectionsDemo01.main(new String[0]))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Collections.addAll 批量添加")
    public void testCollectionsDemo02() {
        String output = captureOutput(CollectionsDemo02::demo);
        assertThat(output).isEqualTo("MLDN、LXH、mldnjava、");
    }

    @Test
    @DisplayName("Collections.reverse 反转")
    public void testCollectionsDemo03() {
        String output = captureOutput(CollectionsDemo03::demo);
        assertThat(output).isEqualTo("mldnjava、LXH、MLDN、");
    }

    @Test
    @DisplayName("Collections.binarySearch 二分查找")
    public void testCollectionsDemo04() {
        String output = captureOutput(CollectionsDemo04::demo);
        assertThat(output).contains("检索结果：");
    }

    @Test
    @DisplayName("Collections.replaceAll 替换元素")
    public void testCollectionsDemo05() {
        String output = captureOutput(CollectionsDemo05::demo);
        assertThat(output).contains("内容替换成功！");
        assertThat(output).contains("替换之后的结果：[MLDN, 李兴华, mldnjava]");
    }

    @Test
    @DisplayName("Collections.sort 自然序排序")
    public void testCollectionsDemo06() {
        String output = captureOutput(CollectionsDemo06::demo);
        assertThat(output).contains("排序之前的集合：[1、MLDN, 2、LXH, 3、mldnjava, B、www.mldn.cn, A、www.mldnjava.cn]");
        assertThat(output).contains("排序之后的集合：[1、MLDN, 2、LXH, 3、mldnjava, A、www.mldnjava.cn, B、www.mldn.cn]");
    }

    @Test
    @DisplayName("Collections.swap 交换元素")
    public void testCollectionsDemo07() {
        String output = captureOutput(CollectionsDemo07::demo);
        assertThat(output).contains("交换之前的集合：[1、MLDN, 2、LXH, 3、mldnjava]");
        assertThat(output).contains("交换之后的集合：[3、mldnjava, 2、LXH, 1、MLDN]");
    }

    @Test
    @DisplayName("Collection 接口常用方法综合演示")
    public void testCollectionDemo() {
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
    @DisplayName("Collection 添加元素")
    public void testCollectionDemo2() {
        String output = captureOutput(CollectionDemo2::demo);
        assertThat(output).contains("[12345, abced]");
    }

    @Test
    @DisplayName("Countries 样本数据在不同容器中的展示")
    public void testCountries() {
        String output = captureOutput(Countries::demo);
        assertThat(output).contains("ALGERIA=Algiers");
        assertThat(output).contains("ANGOLA=Luanda");
        assertThat(output).contains("BENIN=Porto-Novo");
        assertThat(output).contains("Brasilia");
        assertThat(Countries.names(3)).containsExactly("ALGERIA", "ANGOLA", "BENIN");
    }

    @Test
    @DisplayName("subList 视图陷阱：原列表结构变化后访问子列表抛异常")
    public void testListSubListDemo() {
        String output = captureOutput(ListSubListDemo::demo);
        assertThat(output).contains("[2, 3, 4]");
        assertThat(output).contains("[1, 2, 4, 5, 6, 7, 8, 9, 10]");
    }

    @Test
    @DisplayName("Arrays.asList 的两个经典坑（wrong 演示问题，right 演示修复）")
    public void testArraysAsListDemo() {
        String output = captureOutput(() -> ArraysAsListDemo.main(new String[0]));
        assertThat(output).contains("wrong1");
        assertThat(output).contains("right1");
        assertThat(output).contains("wrong2");
        assertThat(output).contains("right2");
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
