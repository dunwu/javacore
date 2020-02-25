package io.github.dunwu.javacore.io;

import java.util.Properties;

public class PropertiesDemo01 {

    public static void main(String[] args) {
        Properties pro = new Properties();    // 创建Properties对象
        pro.setProperty("BJ", "BeiJing");    // 设置属性
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");
        System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
        System.out.println("2、SC属性不存在：" + pro.getProperty("SC"));
        System.out.println("3、SC属性不存在，同时设置显示的默认值：" + pro.getProperty("SC", "没有发现"));
    }

}
