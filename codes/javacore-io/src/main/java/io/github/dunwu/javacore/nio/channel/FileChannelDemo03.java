package io.github.dunwu.javacore.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例：使用 {@link FileChannel#map} 内存映射文件 —— 将文件直接映射到内存中读取，
 * 无需反复的系统调用，适合大文件读取。
 */
public class FileChannelDemo03 {

    /**
     * 演示内存映射读文件。
     * <p>
     * 为了使示例自包含，文件不存在时会先调用 {@link FileChannelDemo01#demo()} 生成。
     */
    public static void demo() throws Exception {
        File file = new File(FileChannelDemo01.FILE_PATH);
        if (!file.exists()) {
            // 文件不存在时先写入，保证示例可独立运行
            FileChannelDemo01.demo();
        }
        FileInputStream input = new FileInputStream(file);
        FileChannel fin = input.getChannel(); // 得到输入的通道
        MappedByteBuffer mbb = fin.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        byte[] data = new byte[(int) file.length()]; // 开辟空间接收内容
        int foot = 0;
        while (mbb.hasRemaining()) {
            data[foot++] = mbb.get(); // 读取数据
        }
        System.out.println(new String(data)); // 输出内容（使用平台默认编码）
        fin.close();
        input.close();
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
