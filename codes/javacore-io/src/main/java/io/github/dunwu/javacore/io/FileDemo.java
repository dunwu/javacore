package io.github.dunwu.javacore.io;

import java.io.File;
import java.io.IOException;

/**
 * File 类常用方法示例
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2018/4/26
 */
public class FileDemo {

    /**
     * createNewFile 方法使用：创建新文件
     */
    public static boolean createNewFile(String pathname) {
        File f = new File(pathname);
        try {
            // 创建文件，根据给定的路径创建
            return f.createNewFile();
        } catch (IOException e) {
            // 输出异常信息
            e.printStackTrace();
        }
        return false;
    }

    /**
     * delete 方法使用：可以用于删除文件或文件夹
     */
    public static boolean delete(String pathname) {
        File f = new File(pathname);
        return f.delete();
    }

    /**
     * list 方法使用：返回文件名称数组
     */
    public static void list() {
        // 实例化File类的对象
        File f = new File(".");
        // 列出给定目录中的内容
        String[] str = f.list();
        for (String item : str) {
            System.out.println(item);
        }
    }

    /**
     * list 方法使用：File 数组 getAbsolutePath 方法使用：返回绝对路径 getCanonicalPath 方法使用：返回相对路径
     */
    public static void listFiles() {
        // 实例化File类的对象
        File f = new File(".");
        // 列出全部内容
        File[] files = f.listFiles();
        if (files != null && files.length > 0) {
            System.out.println("绝对路径");
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
            }

            System.out.println("相对路径");
            for (File file : files) {
                try {
                    System.out.println(file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * mkdir 方法使用：创建新目录
     */
    public static boolean mkdir(String pathname) {
        File f = new File(pathname);
        return f.mkdir();
    }

}
