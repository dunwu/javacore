package io.github.dunwu.javacore.io;

import java.io.File;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * FileDemo 测试类
 *
 * @author Zhang Peng
 * @date 2018/4/26
 * @see FileDemo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileDemoTest {

    private final String filename = "d:" + File.separator + "test.log";
    private final String dirname = "d:" + File.separator + "test";

    @Test
    public void test01_createNewFile() {
        FileDemo.createNewFile(filename);
    }

    @Test
    public void test02_mkdir() {
        FileDemo.mkdir(dirname);
    }

    @Test
    public void test03_deleteDir() {
        boolean flag = FileDemo.delete(dirname);
        System.out.println(flag);

        flag = FileDemo.delete(filename);
        System.out.println(flag);
    }

    @Test
    public void test04_list() {
        FileDemo.list();
    }

    @Test
    public void test05_listFiles() {
        FileDemo.listFiles();
    }
}
