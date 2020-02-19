package io.github.dunwu.javacore.nio.lock;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockDemo {

    public static void main(String[] args) throws Exception {
        File file = new File("d:" + File.separator + "out.txt");
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

}
