package io.github.dunwu.javacore.nio.file;

import io.github.dunwu.javacore.DemoFiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

/**
 * 示例：用 {@link WatchService} 监听目录变化（NIO.2 的文件系统事件通知）。
 * <p>
 * 它取代了「定时轮询目录再比对文件列表」的土办法：向操作系统注册关心的事件类型，
 * 由底层机制（Linux 的 inotify、Windows 的 ReadDirectoryChangesW、macOS 的 FSEvents）主动推送变更。
 * 典型用途是配置热加载、静态资源目录监视、构建工具的增量编译。
 * <p>
 * <b>三个必须知道的限制</b>：
 * <ul>
 *     <li>只能监听<b>目录</b>，不能监听单个文件；事件里的 {@code context()} 是相对该目录的文件名</li>
 *     <li>事件是<b>异步且不保证投递</b>的，只保证「变更发生后才可能收到」，不补发注册之前的历史事件；
 *     变更过于密集时可能合并成一条，甚至因缓冲区溢出而丢失（此时会收到 {@code OVERFLOW}）</li>
 *     <li>平台差异明显，某些文件系统（如部分网络挂载、容器内的 overlayfs）根本不支持，
 *     因此本示例用带超时的 {@code poll} 而非阻塞的 {@code take}，收不到事件时也能正常结束</li>
 * </ul>
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class WatchServiceDemo {

    /**
     * 等待文件系统事件的超时秒数
     */
    private static final long POLL_TIMEOUT_SECONDS = 3;

    /**
     * 四种标准事件类型
     */
    public static void eventKinds() {
        // 前三种需要在 register 时显式声明，没声明的事件不会通知
        System.out.println("ENTRY_CREATE: " + StandardWatchEventKinds.ENTRY_CREATE.name());
        System.out.println("ENTRY_MODIFY: " + StandardWatchEventKinds.ENTRY_MODIFY.name());
        System.out.println("ENTRY_DELETE: " + StandardWatchEventKinds.ENTRY_DELETE.name());
        // OVERFLOW 无需注册，它表示有事件因缓冲区溢出而丢失，收到后应当重新全量扫描目录
        System.out.println("OVERFLOW: " + StandardWatchEventKinds.OVERFLOW.name());
    }

    /**
     * 完整流程：注册 → 触发变更 → 轮询事件 → 重置 key → 取消注册
     */
    public static void watch() throws IOException, InterruptedException {
        Path dir = prepareDir();
        Path file = dir.resolve("watched.txt");
        // 先清掉上一轮遗留的文件，保证 CREATE 事件能真正触发
        Files.deleteIfExists(file);

        // WatchService 持有操作系统级的监听句柄，必须关闭，因此用 try-with-resources
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            WatchKey key = dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
            System.out.println("注册成功: " + (key != null));
            System.out.println("被监听的目录: " + dir.getFileName());

            // 注册必须在触发变更之前完成：WatchService 不会补发注册之前发生的事件
            Files.write(file, "第一版内容".getBytes(StandardCharsets.UTF_8));
            drainEvents(watchService, "创建文件后");

            Files.write(file, "第二版内容".getBytes(StandardCharsets.UTF_8));
            drainEvents(watchService, "修改文件后");

            Files.deleteIfExists(file);
            drainEvents(watchService, "删除文件后");

            // 不再需要监听时取消注册，key 随之失效
            key.cancel();
            System.out.println("取消注册后 key 是否有效: " + key.isValid());
        }
    }

    /**
     * 依次演示事件类型与完整的监听流程
     */
    public static void demo() throws IOException, InterruptedException {
        eventKinds();
        watch();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        demo();
    }

    /**
     * 准备被监听的目录
     */
    private static Path prepareDir() throws IOException {
        Path dir = DemoFiles.temp("watchdemo").toPath();
        Files.createDirectories(dir);
        return dir;
    }

    /**
     * 轮询并打印已到达的事件
     * <p>
     * 用带超时的 {@code poll} 而不是阻塞的 {@code take}：事件投递是异步的，各平台延迟差异很大，
     * 用 {@code take} 会在收不到事件时把示例永久挂住。生产代码通常把 {@code take} 放在独立线程里循环调用。
     */
    private static void drainEvents(WatchService watchService, String stage) throws InterruptedException {
        WatchKey key = watchService.poll(POLL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        if (key == null) {
            System.out.println(stage + "：等待 " + POLL_TIMEOUT_SECONDS + " 秒未收到事件（平台或文件系统可能不支持监听）");
            return;
        }
        for (WatchEvent<?> event : key.pollEvents()) {
            // context() 返回的是相对于被监听目录的路径，不是绝对路径
            System.out.println(stage + "：收到 " + event.kind().name() + " -> " + event.context());
        }
        // reset 必须调用：不重置的话这个 key 不会再收到任何后续事件。
        // 返回 false 表示目录已不可访问（被删除或失去权限），此时应当停止监听。
        System.out.println(stage + "：key 重置后仍有效: " + key.reset());
    }

}
