package io.github.dunwu.javacore.nio.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * NIO.2（{@code java.nio.file}）示例测试
 * <p>
 * <b>跨平台说明</b>：{@code Path.toString()} 使用当前文件系统的分隔符，Windows 是 {@code \}、Linux 是 {@code /}。
 * 本类的处理方式是——凡是断言中含路径的行，都用 {@link File#separator} 拼出预期值；
 * 而 {@link WalkFileTreeDemo} 内部已把分隔符统一成 {@code /}，因此它的输出可以直接精确断言。
 * <p>
 * {@link WatchServiceDemo} 的事件投递依赖操作系统与文件系统实现，收不到事件也属正常，
 * 因此只断言注册、取消注册等确定行为。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class NioFileDemoTest {

    /**
     * 当前平台的路径分隔符
     */
    private static final String SEP = File.separator;

    @FunctionalInterface
    interface ThrowingRunnable {

        void run() throws Exception;

    }

    private static String captureOutput(ThrowingRunnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(original);
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    @Test
    @DisplayName("Path：文件名、名称元素个数、根、subpath 等基础信息")
    void testPathBasicInfo() {
        String output = captureOutput(PathDemo::basicInfo);
        assertThat(output).isEqualTo("路径: docs" + SEP + "java" + SEP + "nio" + SEP + "path.txt\n"
            + "文件名: path.txt\n"
            + "父路径: docs" + SEP + "java" + SEP + "nio\n"
            + "根: null\n"
            + "名称元素个数: 4\n"
            + "索引 0 的名称: docs\n"
            + "索引 3 的名称: path.txt\n"
            + "子路径 [1,3): java" + SEP + "nio\n"
            + "是否为绝对路径: false\n");
    }

    @Test
    @DisplayName("Path：以 / 构造的路径，toString() 返回平台分隔符，且与多参数写法相等")
    void testPathSeparatorDifference() {
        String output = captureOutput(PathDemo::separatorDifference);
        assertThat(output).isEqualTo("构造入参: docs/java/nio/path.txt\n"
            + "toString(): docs" + SEP + "java" + SEP + "nio" + SEP + "path.txt\n"
            + "文件系统分隔符: " + SEP + "\n"
            + "与 Paths.get(\"docs\",\"java\",\"nio\",\"path.txt\") 相等: true\n");
    }

    @Test
    @DisplayName("Path：resolve 拼接、relativize 求相对路径")
    void testPathResolveAndRelativize() {
        String output = captureOutput(PathDemo::resolveAndRelativize);
        assertThat(output).isEqualTo("resolve 字符串: docs" + SEP + "java" + SEP + "nio" + SEP + "path.txt\n"
            + "resolve 另一个 Path: docs" + SEP + "java" + SEP + "nio" + SEP + "files.txt\n"
            + "resolve 自身: docs" + SEP + "java" + SEP + "docs" + SEP + "java\n"
            + "nio.relativize(buffer): .." + SEP + "buffer\n"
            + "buffer.relativize(nio): .." + SEP + "nio\n");
    }

    @Test
    @DisplayName("Path：normalize 是纯词法运算，可消除 . 与 ..")
    void testPathNormalize() {
        String output = captureOutput(PathDemo::normalize);
        assertThat(output).isEqualTo("normalize 前: docs" + SEP + "." + SEP + "java" + SEP + ".." + SEP
            + "java" + SEP + "nio" + SEP + "path.txt\n"
            + "normalize 后: docs" + SEP + "java" + SEP + "nio" + SEP + "path.txt\n"
            + "归一化后与直接构造的路径相等: true\n");
    }

    @Test
    @DisplayName("Path：startsWith/endsWith 比较名称元素，因此 doc 不是 docs 的前缀")
    void testPathAbsoluteAndCompare() {
        String output = captureOutput(PathDemo::absoluteAndCompare);
        assertThat(output).contains("相对路径: target" + SEP + "demo.txt")
            .contains("相对路径 isAbsolute: false")
            .contains("转为绝对路径后 isAbsolute: true")
            .contains("绝对路径以工作目录开头: true")
            .contains("startsWith(docs): true")
            // 关键结论：Path 的比较是按名称元素而非字符串前缀
            .contains("startsWith(doc): false")
            .contains("endsWith(nio): true")
            // Path 没有 contains 方法，但实现了 Iterable<Path>，可逐级遍历名称元素
            .contains("逐级遍历名称元素: docs java nio");
    }

    @Test
    @DisplayName("Path：与 java.io.File 互转，toUri 总是绝对的")
    void testPathToFileAndUri() {
        String output = captureOutput(PathDemo::toFileAndUri);
        assertThat(output).isEqualTo("toFile 的类型: File\n"
            + "File.toPath() 能回到相等的 Path: true\n"
            + "toUri 以 file: 开头: true\n"
            + "toUri 是绝对 URI: true\n");
    }

    @Test
    @DisplayName("Files：创建目录、写入与追加")
    void testFilesCreateAndWrite() {
        String output = captureOutput(FilesDemo::createAndWrite);
        assertThat(output).isEqualTo("演示目录已就绪: true\n"
            + "写入行数: 3\n"
            + "追加后行数: 4\n");
    }

    @Test
    @DisplayName("Files：readAllLines / readString / lines 三种读取方式")
    void testFilesReadBack() {
        String output = captureOutput(FilesDemo::readBack);
        assertThat(output).isEqualTo("readAllLines 行数: 3\n"
            + "第一行: 第一行：NIO.2\n"
            + "readString 含换行符: true\n"
            + "lines 中以「第」开头的行数: 3\n");
    }

    @Test
    @DisplayName("Files：存在性、类型、大小等属性查询")
    void testFilesQueryAttributes() {
        String output = captureOutput(FilesDemo::queryAttributes);
        assertThat(output).isEqualTo("exists(已存在的文件): true\n"
            + "exists(不存在的路径): false\n"
            + "notExists(不存在的路径): true\n"
            + "isRegularFile(文件): true\n"
            + "isRegularFile(目录): false\n"
            + "isDirectory(目录): true\n"
            + "文件字节数大于 0: true\n"
            + "最后修改时间大于 0: true\n"
            + "isReadable: true\n");
    }

    @Test
    @DisplayName("Files：复制、移动、删除，以及 deleteIfExists 对不存在路径返回 false")
    void testFilesCopyMoveDelete() {
        String output = captureOutput(FilesDemo::copyMoveDelete);
        assertThat(output).isEqualTo("复制后内容与源文件一致: true\n"
            + "移动后原路径已不存在: true\n"
            + "移动后新路径存在: true\n"
            + "delete 后文件已删除: true\n"
            + "deleteIfExists 对不存在的路径返回: false\n");
    }

    @Test
    @DisplayName("Files：list 只数一层，walk 递归且包含起始路径自身")
    void testFilesListDirectory() {
        String output = captureOutput(FilesDemo::listDirectory);
        assertThat(output).isEqualTo("list 一层内的条目数: 3\n"
            + "walk 递归遍历的条目数: 5\n"
            + "walk(root, 1) 的条目数: 4\n"
            + "递归找到的 .txt 文件: [a.txt, c.txt]\n");
    }

    @Test
    @DisplayName("walkFileTree：四个回调都会触发，且子目录的进出夹在父目录之间")
    void testWalkFileTreeTraverse() {
        String output = captureOutput(WalkFileTreeDemo::traverse);
        // 同一目录内条目的访问顺序由文件系统决定，因此只断言各节点都被访问到，以及进出的嵌套关系
        assertThat(output).contains("[进入目录] \n")
            .contains("[访问文件] a.txt (1 字节)")
            .contains("[访问文件] b.log (2 字节)")
            .contains("[进入目录] sub\n")
            .contains("[访问文件] sub/c.txt (3 字节)")
            .contains("[进入目录] sub/deep\n")
            .contains("[访问文件] sub/deep/d.txt (4 字节)")
            .contains("[离开目录] sub/deep\n")
            .contains("[离开目录] sub\n")
            .contains("[离开目录] \n");
        // 深度优先后序：sub/deep 的进出必须完整地落在 sub 的进出之间
        assertThat(output.indexOf("[进入目录] sub\n")).isLessThan(output.indexOf("[进入目录] sub/deep\n"));
        assertThat(output.indexOf("[离开目录] sub/deep\n")).isLessThan(output.indexOf("[离开目录] sub\n"));
    }

    @Test
    @DisplayName("walkFileTree：统计目录数、文件数与总字节数")
    void testWalkFileTreeCountAndSize() {
        String output = captureOutput(WalkFileTreeDemo::countAndSize);
        assertThat(output).isEqualTo("目录数（含根目录）: 3\n"
            + "文件数: 4\n"
            + "总字节数: 10\n");
    }

    @Test
    @DisplayName("walkFileTree：按扩展名查找，TERMINATE 可在命中后立即停止")
    void testWalkFileTreeSearch() {
        String output = captureOutput(WalkFileTreeDemo::searchByExtension);
        assertThat(output).isEqualTo("找到的 .txt 文件: [a.txt, sub/c.txt, sub/deep/d.txt]\n"
            + "TERMINATE 找到的 .log 文件: b.log\n");
    }

    @Test
    @DisplayName("walkFileTree：SKIP_SUBTREE 跳过整个子目录")
    void testWalkFileTreeSkipSubtree() {
        String output = captureOutput(WalkFileTreeDemo::skipSubtree);
        assertThat(output).isEqualTo("跳过 sub 子树后访问到的文件: [a.txt, b.log]\n");
    }

    @Test
    @DisplayName("walkFileTree：maxDepth 边界上的目录不展开，会作为叶子交给 visitFile")
    void testWalkFileTreeMaxDepth() {
        String output = captureOutput(WalkFileTreeDemo::maxDepth);
        // sub 是目录，但处在 maxDepth 边界上，因此也出现在 visitFile 收到的条目里
        assertThat(output).isEqualTo("maxDepth=1 时 visitFile 收到的全部条目: [a.txt, b.log, sub]\n"
            + "其中真正的文件: [a.txt, b.log]\n"
            + "Files.walk(root, 1) 过滤后的文件: [a.txt, b.log]\n"
            + "两种写法结果一致: true\n");
    }

    @Test
    @DisplayName("WatchService：四种标准事件类型的名称")
    void testWatchServiceEventKinds() {
        String output = captureOutput(WatchServiceDemo::eventKinds);
        assertThat(output).isEqualTo("ENTRY_CREATE: ENTRY_CREATE\n"
            + "ENTRY_MODIFY: ENTRY_MODIFY\n"
            + "ENTRY_DELETE: ENTRY_DELETE\n"
            + "OVERFLOW: OVERFLOW\n");
    }

    @Test
    @DisplayName("WatchService：注册与取消注册成功，事件是否到达取决于平台实现")
    void testWatchServiceWatch() {
        String output = captureOutput(WatchServiceDemo::watch);
        assertThat(output).contains("注册成功: true")
            .contains("被监听的目录: watchdemo")
            // cancel() 之后 key 一定失效，这是确定的行为
            .contains("取消注册后 key 是否有效: false");
        // 事件投递依赖操作系统实现，收到与收不到都算正常，只要求示例给出了明确说明
        assertThat(output).containsAnyOf("收到 ENTRY_", "未收到事件");
    }

    @Test
    @DisplayName("demo：四个示例的完整演示均可正常执行")
    void testAllDemos() {
        // 用 captureOutput 包裹，避免示例的输出直接打到构建日志里
        assertThatCode(() -> captureOutput(PathDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(FilesDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(WalkFileTreeDemo::demo)).doesNotThrowAnyException();
        assertThatCode(() -> captureOutput(WatchServiceDemo::demo)).doesNotThrowAnyException();
    }

}

