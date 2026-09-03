package io.github.dunwu.javacore.nio.channel;

import io.github.dunwu.javacore.DemoFiles;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 示例：使用 {@link FileChannel} 输出通道写文件 —— 将字符串写入缓冲区，再通过通道写入文件。
 */
public class FileChannelDemo01 {

    /**
     * 示例写入的文件路径，供 {@link FileChannelDemo02}、{@link FileChannelDemo03} 读取。
     * 统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}
     */
    public static final String FILE_PATH = DemoFiles.tempPath("temp_channel_out.txt");

    /**
     * 演示通过输出通道写文件：字符串 → 字节 → ByteBuffer → FileChannel.write。
     */
    public static void demo() throws Exception {
        String[] info = { "大风起兮云飞扬，", "威加海内兮归故乡，", "安得猛士兮守四方。" };
        File file = new File(FILE_PATH);
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel fc = fos.getChannel(); // 得到输出的通道
        ByteBuffer buf = ByteBuffer.allocate(1024);
        for (int i = 0; i < info.length; i++) {
            buf.put(info[i].getBytes()); // 字符串变为字节数组放进缓冲区之中
        }
        buf.flip();
        fc.write(buf); // 输出缓冲区的内容
        fc.close();
        fos.close();
        System.out.println("内容已通过通道写入文件：" + file.getPath());
    }

    public static void main(String[] args) throws Exception {
        demo();
    }

}
