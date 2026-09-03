package io.github.dunwu.javacore.bio.chars;

import io.github.dunwu.javacore.DemoFiles;

import java.io.*;

/**
 * Reader 和 Writer 示例：字符流读写文件，注意字符流带缓冲区，必须 close/flush 才能把内容写入磁盘。
 *
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 */
public class FileReadWriteDemo {

    /**
     * 示例使用的数据文件路径。统一写到 {@code target/} 目录下，避免污染仓库工作目录，详见 {@link DemoFiles}。
     * <p>
     * 文件名带上示例名作为前缀，避开本模块其他示例（旧代码中多个示例共用 {@code temp.log}，会互相覆盖）。
     */
    private static final String FILEPATH = DemoFiles.tempPath("temp_filereadwrite.log");

    /** 演示用 FileWriter 写入、FileReader 逐字符读回。 */
    public static void demo() throws IOException {
        write(FILEPATH);
        System.out.println("内容为：" + new String(read(FILEPATH)));
    }

    public static void main(String[] args) throws IOException {
        demo();
    }

    public static void write(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        Writer out = new FileWriter(f);
        // Writer out = new FileWriter(f, true); // 追加内容方式

        // 3.进行读或写操作
        String str = "Hello World!!!\r\n";
        out.write(str);

        // 4.关闭流
        // 字符流操作时使用了缓冲区，并在关闭字符流时会强制将缓冲区内容输出
        // 如果不关闭流，则缓冲区的内容是无法输出的
        // 如果想在不关闭流时，将缓冲区内容输出，可以使用 flush 强制清空缓冲区
        out.flush();
        out.close();
    }

    public static char[] read(String filepath) throws IOException {
        // 1.使用 File 类绑定一个文件
        File f = new File(filepath);

        // 2.把 File 对象绑定到流对象上
        Reader input = new FileReader(f);

        // 3.进行读或写操作
        int temp = 0; // 接收每一个内容
        int len = 0; // 读取内容
        char[] c = new char[1024];
        while ((temp = input.read()) != -1) {
            // 如果不是-1就表示还有内容，可以继续读取
            c[len] = (char) temp;
            len++;
        }
        System.out.println("文件字符数为：" + len);

        // 4.关闭流
        input.close();

        return c;
    }

}
