package io.github.dunwu.javacore.nio.file;

import io.github.dunwu.javacore.DemoFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 示例：NIO.2 的 {@link Files} —— 面向文件的静态工具类。
 * <p>
 * {@code Files} 把「读写、复制移动、属性查询、目录遍历」都收敛成了以 {@link Path} 为参数的静态方法，
 * 取代了 {@code java.io} 里 {@code FileInputStream} / {@code FileOutputStream} / {@code BufferedReader}
 * 需要手动组装流、手动关闭的写法。多数方法都直接支持 {@code Charset}，不再有默认编码带来的乱码风险。
 * <p>
 * 本示例产生的所有文件都写在 {@code target/niodemo} 目录下（见 {@link DemoFiles}），
 * 因此可以被重复运行，{@code mvn clean} 后自动清理。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FilesDemo {

    /**
     * 演示素材所在目录，位于模块的 {@code target/} 下
     */
    private static final Path WORK_DIR = DemoFiles.temp("niodemo").toPath();

    /**
     * 演示用的固定文本内容，便于测试对输出做精确断言
     */
    private static final List<String> LINES = Arrays.asList("第一行：NIO.2", "第二行：Files 工具类", "第三行：中文内容");

    /**
     * 创建目录与写入文件
     */
    public static void createAndWrite() throws IOException {
        Path dir = workDir();
        // createDirectories 会递归创建缺失的父目录，且目录已存在时不报错（对比 File.mkdir() 只建一层）
        System.out.println("演示目录已就绪: " + Files.isDirectory(dir));

        Path file = dir.resolve("files.txt");
        // Files.write 默认行为是「不存在则创建、存在则清空重写」，所以示例可以反复运行
        Files.write(file, LINES, StandardCharsets.UTF_8);
        System.out.println("写入行数: " + LINES.size());

        // 追加需要显式指定 APPEND，并配合 CREATE（否则文件不存在时会抛 NoSuchFileException）
        Files.write(file, Arrays.asList("第四行：追加内容"), StandardCharsets.UTF_8,
            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("追加后行数: " + Files.readAllLines(file, StandardCharsets.UTF_8).size());
    }

    /**
     * 读取文件的三种方式
     */
    public static void readBack() throws IOException {
        Path file = prepareTextFile();

        // 方式一：readAllLines 一次性读入内存，适合小文件
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        System.out.println("readAllLines 行数: " + lines.size());
        System.out.println("第一行: " + lines.get(0));

        // 方式二：readString（Java 11 引入）直接读成字符串，省去手动 join，
        // 详见 javacore-newjdk 模块的 jdk11/file/FileReadWriteDemo
        String content = Files.readString(file, StandardCharsets.UTF_8);
        System.out.println("readString 含换行符: " + content.contains("\n"));

        // 方式三：lines 返回惰性 Stream，逐行读取不把整个文件载入内存，适合大文件；用完必须关闭
        try (Stream<String> stream = Files.lines(file, StandardCharsets.UTF_8)) {
            System.out.println("lines 中以「第」开头的行数: " + stream.filter(line -> line.startsWith("第")).count());
        }
    }

    /**
     * 查询文件属性与存在性
     */
    public static void queryAttributes() throws IOException {
        Path dir = workDir();
        Path file = prepareTextFile();
        Path missing = dir.resolve("missing.txt");

        System.out.println("exists(已存在的文件): " + Files.exists(file));
        System.out.println("exists(不存在的路径): " + Files.exists(missing));
        System.out.println("notExists(不存在的路径): " + Files.notExists(missing));
        System.out.println("isRegularFile(文件): " + Files.isRegularFile(file));
        System.out.println("isRegularFile(目录): " + Files.isRegularFile(dir));
        System.out.println("isDirectory(目录): " + Files.isDirectory(dir));
        System.out.println("文件字节数大于 0: " + (Files.size(file) > 0));
        System.out.println("最后修改时间大于 0: " + (Files.getLastModifiedTime(file).toMillis() > 0));
        // isReadable / isWritable 会真实检查文件系统权限，而不是只看文件是否存在
        System.out.println("isReadable: " + Files.isReadable(file));
    }

    /**
     * 复制、移动与删除
     */
    public static void copyMoveDelete() throws IOException {
        Path dir = workDir();
        Path source = prepareTextFile();

        // copy 在目标已存在时默认抛 FileAlreadyExistsException，需要显式传 REPLACE_EXISTING 才覆盖
        Path copy = dir.resolve("files_copy.txt");
        Files.copy(source, copy, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("复制后内容与源文件一致: " + Files.readAllLines(copy, StandardCharsets.UTF_8)
            .equals(Files.readAllLines(source, StandardCharsets.UTF_8)));

        // move 在同一文件系统内通常是重命名（改 inode 指向），跨文件系统才会真正读写数据
        Path moved = dir.resolve("files_moved.txt");
        Files.move(copy, moved, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("移动后原路径已不存在: " + Files.notExists(copy));
        System.out.println("移动后新路径存在: " + Files.exists(moved));

        Files.delete(moved);
        System.out.println("delete 后文件已删除: " + Files.notExists(moved));
        // delete 对不存在的路径会抛 NoSuchFileException；deleteIfExists 则安静地返回 false
        System.out.println("deleteIfExists 对不存在的路径返回: " + Files.deleteIfExists(moved));
    }

    /**
     * 目录遍历：{@code Files.list} 与 {@code Files.walk} 的区别
     * <p>
     * 关键差异：<b>{@code walk} 的结果包含起始路径自身，{@code list} 不包含</b>。
     * 两者都返回 Stream，且底层持有目录句柄，<b>必须用 try-with-resources 关闭</b>，否则在 Windows 上
     * 目录会被占用而无法删除。
     */
    public static void listDirectory() throws IOException {
        // 自建一个确定的目录结构：listdemo/{a.txt, b.log, sub/c.txt}，避免输出受其他示例影响
        Path root = workDir().resolve("listdemo");
        Files.createDirectories(root.resolve("sub"));
        Files.write(root.resolve("a.txt"), Arrays.asList("a"), StandardCharsets.UTF_8);
        Files.write(root.resolve("b.log"), Arrays.asList("b"), StandardCharsets.UTF_8);
        Files.write(root.resolve("sub").resolve("c.txt"), Arrays.asList("c"), StandardCharsets.UTF_8);

        long listCount;
        try (Stream<Path> stream = Files.list(root)) {
            listCount = stream.count();
        }
        long walkCount;
        try (Stream<Path> stream = Files.walk(root)) {
            walkCount = stream.count();
        }
        // list 只数一层：a.txt、b.log、sub 共 3 个
        System.out.println("list 一层内的条目数: " + listCount);
        // walk 递归整棵树，并且把 root 自身也算进去：root、a.txt、b.log、sub、sub/c.txt 共 5 个
        System.out.println("walk 递归遍历的条目数: " + walkCount);

        // walk 可以限制深度：maxDepth=1 表示 root 自身 + 第一层
        long depthOneCount;
        try (Stream<Path> stream = Files.walk(root, 1)) {
            depthOneCount = stream.count();
        }
        System.out.println("walk(root, 1) 的条目数: " + depthOneCount);

        // 递归找出所有 .txt 文件并取文件名，是 Files.walk 最常见的用法
        try (Stream<Path> stream = Files.walk(root)) {
            List<String> txtNames = stream.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".txt"))
                .map(path -> path.getFileName().toString())
                .sorted()
                .collect(Collectors.toList());
            System.out.println("递归找到的 .txt 文件: " + txtNames);
        }
    }

    /**
     * 依次演示写入、读取、属性查询、复制移动删除与目录遍历
     */
    public static void demo() throws IOException {
        createAndWrite();
        readBack();
        queryAttributes();
        copyMoveDelete();
        listDirectory();
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

    /**
     * 确保演示目录存在并返回它
     */
    private static Path workDir() throws IOException {
        Files.createDirectories(WORK_DIR);
        return WORK_DIR;
    }

    /**
     * 准备内容确定的演示文本文件，每次调用都会重写，保证结果与调用顺序无关
     */
    private static Path prepareTextFile() throws IOException {
        Path file = workDir().resolve("files.txt");
        Files.write(file, LINES, StandardCharsets.UTF_8);
        return file;
    }
    // Output:
    // 演示目录已就绪: true
    // 写入行数: 3
    // 追加后行数: 4
    // readAllLines 行数: 3
    // 第一行: 第一行：NIO.2
    // readString 含换行符: true
    // lines 中以「第」开头的行数: 3
    // exists(已存在的文件): true
    // exists(不存在的路径): false
    // notExists(不存在的路径): true
    // isRegularFile(文件): true
    // isRegularFile(目录): false
    // isDirectory(目录): true
    // 文件字节数大于 0: true
    // 最后修改时间大于 0: true
    // isReadable: true
    // 复制后内容与源文件一致: true
    // 移动后原路径已不存在: true
    // 移动后新路径存在: true
    // delete 后文件已删除: true
    // deleteIfExists 对不存在的路径返回: false
    // list 一层内的条目数: 3
    // walk 递归遍历的条目数: 5
    // walk(root, 1) 的条目数: 4
    // 递归找到的 .txt 文件: [a.txt, c.txt]
}

