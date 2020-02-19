package io.github.dunwu.javacore.nio.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用輸出通道輸出內容
 */
public class FileChannelDemo01 {

    public static void main(String[] args) throws Exception {
        String[] info = { "大风起兮云飞扬，", "威加海内兮归故乡，", "安得猛士兮守四方。" };
        File file = new File("d:" + File.separator + "out.txt");
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
    }

}
