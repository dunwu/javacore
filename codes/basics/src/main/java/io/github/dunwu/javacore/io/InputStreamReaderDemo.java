package io.github.dunwu.javacore.io;

import java.io.*;

/**
 * 将 InputStream 转为 Reader
 * @author Zhang Peng
 */
public class InputStreamReaderDemo {

    public static void main(String args[]) throws IOException {
        File f = new File("d:" + File.separator + "test.txt");
        Reader reader = new InputStreamReader(new FileInputStream(f));
        char c[] = new char[1024];
        int len = reader.read(c);
        reader.close();
        System.out.println(new String(c, 0, len));
    }
}
