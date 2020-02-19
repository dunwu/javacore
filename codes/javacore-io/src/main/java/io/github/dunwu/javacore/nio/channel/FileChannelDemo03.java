package io.github.dunwu.javacore.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 内存映射
 */
public class FileChannelDemo03 {

    public static void main(String[] args) throws Exception {
        File file = new File("d:" + File.separator + "out.txt");
        FileInputStream input = new FileInputStream(file);
        FileChannel fin = input.getChannel(); // 得到输入的通道
        MappedByteBuffer mbb = fin.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        byte[] data = new byte[(int) file.length()]; // 开辟空间接收内容
        int foot = 0;
        while (mbb.hasRemaining()) {
            data[foot++] = mbb.get(); // 读取数据
        }
        System.out.println(new String(data)); // 输出内容
        fin.close();
        input.close();
    }

}
