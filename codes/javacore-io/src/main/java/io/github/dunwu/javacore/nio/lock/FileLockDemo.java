package io.github.dunwu.javacore.nio.lock;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * 示例：使用 {@link FileChannel#tryLock} 对文件加独占锁，模拟“文件被占用期间不可被其他进程修改”。
 * <p>
 * 注：本示例包含数秒等待（模拟锁定持续时间），不纳入自动化测试。
 */
public class FileLockDemo {

    /**
     * 演示文件锁定与释放：锁定 → 等待 5 秒 → 释放。
     */
    public static void demo() throws Exception {
        File file = new File("temp_lock.txt");
        FileOutputStream output = new FileOutputStream(file, true);
        FileChannel fout = output.getChannel();// 得到通道
        FileLock lock = fout.tryLock(); // 进行独占锁的操作
        if (lock != null) {
            System.out.println(file.getName() + "文件锁定300秒");
            Thread.sleep(5000);
            lock.release(); // 释放
            System.out.println(file.getName() + "文件解除锁定。");
        }
        fout.close();
        output.close();
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
