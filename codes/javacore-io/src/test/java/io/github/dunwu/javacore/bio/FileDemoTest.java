package io.github.dunwu.javacore.bio;

import io.github.dunwu.javacore.io.FileDemo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FileDemo 测试类
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @see FileDemo
 * @since 2018/4/26
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class FileDemoTest {

    private final String filename = "temp_test.log";

    private final String dirname = "temp_test_dir";

    @Test
    @DisplayName("创建新文件")
    public void test01_createNewFile() {
        boolean flag = FileDemo.createNewFile(filename);
        assertThat(flag).isTrue();
    }

    @Test
    @DisplayName("创建目录")
    public void test02_mkdir() {
        boolean flag = FileDemo.mkdir(dirname);
        assertThat(flag).isTrue();
    }

    @Test
    @DisplayName("删除目录与文件")
    public void test03_deleteDir() {
        boolean flag = FileDemo.delete(dirname);
        System.out.println(flag);
        assertThat(flag).isTrue();

        flag = FileDemo.delete(filename);
        System.out.println(flag);
        assertThat(flag).isTrue();
    }

    @Test
    @DisplayName("列出目录下的文件名")
    public void test04_list() {
        FileDemo.list();
    }

    @Test
    @DisplayName("列出目录下的文件对象")
    public void test05_listFiles() {
        FileDemo.listFiles();
    }

}
