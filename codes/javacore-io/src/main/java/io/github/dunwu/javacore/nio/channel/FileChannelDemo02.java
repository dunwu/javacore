package io.github.dunwu.javacore.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用通道进行读写操作
 */
public class FileChannelDemo02 {

    public static void main(String[] args) throws Exception {
        File file1 = new File("d:" + File.separator + "out.txt");
        File file2 = new File("d:" + File.separator + "outnote.txt");
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
    }

}
