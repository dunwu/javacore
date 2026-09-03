package io.github.dunwu.javacore.nio.channel;

import io.github.dunwu.javacore.DemoFiles;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例：使用 {@link FileChannel} 通道同时读写文件 —— 从输入通道读，向输出通道写，实现文件复制。
 */
public class FileChannelDemo02 {

    /**
     * 复制生成的目标文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}
     */
    public static final String COPY_FILE_PATH = DemoFiles.tempPath("temp_channel_outnote.txt");

    /**
     * 演示通道读写复制文件。
     * <p>
     * 为了使示例自包含，源文件不存在时会先调用 {@link FileChannelDemo01#demo()} 生成。
     */
    public static void demo() throws Exception {
        File file1 = new File(FileChannelDemo01.FILE_PATH);
        if (!file1.exists()) {
            // 源文件不存在时先写入，保证示例可独立运行
            FileChannelDemo01.demo();
        }
        File file2 = new File(COPY_FILE_PATH);
        FileInputStream input = new FileInputStream(file1);
        FileOutputStream output = new FileOutputStream(file2);
        FileChannel fout = output.getChannel(); // 得到输出的通道
        FileChannel fin = input.getChannel(); // 得到输入的通道
        ByteBuffer buf = ByteBuffer.allocate(1024);

        int temp = 0;
        while ((temp = fin.read(buf)) != -1) {
            buf.flip();
            fout.write(buf);
            buf.clear(); // 清空缓冲区,所有的状态变量的位置恢复到原点
        }
        fin.close();
        fout.close();
        input.close();
        output.close();
        System.out.println("文件已复制为：" + file2.getPath());
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
