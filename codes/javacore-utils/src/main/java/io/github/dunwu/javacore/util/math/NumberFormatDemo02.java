package io.github.dunwu.javacore.util.math;

import java.text.DecimalFormat;

class FormatDemo {

    public void format1(String pattern, double value) { // 此方法专门用于完成数字的格式化显示
        DecimalFormat df = null; // 声明一个DecimalFormat类的对象
        df = new DecimalFormat(pattern); // 实例化对象，传入模板
        String str = df.format(value); // 格式化数字
        System.out.println("使用" + pattern + "格式化数字" + value + "：" + str);
    }

}

/**
 * 示例：DecimalFormat 按自定义模板格式化数字（千分位、前导 0、百分号、千分号等）。
 */
public class NumberFormatDemo02 {

    /**
     * 演示各种 DecimalFormat 模板的效果。
     */
    public static void demo() {
        FormatDemo demo = new FormatDemo(); // 格式化对象的类
        demo.format1("###,###.###", 111222.34567);
        demo.format1("000,000.000", 11222.34567);
        demo.format1("###,###.###￥", 111222.34567);
        demo.format1("000,000.000￥", 11222.34567);
        demo.format1("##.###%", 0.345678);
        demo.format1("00.###%", 0.0345678);
        demo.format1("###.###\u2030", 0.345678);
    }

    public static void main(String[] args) {
        demo();
    }

}
