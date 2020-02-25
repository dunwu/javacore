package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDemo04 {

    public static void main(String[] args) {
        Properties pro = new Properties();    // 创建Properties对象
        pro.setProperty("BJ", "BeiJing");    // 设置属性
        pro.setProperty("TJ", "TianJin");
        pro.setProperty("NJ", "NanJing");
        File file = new File("D:" + File.separator + "area.xml");    // 指定要操作的文件
        try {
            pro.storeToXML(new FileOutputStream(file), "Area Info");    // 保存属性到普通文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
