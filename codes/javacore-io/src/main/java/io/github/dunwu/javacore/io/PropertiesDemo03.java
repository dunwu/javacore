package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 示例：使用 {@link Properties#load} 从普通文件读取属性。
 * <p>
 * 与 {@link PropertiesDemo02} 配对：{@link PropertiesDemo02} 写入文件，本示例读取文件。
 */
public class PropertiesDemo03 {

    /**
     * 演示 Properties 的 load 用法。
     * <p>
     * 为了使示例自包含，先写入属性文件（若不存在），再读取。
     */
    public static void demo() {
        File file = new File(PropertiesDemo02.FILE_PATH);    // 指定要操作的文件（与 PropertiesDemo02 共用）
        if (!file.exists()) {
            // 文件不存在时先写入，保证示例可独立运行
            PropertiesDemo02.demo();
        }
        Properties pro = new Properties();    // 创建Properties对象
        try {
            pro.load(new FileInputStream(file));    // 读取属性文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
        System.out.println("2、SH属性存在：" + pro.getProperty("SH"));
    }

    public static void main(String[] args) {
        demo();
    }

}
