package io.github.dunwu.javacore.generics;

import org.junit.Test;

/**
 * 泛型示例
 * @author Zhang Peng
 */
public class GenericsDemoTest {
    @Test
    public void testString() {
        GenericsDemo<String> p = new GenericsDemo<>(); // 里面的var类型为String类型
        p.setVar("javase"); // 设置字符串
        System.out.println(p.getVar()); // 取得字符串的长度
    }

    @Test
    public void testIntegerToObject() {
        GenericsDemo02<String> p = new GenericsDemo02<>("泛型"); // 里面的var类型为String类型
        System.out.println("内容：" + p.getVar());
    }

    @Test
    public void testDoubleGenerics() {
        GenericsDemo03<Integer, String> t = new GenericsDemo03<>(); // 定义两个泛型类型的对象
        t.setX(12345); // 设置第一个内容
        t.setY("上山打老虎"); // 设置第二个内容
        System.out.println("上句；" + t.getX()); // 取得信息
        System.out.println("下句；" + t.getY()); // 取得信息
    }

    @Test
    public void testDoubleGenerics02() {
        GenericsDemo03<Integer, String> p = new GenericsDemo03<>();
        p.setX(10); // 利用自动装箱操作：int --> Integer
        p.setY("北纬210度"); // 利用自动装箱操作：int --> Integer
        System.out.println("整数表示，X坐标为：" + p.getX());
        System.out.println("整数表示，Y坐标为：" + p.getY());
    }

    @Test
    public void test() {
        GenericsDemo demo = new GenericsDemo();
        demo.setVar("泛型"); // 设置字符串
        System.out.println("内容：" + demo.getVar());
    }

    @Test
    public void test2() {
        GenericsDemo<Object> i = new GenericsDemo<>(); // 指定Object为泛型类型
        i.setVar("泛型"); // 设置字符串
        System.out.println("内容：" + i.getVar());
    }
}
