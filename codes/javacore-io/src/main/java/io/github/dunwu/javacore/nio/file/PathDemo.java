package io.github.dunwu.javacore.nio.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 示例：NIO.2 的 {@link Path} —— 面向路径（而非文件）的抽象。
 * <p>
 * {@code java.io.File} 把「路径字符串」和「磁盘上的文件」混在一起，而 {@code Path} 只表示路径本身，
 * 不要求该路径真实存在，因此可以放心地做拼接、求相对路径、归一化等纯计算操作。
 * 真正访问磁盘是 {@code java.nio.file.Files} 的职责（见 {@link FilesDemo}）。
 * <p>
 * <b>跨平台注意</b>：{@code Path.toString()} 使用当前文件系统的分隔符（Windows 为 {@code \}，
 * Linux/macOS 为 {@code /}），因此本示例的输出在不同平台上分隔符不同，见 {@link #separatorDifference()}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class PathDemo {

    /**
     * Path 的基础信息：文件名、父路径、根、各级名称元素
     */
    public static void basicInfo() {
        // Paths.get(String first, String... more) 会用平台分隔符拼接，是构造路径最直观的写法
        Path path = Paths.get("docs", "java", "nio", "path.txt");
        System.out.println("路径: " + path);
        System.out.println("文件名: " + path.getFileName());
        System.out.println("父路径: " + path.getParent());
        // 相对路径没有根；Windows 上的根形如 C:\，Linux 上是 /
        System.out.println("根: " + path.getRoot());
        System.out.println("名称元素个数: " + path.getNameCount());
        System.out.println("索引 0 的名称: " + path.getName(0));
        System.out.println("索引 3 的名称: " + path.getName(3));
        // subpath(beginIndex, endIndex) 取 [begin, end) 区间，不含根，且是相对于原路径的
        System.out.println("子路径 [1,3): " + path.subpath(1, 3));
        System.out.println("是否为绝对路径: " + path.isAbsolute());
    }

    /**
     * 分隔符的平台差异：无论用什么分隔符构造，{@code toString()} 都返回平台分隔符
     */
    public static void separatorDifference() {
        Path slashPath = Paths.get("docs/java/nio/path.txt");
        System.out.println("构造入参: docs/java/nio/path.txt");
        System.out.println("toString(): " + slashPath);
        System.out.println("文件系统分隔符: " + slashPath.getFileSystem().getSeparator());
        // 两种写法得到的是相等的 Path（Path 的 equals 比较的是规范化后的路径，不是原始字符串）
        System.out.println("与 Paths.get(\"docs\",\"java\",\"nio\",\"path.txt\") 相等: "
            + slashPath.equals(Paths.get("docs", "java", "nio", "path.txt")));
    }

    /**
     * resolve 拼接路径，relativize 求两个路径之间的相对路径
     */
    public static void resolveAndRelativize() {
        Path base = Paths.get("docs", "java");
        System.out.println("resolve 字符串: " + base.resolve("nio/path.txt"));
        System.out.println("resolve 另一个 Path: " + base.resolve(Paths.get("nio", "files.txt")));
        // resolve 的参数若是绝对路径，则直接返回该参数（base 被丢弃）；
        // 若是相对路径则是在 base 后面拼接 —— 所以「resolve 自身」得到的是重复的路径，而不是原路径
        System.out.println("resolve 自身: " + base.resolve(base));

        // relativize 要求两个路径「同为绝对」或「同为相对」，否则抛 IllegalArgumentException
        Path nio = Paths.get("docs", "java", "nio");
        Path buffer = Paths.get("docs", "java", "buffer");
        System.out.println("nio.relativize(buffer): " + nio.relativize(buffer));
        System.out.println("buffer.relativize(nio): " + buffer.relativize(nio));
    }

    /**
     * normalize 消除 {@code .} 与 {@code ..}
     * <p>
     * 注意它是<b>纯词法</b>运算，不会去查文件系统。若路径中含符号链接，normalize 的结果可能并不指向同一个文件，
     * 需要真实解析符号链接请用 {@code path.toRealPath()}。
     */
    public static void normalize() {
        Path messy = Paths.get("docs/./java/../java/nio/path.txt");
        System.out.println("normalize 前: " + messy);
        System.out.println("normalize 后: " + messy.normalize());
        System.out.println("归一化后与直接构造的路径相等: "
            + messy.normalize().equals(Paths.get("docs", "java", "nio", "path.txt")));
    }

    /**
     * 绝对路径转换与路径比较
     * <p>
     * {@code startsWith} / {@code endsWith} 比较的是<b>名称元素序列</b>，不是字符串前缀，
     * 因此 {@code docs} 开头的路径并不 {@code startsWith("doc")}。
     * <p>
     * 另外注意：{@code Path} 并<b>没有</b> {@code contains} 方法，但它实现了 {@code Iterable<Path>}，
     * 要判断「中间是否包含某一级」只能自己遍历。
     */
    public static void absoluteAndCompare() {
        Path relative = Paths.get("target", "demo.txt");
        Path absolute = relative.toAbsolutePath();
        System.out.println("相对路径: " + relative);
        System.out.println("相对路径 isAbsolute: " + relative.isAbsolute());
        System.out.println("转为绝对路径后 isAbsolute: " + absolute.isAbsolute());
        System.out.println("绝对路径以工作目录开头: " + absolute.startsWith(System.getProperty("user.dir")));

        Path docs = Paths.get("docs", "java", "nio");
        System.out.println("startsWith(docs): " + docs.startsWith(Paths.get("docs")));
        System.out.println("startsWith(doc): " + docs.startsWith(Paths.get("doc")));
        System.out.println("endsWith(nio): " + docs.endsWith(Paths.get("nio")));

        // Path 实现了 Iterable<Path>，可以直接用 for-each 遍历各级名称元素
        StringBuilder names = new StringBuilder();
        for (Path name : docs) {
            names.append(name).append(' ');
        }
        System.out.println("逐级遍历名称元素: " + names.toString().trim());
    }

    /**
     * Path 与 {@code java.io.File} 互转
     * <p>
     * 老的 IO API 仍大量使用 {@code File}，互转是迁移到 NIO.2 时最常写的两行代码。
     */
    public static void toFileAndUri() {
        Path path = Paths.get("docs", "java", "nio", "path.txt");
        File file = path.toFile();
        System.out.println("toFile 的类型: " + file.getClass().getSimpleName());
        System.out.println("File.toPath() 能回到相等的 Path: " + file.toPath().equals(path));
        System.out.println("toUri 以 file: 开头: " + path.toUri().toString().startsWith("file:"));
        // URI 总是绝对的，即使原 Path 是相对路径
        System.out.println("toUri 是绝对 URI: " + path.toUri().isAbsolute());
    }

    /**
     * 依次演示 Path 的基础信息、分隔符差异、拼接与相对化、归一化、比较以及互转
     */
    public static void demo() {
        basicInfo();
        separatorDifference();
        resolveAndRelativize();
        normalize();
        absoluteAndCompare();
        toFileAndUri();
    }

    public static void main(String[] args) {
        demo();
    }

}
