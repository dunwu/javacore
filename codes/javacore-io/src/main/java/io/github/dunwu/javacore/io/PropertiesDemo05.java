package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 示例：使用 {@link Properties#loadFromXML} 从 XML 文件读取属性。
 * <p>
 * 与 {@link PropertiesDemo04} 配对：{@link PropertiesDemo04} 写入文件，本示例读取文件。
 */
public class PropertiesDemo05 {

    /**
     * 演示 Properties 的 loadFromXML 用法。
     * <p>
     * 为了使示例自包含，先写入 XML 属性文件（若不存在），再读取。
     */
    public static void demo() {
        File file = new File(PropertiesDemo04.FILE_PATH);    // 指定要操作的文件（与 PropertiesDemo04 共用）
        if (!file.exists()) {
            // 文件不存在时先写入，保证示例可独立运行
            PropertiesDemo04.demo();
        }
        Properties pro = new Properties();    // 创建Properties对象
        try {
            pro.loadFromXML(new FileInputStream(file));    // 读取XML属性文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("1、BJ属性存在：" + pro.getProperty("BJ"));
    }

    public static void main(String[] args) {
        demo();
    }

}
