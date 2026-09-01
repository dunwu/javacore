package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 示例：使用 {@link Properties#storeToXML} 将属性保存到 XML 文件。
 * <p>
 * 与 {@link PropertiesDemo05} 配对：本示例写入文件，{@link PropertiesDemo05} 读取文件。
 */
public class PropertiesDemo04 {

    /**
     * XML 属性文件路径（相对路径，避免硬编码磁盘盘符）
     */
    public static final String FILE_PATH = "temp_area.xml";

    /**
     * 演示 Properties 的 storeToXML 用法：设置若干属性后保存到 XML 文件。
     */
    public static void demo() {
        Properties pro = new Properties();    // 创建Properties对象
        pro.setProperty("BJ", "BeiJing");    // 设置属性
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");
        File file = new File(FILE_PATH);    // 指定要操作的文件
        try {
            pro.storeToXML(new FileOutputStream(file), "Area Info");    // 保存属性到XML文件
            System.out.println("属性已保存到XML文件：" + file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        demo();
    }

}
