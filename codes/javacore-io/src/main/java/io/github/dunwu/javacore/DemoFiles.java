package io.github.dunwu.javacore;

import java.io.File;

/**
 * 示例代码的临时文件工具。
 * <p>
 * 本模块的示例会产生临时文件（Properties 文件、RandomAccessFile 记录、通道复制结果、文件锁占位文件等）。
 * 统一约定：所有示例临时文件都写到 Maven 构建输出目录 {@code target/} 下，理由是
 * <ul>
 *     <li>{@code target} 已在 {@code .gitignore} 中，不会污染仓库工作目录</li>
 *     <li>{@code mvn clean} 会一并清理，无需示例自己负责善后</li>
 *     <li>产物留在模块目录内，运行示例后可直接打开查看，比系统临时目录更直观</li>
 * </ul>
 * <b>注意</b>：{@code target} 是相对路径，解析结果取决于 JVM 的工作目录。通过 Maven（{@code mvn test}）
 * 或 IDE 运行本模块时，工作目录即模块根目录，路径正确。{@link #tempDir()} 会在目录不存在时自动创建，
 * 因此 {@code mvn clean} 之后直接运行 {@code main} 方法也不会因父目录缺失而抛 {@code FileNotFoundException}。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public final class DemoFiles {

    /**
     * 示例临时文件统一存放的目录（相对于 JVM 工作目录，即模块根目录）
     */
    public static final String TEMP_DIR = "target";

    private DemoFiles() {
    }

    /**
     * 返回示例临时目录，若不存在则创建。
     */
    public static File tempDir() {
        File dir = new File(TEMP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 返回临时目录下的文件对象，并确保父目录已存在。
     * <p>
     * 例：{@code temp("area.properties")} → {@code target/area.properties}
     */
    public static File temp(String filename) {
        return new File(tempDir(), filename);
    }

    /**
     * 同 {@link #temp(String)}，但返回路径字符串。
     * <p>
     * 供示例沿用 {@code String} 形式的文件路径写法（如把路径作为参数传递给读写方法）。
     */
    public static String tempPath(String filename) {
        return temp(filename).getPath();
    }

}
