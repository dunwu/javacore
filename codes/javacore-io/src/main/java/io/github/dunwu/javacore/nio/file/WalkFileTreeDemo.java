package io.github.dunwu.javacore.nio.file;

import io.github.dunwu.javacore.DemoFiles;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 示例：用 {@link Files#walkFileTree} + {@link SimpleFileVisitor} 遍历目录树。
 * <p>
 * {@code walkFileTree} 是<b>回调式</b>遍历：把「进入目录 / 访问文件 / 访问失败 / 离开目录」四个时机交给
 * {@code FileVisitor} 处理，并可以用 {@link FileVisitResult} 精细控制流程（跳过子树、跳过兄弟、提前终止）。
 * 相比之下 {@link Files#walk} 是 Stream 式的，写起来更短，但拿不到「进入/离开目录」这两个时机，
 * 也不便做「删除时先处理文件再处理目录」这类需要后序遍历的事。
 * <p>
 * <b>跨平台注意</b>：同一目录内的访问顺序由文件系统决定（NTFS 大致按名称，ext4 不保证），
 * 因此下面的示例在收集结果后都做了排序，输出才是确定的。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class WalkFileTreeDemo {

    /**
     * 完整遍历：观察四个回调的触发时机与嵌套关系
     * <p>
     * 输出中「进入目录」总在「离开目录」之前，且子目录的进出被夹在父目录的进出之间 —— 这就是深度优先后序遍历。
     */
    public static void traverse() throws IOException {
        Path root = prepareTree();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                // root.relativize(root) 得到空路径，因此根目录会打印成空字符串
                System.out.println("[进入目录] " + relative(root, dir));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                System.out.println("  [访问文件] " + relative(root, file) + " (" + attrs.size() + " 字节)");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                // 单个文件访问失败（如权限不足、文件在遍历途中被删）不应中断整棵树的遍历
                System.out.println("  [访问失败] " + relative(root, file) + " : " + exc);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                System.out.println("[离开目录] " + relative(root, dir));
                return FileVisitResult.CONTINUE;
            }

        });
    }

    /**
     * 统计目录数、文件数与总字节数
     */
    public static void countAndSize() throws IOException {
        Path root = prepareTree();
        // 匿名内部类只能捕获 final / effectively final 的局部变量，因此用 AtomicLong 承载可变计数
        AtomicLong dirCount = new AtomicLong();
        AtomicLong fileCount = new AtomicLong();
        AtomicLong totalSize = new AtomicLong();

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                dirCount.incrementAndGet();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                fileCount.incrementAndGet();
                // attrs 是遍历时顺带读出来的，用 attrs.size() 比再调一次 Files.size(file) 少一次系统调用
                totalSize.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }

        });

        System.out.println("目录数（含根目录）: " + dirCount.get());
        System.out.println("文件数: " + fileCount.get());
        System.out.println("总字节数: " + totalSize.get());
    }

    /**
     * 按扩展名查找，并用 {@code TERMINATE} 在命中后提前结束整棵树的遍历
     */
    public static void searchByExtension() throws IOException {
        Path root = prepareTree();
        List<String> found = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".txt")) {
                    found.add(relative(root, file));
                }
                return FileVisitResult.CONTINUE;
            }

        });
        // 遍历顺序依赖文件系统，排序后输出才是确定的
        Collections.sort(found);
        System.out.println("找到的 .txt 文件: " + found);

        // 只需要第一个命中结果时，返回 TERMINATE 让 walkFileTree 立刻停止，避免白跑完整棵树
        AtomicReference<String> firstLog = new AtomicReference<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (file.toString().endsWith(".log")) {
                    firstLog.set(relative(root, file));
                    return FileVisitResult.TERMINATE;
                }
                return FileVisitResult.CONTINUE;
            }

        });
        System.out.println("TERMINATE 找到的 .log 文件: " + firstLog.get());
    }

    /**
     * 用 {@code SKIP_SUBTREE} 跳过整个子目录
     */
    public static void skipSubtree() throws IOException {
        Path root = prepareTree();
        List<String> visited = new ArrayList<>();
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                // 在 preVisitDirectory 返回 SKIP_SUBTREE，该目录下的所有内容都不会被访问，
                // 但它的 postVisitDirectory 仍然会被调用
                if ("sub".equals(String.valueOf(dir.getFileName()))) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                visited.add(relative(root, file));
                return FileVisitResult.CONTINUE;
            }

        });
        Collections.sort(visited);
        // sub 下的 c.txt、deep/d.txt 都被跳过了
        System.out.println("跳过 sub 子树后访问到的文件: " + visited);
    }

    /**
     * 限制遍历深度，并与 {@link Files#walk} 的 Stream 写法对照
     * <p>
     * <b>一个容易踩的边界行为</b>：处于 {@code maxDepth} 那一层的目录<b>不会被展开</b>——
     * 它的 {@code preVisitDirectory} / {@code postVisitDirectory} 都不会被调用，而是作为一个「叶子」
     * 直接交给 {@code visitFile}。因此 {@code visitFile} 收到的并不一定是常规文件，
     * 需要「只要文件」时必须用 {@code attrs.isRegularFile()} 过滤。
     */
    public static void maxDepth() throws IOException {
        Path root = prepareTree();

        // visitFile 收到的全部条目，以及用 isRegularFile 过滤后的真正文件
        List<String> allEntries = new ArrayList<>();
        List<String> regularFiles = new ArrayList<>();
        // 四参数重载：options 传空集表示不跟随符号链接，maxDepth=1 表示根目录自身 + 第一层
        Files.walkFileTree(root, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String name = relative(root, file);
                allEntries.add(name);
                // sub 是目录，但因为处在 maxDepth 边界上不被展开，也会走到这里
                if (attrs.isRegularFile()) {
                    regularFiles.add(name);
                }
                return FileVisitResult.CONTINUE;
            }

        });
        Collections.sort(allEntries);
        Collections.sort(regularFiles);
        System.out.println("maxDepth=1 时 visitFile 收到的全部条目: " + allEntries);
        System.out.println("其中真正的文件: " + regularFiles);

        // 对照：Files.walk(root, 1) 语义等价，但写法紧凑得多；需要精细控制流程时才用 walkFileTree
        try (Stream<Path> stream = Files.walk(root, 1)) {
            List<String> byStream = stream.filter(Files::isRegularFile)
                .map(path -> relative(root, path))
                .sorted()
                .collect(Collectors.toList());
            System.out.println("Files.walk(root, 1) 过滤后的文件: " + byStream);
            System.out.println("两种写法结果一致: " + byStream.equals(regularFiles));
        }
    }

    /**
     * 依次演示完整遍历、统计、按扩展名查找、跳过子树与限制深度
     */
    public static void demo() throws IOException {
        traverse();
        countAndSize();
        searchByExtension();
        skipSubtree();
        maxDepth();
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

    /**
     * 准备一棵结构确定的目录树：{@code walkdemo/{a.txt, b.log, sub/{c.txt, deep/d.txt}}}
     * <p>
     * 这里刻意用 {@code Files.write(Path, byte[])}，而不是接收 {@code Iterable<CharSequence>} 的重载：
     * 后者会在每行末尾追加<b>平台行分隔符</b>（Windows 是 {@code \r\n}，Linux 是 {@code \n}），
     * 会让文件字节数在不同平台上不一致。本示例要演示按大小统计，字节数必须确定。
     */
    private static Path prepareTree() throws IOException {
        Path root = DemoFiles.temp("walkdemo").toPath();
        Files.createDirectories(root.resolve("sub").resolve("deep"));
        Files.write(root.resolve("a.txt"), "a".getBytes(StandardCharsets.UTF_8));
        Files.write(root.resolve("b.log"), "bb".getBytes(StandardCharsets.UTF_8));
        Files.write(root.resolve("sub").resolve("c.txt"), "ccc".getBytes(StandardCharsets.UTF_8));
        Files.write(root.resolve("sub").resolve("deep").resolve("d.txt"), "dddd".getBytes(StandardCharsets.UTF_8));
        return root;
    }

    /**
     * 返回相对于根目录的路径，并把分隔符统一成 {@code /}，让输出与平台无关
     */
    private static String relative(Path root, Path target) {
        return root.relativize(target).toString().replace(File.separatorChar, '/');
    }

}
