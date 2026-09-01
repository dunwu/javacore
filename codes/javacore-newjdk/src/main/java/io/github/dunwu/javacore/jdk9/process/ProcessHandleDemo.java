package io.github.dunwu.javacore.jdk9.process;

import java.util.Comparator;
import java.util.Optional;

/**
 * Java 9 ProcessHandle 进程管理 API 示例。
 * <p>
 * Java 9 之前，获取进程信息（如 PID）只能依赖不稳定的 {@code Runtime} 或 JMX 私有 API。
 * Java 9 引入了 {@link ProcessHandle} 接口，提供统一的进程查看与管理能力：
 * <ul>
 * <li>{@code ProcessHandle.current()}：获取当前 JVM 进程句柄</li>
 * <li>{@code ProcessHandle.allProcesses()}：以 Stream 形式遍历系统所有存活进程</li>
 * <li>{@code info()}：获取进程的命令、启动时间、用户等元信息</li>
 * <li>{@code destroy()} / {@code destroyForcibly()}：请求终止进程</li>
 * </ul>
 */
public class ProcessHandleDemo {

    /**
     * 示例 1：当前 JVM 进程句柄——PID、存活状态、元信息与父进程
     */
    public static void currentProcessInfo() {
        ProcessHandle current = ProcessHandle.current();
        System.out.println("当前进程 PID: " + current.pid());
        System.out.println("当前进程是否存活: " + current.isAlive());

        // 当前进程的详细信息
        ProcessHandle.Info info = current.info();
        System.out.println("进程命令: " + info.command().orElse("未知"));
        System.out.println("进程启动用户: " + info.user().orElse("未知"));
        System.out.println("进程总 CPU 时长: " + info.totalCpuDuration().orElse(null));

        // 父进程
        Optional<ProcessHandle> parent = current.parent();
        parent.ifPresent(p -> System.out.println("父进程 PID: " + p.pid()));
    }

    /**
     * 示例 2：遍历系统进程快照（按 PID 排序取前 5 个）并统计进程总数
     */
    public static void allProcessesSnapshot() {
        System.out.println("系统中部分进程快照：");
        ProcessHandle.allProcesses()
            .sorted(Comparator.comparingLong(ProcessHandle::pid))
            .limit(5)
            .forEach(p -> System.out.println(
                "  PID=" + p.pid() + ", 命令=" + p.info().command().orElse("未知")));

        // 统计当前进程总数
        long total = ProcessHandle.allProcesses().count();
        System.out.println("当前系统进程总数: " + total);
    }

    public static void main(String[] args) {
        currentProcessInfo();
        allProcessesSnapshot();
    }

}
