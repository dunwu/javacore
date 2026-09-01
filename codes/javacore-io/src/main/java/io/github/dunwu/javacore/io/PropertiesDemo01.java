package io.github.dunwu.javacore.io;

import java.util.Properties;

/**
 * 示例：{@link Properties} 基本用法 —— 纯内存中设置属性、读取属性。
 */
public class PropertiesDemo01 {

    /**
     * 演示 Properties 的 setProperty / getProperty 用法，
     * 包括读取不存在的属性以及读取不存在属性时指定默认值。
     */
    public static void demo() {
        Properties pro = new Properties();    // 创建Properties对象
        pro.setProperty("BJ", "BeiJing");    // 设置属性
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");
        System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
        System.out.println("2、SC属性不存在：" + pro.getProperty("SC"));
        System.out.println("3、SC属性不存在，同时设置显示的默认值：" + pro.getProperty("SC", "没有发现"));
    }

    public static void main(String[] args) {
        demo();
    }

}
