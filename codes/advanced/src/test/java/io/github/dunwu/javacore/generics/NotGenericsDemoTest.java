package io.github.dunwu.javacore.generics;

import org.junit.Test;

/**
 * 非泛型示例
 * @author Zhang Peng
 */
public class NotGenericsDemoTest {
    @Test
    public void testIntegerToObject() {
        NotGenericsDemo p = new NotGenericsDemo(); // 声明一个Point的对象
        p.setX(10); // 利用自动装箱操作：int --> Integer --> Object
        p.setY(20); // 利用自动装箱操作：int --> Integer --> Object
        int x = (Integer) p.getX(); // 取出数据先变为Integer，之后自动拆箱
        int y = (Integer) p.getY(); // 取出数据先变为Integer，之后自动拆箱
        System.out.println("整数表示，X坐标为：" + x);
        System.out.println("整数表示，Y坐标为：" + y);
    }

    @Test
    public void testFloatToObject() {
        NotGenericsDemo p = new NotGenericsDemo(); // 声明一个Point的对象
        p.setX(10.5f); // 利用自动装箱操作：float --> Float --> Object
        p.setY(20.6f); // 利用自动装箱操作：float --> Float --> Object
        float x = (Float) p.getX(); // 取出数据先变为Integer，之后自动拆箱
        float y = (Float) p.getY(); // 取出数据先变为Integer，之后自动拆箱
        System.out.println("小数表示，X坐标为：" + x);
        System.out.println("小数表示，Y坐标为：" + y);
    }

    @Test
    public void testStringToObject() {
        NotGenericsDemo p = new NotGenericsDemo(); // 声明一个Point的对象
        p.setX("东经180度"); // String --> Object
        p.setY("北纬210度"); // String --> Object
        String x = (String) p.getX(); // 取出数据先变为Integer，之后自动拆箱
        String y = (String) p.getY(); // 取出数据先变为Integer，之后自动拆箱
        System.out.println("字符串表示，X坐标为：" + x);
        System.out.println("字符串表示，Y坐标为：" + y);
    }

    @Test
    public void testIntegerAndStringToObject() {
        NotGenericsDemo p = new NotGenericsDemo(); // 声明一个Point的对象
        p.setX(10); // 利用自动装箱操作：int --> Integer --> Object
        p.setY("北纬210度"); // String --> Object
        int x = (Integer) p.getX(); // 取出数据先变为Integer，之后自动拆箱
        int y = (Integer) p.getY(); // 取出数据先变为Integer，之后自动拆箱
        System.out.println("整数表示，X坐标为：" + x);
        System.out.println("整数表示，Y坐标为：" + y);
    }
}
