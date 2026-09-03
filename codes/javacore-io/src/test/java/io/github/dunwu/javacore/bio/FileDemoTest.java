package io.github.dunwu.javacore.bio;

import io.github.dunwu.javacore.DemoFiles;
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

    /**
     * 测试用的临时文件与目录。统一放到 {@code target/} 下，避免污染仓库工作目录，详见 {@link DemoFiles}。
     * <p>
     * 这里用 {@link DemoFiles#tempPath} 而不是手写 {@code "target/..."}：它会在返回路径前确保 {@code target}
     * 目录存在，而 {@link FileDemo#mkdir} 用的是 {@code File.mkdir()}（不会递归创建父目录），
     * 父目录缺失时会直接返回 false 导致测试失败。
     */
    private final String filename = DemoFiles.tempPath("temp_test.log");

    private final String dirname = DemoFiles.tempPath("temp_test_dir");

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
